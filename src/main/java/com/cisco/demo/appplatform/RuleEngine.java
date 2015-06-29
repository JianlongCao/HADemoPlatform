package com.cisco.demo.appplatform;

import com.cisco.demo.core.Utils;
import com.cisco.demo.generaladapter.Device;
import com.cisco.demo.generaladapter.GeneralPlatform;
import com.cisco.demo.generaladapter.OnOffSwitch;
import com.cisco.demo.generaladapter.OpenCloseSensor;
import com.google.gson.Gson;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;

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
                    //Action.find()
                    Rules.Action ruleAction = Rules.Action.get(action);
                    if(ruleAction != null) {
                        Rules.ActionBelonging rulesSwitch = Rules.ActionBelonging.getActionBelonging(ruleAction);
                        if(rulesSwitch == Rules.ActionBelonging.OPENCLOSESENSOR && device instanceof OpenCloseSensor) {
                            OpenCloseSensor openCloseSensor = (OpenCloseSensor) device;
                            if (openCloseSensor.getState() == true && ruleAction.getname().equals("open") ||
                                    openCloseSensor.getState() == false && ruleAction.getname().equals("close")) {
                                matched = true;
                                break;
                            }
                        }
                    }
                }
                if (!matched) continue;

                devices = GeneralPlatform.Instance().get(rule.getThen().getAddr(), rule.getThen().getRadio());
                if (devices == null || devices.isEmpty()) continue;
                action = rule.getThen().getAction();
                for (Device device : devices) {
                    Rules.Action ruleAction = Rules.Action.get(action);
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
