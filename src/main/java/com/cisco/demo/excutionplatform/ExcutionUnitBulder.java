package com.cisco.demo.excutionplatform;

import com.cisco.demo.core.consts;
import com.cisco.demo.generaladapter.Device;

public class ExcutionUnitBulder {

    private Device device;

    public ExcutionUnitBulder(Device device) {
        this.device = device;
    }

    public ExcutionUnitBulder() {

    }

    public ExcutionUnit builder() {
        if(consts.EXCUTIONUNIT.equalsIgnoreCase("sctpa")) {
            return new SctpaExcutionUnit(device);
        }
        else return null;
    }

}
