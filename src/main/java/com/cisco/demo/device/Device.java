package com.cisco.demo.device;

import com.cisco.demo.entity.Entity;

public class Device {
    private  Entity entity;
    private  Device device;

    public Device(Entity entity) {
        this.entity = entity;
        this.device = this;
    }

    public void setParent(Device device) {
        this.device = device;
    }

    public Device getParent() {
        return this.device;
    }

    public String getIEEE() {
        return entity!=null ? entity.ieee : null;
    }

    public int getDeviceID() {
        return entity!=null ? entity.deviceID : null;
    }

    public String getDeviceName() {
        return entity!=null ? entity.deviceName : null;
    }

    public String getCategory() {
        return entity!=null ? entity.category : null;
    }

    public String getType() {
        return entity!=null ? entity.category : null;
    }

    public String getLocation() {
        return entity!=null ? entity.location : null;
    }

    public Boolean getStatus() {
        return entity!=null ? entity.status : null;
    }

    public String getValue() {
        return entity!=null ? entity.value : null;
    }

    public String getInfo() {
        return entity!=null ? entity.desciption : null;
    }

    public void setLocation(String location) {
        entity.location = location;
    }

    public void setInfo(String info) {
        entity.desciption = info;
    }

    public void clean() {

    }

    public String getRadio() {
        return entity.radio;
    }

    public Device getOnOffSwitch() {
        return null;
    }

    public Device getLevelSwitch() {
        return null;
    }

    public static Device jsonToDevices(String jsonData) {
        return null;
    }
}
