package com.lespayne.smartwear.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lespayne.base.BaseActivity;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.adapter.MainAdapter;
import com.lespayne.smartwear.chart.LineChartManager;
import com.lespayne.smartwear.dataHelper.GsonUtil;
import com.lespayne.smartwear.databinding.ActivityHistoryBinding;
import com.lespayne.smartwear.model.HistoryBean;
import com.lespayne.smartwear.model.MainBean;
import com.lespayne.smartwear.net.RetrofitManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity<ActivityHistoryBinding> {
    private List<MainBean> list0 = new ArrayList<>();
    private List<MainBean> list1 = new ArrayList<>();
    private List<MainBean> list2 = new ArrayList<>();
    private List<MainBean> list3 = new ArrayList<>();
    private List<MainBean> list4 = new ArrayList<>();
    private List<MainBean> list5 = new ArrayList<>();
    private List<MainBean> list6 = new ArrayList<>();
    private int state = 0;
    private LineChartManager lineChartManager;
    private MainAdapter mainAdapter;
    private List<Float> yList = new ArrayList<>();
    private List<String> xList = new ArrayList<>();

    @Override
    protected int bindLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void initEvent() {
        lineChartManager = new LineChartManager(dataBinding.headerChart);
        dataBinding.layoutHisTitle.setTitle("历史数据", this);
        mainAdapter = new MainAdapter(this, new ArrayList<>());
        dataBinding.hisList.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.hisList.setAdapter(mainAdapter);
        setStateClick();
        dataBinding.tvLast.setOnClickListener(v -> {
            state--;
            setStateClick();
        });
        dataBinding.tvNext.setOnClickListener(v -> {
            state++;
            setStateClick();
        });
    }

    private void setStateClick() {
        dataBinding.tvLast.setEnabled(state != 0);
        dataBinding.tvNext.setEnabled(state != 6);
        dataBinding.tvLast.setTextColor(state != 0 ?
                ContextCompat.getColor(this, R.color.red_d21034) :
                ContextCompat.getColor(this, R.color.gray_999999));
        dataBinding.tvNext.setTextColor(state != 6 ?
                ContextCompat.getColor(this, R.color.red_d21034) :
                ContextCompat.getColor(this, R.color.gray_999999));
        loadData();
    }

    private void loadData() {
        RetrofitManager.getInstance().getHistoryList(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null) return;
                    String string = response.body().string();
                    HistoryBean historyBean = GsonUtil.jsonToObject(string, HistoryBean.class);
                    getList3(historyBean.getData());
                    runOnUiThread(() -> {
                        mainAdapter.setDataList(getDataList());
                        setLineData(getDataList());
                        mainAdapter.notifyDataSetChanged();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        }, past7Days(state), past7Days(state));
    }


    public void setLineData(List<MainBean> list) {

        if (list == null || list.size() == 0) {
            dataBinding.headerChart.clear();
            return;
        }
        xList.clear();
        yList.clear();
        xList.add("0");
        yList.add(0f);
        for (MainBean bean : list) {
            xList.add(formatHmsTime(bean.getTime()));
            yList.add(TextUtils.isEmpty(bean.getHeart()) ? 0f : Float.parseFloat(bean.getHeart()));
        }
        Float max = Collections.max(yList);
        lineChartManager.setYAxisData(max, 40f, 3);
        lineChartManager.showLineChart(yList, xList, ContextCompat.getColor(this, R.color.light));
        lineChartManager.setMarkerView(this, R.drawable.marker_view_blue_bg);
    }

    private List<MainBean> getDataList() {
        if (state == 0) {
            return list0;
        } else if (state == 1) {
            return list1;
        } else if (state == 2) {
            return list2;
        } else if (state == 3) {
            return list3;
        } else if (state == 4) {
            return list4;
        } else if (state == 5) {
            return list5;
        } else if (state == 6) {
            return list6;
        } else {
            return list0;
        }
    }

    public void getList3(List<HistoryBean.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            MainBean mainBean = new MainBean();
            mainBean.setTime(spitData(data.get(i).getCreateTime()));
            mainBean.setTimeStr(data.get(i).getCreateTime());
            mainBean.setRecord(data.get(i).getName());
            mainBean.setLength(data.get(i).getCreateTime());
            mainBean.setNote("无");
            mainBean.setHeart(TextUtils.isEmpty(data.get(i).getHeart()) ? "0" : data.get(i).getHeart());
            mainBean.setBody(TextUtils.isEmpty(data.get(i).getTemperature()) ? "0" : data.get(i).getTemperature());
            mainBean.setHumidity(TextUtils.isEmpty(data.get(i).getHumidity()) ? "0" : data.get(i).getHumidity());
            //过去7天数据集合处理
            if (data.get(i).getCreateTime().contains(past7Days(0))) {
                list0.add(mainBean);
            } else if (data.get(i).getCreateTime().contains(past7Days(1))) {
                list1.add(mainBean);
            } else if (data.get(i).getCreateTime().contains(past7Days(2))) {
                list2.add(mainBean);
            } else if (data.get(i).getCreateTime().contains(past7Days(3))) {
                list3.add(mainBean);
            } else if (data.get(i).getCreateTime().contains(past7Days(4))) {
                list4.add(mainBean);
            } else if (data.get(i).getCreateTime().contains(past7Days(5))) {
                list5.add(mainBean);
            } else if (data.get(i).getCreateTime().contains(past7Days(6))) {
                list6.add(mainBean);
            }
        }
    }

    private long spitData(String times) {
        long time = 0;
        Date date;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sf.parse(times);
            time = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String formatHmsTime(long timeInMillis) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timeInMillis * 1000));
    }

    public static String past7Days(int lastCount) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -lastCount);
        Date d = c.getTime();
        return sd.format(d);
    }
}