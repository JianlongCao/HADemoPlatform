package com.cisco.demo.device;

import com.cisco.demo.comm.HTTPHelper;
import com.cisco.demo.comm.HttpCmd;
import com.cisco.demo.entity.Entity;
import com.cisco.demo.entity.consts;
import org.json.JSONArray;
import org.json.JSONObject;

public class LevelSwitch extends Device {

    public static String type = "LevelSwitch";

    public LevelSwitch(Entity entity) {
        super(entity);
    }

    /**
     * { "actuators":[ {"type":"light","state":"enabled", "measurement":[{"value":{"level":0}}]}] }
     *
     * @param level
     */
    public void setLevel(short level) {
        System.out.println("set level");

        JSONArray messure_array = new JSONArray();
        JSONObject messure_value = new JSONObject();
        JSONObject level_value = new JSONObject();
        level_value.put("level", new Integer(level % 256));
        messure_value.put("value", level_value);
        messure_array.put(messure_value);

        JSONObject actuators_value = new JSONObject();
        actuators_value.put("type", new String(getParent().getType()));
        actuators_value.put("state", new String("enabled"));
        actuators_value.put("measurement", messure_array);

        JSONArray actuators_array = new JSONArray();
        actuators_array.put(actuators_value);

        JSONObject actuators_root = new JSONObject();
        actuators_root.put("actuators", actuators_array);

        System.out.println(actuators_root);

        HttpCmd httpCmd = new HttpCmd("put",
                                      consts.SERVERIP + "/devices/" + getParent()
                                              .getRadio() + "/" + getParent().getIEEE(),
                                      actuators_root.toString());
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
    public Device getLevelSwitch() {
        return this;
    }
}
