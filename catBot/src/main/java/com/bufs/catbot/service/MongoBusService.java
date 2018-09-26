package com.bufs.catbot.service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bufs.catbot.domain.MongoBusTimeFormat;
import com.bufs.catbot.persistence.MongoBusDAO;

@Service
public class MongoBusService{

	@Autowired
	private MongoBusDAO busDAO;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	public static final int BUFS_SHUTTLE_TERM_DOMI = 2;
	public static final int BUFS_SHUTTLE_TERM_BEOMEOSA = 7;
	public static final int BUFS_SHUTTLE_TERM_NAMSAN = 10;
	public static final int BUFS_SHUTTLE_TERM_FIRESTATION = 13;
	public static final int BUFS_TOWNBUS_TERM_BOUND_NAMSAN = 6;


	

	
	 public Map<String, Object> getAllBusInfo(String type, String station, String bound){
		
		 Boolean isShuttleBus = false;
		 Boolean isWeekend = false;
		 if(type.contains("셔틀")) {
			 isShuttleBus = true;
		 }
		 
		 LocalDateTime nowT = LocalDateTime.now();
			
			if(nowT.getDayOfWeek() == DayOfWeek.SATURDAY || nowT.getDayOfWeek() == DayOfWeek.SUNDAY) {
				
				isWeekend = true;
		 }
		 
		 
		 Map<String, Object> result = new HashMap<String, Object>();
		 List<String> pBusTable = busDAO.getAllBusInfo(isShuttleBus, station, bound, isWeekend);
		 String message = GetAllBusMessage(parseBusTimeFormat(pBusTable), isShuttleBus, station, bound, isWeekend);
		 result.put("text", message); 
		 
		 
		 return result;
	 
	 }
	 
	 
	 public List<MongoBusTimeFormat> parseBusTimeFormat(List<String> pBusTime){
		 
		 
		 List<MongoBusTimeFormat> busTimes = new ArrayList<>();
		 String[] timeArr;
		 int hour;
		 int min;
		 
		 
		 for(String time : pBusTime) {
			 
			 timeArr = time.split(":");
			 hour = Integer.parseInt(timeArr[0]);
			 min = Integer.parseInt(timeArr[1]);
			 
			 
			 busTimes.add(new MongoBusTimeFormat(time, hour, min));
	 
			 
		 }
		 
		 return busTimes;
		 
		 
	 }
	
	
	
