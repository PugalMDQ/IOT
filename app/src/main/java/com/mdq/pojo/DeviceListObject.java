package com.mdq.pojo;

public class DeviceListObject {
    private String deviceName;

    public DeviceListObject(){}

    public DeviceListObject(String deviceName){
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
