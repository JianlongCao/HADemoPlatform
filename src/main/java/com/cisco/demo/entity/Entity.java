package com.cisco.demo.entity;

public class Entity {
    public static final String TYPE_CAMERA = "IPCamera";
    public static final String TYPE_COMBINED = "Combined_Interface";
    public static final String TYPE_DLIGNT = "DLight";
    public static final String TYPE_FAKE = "Fake";
    public static final String TYPE_DOOR = "Door";
    public static final String TYPE_OCCUPANCY = "Occupancy";
    public static final String TYPE_OFLIGHT = "OFLight";
    public static final String TYPE_TEMPERATURE = "Temperature";
    public static final String TYPE_LIGHT = "light";
    public static final String RADIO_ZIGBEE = "zb";

    public String ieee;
    public int deviceID;
    public String deviceName;
    public String category;
    public String type;
    public Boolean status;
    public String value;
    public String location;
    public String desciption;
    public String radio;

    public Entity(String ieee, int deviceID, String radio, String deviceName, String category, String type,
                  Boolean status,
                  String value, String location, String desciption) {
        this.ieee = ieee;
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.category = category;
        this.type = type;
        this.status = status;
        this.value = value;
        this.location = location;
        this.desciption = desciption;
        this.radio = radio;
    }

    public String getIeee() {
        return ieee;
    }

    public void setIeee(String ieee) {
        this.ieee = ieee;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public Entity setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String toString() {

        String on_off;
        if (status.booleanValue())
            on_off = "ON";
        else
            on_off = "OFF";

        String ret = "{ieee:\'" + ieee + "\',id:\'" + deviceID + "\',name:\'"
                + deviceName + "\',thumb:\'" + "bulb_yellow.png"
                + "\',category:\'" + category + "\',type:\'" + type
                + "\',status:\'" + on_off + "\',value:\'" + value + "\'}";
        return ret;
    }
}
