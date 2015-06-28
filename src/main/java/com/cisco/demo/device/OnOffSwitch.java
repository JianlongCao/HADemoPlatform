package com.cisco.demo.device;


import com.cisco.demo.comm.HTTPHelper;
import com.cisco.demo.comm.HttpCmd;
import com.cisco.demo.entity.Entity;
import com.cisco.demo.entity.consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class OnOffSwitch extends Device{

    private static boolean state = false;
    public static String type = "OnOffSwitch";

    public OnOffSwitch(Entity entity) {
        super(entity);
    }

    /*
    { "actuators"[ {"type":"light", "state":"enabled", "measurement":[{"value":false,"name":"state"}]}] }
     */

    public void turnOn() {
        turn(true);
    }

    public void turnOff() {
        turn(false);
    }

    public void toggle() {
        turn(state);
        state = !state;
    }

    private void turn(boolean on) {
        JSONArray messure_array = new JSONArray();
        JSONObject messure_value = new JSONObject();
        messure_value.put("value", new Boolean(on));
        messure_value.put("name", new String("state"));
        messure_array.put(messure_value);

        JSONObject actuators_value = new JSONObject();
        actuators_value.put("type", new String(getParent().getType()));
        actuators_value.put("state", new String("enabled"));
        actuators_value.put("measurement", messure_array);

        JSONArray actuators_array = new JSONArray();
        actuators_array.put(actuators_value);

        JSONObject actuators_root = new JSONObject();
        actuators_root.put("actuators",actuators_array);

        System.out.println(actuators_root);

        HttpCmd httpCmd = new HttpCmd("put", consts.SERVERIP + "/devices/" + getParent().getRadio()+ "/" + getParent
                ().getIEEE(), actuators_root.toString());
        HTTPHelper.Instance().addCmd(httpCmd);
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPut httpput = new HttpPut(consts.SERVERIP + "/devices/" + getParent().getRadio()+ "/" + getParent().getIEEE());
//        try {
//            StringEntity input = new StringEntity(actuators_root.toString());
//            input.setContentType("application/json");
//            httpput.setEntity(input);
//            client.execute(httpput);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public Device getOnOffSwitch() {
        return this;
    }
}
