package com.lespayne.smartwear.model;

public class HeartCLickBean {
    private int status;
    private int client;
    private String userId;
    private String machineId;
    private DataBean data;

    public HeartCLickBean() {
    }

    public HeartCLickBean(int status) {
        this.status = status;
    }

    public HeartCLickBean(int status, int client, String userId, String machineId, DataBean data) {
        this.status = status;
        this.client = client;
        this.userId = userId;
        this.machineId = machineId;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String heart;
        private String humidity;
        private String temperature;


        public String getHeart() {
            return heart;
        }

        public void setHeart(String heart) {
            this.heart = heart;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }
    }
}
