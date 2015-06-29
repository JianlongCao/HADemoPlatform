package com.cisco.demo.excutionplatform;

import com.cisco.demo.comm.HTTPHelper;
import com.cisco.demo.comm.HttpCmd;
import com.cisco.demo.core.consts;
import com.cisco.demo.generaladapter.Device;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SctpaExcutionUnit implements ExcutionUnit{

    private Device device = null;

    public SctpaExcutionUnit(Device device) {
        this.device = device;
    }

    public SctpaExcutionUnit() {

    }

    @Override
    public void on() {
        if(device == null) return;
        turn(true);
    }

    @Override
    public void off() {
        if(device == null) return;
        turn(false);
    }

    @Override
    public void level(int level) {
        if(device == null) return;
        level = (int)(level*2.55);
        Map<String, Object> props = new HashMap<>();
        JSONObject level_value = new JSONObject();
        level_value.put("level", new Integer(level % 256));
        props.put("value", level_value);
        packageData(props);
    }

    @Override
    public void color(int hue, int sat, int brightness) {
        if(device == null) return;
        short h = (short)((hue)*1.0/360*255);
        short s = (short)((sat)*1.0/100*255);
        short b = (short)((brightness));
        JSONObject color_value = new JSONObject();
        color_value.put("hue", new Short((short) h));
        color_value.put("saturation", new Short((short) s));
        Map<String, Object> props = new HashMap<>();
        props.put("value", color_value);
        packageData(props);
        level(b);
    }

    @Override
    public List<Device> get(String addr, String radio) {
        HttpResponse response = HTTPHelper.Instance().excuteHTTPCmd(new HttpCmd("get",
                consts.SERVERIP+"/devices/"+radio +"/"
                        +addr,""));
        if (response == null || response.getStatusLine().getStatusCode() != 200) {
            System.out.println("ERROR:Sctpa Server not available or get error response");
            return null;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            String output;
            String jsonRes = "";
            while ((output = br.readLine()) != null) {
                jsonRes += output;
            }
            System.out.println("From SCTPA :" + jsonRes);
            Gson gson = new Gson();
            SctpaResponse responseData = gson.fromJson(jsonRes, SctpaResponse.class);
            responseData.setJSONRawPacket(jsonRes);
            return responseData.getDevice();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void turn(boolean state) {
        Map<String, Object> props = new HashMap<>();
        props.put("value", new Boolean(state));
        props.put("name", new String("state"));
        packageData(props);
    }

    private void packageData(Map<String,Object> props) {
        if(props.isEmpty())return;
        JSONArray measure_array = new JSONArray();
        JSONObject measure_value = new JSONObject();
        for (Map.Entry<String, Object> entry : props.entrySet())
        {
            measure_value.put(entry.getKey(), entry.getValue());
        }
        measure_array.put(measure_value);

        JSONObject actuators_value = new JSONObject();
        actuators_value.put("type", new String(device.getType()));
        actuators_value.put("state", new String("enabled"));
        actuators_value.put("measurement", measure_array);

        JSONArray actuators_array = new JSONArray();
        actuators_array.put(actuators_value);

        JSONObject actuators_root = new JSONObject();
        actuators_root.put("actuators",actuators_array);

        System.out.println("To SCTPA :" + actuators_root.toString());
        HttpCmd httpCmd = new HttpCmd("put", consts.SERVERIP + "/devices/" + device.getRadio()+ "/" + device.getAddr(), actuators_root.toString());
        HTTPHelper.Instance().addCmd(httpCmd);

    }


}
