package com.cisco.demo.comm;

import org.jivesoftware.smack.packet.Message;

public interface MessageListener {
	
	public void processMessage(Message m);

}
