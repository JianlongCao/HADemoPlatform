package com.cisco.demo.excutionplatform;

import com.cisco.demo.generaladapter.ColorSwitch;
import com.cisco.demo.generaladapter.Device;

import java.util.List;

public interface ExcutionUnit {

    public void on();
    public void off();
    public void level(int level);
    public void color(int hue, int saturation, int brightness);
    public List<Device> get(String addr, String radio);

}
