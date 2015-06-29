package com.cisco.demo.appplatform;

import com.cisco.demo.core.Utils;
import com.cisco.demo.generaladapter.Device;
import com.cisco.demo.generaladapter.GeneralPlatform;
import com.cisco.demo.generaladapter.OnOffSwitch;
import com.cisco.demo.generaladapter.OpenCloseSensor;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

public class RuleEngine implements Runnable {

    List<Rules.SimpleRule> rules = new LinkedList<>();
    private boolean Running = false;

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

    @Override
    public void run() {
        while (Running) {
            for (Rules.SimpleRule rule : rules) {
                boolean matched = false;
                String action = rule.getIf().getAction();
                List<Device> devices = GeneralPlatform.Instance().get(rule.getIf().getAddr(), rule.getIf().getRadio());
                if (devices == null || devices.isEmpty()) continue;
                for (Device device : devices) {
                    if ((action.equals("open") || action.equals("close")) && device instanceof OpenCloseSensor) {
                        OpenCloseSensor openCloseSensor = (OpenCloseSensor) device;
                        if (openCloseSensor.getState() == true && action.equals("open") ||
                                openCloseSensor.getState() == false && action.equals("close")) {
                            matched = true;
                            break;
                        }
                    }
                }
                if (!matched) continue;

                devices = GeneralPlatform.Instance().get(rule.getThen().getAddr(), rule.getThen().getRadio());
                if (devices == null || devices.isEmpty()) continue;
                action = rule.getThen().getAction();
                for (Device device : devices) {
                    if ((action.equals("open") || action.equals("close")) && device instanceof OnOffSwitch) {
                        OnOffSwitch onOffSwitch = (OnOffSwitch) device;
                        if (action.equals("open"))
                            onOffSwitch.turnOn();
                        else
                            onOffSwitch.turnOff();
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

}
