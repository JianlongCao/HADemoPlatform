package com.cisco.demo.appplatform;

import com.cisco.demo.comm.SimpleXMPPClient;
import com.cisco.demo.core.Settings;
import com.cisco.demo.core.Utils;
import com.cisco.demo.generaladapter.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuleEngine implements Runnable {

    List<Rules.SimpleRule> rules = new LinkedList<>();
    private boolean Running = false;

    public static Map<String,DeviceListener> registerList = new ConcurrentHashMap<>();
    String userid = Settings.Instance().getGlobalConfig().getOpenhab().getUSERID();
    String server = Settings.Instance().getGlobalConfig().getOpenhab().getXMPPSERVER();

    public void addRule(Rules.SimpleRule rule) {
        if (rule != null) {
            rules.add(rule);
        }
    }

    public void start() {
        if (Running == false) {
            Running = true;
            new Thread(this).start();
        }
    }

    public void stop() {
        Running = false;
    }

    //push
    @Override
    public void run() {
        while(Running) {
            for (Rules.SimpleRule rule : rules) {
                String action = rule.getIf().getAction();
                String addr = rule.getIf().getAddr();
                String radio = rule.getIf().getRadio();
                if(registerList.get(addr) == null) {
                    List<Device> devices = GeneralPlatform.Instance().get(rule.getIf().getAddr(), rule.getIf().getRadio());
                    if(devices == null || devices.size() == 0) continue;
                    ArrayList<String> alias = devices.get(0).getNames();
                    if(alias != null && alias.size() >= 1) {
                        boolean bFindAlias = false;
                        for(String name : alias) {
                            if(name.contains(radio+ "_" + addr)) {
                                bFindAlias = true;
                            }
                        }

                        if(!bFindAlias){
                            devices.get(0).setName(radio+ "_" + addr);
                            continue;
                        } else {
                            DeviceListener deviceListener = new SimpleDeviceListener();
                            boolean ret = devices.get(0).registerListener(deviceListener);
                            if (ret)
                                registerList.put(addr, deviceListener);
                        }
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //poll
//    @Override
//    public void run() {
//        while (Running) {
//            for (Rules.SimpleRule rule : rules) {
//                boolean matched = false;
//                String action = rule.getIf().getAction();
//
//                List<Device> devices = GeneralPlatform.Instance().get(rule.getIf().getAddr(), rule.getIf().getRadio());
//                if (devices == null || devices.isEmpty()) continue;
//                for (Device device : devices) {
//                    //Action.find()
//                    Rules.Action ruleAction = Rules.Action.get(action);
//                    if(ruleAction != null) {
//                        Rules.ActionBelonging rulesSwitch = Rules.ActionBelonging.getActionBelonging(ruleAction);
//                        if(rulesSwitch == Rules.ActionBelonging.OPENCLOSESENSOR && device instanceof OpenCloseSensor) {
//                            OpenCloseSensor openCloseSensor = (OpenCloseSensor) device;
//                            if (openCloseSensor.getState() == true && ruleAction.getname().equals("open") ||
//                                    openCloseSensor.getState() == false && ruleAction.getname().equals("close")) {
//                                matched = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//                if (!matched) continue;
//
//
//                action = rule.getThen().getAction();
//                if(action == null) continue;
//                devices = GeneralPlatform.Instance().get(rule.getThen().getAddr(), rule.getThen().getRadio());
//                if (devices == null || devices.isEmpty()) continue;
//
//
//                for (Device device : devices) {
//                    Rules.Action ruleAction = Rules.Action.get(action);
//                    if(ruleAction == null) break;
//                    Rules.ActionBelonging rulesSwitch = Rules.ActionBelonging.getActionBelonging(ruleAction);
//                    if(rulesSwitch == Rules.ActionBelonging.ONOFFSWITCH && device instanceof OnOffSwitch) {
//                        OnOffSwitch onOffSwitch = (OnOffSwitch) device;
//                        if (ruleAction.getname().equals("on") && !onOffSwitch.getState())
//                            onOffSwitch.turnOn();
//                        else if(ruleAction.getname().equals("off") && onOffSwitch.getState())
//                            onOffSwitch.turnOff();
//                        else if(ruleAction.getname().equals("blink"))
//                            onOffSwitch.blink(rule.getThen().getBlink_times(),rule.getThen().getBlink_interval());
//                    }
//                    else if (rulesSwitch == Rules.ActionBelonging.COLORSWITCH && device instanceof ColorSwitch) {
//                        ColorSwitch colorSwitch = (ColorSwitch) device;
//                        if(ruleAction == Rules.Action.RED && !colorSwitch.isColor(ColorSwitch.COLOR.RED))
//                            colorSwitch.setColor(ColorSwitch.COLOR.RED);
//                        else if(ruleAction == Rules.Action.BLUE && !colorSwitch.isColor(ColorSwitch.COLOR.BLUE))
//                            colorSwitch.setColor(ColorSwitch.COLOR.BLUE);
//                        else if(ruleAction == Rules.Action.GREEN && !colorSwitch.isColor(ColorSwitch.COLOR.GREEN))
//                            colorSwitch.setColor(ColorSwitch.COLOR.GREEN);
//                    }
//                }
//            }
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    public void populateRules(String filename) {
        String json_rules = Utils.Instance().getFile(filename);
        if(!json_rules.equals("")) {
            Gson gson = new Gson();
            Rules rules = gson.fromJson(json_rules, Rules.class);
            if(rules != null && rules.getRules() != null) {
                for(Rules.SimpleRule rule:rules.getRules()) {
                    System.out.println("insert rule : " + rule);
                    this.addRule(rule);
                }
            }
        } else {
            System.out.println("No default rules file");
        }
    }

    public class SimpleDeviceListener implements DeviceListener {

        private  String addr;
        private boolean oldState = false;
        private int filter = 0;
        @Override
        public void changed(Device device) {

        }

        @Override
        public void changed(String addr) {

        }

        @Override
        public void changed(String addr, boolean state) {
            System.out.println("device status changed : "+ addr +" " + state);
            for (Rules.SimpleRule rule : rules) {
                if(!rule.getIf().getAddr().equals(addr))
                    continue;
                Rules.Action ruleAction = Rules.Action.get(rule.getIf().getAction());
                if(ruleAction!=null && ruleAction.getState() == state) {
                    //triggered
                    //add filter
                    if(filter != 0 && this.oldState == state && this.addr == addr)
                        return;
                    if(filter == 0) filter = 1;

                    String message = rule.getIf().getMessage();

                    if (message != null && !message.trim().equals("")) {
                        SimpleXMPPClient xmppClient = OpenHabPlatform.getXmppClient();
                        if (xmppClient != null)
                            xmppClient.sendMessage(userid + "@" + server, message);
                    }

                    String thenAction = rule.getThen().getAction();
                if(thenAction == null) continue;
                List<Device> devices = GeneralPlatform.Instance().get(rule.getThen().getAddr(), rule.getThen().getRadio
                        ());
                if (devices == null || devices.isEmpty()) continue;

                for (Device device : devices) {
                    ruleAction = Rules.Action.get(thenAction);
                    if(ruleAction == null) break;
                    Rules.ActionBelonging rulesSwitch = Rules.ActionBelonging.getActionBelonging(ruleAction);
                    if(rulesSwitch == Rules.ActionBelonging.ONOFFSWITCH && device instanceof OnOffSwitch) {
                        OnOffSwitch onOffSwitch = (OnOffSwitch) device;
                        if (ruleAction.getname().equals("on") && !onOffSwitch.getState())
                            onOffSwitch.turnOn();
                        else if(ruleAction.getname().equals("off") && onOffSwitch.getState())
                            onOffSwitch.turnOff();
                        else if(ruleAction.getname().equals("blink"))
                            onOffSwitch.blink(rule.getThen().getBlink_times(),rule.getThen().getBlink_interval());
                    }
                    else if (rulesSwitch == Rules.ActionBelonging.COLORSWITCH && device instanceof ColorSwitch) {
                        ColorSwitch colorSwitch = (ColorSwitch) device;
                        if(ruleAction == Rules.Action.RED && !colorSwitch.isColor(ColorSwitch.COLOR.RED))
                            colorSwitch.setColor(ColorSwitch.COLOR.RED);
                        else if(ruleAction == Rules.Action.BLUE && !colorSwitch.isColor(ColorSwitch.COLOR.BLUE))
                            colorSwitch.setColor(ColorSwitch.COLOR.BLUE);
                        else if(ruleAction == Rules.Action.GREEN && !colorSwitch.isColor(ColorSwitch.COLOR.GREEN))
                            colorSwitch.setColor(ColorSwitch.COLOR.GREEN);
                    }
                }
                }
            }

        }
    }

}
