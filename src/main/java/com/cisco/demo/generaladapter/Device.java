package com.cisco.demo.generaladapter;

import com.cisco.demo.excutionplatform.ExcutionUnit;
import com.cisco.demo.excutionplatform.ExcutionUnitBulder;

import java.util.ArrayList;

public class Device {

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

    private String addr;
    private String radio;
    private String type;
    public ArrayList<String> names;

    public ArrayList<String> getNames() {
        return names;
    }

    public void setName(String name) {
        ExcutionUnit excutionUnit = new ExcutionUnitBulder(this).builder();
        excutionUnit.setName(addr,radio,name);
    }

    private OnOffSwitch onOffSwitch = null;
    private LevelSwitch levelSwitch = null;
    private ColorSwitch colorSwitch = null;

    public void setOnOffSwitch(OnOffSwitch onOffSwitch) {
        this.onOffSwitch = onOffSwitch;
    }

    public void setLevelSwitch(LevelSwitch levelSwitch) {
        this.levelSwitch = levelSwitch;
    }

    public void setColorSwitch(ColorSwitch colorSwitch) {
        this.colorSwitch = colorSwitch;
    }

    public OnOffSwitch getOnOffSwitch() {
        return onOffSwitch;
    }

    public LevelSwitch getLevelSwitch() {
        return levelSwitch;
    }

    public ColorSwitch getColorSwitch() {
        return colorSwitch;
    }

    public Device(String addr, String radio, String type) {
        this.addr = addr;
        this.radio = radio;
        this.type = type;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

