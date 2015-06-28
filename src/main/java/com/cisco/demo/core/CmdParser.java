package com.cisco.demo.core;

import com.cisco.demo.comm.HTTPHelper;
import com.cisco.demo.comm.HttpCmd;
import com.cisco.demo.device.*;
import com.cisco.demo.entity.Entity;
import com.cisco.demo.entity.consts;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CmdParser implements Runnable {

    private static boolean                       Running = false;
    private        ConcurrentLinkedQueue<String> cmdList    = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        while (Running) {
            if(cmdList.isEmpty()) continue;
            String cmd = cmdList.poll();
            parseCmd(cmd);
            System.out.println("finished handle cmd :" + cmd);
        }
    }

    public void start() {
        Running = true;
        new Thread(this).start();
    }

    private void parseCmd(String msg) {
        if(msg == null || msg.equals(""))
            return;
        else {
            String[] tokens = msg.split(":");
            if (tokens.length < 2) {
                System.out.println("Wrong command " + msg);
                return;
            }
        }
        String[] tokens = msg.split(":");
        if (tokens.length < 2) {
            System.out.println("Wrong command " + msg);
            return;
        }
        String cmd = tokens[0].trim();
        String id = tokens[1].trim();

        if(id.endsWith("_RGB")) {
            id = id.substring(0, (id.length() - 4));
        }

        String[] datas = id.split("_");
        if(datas.length < 3) {
            return;
        }
        String Code = datas[0];
        if(!Code.equalsIgnoreCase("sctpa")) return;
        String radio = datas[1];
        String iEEE = datas[2];


//        Entity entity = WebUtil.getInstance().getDevice(id);
        Entity entity = new Entity("12345678", 1, "zb", "test", "testcatorgory", "hue", true, "on", "home","");
        if (cmd.equals("GET")) {
            // GET:SCTPA_ZB_0017880100E7821E_11_RGB
            getDevice(iEEE,radio);
            String res = getResopnseString(entity);
//            cmdHander.sendCmd(res);
        } else if (cmd.equals("SET")) {
            if (tokens.length < 3) {
                return;
            }
            String state = tokens[2];




            String actionName = "";
            Device newDevice = null;
            if(state.equalsIgnoreCase("ON") || state.equalsIgnoreCase("OFF")){
                actionName = "ONOFF";
                newDevice = new OnOffSwitch(new Entity(iEEE, DeviceManager.deviceLists.size(), radio, id,
                                                       OnOffSwitch.type, OnOffSwitch.type, true, null, "", ""));

            } else if(state.contains(",")) {
                actionName = "COLOR";
                newDevice = new Hue(new Entity(iEEE, DeviceManager.deviceLists.size(), radio, id,
                                                       Hue.type, Hue.type, true, null, "", ""));
            } else {
                actionName = "LEVEL";
                newDevice = new LevelSwitch(new Entity(iEEE, DeviceManager.deviceLists.size(), radio, id,
                                                       LevelSwitch.type, LevelSwitch.type, true, null, "", ""));
            }
            DeviceManager.deviceLists.put(iEEE, newDevice);

            System.out.println("set device id = " + id + ", state = "+ state);

            setDevice(iEEE, actionName, state);
//            WebUtil.getInstance().setDevice(id, state, state);
        }

    }

    private String getResopnseString(Entity entity) {
        return "";
    }
    private boolean setDevice(String ieee, String name, String value) {
        boolean ret = true;
        Device base = DeviceManager.deviceLists.get(ieee);
        if(base == null) return false;

        if(name.equalsIgnoreCase("ONOFF")) {
            OnOffSwitch onOffSwitch = (OnOffSwitch) base.getOnOffSwitch();
            if(onOffSwitch == null)
                return false;
            if(value.equalsIgnoreCase("ON")) {
                onOffSwitch.turnOn();
            } else if(value.equalsIgnoreCase("OFF")){
                onOffSwitch.turnOff();
            } else return false;
        } else if(name.equalsIgnoreCase("LEVEL")) {
            LevelSwitch levelSwitch = (LevelSwitch)base.getLevelSwitch();
            if(levelSwitch == null) return false;
            try {
                short level = (short)(Short.parseShort(value) * 2.55);
                levelSwitch.setLevel(level);
            } catch(java.lang.NumberFormatException e){
                return false;
            }
        } else if(name.equalsIgnoreCase("COLOR")) {
            Hue hue = (Hue) base;
            if(hue == null) return false;
            String[] tmps = value.split(",");
            if(tmps.length != 3){
                System.out.println("Wrong color value: " + value +" for device " + ieee);
                return false;
            }

            short h = (short)(Integer.parseInt(tmps[0])*1.0/360*255);
            short s = (short)(Integer.parseInt(tmps[1])*1.0/100*255);
            short b = (short)(Integer.parseInt(tmps[2])*1.0/100*255);

            hue.setColor(h, s);
            LevelSwitch levelSwitch = (LevelSwitch)(hue.getLevelSwitch());
            levelSwitch.setLevel(b);
        }


        return ret;
    }

    private Device getDevice(String ieee, String radio) {
        HttpResponse response = HTTPHelper.Instance().excuteHTTPCmd(new HttpCmd("get",
                                                                              consts.SERVERIP+"/devices/"+radio +"/"
                                                                            +ieee,""));
        if (response.getStatusLine().getStatusCode() != 200) {
            return null;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            Gson gson = new Gson();
            ResponseData responseData = gson.fromJson(output, ResponseData.class).setJSONRawPacket(output);
            DeviceAttribute deviceAttribute = responseData.getDevice_attrib();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void addCmd(String cmd) {
        cmdList.add(cmd);
    }

    public void stop() {
        Running = false;
    }

}
