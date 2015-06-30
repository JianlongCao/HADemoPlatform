package com.cisco.demo.comm;

import com.cisco.demo.appplatform.RuleEngine;
import com.cisco.demo.excutionplatform.SctpaDeviceAttribute;
import com.cisco.demo.generaladapter.DeviceListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.language.bm.Rule;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.ArrayList;

public class SctpaServerResource extends ServerResource{

    @Get
    public String represent() {
        return "hello world";
    }

    @Put
    public void representPut(Representation representation) {
        System.out.println(representation);
        try {
            System.out.println(representation.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Post
    public void representPost(Representation representation) {
        try {
            String content = representation.getText();
            if(content == null || content.trim().equals("")) return;
            System.out.println("receive interest :" + content);
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(content).getAsJsonObject();
            JsonElement je_device_attr = object.get("device_attrib");
            if(je_device_attr == null) return;
            JsonObject device_attr = je_device_attr.getAsJsonObject();
            Gson gson = new Gson();
            SctpaDeviceAttribute sda = gson.fromJson(device_attr.toString(), SctpaDeviceAttribute.class);
            if(sda != null) {
                String addr = sda.getAddr();
                String radio = sda.getRadio();
                DeviceListener deviceListener = RuleEngine.registerList.get(radio + "/" +addr);
                if(deviceListener == null) return;
                JsonElement je_sensors = object.get("sensors");
                if(je_sensors == null) return;
                SctpaDeviceAttribute.Sensors sensor = gson.fromJson(je_sensors.toString(), SctpaDeviceAttribute
                        .Sensors.class);
                if(sensor!= null){
                    ArrayList<SctpaDeviceAttribute.Measurement_Sensor> measurements = sensor.getMeasurement();
                    if(measurements == null || measurements.size() ==0 ) return;
                    for(SctpaDeviceAttribute.Measurement_Sensor measurement : measurements) {
                        if(measurement.getName().equals("state") && measurement.getValue() instanceof Boolean) {
                            deviceListener.changed(addr, (boolean)measurement.getValue());
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
