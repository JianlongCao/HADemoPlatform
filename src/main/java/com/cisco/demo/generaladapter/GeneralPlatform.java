package com.cisco.demo.generaladapter;

import com.cisco.demo.excutionplatform.ExcutionUnit;
import com.cisco.demo.excutionplatform.ExcutionUnitBulder;

import java.util.List;
import java.util.Map;

public class GeneralPlatform {

    private static GeneralPlatform generalPlatform = null;

    public static GeneralPlatform Instance() {
        if(generalPlatform == null) {
            generalPlatform = new GeneralPlatform();
        }
        return generalPlatform;
    }

    private GeneralPlatform() {

    }
    public void set(String addr, String radio, String type, Map<String,Object> props) {
        for (Map.Entry<String, Object> entry : props.entrySet())
        {
            if(entry.getKey().equalsIgnoreCase("level")) {
                LevelSwitch levelSwitch = new LevelSwitch(addr, radio, "");
                if(entry.getValue() instanceof Integer)
                    levelSwitch.setLevel((int)entry.getValue());
                else
                    System.out.println("value type not acceptable.");
            } else if(entry.getKey().equalsIgnoreCase("hsb")) {
                ColorSwitch colorSwitch = new ColorSwitch(addr,radio, "");
                if(entry.getValue() instanceof Map) {
                    try {
                        Map<String, Integer> hsb = (Map<String, Integer>)entry.getValue();
                        int hue = hsb.get("hue");
                        int sat = hsb.get("sat");
                        int brightness = hsb.get("bri");
                        colorSwitch.setColor(hue, sat, brightness);
                    } catch(Exception e) {
                        System.out.println("invalid hsb value");
                    }
                }
            } else if(entry.getKey().equalsIgnoreCase("onoff")) {
                OnOffSwitch onOffSwitch1 = new OnOffSwitch(addr, radio, "");
                if(entry.getValue() instanceof Boolean) {
                    boolean state = (Boolean)entry.getValue();
                    if(state)
                        onOffSwitch1.turnOn();
                    else
                        onOffSwitch1.turnOff();
                }
            }
        }
    }

    public List<Device> get(String addr, String radio) {
        ExcutionUnit excutionUnit = new ExcutionUnitBulder().builder();
        return excutionUnit.get(addr, radio);
    }

}
