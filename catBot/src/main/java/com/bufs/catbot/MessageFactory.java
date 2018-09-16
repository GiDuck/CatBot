package com.bufs.catbot;

public class MessageFactory {
	
	private static MessageFactory mc = new MessageFactory();
	
	//getInstance 정의
	public static MessageFactory getInstance() {
		
		if(mc != null) { mc = new MessageFactory(); }	
		return mc;
		
	}
	
	
	
	
	
	

	
	
	
	
	

}
