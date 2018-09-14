package com.bufs.catbot;

public class MessageConverter {
	
	private static MessageConverter mc = new MessageConverter();
	
	//getInstance 정의
	public static MessageConverter getInstance() {
		
		if(mc != null) {
			
			mc = new MessageConverter();
			
		}
		
		return mc;
		
	}
	
	
	
	
	
	

	
	
	
	
	

}
