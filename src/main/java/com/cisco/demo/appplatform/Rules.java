package com.cisco.demo.appplatform;

import java.util.*;

/**
 * {
 *     "If":{
 *         "addr":"12345678",
 *         "action":"open",
 *         "name":"ContactSwitch" (Optional)
 *         },
 *     "Then: {
 *          "addr":"87654321",
 *          "action":"close",
 *          "name":"Hue"
 *     }
 * }
 */
public class Rules {
    ArrayList<SimpleRule> rules;

    public ArrayList<SimpleRule> getRules() {
        return rules;
    }

    public static class SimpleRule {

        private Target If;
        private Target Then;

        public Target getIf() {
            return If;
        }

        public Target getThen() {
            return Then;
        }

        @Override
        public String toString() {
            return "Rule ---- " +
                    "If " + If.name + " is " + If.action +
                    ", Then " + Then.name + " will " + Then.action;
        }

        public static class Target {
            private String addr;
            private String radio;
            private String action;
            private String name;
            private int blink_times = 3;
            private int blink_interval = 200;

            public int getBlink_times() {
                return blink_times;
            }

            public int getBlink_interval() {
                return blink_interval;
            }

            public String getAddr() {
                return addr;
            }

            public String getRadio() {
                return radio;
            }

            public String getAction() {
                return action;
            }

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return "Target{" +
                        "addr='" + addr + '\'' +
                        ", radio='" + radio + '\'' +
                        ", action='" + action + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }
    }

    public enum Action {
        OPEN("open"),
        CLOSE("close"),
        BLINK("blink"),
        RED("red"),
        GREEN("green"),
        BLUE("blue"),
        LEVEL("level"),
        ON("on"),
        OFF("off");

        private final String name;
        Action(String name) {
            this.name = name;
        }

        public String getname() {
            return this.name;
        }
        // Reverse-lookup map for getting a day from an abbreviation
        private static final Map<String, Action> lookup = new HashMap<String, Action>();

        static {
            for (Action action : Action.values()) {
                lookup.put(action.getname(), action);
            }
        }

        public static Action get(String name) {
            return lookup.get(name);
        }

    }

    public enum ActionBelonging{
        ONOFFSWITCH("onoffswitch", Action.ON, Action.OFF, Action.BLINK),
        COLORSWITCH("colorswitch", Action.RED, Action.BLUE, Action.GREEN),
        LEVELSWITCH("levelswitch", Action.LEVEL),
        OPENCLOSESENSOR("openclosesensor", Action.OPEN, Action.CLOSE),;


        private String       switchName;
        private Set<Action> actions;

        ActionBelonging(String switchName, Action... a) {
            actions = new HashSet<Action>();
            this.switchName = switchName;

            for(Action action : a) {
                actions.add(action);
            }
        }

        public String getSwitchName() {
            return this.switchName;
        }

        public static  ActionBelonging getActionBelonging(Action a) {
            for(ActionBelonging actionBelonging : ActionBelonging.values()) {
                if(actionBelonging.actions.contains(a)) {
                    return actionBelonging;
                }
            }
            return null;
        }
    }

}


