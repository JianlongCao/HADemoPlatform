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

    public String toString() {

        String on_off;
        if(status.booleanValue())
            on_off = "ON";
        else
            on_off ="OFF";

        String ret = "{ieee:\'" + ieee + "\',id:\'" + deviceID + "\',name:\'"
                     + deviceName + "\',thumb:\'" + "bulb_yellow.png"
                     + "\',category:\'" + category + "\',type:\'" + type
                     + "\',status:\'" + on_off + "\',value:\'" + value + "\'}";
        return ret;
    }
}
