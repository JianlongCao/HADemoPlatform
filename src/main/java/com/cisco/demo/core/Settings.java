package com.cisco.demo.core;

import com.google.gson.Gson;

public class Settings {
    private static Settings settings = null;
    private GlobalConfig globalConfig = null;

    public static Settings Instance() {
        if(settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    private Settings() {
        String gb_settings = Utils.Instance().getFile("settings.json");
        Gson gson = new Gson();
        globalConfig = gson.fromJson(gb_settings, GlobalConfig.class);
        if(globalConfig == null) globalConfig = new GlobalConfig();
        System.out.println(globalConfig.toString());
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    /*
      "sctpa":{
    "SERVERIP" : "http://64.104.161.59:8090"
  },

  "EXCUTIONUNIT" : "sctpa",

  "openhab": {
    "XMPPSERVER": "173.39.210.33",
    "XMPPPORT": 5222,
    "APPID": "clg002",
    "OPENHABID": "clg003",
    "XMPPPWD": "Cisco123!"
  }
     */
    public static class GlobalConfig{
        private SCTPA sctpa = new SCTPA();
        private String EXCUTIONUNIT = "sctpa";
        private OPENHAB openhab = new OPENHAB();

        public SCTPA getSctpa() {
            return sctpa;
        }

        public OPENHAB getOpenhab() {
            return openhab;
        }

        public String getEXCUTIONUNIT() {
            return EXCUTIONUNIT;
        }

        @Override
        public String toString() {
            return "GlobalConfig{" +
                    "sctpa=" + sctpa +
                    ", EXCUTIONUNIT='" + EXCUTIONUNIT + '\'' +
                    ", openhab=" + openhab +
                    '}';
        }

        public static class OPENHAB {
            private String XMPPSERVER = "173.39.210.33";
            private int XMPPPORT = 5222;
            private String APPID = "clg002";
            private String OPENHABID = "clg003";
            private String USERID  = "clg001";
            private String XMPPPWD = "Cisco123!";

            public String getUSERID() {
                return USERID;
            }
            public String getXMPPSERVER() {
                return XMPPSERVER;
            }

            public int getXMPPPORT() {
                return XMPPPORT;
            }

            public String getAPPID() {
                return APPID;
            }

            public String getOPENHABID() {
                return OPENHABID;
            }

            public String getXMPPPWD() {
                return XMPPPWD;
            }

            @Override
            public String toString() {
                return "OPENHAB{" +
                        "XMPPSERVER='" + XMPPSERVER + '\'' +
                        ", XMPPPORT=" + XMPPPORT +
                        ", APPID='" + APPID + '\'' +
                        ", OPENHABID='" + OPENHABID + '\'' +
                        ", XMPPPWD='" + XMPPPWD + '\'' +
                        '}';
            }
        }

        public static class SCTPA {
            private String SERVERIP = "http://64.104.161.59:8090";
            private String HOSTIP = null;

            public String getHOSTIP() {
                return HOSTIP;
            }

            public String getSERVERIP() {
                return SERVERIP;
            }

            @Override
            public String toString() {
                return "SCTPA{" +
                        "SERVERIP='" + SERVERIP + '\'' +
                        '}';
            }
        }
    }
}