	//셔틀버스 정보를 가져오는 서비스 메소드
	 public Map<String, Object> getBusInfo(String type, String station, String bound){
		 
		 Date now = new Date();
		 Boolean isWeekend = false;
		 Boolean isShuttleBus = false;
		 String message = "";
		 Map<String, Object> result = new HashMap<String, Object>();
		 
		 
		 if(type.contains("셔틀")) {
			 isShuttleBus = true;	 
		 }
		 
		LocalDateTime nowT = LocalDateTime.now();
		
		if(nowT.getDayOfWeek() == DayOfWeek.SATURDAY || nowT.getDayOfWeek() == DayOfWeek.SUNDAY) {
			
			isWeekend = true;
		}
		

		if(isShuttleBus && isWeekend) {
			 result.put("text", getBlockMessage()); 
			 return result;
		}

		 String nowTime = timeFormat.format(now);
		 String[] timeArr = nowTime.split(":");
		 
		 int nowH = Integer.parseInt(timeArr[0]);
		 int nowM = Integer.parseInt(timeArr[1]);

		 
		 
		 List<MongoBusTimeFormat> pBusTable = null;
		 List<String> timeTable = null;

		 System.out.println("isShuttleBus : " + isShuttleBus);
		 
		 
		 if(isShuttleBus) {
		 
			 timeTable = busDAO.getBusInfo(nowTime, "셔틀", null, null, null);
			 pBusTable = BusTimeChooser(timeTable,nowH, nowM,isShuttleBus);
		
		 }else {
			 
			 
		timeTable = busDAO.getBusInfo(nowTime, "마을", station, bound, isWeekend);
		pBusTable = BusTimeChooser(timeTable, nowH, nowM, isShuttleBus);
			 
			 
		 }
		 

		 if(pBusTable.size()!=0) {
		 
			 if(isShuttleBus) {
			 message = GetShuttleBusMessage(pBusTable);
			 }else {
				 message = GetTownBusMessage(pBusTable, station, bound);
			 }

		 
		 }else {
		 
			 message = getBlockMessage();
		 
		 }
		 result.put("text", message); 
		 return result;
		 
	 }
	 

	 
	//현재 시간에 가장 가까운 버스 정보를 찾아내는 서비스 메소드
	 public List<MongoBusTimeFormat> BusTimeChooser(List<String> busTable, int nowH, int nowM, Boolean isShuttleBus){
 
		 List<MongoBusTimeFormat> choicedBus = new ArrayList<MongoBusTimeFormat>();
		 
		 LocalTime timeNow = LocalTime.of(nowH, nowM);
		 LocalTime compTime;
		 Duration timeGap;
		 int minGap;
		 

		 
		 //현재 시간과 비교해서 차이가 18분 이하이고 현재 시간보다 빠를때 (즉, 버스가 출발했을때를 가정)
		 for( String time : busTable ) {
			 String[] tempTimeArr = time.split(":");
			 int compH = Integer.parseInt(tempTimeArr[0]);
			 int compM = Integer.parseInt(tempTimeArr[1]);
			 
			 compTime = LocalTime.of(compH, compM);

			 timeGap = Duration.between(timeNow, compTime);
			 minGap = Integer.valueOf(String.valueOf(timeGap.getSeconds()))/60;
		

				 if( isShuttleBus && minGap > -15 && minGap < 10) {
					 
					 MongoBusTimeFormat busTime = new MongoBusTimeFormat(time, compH, compM);
					 choicedBus.add(busTime);
					 
				 	}else if(!isShuttleBus && minGap > -30 && minGap < 30) {
				 		
				 		
				 	 MongoBusTimeFormat busTime = new MongoBusTimeFormat(time, compH, compM);
					 choicedBus.add(busTime);
				 		
				 	}
 
			 }
		 
		 
		 return choicedBus;
		 
		 
		 
	 }
	 
	 
	 //건네 받은 셔틀버스 목록을 메시지로 바꿔주는 메소드
	 public String GetShuttleBusMessage(List<MongoBusTimeFormat> busTable) {
		 
		 StringBuffer message = new StringBuffer("부산외대 셔틀 버스 정보라냥. \n출발시간 제외하고는 도로사정에 따라서 1~3분정도 차이가 날 수 있다냥. \n버스가 안온다고 날 탓하지 말라냥!! \n\n");
		 
		 
		 for(MongoBusTimeFormat time : busTable) {
			 

			 int choicedH = time.getHour();
			 int choicedM = time.getMin();
			 
			 message.append("외대에서 출발 ").append(timeFormatter(choicedH, choicedM)).append("\n")
			 .append("외성생활관 ").append(timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_DOMI)).append("\n")
			 .append("범어사역 ").append(timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_BEOMEOSA)).append("\n")
			 .append("남산역 ").append(timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_NAMSAN)).append("\n")
			 .append("남산소방서 ").append(timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_FIRESTATION)).append("\n")
			 .append("----------------------------------- \n");			 
			 
		 }
		 
		 
		 
		 return message.toString();
		 
		 
		 
	 }
	 
	 
	 
	 
 public String GetTownBusMessage(List<MongoBusTimeFormat> busTable, String station, String bound) {
		 
		 String message = station+"-"+bound+" 마을버스 정보라냥. \n\n남산역에서 출발하는 버스는 시간이 정확하지 않다냥. 보통 외대에서 출발후에 6분에서 10분 정도 걸린다냥 \n마을버스 시간은 경우에 따라서 바뀔 수 있으니 유의하라냥 \n\n";
		 
		 
		 for(MongoBusTimeFormat time : busTable) {
			 

			 int choicedH = time.getHour();
			 int choicedM = time.getMin();
			 
			
			 message += timeFormatter(choicedH, choicedM) + "\n";	 
			 
			 if(station.contains("외대") && bound.contains("남산")) {
				 
				 message += "남산역 도착 예상시간 " + timeFormatter(choicedH, choicedM + BUFS_TOWNBUS_TERM_BOUND_NAMSAN) + "\n\n";
			 }

			 
		 }
		 
		 return message;
		 
		 
		 
	 }
	 
	 
	 public String GetAllBusMessage(List<MongoBusTimeFormat> busTable, Boolean isShuttleBus, String station, String bound, Boolean isWeekend) {
		 
		 
		 int compH = 0;
		 int rowJumper = 0;
		 String weekend = isWeekend? "주말" : "평일";
		 StringBuffer message =new StringBuffer();
		 if(isShuttleBus) {
			 message.append("부산외대 전체 버스 시간표라냥 \n\n");
		 }else {
			 
			 message.append(station).append("-").append(bound).append(" 마을버스 ").append(weekend).append("시간표 정보라냥 \n\n");

		 }
		 
		 compH = busTable.get(0).getHour();
		 
		 
		 
		 for(MongoBusTimeFormat time : busTable) {
			
			 if(compH != time.getHour()){
				 message.append("\n"); 
				 compH = time.getHour();
				 rowJumper = 0;
				 
			 }
			 
			 rowJumper++;
			 
			
			 
			 message.append(time.getTime()).append(" ");
			 
			 if(rowJumper % 5 == 0) {
				 
				 message.append("\n"); 
			 }
		 }
		 
		 
		 return message.toString();
		 
	 }
	 
	 
	 
	 
	 
	 public String getBlockMessage() {
		 
		 
		 
		 return "운행 시간이 아니다냥.";
		 
	 }
	 
	 
	 //시간 표현을 올바르게 바꾸어 주는 메소드
	 public String timeFormatter(int hour, int min) {
		 
		 boolean isAm = true;
		 String result;
		 String pHour;
		 String pMin;
		 
		 if(min >= 60) {
			 hour += 1;
			 min = (min-60);
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
		 result = "PM " +  pHour + " : " + pMin;
		 
		 
		 return result;
	 }


	 
	 
	 
	 
	 
	
	
}
