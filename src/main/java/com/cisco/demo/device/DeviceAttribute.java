package com.cisco.demo.device;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeviceAttribute {
    String  id;
    String  radio;
    String  addr;
    String  type;
    boolean connected;
    boolean authorized;

    ArrayList<Actuators> actuators = new ArrayList<>();
//    ArrayList<Sensors>   sensors;

    public static class Actuators {
        String                type;
        String                state;
        ArrayList<JsonObject> measurement;
        String                subtype;
        Measurement_State measurement_state = new Measurement_State();
        Measurement_Value measurement_value = new Measurement_Value();

    }

    public static class Sensors {
        String                type;
        String                state;
        ArrayList<JsonObject> measurements;
        String                subtype;
    }

//    public static class Measurement {
//        ArrayList<JsonElement>
//    }

    public static class Measurement_State {
        boolean value;
        String  unit;
        String  timestamp;
        String  name;
    }

    public static class Measurement_Value {
        JsonObject value;
        String     unit;
        String     timestamp;
        public Measurement_Value_Light measurement_value_light = new Measurement_Value_Light();
    }

    public static class Measurement_Value_Light {
        short hue;
        short saturation;
        short level;
    }

    public String getId() {
        return id;
    }

    public String getRadio() {
        return radio;
    }

    public String getAddr() {
        return addr;
    }

    public String getType() {
        return type;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public ArrayList<Actuators> getActuators() {
        return actuators;
    }

    @Override
    public String toString() {
        return "DeviceAttribute{" +
                "authorized=" + authorized +
                ", id='" + id + '\'' +
                ", radio='" + radio + '\'' +
                ", addr='" + addr + '\'' +
                ", type='" + type + '\'' +
                ", connected=" + connected +
                '}';
    }
}


