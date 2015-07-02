package com.cisco.demo.appplatform;

import com.cisco.demo.comm.SimpleXMPPClient;
import com.cisco.demo.core.Settings;
import com.cisco.demo.generaladapter.*;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class OpenHabPlatform implements AppInterface, Runnable{
    private static SimpleXMPPClient client = null;
    private LinkedBlockingDeque<String> cmdLists = null;
    boolean running = false;
    String user = Settings.Instance().getGlobalConfig().getOpenhab().getAPPID();
    String server = Settings.Instance().getGlobalConfig().getOpenhab().getXMPPSERVER();
    String pwd = Settings.Instance().getGlobalConfig().getOpenhab().getXMPPPWD();
    String touser = Settings.Instance().getGlobalConfig().getOpenhab().getOPENHABID();

    public OpenHabPlatform() {
        cmdLists = new LinkedBlockingDeque<>();
    }

    @Override
    public void start() {
        if(running == false){
            System.out.println("App: Waiting for connect OpenHab Server");
            running = true;
            client = new SimpleXMPPClient(user + "@" + server, pwd);
            System.out.println("App: OpenHab Server is starting");
            new Thread(this).start();
            new Thread(new CommandParser()).start();
        }
    }

    @Override
    public void stop() {
        running = false;
        client.disconnect();
        cmdLists.clear();
        System.out.println("App: OpenHab platform is stopped");
    }

    @Override
    public void run() {
        while(running) {
            if (client != null) {
                Message m = client.nextMessage();
                String message = m.getBody();
                if (message == null || message.trim().equals("")) continue;
                cmdLists.add(message);
            }
        }

    }

    public static SimpleXMPPClient getXmppClient() {
        return client;
    }
    private class CommandParser implements Runnable{

        @Override
        public void run() {
            while(running) {

                String msg = null;
                try {
                    msg = cmdLists.take();
                } catch (InterruptedException e) {
                    continue;
                }
                String[] tokens = msg.split(":");
                if (tokens.length < 2) {
                    System.out.println("Wrong command " + msg);
                    continue;
                }
                String cmd = tokens[0].trim();
                String id = tokens[1].trim();
                String value = null;
                if(tokens.length > 2)
                    value = tokens[2].trim();
                String[] datas = id.split("_");
                if(datas.length < 3) {
                    continue;
                }
                String code = datas[0];
                if(!code.equalsIgnoreCase("sctpa")) continue;
                System.out.println("From OpenHab: " + msg);
                String radio = datas[1];
                String iEEE = datas[2];

                if(cmd.equalsIgnoreCase("get")){
                    List<Device> devices = GeneralPlatform.Instance().get(iEEE, radio);
                    if(devices== null) continue;
                    for(Device device: devices) {
                        String ret = "SET:" + id + ":";
                        if(device instanceof OnOffSwitch && !id.contains("RGB")) {
                            ret += (((OnOffSwitch) device).getState() == true?"ON":"OFF");
                        } else if(device instanceof LevelSwitch && !id.contains("RGB")) {
                            ret += ((LevelSwitch) device).getLevel();
                        } else if(device instanceof ColorSwitch && id.contains("RGB")) {
                            ret += ((ColorSwitch) device).getHue()+","+((ColorSwitch) device).getSat() + "," + ((ColorSwitch) device).getBri();
                        } else {
                            continue;
                        }
                        client.sendMessage(touser + "@" + server, ret);
                        System.out.println("To OpenHab:" + ret);
                    }
                } else if(cmd.equalsIgnoreCase("set")) {
                    Map<String, Object> props = new HashMap<>();
                    if(value.equalsIgnoreCase("ON") || value.equalsIgnoreCase("OFF")){
                        props.put("onoff", value.equalsIgnoreCase("ON") ? true: false);
                    } else if(value.contains(",")) {
                        String[] tmps = value.split(",");
                        if(tmps.length != 3){
                            System.out.println("Wrong color value: " + value +" for device " + iEEE);
                            continue;
                        }
                        Map<String, Integer> hsb = new HashMap<>();
                        hsb.put("hue", Integer.parseInt(tmps[0]));
                        hsb.put("sat", Integer.parseInt(tmps[1]));
                        hsb.put("bri", Integer.parseInt(tmps[2]));
                        props.put("hsb", hsb);
                    } else{
                        try{
                            int level = Integer.parseInt(value);
                            props.put("level",level);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    GeneralPlatform.Instance().set(iEEE,radio,"",props);

                } else {
                    continue;
                }
            }
        }
    }
}
