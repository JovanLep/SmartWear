package com.lespayne.smartwear.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.chileaf.WearManager;
import com.android.chileaf.bluetooth.scanner.ScanResult;
import com.android.chileaf.fitness.common.FilterScanCallback;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.dataHelper.DeviceListAdapter;
import com.lespayne.smartwear.model.ExtendedBluetoothDevice;
import com.lespayne.smartwear.user.UserManager;

import java.util.List;
import java.util.Objects;
import java.util.Set;


public class ScannerFragment extends DialogFragment {

    private static final long SCAN_DURATION = 5000;
    private static final String[] FILTER_NAMES = null;
    private OnDeviceSelectedListener mListener;
    private DeviceListAdapter mAdapter;
    private final Handler mHandler = new Handler();
    private Button mScanButton;
    private boolean mIsScanning = false;
    private WearManager mManager;

    public static ScannerFragment getInstance() {
        final ScannerFragment fragment = new ScannerFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        Objects.requireNonNull(getDialog()).getWindow().setLayout(dm.widthPixels - 120, dm.widthPixels - 120);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface OnDeviceSelectedListener {
        void onDeviceSelected(final BluetoothDevice device, final String name);

        default void onDialogCanceled() {
        }
    }

    public void setListener(OnDeviceSelectedListener mListener) {
        this.mListener = mListener;
    }


    @Override
    public void onDestroyView() {
        stopScan();
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        mManager = WearManager.getInstance(requireContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_device_scan, null);
        final ListView listview = dialogView.findViewById(R.id.lv_device);

        listview.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listview.setAdapter(mAdapter = new DeviceListAdapter(getActivity()));
        final AlertDialog dialog = builder.setView(dialogView).create();

        listview.setOnItemClickListener((parent, view, position, id) -> {
            final ExtendedBluetoothDevice d = (ExtendedBluetoothDevice) mAdapter.getItem(position);
            mListener.onDeviceSelected(d.getDevice(), d.getName());
            stopScan();
            dialog.dismiss();
        });
        mScanButton = dialogView.findViewById(R.id.action_cancel);
        mScanButton.setOnClickListener(v -> {
            if (v.getId() == R.id.action_cancel) {
                if (mIsScanning) {
                    dialog.cancel();
                } else {
                    startScan();
                }
            }
        });
        if (savedInstanceState == null)
            startScan();
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mListener.onDialogCanceled();
    }

    private void startScan() {
        mAdapter.clearDevices();
        mScanButton.setText("关闭");
        mManager.setFilterNames(FILTER_NAMES);
        mManager.startScan(results -> {
            for (int i = 0; i < results.size(); i++) {
                mAdapter.update(results);
            }
        });

        mIsScanning = true;
        mHandler.postDelayed(() -> {
            if (mIsScanning) {
                stopScan();
            }
        }, SCAN_DURATION);
    }

    private void stopScan() {
        if (mIsScanning) {
            mScanButton.setText("继续扫描");
            WearManager.getInstance(getActivity()).stopScan();
            mIsScanning = false;
        }
    }
}
