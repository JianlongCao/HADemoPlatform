package com.cisco.demo.device;

import com.cisco.demo.entity.Entity;
import com.google.gson.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ResponseData {
    private DeviceAttribute device_attrib = null;
    private String          result;
    private String          href;
    private String          host_port;
    private String          timestamp;
    private String          json="";

    public ResponseData setJSONRawPacket(String json) {
        this.json = json;
        return this;
    }

    public DeviceAttribute getDevice_attrib() {

        initForArbitraryMeasurements();

        return device_attrib;
    }

    public Device[] getDevice() {
        DeviceAttribute deviceAttribute = getDevice_attrib();
        Entity entity = new Entity(deviceAttribute.getAddr(), 0, deviceAttribute.getRadio(), deviceAttribute.getId(),
                deviceAttribute.getType(), deviceAttribute.getType(), false, "", "", "");
        ArrayList<Device> devices = new ArrayList<>();
        for(DeviceAttribute.Actuators actuator :deviceAttribute.getActuators()) {
            if(actuator.type.equalsIgnoreCase(Hue.type)){
                DeviceAttribute.Measurement_Value value = actuator.measurement_value;
                Hue hue = new Hue(entity.setStatus(actuator.measurement_state.value));
                LevelSwitch levelSwitch = (LevelSwitch)hue.getLevelSwitch();
                levelSwitch.setDefaultLevel(value.measurement_value_light.level);
                hue.setDefaultHue(value.measurement_value_light.hue).setDefaultSat(value.measurement_value_light.saturation);
                devices.add(hue);
            }
        }
        return devices.toArray(new Device[devices.size()]);
    }

    public String getResult() {
        return result;
    }

    public String getHref() {
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
                    System.out.println(measurement.toString());
                    JsonElement obj_name = measurement.get("name");
                    if(measurement.toString().contains("level") || measurement.toString().contains("hue")) {
                        System.out.println("light value");
                        device_attrib.getActuators().get(actuator_index).measurement.set(measurement_index, measurement);

                        Gson gson = new Gson();
                        DeviceAttribute.Measurement_Value_Light light = gson.fromJson(measurement.get("value").toString(),
                                                                                DeviceAttribute.Measurement_Value_Light
                                                                                        .class);
                        device_attrib.getActuators().get(actuator_index).measurement_value.measurement_value_light = light;
                    }
                    if(obj_name != null && obj_name.getAsString().equals("state")){
                        System.out.println("state member");
                        Gson gson = new Gson();
                        DeviceAttribute.Measurement_State state = gson.fromJson(measurement.toString(),
                                                                                DeviceAttribute.Measurement_State
                                .class);
                        device_attrib.getActuators().get(actuator_index).measurement_state = state;
                        System.out.println("state value :" + device_attrib.getActuators().get(actuator_index)
                                .measurement_state.value);
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