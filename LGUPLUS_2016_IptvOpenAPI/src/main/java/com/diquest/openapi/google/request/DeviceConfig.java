package com.diquest.openapi.google.request;

public class DeviceConfig {
    private String deviceModelId = "";

    public String getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(String deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    @Override
    public String toString() {
        return "DeviceConfig{" +
                "deviceModelId='" + deviceModelId + '\'' +
                '}';
    }
}
