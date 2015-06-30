package com.cisco.demo.generaladapter;

public interface DeviceListener {

    public void changed(Device device);

    public void changed(String addr);

    public void changed(String addr, boolean state);
}
