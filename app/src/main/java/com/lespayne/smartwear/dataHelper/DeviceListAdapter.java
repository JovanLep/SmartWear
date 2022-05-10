package com.lespayne.smartwear.dataHelper;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.chileaf.bluetooth.scanner.ScanResult;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.model.ExtendedBluetoothDevice;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {

    private final ArrayList<ExtendedBluetoothDevice> mListValues = new ArrayList<>();
    private final Context mContext;

    public DeviceListAdapter(final Context context) {
        mContext = context;
    }

    public void update(final List<ScanResult> results) {
        for (final ScanResult result : results) {
            final ExtendedBluetoothDevice device = findDevice(result);
            if (device == null) {
                mListValues.add(new ExtendedBluetoothDevice(result));
            } else if (result.getScanRecord() != null) {
                device.setName(result.getScanRecord().getDeviceName());
                device.setRssi(result.getRssi());
            }
        }
        notifyDataSetChanged();
    }

    private ExtendedBluetoothDevice findDevice(final ScanResult result) {
        for (final ExtendedBluetoothDevice device : mListValues)
            if (device.matches(result))
                return device;
        return null;
    }

    public void clearDevices() {
        mListValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListValues.isEmpty() ? 0 : mListValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mListValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View oldView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = oldView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_device_list, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.name = view.findViewById(R.id.name);
            holder.address = view.findViewById(R.id.address);
            holder.signal = view.findViewById(R.id.rssi);
            view.setTag(holder);
        }

        final ExtendedBluetoothDevice device = (ExtendedBluetoothDevice) getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();
        final String name = device.getName();
        holder.name.setText(name != null ? name : "--");
        holder.address.setText(device.getDevice().getAddress());
        if (!device.isBonded() || device.getRssi() != ExtendedBluetoothDevice.NO_RSSI) {
            holder.signal.setText(device.getRssi() + "dBm");
            holder.signal.setVisibility(View.VISIBLE);
        } else {
            holder.signal.setVisibility(View.GONE);
        }
        return view;
    }

    private class ViewHolder {
        private TextView name;
        private TextView address;
        private TextView signal;
    }
}