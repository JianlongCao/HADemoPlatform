package com.cisco.demo.core;

import com.cisco.demo.comm.CommBucket;
import com.cisco.demo.comm.SimpleXMPPClient;
import org.jivesoftware.smack.packet.Message;

import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CmdHander {

    private static boolean          Running = false;
    private static SimpleXMPPClient client  = null;
    ConcurrentLinkedQueue<CommBucket> inputCmds  = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<String>     outputCmds = new ConcurrentLinkedQueue<>();
    public static String           touser   = "clg003";

    public static class cmdHandler implements Runnable {
        InputStream in;
        ConcurrentLinkedQueue<CommBucket> list = null;

        public cmdHandler() {
        }

        public void run() {
            while (Running) {
                if (client != null) {
                    Message m = client.nextMessage();
                    String message = m.getBody();
                    if(list == null || list.size() == 0 || message == null) continue;
                    for(CommBucket commBucket: list) {
                        commBucket.set(new String(message));
                    }
                }
            }
        }

        public cmdHandler setList(ConcurrentLinkedQueue<CommBucket> list) {
            this.list = list;
            return this;
        }
    }

    public static class cmdResponse implements Runnable {

        @Override
        public void run() {
            while(Running) {
                if(client != null || !outputCmds.isEmpty()) {
                    String msg = outputCmds.poll();
                    client.sendMessage(touser, msg);
                }
            }
        }
    }

    public void start() {
        Running = true;
        new Thread(new cmdHandler().setList(inputCmds)).start();
    }

    public void stop() {
        Running = false;
    }

    public CmdHander setXmppHelper(SimpleXMPPClient simpleXMPPClient) {
        client = simpleXMPPClient;
        return this;
    }

    public CmdHander setCommandList(CommBucket commBucket) {
        if(commBucket!=null)
            this.inputCmds.add(commBucket);
        return this;
    }

    public void sendCmd(String msg) {
        outputCmds.add(msg);
    }

}
