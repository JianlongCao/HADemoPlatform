package com.cisco.demo.appplatform;

import java.util.ArrayList;

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

}


