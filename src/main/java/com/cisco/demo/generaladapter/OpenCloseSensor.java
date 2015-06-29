package com.cisco.demo.generaladapter;


import com.cisco.demo.excutionplatform.ExcutionUnit;
import com.cisco.demo.excutionplatform.ExcutionUnitBulder;

public class OpenCloseSensor extends Device {

    private boolean state = false;
    public OpenCloseSensor(String addr, String radio, String type) {
        super(addr, radio, type);
    }

    public OpenCloseSensor(String addr, String radio, String type, boolean state) {
        super(addr, radio, type);
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }
}
