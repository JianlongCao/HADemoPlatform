package com.cisco.demo.core;

import com.cisco.demo.appplatform.OpenHabPlatform;

import java.util.Scanner;

public class MainActivity {



    /**
     * @param args
     */
    public static void main(String[] args) {
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
        OpenHabPlatform openHabPlatform = new OpenHabPlatform();
        openHabPlatform.start();

        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter something to exit");
        String myint = keyboard.nextLine();

        openHabPlatform.stop();


    }

}
