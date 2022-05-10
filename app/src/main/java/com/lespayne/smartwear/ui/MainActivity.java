package com.lespayne.smartwear.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.chileaf.WearManager;
import com.android.chileaf.bluetooth.scanner.ScanResult;
import com.android.chileaf.fitness.callback.TemperatureCallback;
import com.android.chileaf.fitness.callback.WearManagerCallbacks;
import com.google.gson.Gson;
import com.lespayne.base.BaseActivity;
import com.lespayne.base.utils.OSUtils;
import com.lespayne.smartwear.App;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.chart.LineChartManager;
import com.lespayne.smartwear.dataHelper.GsonUtil;
import com.lespayne.smartwear.dataHelper.NetClient;
import com.lespayne.smartwear.dataHelper.NetUtils;
import com.lespayne.smartwear.databinding.ActivityMainBinding;
import com.lespayne.smartwear.fragment.ScannerFragment;
import com.lespayne.smartwear.model.HeartCLickBean;
import com.lespayne.smartwear.model.WeatherBean;
import com.lespayne.smartwear.noti.VoiceService;
import com.lespayne.smartwear.user.UserManager;
import com.permissionx.guolindev.PermissionX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements ScannerFragment.OnDeviceSelectedListener, WearManagerCallbacks, TemperatureCallback {
    private long exitTime = 0;
    private static final int max = 180 - UserManager.getInstance().getAge();
    private static final String post = "82.157.6.212";
    private static final int host = 8090;
    private WearManager wearManager;
    private boolean autoConnected = false;
    private String deviceName = "";
    private PrintWriter printWriter;
    private BufferedReader bufferR;
    public String byXmlString;
    public String tem = "";
    public int heart = 0;

    private int perSlows = 0;
    private int perFasts = 0;
    private int perNormal = 0;

    private int currSlows = 0;
    private int currFasts = 0;
    private int currWeather = 0;
    private int showFast = 0;
    private int showSlow = 0;
    private LineChartManager lineChartManager;
    private AlertDialog dialog;
    private MediaPlayer mediaPlayer1;
    private ScannerFragment instance;
    private long socketOutTime = 0;
    private Socket socket;


    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        dataBinding.layoutMainTitle.setTitle("健康监测");
        dataBinding.layoutMainTitle.setBackGone();
        dataBinding.layoutMainTitle.setTitleColor();
        dataBinding.layoutMainTitle.setBackgroundColour(ContextCompat.getColor(this, R.color.light));
        dataBinding.idMenu.closeMenu();
        dataBinding.menuLayout.deviceState.setText(getResources().getString(R.string.menu_device_state));
        dataBinding.menuLayout.updateState.setText(getResources().getString(R.string.menu_beat));
        SpannableStringBuilder ssb1 = new SpannableStringBuilder("稍慢:<60BPM");
        SpannableStringBuilder ssb2 = new SpannableStringBuilder("正常:60~100BPM");
        SpannableStringBuilder ssb3 = new SpannableStringBuilder("稍快:>100BPM");

        lineChartManager = new LineChartManager(dataBinding.headerChart);

        ssb1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.y_FFC107)), 3, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.light)), 3, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb3.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_E4999F)), 3, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb1.setSpan(new RelativeSizeSpan(1.5f), 3, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb2.setSpan(new RelativeSizeSpan(1.5f), 3, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb3.setSpan(new RelativeSizeSpan(1.5f), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dataBinding.tv1.setText(ssb1);
        dataBinding.tv2.setText(ssb2);
        dataBinding.tv3.setText(ssb3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_app)
                .setTitle("心率异常警告对话框!!")
                .setMessage("是否结束提醒？")
                .setPositiveButton("确认", (dialog, which) -> {
                    dialog.dismiss();
                    currFasts = 0;
                    currSlows = 0;
                    mediaPlayer1.stop();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();


    }

    @Override
    protected void initEvent() {
        //初始化连接
        wearManager = WearManager.getInstance(this);
        wearManager.setManagerCallbacks(this);
        wearManager.addTemperatureCallback(this);
        //实时心跳和上报
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new connectService());
        executor.execute(new HeartDiDi());
        executor.execute(new sendService());

        startService(new Intent(this, VoiceService.class));
        dataBinding.userInfo.setOnClickListener(view -> dataBinding.idMenu.toggle());
        dataBinding.mainHis.setOnClickListener(view -> startActivity(HistoryActivity.class));
        dataBinding.menuLayout.menuInfo.setOnClickListener(view -> startActivity(UserInfoActivity.class));
        dataBinding.menuLayout.menuSetting.setOnClickListener(view -> startActivity(SettingActivity.class));
        dataBinding.closeScan.setOnClickListener(v -> dataBinding.searchLayout.setVisibility(View.GONE));

        instance = ScannerFragment.getInstance();
        instance.setListener(this);
        if (!App.getApp().mDeviceConnected) {
            dataBinding.menuLayout.relDev.setOnClickListener(v -> instance.show(getSupportFragmentManager(), "scan_fragment"));
        }
        getSdShow();
        initPermission();
        setLineData();
    }


    public void setLineData() {

        lineChartManager.setYAxisData(max, 40f, 3);
        lineChartManager.showLineChart(ContextCompat.getColor(this, R.color.light));
        lineChartManager.setMarkerView(this, R.drawable.marker_view_blue_bg);
    }

    public void initPermission() {
        List<String> requestList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestList.add(Manifest.permission.BLUETOOTH_SCAN);
            requestList.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            requestList.add(Manifest.permission.BLUETOOTH_CONNECT);
        } else {
            requestList.add(Manifest.permission.BLUETOOTH);
            requestList.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        requestList.add(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        requestList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requestList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!requestList.isEmpty()) {
            PermissionX.init(this)
                    .permissions(requestList)
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason((scope, deniedList) ->
                            scope.showRequestReasonDialog(deniedList,
                                    "PermissionX需要您同意以下权限才能正常使用",
                                    "同意",
                                    "拒绝"))
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            initDevice();
                        }
                    });
        }

        if (!OSUtils.isIgnoringBatteryOptimizations(this)) {
            OSUtils.requestIgnoreBatteryOptimizations(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initDevice() {
        if (isEnabled()) {
            if (!App.getApp().mDeviceConnected) {
                if (TextUtils.isEmpty(UserManager.getInstance().getDeviceId())) {
                    if (!autoConnected) {
                        instance.show(getSupportFragmentManager(), "scan_fragment");
                    }
                } else {
                    dataBinding.searchLayout.setVisibility(View.GONE);
                    wearManager.startScan(list -> {
                        for (int i = 0; i < list.size(); i++) {
                            ScanResult scanResult = list.get(i);
                            if (scanResult.getDevice().getName().equals(UserManager.getInstance().getDeviceId())
                                    && scanResult.getDevice().getAddress().equals(UserManager.getInstance().getAddress())) {
                                autoConnected = true;
                                onDeviceSelected(scanResult.getDevice(), scanResult.getDevice().getName());
                                showToast("已自动为您连接" + scanResult.getDevice().getName());
                                wearManager.stopScan();
                                return;
                            }
                        }
                    });
                }
            } else {
                wearManager.disConnect();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (dataBinding.idMenu.getMenuState()) {
            dataBinding.idMenu.closeMenu();
            return;
        }
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    class HeartDiDi implements Runnable {

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            while (true) {
                if (socketOutTime != 0 && System.currentTimeMillis() - socketOutTime > 60 * 1000) {
                    try {
                        socket = new Socket(post, host);
                        socket.setSoTimeout(60 * 1000);
                        App.getApp().isConnect = socket.isConnected();
                        dataBinding.menuLayout.deviceState.setText(getResources().getString(R.string.menu_upload_state) + ((App.getApp().isConnect && App.getApp().mDeviceConnected) ? "：正常" : "：异常"));
                        printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                        bufferR = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                        receiveMsg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HeartCLickBean heartCLickBean = new HeartCLickBean(0);
                Gson gson = new Gson();
                String s = gson.toJson(heartCLickBean);
                printWriter.println(s);
            }
        }
    }

    class sendService implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (heart == 0 || TextUtils.isEmpty(byXmlString) || TextUtils.isEmpty(tem)) {
                    continue;
                }
                HeartCLickBean.DataBean dataBean = new HeartCLickBean.DataBean();
                dataBean.setHeart(heart + "");
                dataBean.setHumidity(byXmlString);
                dataBean.setTemperature(tem);
                HeartCLickBean heartCLickBean = new HeartCLickBean();
                heartCLickBean.setStatus(1);
                heartCLickBean.setClient(0);
                heartCLickBean.setData(dataBean);
                heartCLickBean.setMachineId(deviceName);
                heartCLickBean.setUserId("977f45407cc24e279c072f555bd158c7");
                Gson gson = new Gson();
                String s = gson.toJson(heartCLickBean);
                printWriter.println(s);
            }
        }
    }

    class connectService implements Runnable {

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            try {
                //步骤一
                socket = new Socket(post, host);
                socket.setSoTimeout(60 * 1000);
                App.getApp().isConnect = socket.isConnected();
                dataBinding.menuLayout.deviceState.setText(getResources().getString(R.string.menu_upload_state) + ((App.getApp().isConnect && App.getApp().mDeviceConnected) ? "：正常" : "：异常"));
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                bufferR = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                receiveMsg();
            } catch (Exception e) {
                log("connectService:连接建立失败" + e.getMessage());
            }
        }
    }

    private void receiveMsg() {
        try {
            while (true) {                                      //步骤三
                if (bufferR.readLine() != null) {
                    //心率范围集合
                    String receiveMsg = bufferR.readLine();
                    socketOutTime = System.currentTimeMillis();
                    log("receiveMsg:成功->" + receiveMsg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSdShow() {
        NetClient.getNetClient().callNet("http://www.weather.com.cn/data/cityinfo/101010100.html", new NetClient.MyCallBack() {
            @Override
            public void onFailure(int code) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String json) {
                WeatherBean weatherBean = GsonUtil.jsonToObject(json, WeatherBean.class);
                WeatherBean.WeatherinfoBean weatherinfo = weatherBean.getWeatherinfo();
                NetClient.getNetClient().callNet("http://wthrcdn.etouch.cn/WeatherApi?city=" + weatherinfo.getCity(), new NetClient.MyCallBack() {
                    @Override
                    public void onFailure(int code) {
                    }

                    @Override
                    public void onResponse(String json) {
                        try {
                            byXmlString = NetUtils.getByXmlString(json);
                            runOnUiThread(() -> dataBinding.tvSd.setText("湿度：" + byXmlString));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    protected boolean isEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    @SuppressLint({"MissingPermission", "NewApi", "SetTextI18n"})
    @Override
    public void onDeviceSelected(BluetoothDevice device, String name) {
        wearManager.connect(device, false);
        deviceName = name;
        UserManager.getInstance().setDeviceId(name);
        UserManager.getInstance().setAddress(device.getAddress());
    }

    @Override
    public void onDialogCanceled() {
        ScannerFragment.OnDeviceSelectedListener.super.onDialogCanceled();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTemperatureReceived(@NonNull BluetoothDevice bluetoothDevice, float v, float v1, float v2) {
        dataBinding.tvWeather.setText("体温:" + v2 + "°");
        tem = v2 + "";
        if (v2 > 37.5f) {
            currWeather++;
            if (currWeather > 300) {
                openRawMusic();
            }
        } else {
            currWeather = 0;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHeartRateMeasurementReceived(@NonNull BluetoothDevice device, int heartRate,
                                               @Nullable Boolean contactDetected,
                                               @Nullable Integer energyExpanded,
                                               @Nullable List<Integer> rrIntervals) {
        WearManagerCallbacks.super.onHeartRateMeasurementReceived(device, heartRate, contactDetected, energyExpanded, rrIntervals);
        //添加历史
        log(heartRate + "");
        String str = formatHmsTime(System.currentTimeMillis() / 1000);
        lineChartManager.addEntry(heartRate * 1f, str);
        //默认值
        heart = heartRate;
        //展示部分
        if (showSlow == 0 || heartRate < showSlow) {
            showSlow = heartRate;
        }

        if (showFast == 0 || heartRate > showFast) {
            showFast = heartRate;
        }
        dataBinding.tvNormalHeart.setText(heart + "");
        dataBinding.tvMinHeart.setText(showSlow + "");
        dataBinding.tvMaxHeart.setText(showFast + "");

        /* ************************百分比****************************** */
        int average = 180 - UserManager.getInstance().getAge();

        if (heartRate < 60) {
            currSlows++;
            perSlows++;
            currFasts = 0;
        } else if (heartRate > average) {
            currFasts++;
            perFasts++;
            currSlows = 0;
        } else {
            perNormal++;
            currSlows = 0;
            currFasts = 0;
        }
        if (currSlows > 300) {
            openRawMusic();
        }

        if (currFasts > 300) {
            openRawMusic();
        }
        int total = perSlows + perNormal + perFasts;
        int slow, normal, fast;
        if (total == 0) {
            fast = normal = slow = 0;
        } else {
            slow = perSlows * 100 / total;
            normal = perNormal * 100 / total;
            fast = perFasts * 100 / total;
        }
        dataBinding.pbHeartSlow.setPercent(slow);
        dataBinding.pbHeartSlow.setCustomText("稍慢");
        dataBinding.pbHeartSlow.setProgressColor(getResources().getColor(R.color.y_FFC107));
        dataBinding.pbHeartNormal.setPercent(normal);
        dataBinding.pbHeartNormal.setCustomText("正常");
        dataBinding.pbHeartNormal.setProgressColor(getResources().getColor(R.color.light));
        dataBinding.pbHeartFast.setPercent(fast);
        dataBinding.pbHeartFast.setCustomText("稍快");
        dataBinding.pbHeartFast.setProgressColor(getResources().getColor(R.color.red_E4999F));
    }

    private void openRawMusic() {
        try {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
            mediaPlayer1 = MediaPlayer.create(this, R.raw.general_fail);
            mediaPlayer1.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBatteryLevelChanged(@NonNull BluetoothDevice device, int batteryLevel) {
        WearManagerCallbacks.super.onBatteryLevelChanged(device, batteryLevel);
        App.getApp().beat = batteryLevel;
        if (batteryLevel > 0) {
            dataBinding.menuLayout.updateState.setText(getResources().getString(R.string.menu_beat_ok));
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDeviceConnected(@NonNull BluetoothDevice bluetoothDevice) {
        App.getApp().mDeviceConnected = true;
        dataBinding.menuLayout.updateState.setText(getResources().getString(R.string.menu_beat_ok));
    }

    @Override
    public void onDeviceDisconnected(@NonNull BluetoothDevice bluetoothDevice) {
        App.getApp().mDeviceConnected = false;
        wearManager.close();
    }

    public String formatHmsTime(long timeInMillis) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timeInMillis * 1000));
    }
}