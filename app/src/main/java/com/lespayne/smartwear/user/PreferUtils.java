package com.lespayne.smartwear.user;

import com.tencent.mmkv.MMKV;

public class PreferUtils {

    private MMKV mmkv;

    public PreferUtils() {
        mmkv = MMKV.defaultMMKV();
    }

    public void put(String key, Object value) {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV();
        }
        if (value instanceof String) {
            mmkv.encode(key, (String) value);
        } else if (value instanceof Integer) {
            mmkv.encode(key, (Integer) value);
        } else if (value instanceof Boolean) {
            mmkv.encode(key, (Boolean) value);
        } else if (value instanceof Float) {
            mmkv.encode(key, (Float) value);
        } else if (value instanceof Long) {
            mmkv.encode(key, (Long) value);
        } else {
            mmkv.encode(key, (String) value);
        }
    }

    public String get(String key, String def) {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV();
        }
        return mmkv.decodeString(key, def);
    }

    public Integer get(String key, Integer def) {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV();
        }
        return mmkv.decodeInt(key, def);
    }

    public Boolean get(String key, Boolean def) {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV();
        }
        return mmkv.decodeBool(key, def);
    }

    public void remove(String key) {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV();
        }
        mmkv.remove(key);
    }
}
