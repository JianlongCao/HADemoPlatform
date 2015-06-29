package com.cisco.demo.generaladapter;


import com.cisco.demo.excutionplatform.ExcutionUnit;
import com.cisco.demo.excutionplatform.ExcutionUnitBulder;

public class OnOffSwitch extends Device {

    private boolean state = false;

    public OnOffSwitch(String addr, String radio, String type) {
        super(addr, radio, type);
    }

    public OnOffSwitch(String addr, String radio, String type, boolean state) {
        super(addr, radio, type);
        this.state = state;
    }
    /*
    { "actuators"[ {"type":"light", "state":"enabled", "measurement":[{"value":false,"name":"state"}]}] }
     */

    public void turnOn() {
        ExcutionUnit excutionUnit = new ExcutionUnitBulder(this).builder();
        excutionUnit.on();
        state = true;
    }

    public void turnOff() {
        ExcutionUnit excutionUnit = new ExcutionUnitBulder(this).builder();
        excutionUnit.off();
        state = false;
    }

    public void toggle() {
        state = !state;
        turn(state);
    }

    private void turn(boolean on) {
        ExcutionUnit excutionUnit = new ExcutionUnitBulder(this).builder();
        if(on)excutionUnit.on();
        else excutionUnit.off();
    }


    public boolean getState() {
        return state;
    }

}
