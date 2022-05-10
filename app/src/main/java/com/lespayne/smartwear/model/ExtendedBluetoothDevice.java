package com.lespayne.smartwear.model;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

import com.android.chileaf.bluetooth.scanner.ScanResult;

public class ExtendedBluetoothDevice {
    public static final int NO_RSSI = -1000;
    private String name;
    private int rssi;
    private boolean isBonded;
    private BluetoothDevice device;

    public ExtendedBluetoothDevice(final ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.name = scanResult.getScanRecord() != null ? scanResult.getScanRecord().getDeviceName() : null;
        this.rssi = scanResult.getRssi();
        this.isBonded = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean isBonded() {
        return isBonded;
    }

    public void setBonded(boolean bonded) {
        isBonded = bonded;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public boolean matches(final ScanResult scanResult) {
        return device.getAddress().equals(scanResult.getDevice().getAddress());
    }
}