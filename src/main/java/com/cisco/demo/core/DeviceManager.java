package com.cisco.demo.core;

import com.cisco.demo.comm.CommBucket;
import com.cisco.demo.comm.SimpleXMPPClient;
import com.cisco.demo.device.Device;
import com.cisco.demo.device.Hue;
import com.cisco.demo.device.LevelSwitch;
import com.cisco.demo.device.OnOffSwitch;
import com.cisco.demo.entity.Entity;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class DeviceManager implements Observer {
    private CommBucket commBucket = new CommBucket();
    private CmdHander  cmdHander  = new CmdHander();
    private CmdParser  cmdParser  = new CmdParser();

    public static ConcurrentHashMap<String, Device> deviceLists = new ConcurrentHashMap<>();

    public static String server = "173.39.210.33";
    int port = 5222;
    public static String           user     = "clg002";
    public static String           touser   = "clg003";
    static        String           password = "Cisco123!";
    public static SimpleXMPPClient sc       = new SimpleXMPPClient(user + "@" + server, password);


    public void start() throws Exception {
        System.out.println("Device manager start");
        cmdHander.setCommandList(commBucket).setXmppHelper(sc).start();
        cmdParser.start();
        commBucket.addObserver(this);
        sc.sendMessage(touser + "@" + server, "test");
    }

    @Override
    public void update(Observable o, Object arg) {
        String cmd = ((CommBucket) o).get();
        cmdParser.addCmd(cmd);
    }

    public void stop() {
        System.out.println("Device manager stop");
        cmdHander.stop();
        cmdParser.stop();
        commBucket.deleteObservers();
	}
}
