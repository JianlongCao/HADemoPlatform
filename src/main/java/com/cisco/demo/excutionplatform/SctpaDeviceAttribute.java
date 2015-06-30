package com.cisco.demo.excutionplatform;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class SctpaDeviceAttribute {
    String  id;
    String  radio;
    String  addr;
    String  type;
    boolean connected;
    boolean authorized;

    ArrayList<Actuators> actuators = new ArrayList<>();
    ArrayList<Sensors>   sensors = new ArrayList<>();

    public static class Actuators {
        String                type;
        String                state;
        ArrayList<JsonObject> measurement;
        String                subtype;
        Measurement_State measurement_state = null;
        Measurement_Value measurement_value = null;

    }

    public static class Sensors {
        String                type;
        String                state;
        ArrayList<Measurement_Sensor> measurement = null;
        String                subtype;

        public String getType() {
            return type;
        }

        public String getState() {
            return state;
        }

        public ArrayList<Measurement_Sensor> getMeasurement() {
            return measurement;
        }

        public String getSubtype() {
            return subtype;
        }
    }

//    public static class Measurement {
//        ArrayList<JsonElement>
//    }

    public static class Measurement_Sensor {
        Object value;
        String unit;
        String timestamp;
        String name;

        public Object getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

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
        public Measurement_Value_Light measurement_value_light = null;
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

    public ArrayList<Sensors> getSensors() {
        return sensors;
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


