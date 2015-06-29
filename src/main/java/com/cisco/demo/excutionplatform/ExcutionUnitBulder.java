package com.cisco.demo.excutionplatform;

import com.cisco.demo.core.Settings;
import com.cisco.demo.generaladapter.Device;

public class ExcutionUnitBulder {

    private Device device;

    public ExcutionUnitBulder(Device device) {
        this.device = device;
    }

    public ExcutionUnitBulder() {

    }

    public ExcutionUnit builder() {
        String eu = Settings.Instance().getGlobalConfig().getEXCUTIONUNIT();
        if(eu.equalsIgnoreCase("sctpa")) {
            return new SctpaExcutionUnit(device);
        }
        else return null;
    }

}
