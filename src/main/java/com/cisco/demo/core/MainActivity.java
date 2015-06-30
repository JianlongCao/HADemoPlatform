package com.cisco.demo.core;

import com.cisco.demo.appplatform.OpenHabPlatform;
import com.cisco.demo.appplatform.RuleEngine;
import com.cisco.demo.comm.RestServer;

import java.util.Scanner;
import java.util.Set;

public class MainActivity {



    /**
     * @param args
     */
    public static void main(String[] args) {


        OpenHabPlatform openHabPlatform = new OpenHabPlatform();
        openHabPlatform.start();

        RuleEngine ruleEngine = new RuleEngine();
        ruleEngine.populateRules("rules.json");

        ruleEngine.start();
        RestServer.Instance().start();

        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter something to exit");
        String myint = keyboard.nextLine();

        RestServer.Instance().stop();
        ruleEngine.stop();
        openHabPlatform.stop();


        //palyground
//        String json = "{\n\t\"device_attrib\":\t{\n\t\t\"id\":\t\"\",\n\t\t\"radio\":\t\"zw\",\n\t\t\"addr\":\t\"4\",\n\t\t\"type\":\t\"\",\n\t\t\"connected\":\ttrue,\n\t\t\"authorized\":\tfalse,\n\t\t\"sensors\":\t[{\n\t\t\t\t\"type\":\t\"openclose\",\n\t\t\t\t\"state\":\t\"enabled\",\n\t\t\t\t\"measurement\":\t[{\n\t\t\t\t\t\t\"value\":\tfalse,\n\t\t\t\t\t\t\"unit\":\t\"\",\n\t\t\t\t\t\t\"timestamp\":\t\"2000-01-04:00:09:37.+0000\",\n\t\t\t\t\t\t\"name\":\t\"state\"\n\t\t\t\t\t}],\n\t\t\t\t\"subtype\":\t\"1\"\n\t\t\t}, {\n\t\t\t\t\"type\":\t\"battery\",\n\t\t\t\t\"state\":\t\"enabled\",\n\t\t\t\t\"measurement\":\t[{\n\t\t\t\t\t\t\"value\":\t80,\n\t\t\t\t\t\t\"unit\":\t\"%\",\n\t\t\t\t\t\t\"timestamp\":\t\"2000-01-04:00:09:37.+0000\",\n\t\t\t\t\t\t\"name\":\t\"battery\"\n\t\t\t\t\t}],\n\t\t\t\t\"subtype\":\t\"1\"\n\t\t\t}],\n\t\t\"actuators\":\t[]\n\t},\n\t\"result\":\t\"success\",\n\t\"href\":\t[\"/devices/zw/4\"],\n\t\"host_port\":\t\"http://fe80::205:4ff:fe03:201:8090\",\n\t\"timestamp\":\t\"2000-01-04T00:09:37Z\"\n}";
//
//                Gson gson = new Gson();
//        SctpaResponse responseData = gson.fromJson(json, SctpaResponse.class).setJSONRawPacket(json);
//        responseData.getDevice();
//        DeviceAttribute deviceAttribute = responseData.getDevice_attrib();


        //        DeviceManager deviceManager = new DeviceManager();
//        try {
//            deviceManager.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("enter something to exit");
//        String myint = keyboard.nextLine();
//
//        deviceManager.stop();


//

//        String json = "{ \n    \"device_attrib\":    { \n        \"id\":    \"\", \n        \"radio\":    \"zb\", " +
//                      "\n        \"addr\":    \"0017880100e7821e\", \n        \"type\":    \"light\", \n        \"connected\":    true, \n        \"authorized\":    false, \n        \"sensors\":    [], \n        \"actuators\":    [{ \n                \"type\":    \"light\", \n                \"state\":    \"enabled\", \n                \"measurement\":    [{ \n                        \"value\":    true, \n                        \"timestamp\":    \"2015-06-26T03:31:39Z\", \n                        \"name\":    \"state\" \n                    }, { \n                        \"value\":    { \n                            \"hue\":    34, \n                            \"saturation\":    114, \n                            \"level\":    96 \n                        }, \n                        \"unit\":    \"HSL256\", \n                        \"timestamp\":    \"2015-06-26T03:31:39Z\" \n                    }], \n                \"subtype\":    \"color\" \n            }] \n    }, \n    \"result\":    \"success\", \n    \"href\":    [\"/devices/zb/0017880100e7821e\"], \n    \"host_port\":    \"http://64.104.161.100:8090\", \n    \"timestamp\":    \"2015-06-26T03:31:39Z\" \n} \n";
//
//        Gson gson = new Gson();
//        ResponseData responseData = gson.fromJson(json, ResponseData.class).setJSONRawPacket(json);
//        DeviceAttribute deviceAttribute = responseData.getDevice_attrib();
//
//        System.out.println(gson.toJson(deviceAttribute));
//
//        System.out.println(deviceAttribute.getActuators().size());
//        JsonParser parser = new JsonParser();
//        JsonObject object = parser.parse(json).getAsJsonObject();
//        JsonElement acutators = (JsonElement)object.get("actuators");
//        JsonArray array = acutators.getAsJsonArray();
//        Iterator<JsonElement> jsonElementIterator = array.iterator();
//        while(jsonElementIterator.hasNext()){
//            JsonObject object1 = (JsonObject)jsonElementIterator.next();
//            System.out.println(object1);
//        }


    }





}
