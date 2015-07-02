package com.cisco.demo.excutionplatform;

import com.cisco.demo.generaladapter.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Iterator;

public class SctpaResponse {
    private SctpaDeviceAttribute device_attrib = null;
    private String          result;
    private ArrayList<String>          href;
    private String          host_port;
    private String          timestamp;
    private String          json="";

    public SctpaResponse setJSONRawPacket(String json) {
        this.json = json;
        return this;
    }

    public SctpaDeviceAttribute getDevice_attrib() {

        initForArbitraryMeasurements();

        return device_attrib;
    }

    public ArrayList<Device> getDevice() {
        SctpaDeviceAttribute deviceAttribute = getDevice_attrib();
        ArrayList<Device> devices = new ArrayList<>();
        for(SctpaDeviceAttribute.Actuators actuator :deviceAttribute.getActuators()) {
            if(actuator.measurement_state != null){
                OnOffSwitch onOffSwitch = new OnOffSwitch(deviceAttribute.getAddr(), deviceAttribute.getRadio(), actuator.type, actuator.measurement_state.value);
                devices.add(onOffSwitch);
            }
            if(actuator.measurement_value != null) {
                if(actuator.measurement_value.measurement_value_light!=null){
                    ColorSwitch colorSwitch = new ColorSwitch(deviceAttribute.getAddr(),
                            deviceAttribute.getRadio(),
                            actuator.type,
                            (int)(actuator.measurement_value.measurement_value_light.hue*360.0/255),
                            (int)(actuator.measurement_value.measurement_value_light.saturation*1.0/255*100),
                            (int)(actuator.measurement_value.measurement_value_light.level * 1.0 / 255 * 100));
                    devices.add(colorSwitch);
                    LevelSwitch levelSwitch = new LevelSwitch(deviceAttribute.getAddr(),
                            deviceAttribute.getRadio(),
                            actuator.type,
                            (int)(actuator.measurement_value.measurement_value_light.level * 1.0 / 255 * 100));
                    devices.add(levelSwitch);
                }
            }

        }

        for(SctpaDeviceAttribute.Sensors sensor :deviceAttribute.getSensors()) {
            if(sensor.type.equalsIgnoreCase("openclose")){
                if(sensor.measurement != null) {
                    for(SctpaDeviceAttribute.Measurement_Sensor measurement_sensor : sensor.measurement) {
                        if(measurement_sensor.name.equalsIgnoreCase("state")) {
                            if((Boolean)measurement_sensor.value instanceof  Boolean)
                                devices.add(new OpenCloseSensor(deviceAttribute.getAddr(),deviceAttribute.getRadio(),"",(Boolean)measurement_sensor.value));
                        }
                    }
                }
            }
        }
        if(href != null && href.size() >1) {
            for(Device device :devices) {
                device.names = href;
            }
        }

        return devices;
    }

    public String getResult() {
        return result;
    }

    public ArrayList<String> getHref() {
        return href;
    }

    public String getHost_port() {
        return host_port;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getJson() {
        return json;
    }

    private JsonObject parseDeviceAttrib() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonElement je_device_attr = object.get("device_attrib");
        if(je_device_attr == null) return null;
        return je_device_attr.getAsJsonObject();
    }

    private JsonArray parseActuators() {
        JsonObject da = parseDeviceAttrib();
        JsonElement je_actuators = da.get("actuators");
        if(je_actuators == null) return null;
        return je_actuators.getAsJsonArray();
    }


    private void initForArbitraryMeasurements() {
        JsonArray acutators = parseActuators();
        if(acutators != null) {
            Iterator<JsonElement> actuatorsIterator = acutators.iterator();
            int actuator_index = 0;
            while (actuatorsIterator.hasNext()) {

                JsonObject actuator = (JsonObject) actuatorsIterator.next();
                JsonArray measurements = actuator.get("measurement").getAsJsonArray();
                Iterator<JsonElement> measurementsIterator = measurements.iterator();
                int measurement_index = 0;
                while(measurementsIterator.hasNext()) {
                    JsonObject measurement = measurementsIterator.next().getAsJsonObject();
//                    System.out.println(measurement.toString());
                    JsonElement obj_name = measurement.get("name");
                    if(measurement.toString().contains("level") || measurement.toString().contains("hue")) {
//                        System.out.println("light value");
                        device_attrib.getActuators().get(actuator_index).measurement.set(measurement_index, measurement);

                        Gson gson = new Gson();
                        SctpaDeviceAttribute.Measurement_Value_Light light = gson.fromJson(measurement.get("value").toString(),
                                                                                SctpaDeviceAttribute.Measurement_Value_Light
                                                                                        .class);
                        if(light == null) continue;
                        if(device_attrib.getActuators().get(actuator_index).measurement_value == null)
                            device_attrib.getActuators().get(actuator_index).measurement_value = new SctpaDeviceAttribute.Measurement_Value();
                        device_attrib.getActuators().get(actuator_index).measurement_value.measurement_value_light = light;
                    }
                    if(obj_name != null && obj_name.getAsString().equals("state")){
//                        System.out.println("state member");
                        Gson gson = new Gson();
                        SctpaDeviceAttribute.Measurement_State state = gson.fromJson(measurement.toString(),
                                                                                SctpaDeviceAttribute.Measurement_State
                                .class);
                        if(state == null) continue;
                        device_attrib.getActuators().get(actuator_index).measurement_state = state;
//                        System.out.println("state value :" + device_attrib.getActuators().get(actuator_index)
//                                .measurement_state.value);
                        device_attrib.getActuators().get(actuator_index).measurement.set(measurement_index,measurement);
                    }

//                    Gson gson = new Gson();
//                    ResponseData responseData = gson.fromJson(json, DeviceAttribute.Actuators..class)
//                    .setJSONRawPacket(json);
                    measurement_index++;
                }
                actuator_index++;
            }
        }
    }
}