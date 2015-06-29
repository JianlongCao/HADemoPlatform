package com.cisco.demo.generaladapter;

import com.cisco.demo.excutionplatform.ExcutionUnit;
import com.cisco.demo.excutionplatform.ExcutionUnitBulder;

public class LevelSwitch extends Device {

    private int level = 0;

    public LevelSwitch(String addr, String radio, String type) {
        super(addr, radio, type);
    }

    public LevelSwitch(String addr, String radio, String type, int level) {
        super(addr, radio, type);
        this.level = level;
    }

    /**
     * { "actuators":[ {"type":"light","state":"enabled", "measurement":[{"value":{"level":0}}]}] }
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
        ExcutionUnit excutionUnit = new ExcutionUnitBulder(this).builder();
        excutionUnit.level(level);
    }

    public int getLevel() {
        return level;
    }

}
