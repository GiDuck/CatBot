package com.bufs.catbot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bufs.catbot.persistence.MongoBusDAO;

@Service
public class MongoBusService {

	@Autowired
	private MongoBusDAO busDAO;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	public static final int BUFS_SHUTTLE_TERM_DOMI = 2;
	public static final int BUFS_SHUTTLE_TERM_BEOMEOSA = 7;
	public static final int BUFS_SHUTTLE_TERM_NAMSAN = 10;
	public static final int BUFS_SHUTTLE_TERM_FIRESTATION = 13;

	


	
	 public Map<String, Object> getShuttleBusInfo(){
		 
		 Date now = new Date();
		 String nowTime = timeFormat.format(now);
		 List<String> timeTable = busDAO.getShuttleBusInfo(nowTime);
		 
		
		 
		 
		 
		 
		 
		 
		 
		 
		 return null;
		 
	 }
	 
	 
	 
	 public List<String> BusTimeChooser(List<String> busTable, String nowTime){
		
		 
		 
		 String[] timeArr = nowTime.split("/:");
		 int nowM = Integer.parseInt(timeArr[1]);
		 
		 List<String> choicedBus = new ArrayList<>();
		 
		 for(String time : busTable ) {
			 String[] tempTimeArr = time.split("/:");
			 int compM = Integer.parseInt(tempTimeArr[1]);
			 
			 if(nowM-compM < 18) {
				 
				 choicedBus.add(time);
				 
			 }	 
		 }
		 
		 return choicedBus;
		 
		 
		 
	 }
	 
	 
	 public String GetShuttleBusMessage(List<String> busTable) {
		 
		 String message = "부산외대 셔틀 버스 정보라냥. 출발시간 제외하고는 도로사정에 따라서 1~3분정도 차이가 날 수 있다냥. \n 버스가 안온다고 날 너무 탓하지 말라냥!! \n";
		 
		 for(String time : busTable) {
			 
			 String[] tempTimeArr = time.split("/:");
			 
			 int choicedH = Integer.parseInt(tempTimeArr[0]);
			 int choicedM = Integer.parseInt(tempTimeArr[1]);
			 
			 message += "외대에서 출발 " + timeFormatter(choicedH, choicedM) + "\n" +
					 	"외성생활관 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_DOMI) + "\n" +
					 	"범어사역 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_BEOMEOSA) + "\n" +
					 	"남산역 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_NAMSAN) + "\n" +
					 	"남산소방서 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_FIRESTATION) + "\n" +
					 	"------------------------------------------- \n";			 
			 
		 }
		 
		 
		 
		 return message;
		 
		 
		 
	 }
	 
	 
	 public String timeFormatter(int hour, int min) {
		 
		 boolean isAm = true;
		 String result;
		 String pHour;
		 String pMin;
		 
		 if(min >= 60) {
			 hour += 1;
			 min = (60-min);
		 }
		 
		 if(hour > 12) {
			 hour = hour-12;
			 isAm = false;
		 }
		 
		 pHour = String.valueOf(hour);
		 pMin = String.valueOf(min);
		 
		 if(hour<10) {
			 pHour = "0"+hour;
		 }
		 
		 if(min < 10) {
			 
			 pMin = "0"+min;
			 
		 }
		 
		 
		 if(isAm)
		 result = "AM " +  pHour + " : " + pMin;
		 else
		 result = "PM" +  pHour + " : " + pMin;
		 
		 
		 return result;
	 }
	
	
}
