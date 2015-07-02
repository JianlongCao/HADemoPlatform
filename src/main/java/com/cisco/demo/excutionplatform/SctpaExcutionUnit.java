package com.cisco.demo.excutionplatform;

import com.cisco.demo.comm.HTTPHelper;
import com.cisco.demo.comm.HttpCmd;
import com.cisco.demo.core.Settings;
import com.cisco.demo.core.Utils;
import com.cisco.demo.generaladapter.Device;
import com.cisco.demo.generaladapter.DeviceListener;
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
    String serverIP = Settings.Instance().getGlobalConfig().getSctpa().getSERVERIP();
    String hostIP = Settings.Instance().getGlobalConfig().getSctpa().getHOSTIP();

    public SctpaExcutionUnit(Device device) {
        this.device = device;
        if(!Utils.checkNotNull(hostIP)){
            try {
                hostIP = Utils.Instance().getIp();
            } catch (Exception e) {
            }
        }
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
        if (device == null) return;
        short h = (short) ((hue) * 1.0 / 360 * 255);
        short s = (short) ((sat) * 1.0 / 100 * 255);
        short b = (short) ((brightness));
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
        if(Utils.checkNotNull(addr) && Utils.checkNotNull(radio)) {
            String uri = serverIP + "/devices/" + radio + "/"
                    + addr;
            HttpResponse response = HTTPHelper.Instance().excuteHTTPCmd(new HttpCmd("get", uri, ""));
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
//            System.out.println("From SCTPA :" + jsonRes);
                Gson gson = new Gson();
                SctpaResponse responseData = gson.fromJson(jsonRes, SctpaResponse.class);
                responseData.setJSONRawPacket(jsonRes);
                return responseData.getDevice();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean registerListener(String addr, String radio, DeviceListener deviceListener) {

        if(Utils.checkNotNull(addr) && Utils.checkNotNull(radio) && deviceListener != null) {
            String registerJson = String.format("'{ \"auth_token\" : \"auth-token-auth_token_auth-token\", \"user_token\" : " +
                    "\"user-token-user_token_user-token\", \"uri\":\"%s/\",\"interests\" : { " +
                    "\"device_discovery\" : [ { \"radio\" : \"%s\" , \"notification_type\" : \"nonstop\" } ] , " +
                    "\"rest_uri\" : [ { \"uri\" : \"%s\", \"notification_type\" " +
                    ": \"nonstop\"} ] } }", radio, hostIP + ":8111", addr);
            HttpCmd httpCmd = new HttpCmd("post", serverIP + "/interests", registerJson);
            HttpResponse response = HTTPHelper.Instance().excuteHTTPCmd(httpCmd);
            //sctpa will return unkown error
//        if (response == null || response.getStatusLine().getStatusCode() != 200) {
//            return ;
//        }
            if(response == null) return false;
        }
        return true;
    }

    @Override
    public boolean setName(String addr, String radio, String name) {
        if(Utils.checkNotNull(name)) {
            String nameJson = String.format("\"{ \\\"devices\\\" : [ { \\\"device_attrib\\\" : { \\\"radio\\\" : " +
                    "\\\"%s\\\" , " +
                    "\\\"addr\\\" : \\\"%s\\\", \\\"sensor_type\\\" : \\\"motion\\\", \\\"sensor_subtype\\\" : " +
                    "\\\"1\\\" } , \\\"logical_attrib\\\" : { \\\"domain\\\" : \\\"alarm\\\" , \\\"location\\\" : " +
                    "\\\"frontdoor\\\" , \\\"class\\\" : \\\"motiondetect\\\" , \\\"name\\\" : \\\"%s\\\" , " +
                            "\\\"priority\\\" : 0 } } ] }\"",
                    radio,addr, name);
            HttpCmd httpCmd = new HttpCmd("post", serverIP + "/devices", nameJson);
            HttpResponse response = HTTPHelper.Instance().excuteHTTPCmd(httpCmd);
        }
        return true;
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
        HttpCmd httpCmd = new HttpCmd("put", serverIP + "/devices/" + device.getRadio()+ "/" + device.getAddr(), actuators_root.toString());
        HTTPHelper.Instance().addCmd(httpCmd);

    }


}
