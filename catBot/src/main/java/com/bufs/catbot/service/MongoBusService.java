package com.bufs.catbot.service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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

	

	
	 public Map<String, Object> getAllShuttleBusInfo(){
		
		 Map<String, Object> result = new HashMap<String, Object>();
		 List<String> pBusTable = busDAO.getAllShuttleBusInfo();
		 String message = GetAllShuttleBusMessage(parseBusTimeFormat(pBusTable));
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
	 public Map<String, Object> getShuttleBusInfo(){
		 
		 Date now = new Date();
		 Map<String, Object> result = new HashMap<String, Object>();
		 
		LocalDateTime nowT = LocalDateTime.now();
		LocalDateTime startT = LocalDateTime.now().withHour(7).withMinute(55);
		LocalDateTime endT = LocalDateTime.now().withHour(22).withMinute(15);
		
		if(nowT.isBefore(startT) || nowT.isAfter(endT) || nowT.getDayOfWeek() == DayOfWeek.SATURDAY || nowT.getDayOfWeek() == DayOfWeek.SUNDAY) {
			
			 result.put("text", getBlockMessage()); 
			 return result;
		}
			
			
		 String nowTime = timeFormat.format(now);
		 String[] timeArr = nowTime.split(":");
		 
		 int nowH = Integer.parseInt(timeArr[0]);
		 int nowM = Integer.parseInt(timeArr[1]);

		 
		 
		 List<MongoBusTimeFormat> pBusTable;
		 List<String> timeTable;
		 List<String> oneHourBeforeTable;
		 List<String> oneHourAfterTable;

		 timeTable = busDAO.getShuttleBusInfo(nowTime);
		 
		 if(nowM > 50) {
			 
			String oneHourAfter = nowH + 1 < 10 ?  "0"+(nowH+1) : String.valueOf(nowH+1);
			 oneHourAfterTable = busDAO.getShuttleBusInfo(oneHourAfter);
			 timeTable.addAll(oneHourAfterTable);
			
		 }else if(nowM < 10) {
			 
			 String oneHourBefore = nowH - 1 < 10 ?  "0"+(nowH - 1) : String.valueOf(nowH-1);
			 oneHourBeforeTable = busDAO.getShuttleBusInfo(oneHourBefore);
			 timeTable.addAll(oneHourBeforeTable);

		 }
	
		 pBusTable = BusTimeChooser(timeTable, nowH, nowM);


		 String message = GetShuttleBusMessage(pBusTable);
		 result.put("text", message); 
		 return result;
		 
	 }
	 
	 
	//현재 시간에 가장 가까운 버스 정보를 찾아내는 서비스 메소드
	 public List<MongoBusTimeFormat> BusTimeChooser(List<String> busTable, int nowH, int nowM){
 
		 List<MongoBusTimeFormat> choicedBus = new ArrayList<MongoBusTimeFormat>();

		 //현재 시간과 비교해서 차이가 18분 이하이고 현재 시간보다 빠를때 (즉, 버스가 출발했을때를 가정)
		 for(String time : busTable ) {
			 String[] tempTimeArr = time.split(":");
			 int compH = Integer.parseInt(tempTimeArr[0]);
			 int compM = Integer.parseInt(tempTimeArr[1]);
			 
			 compM = compM == 0 ? 60 : compM;

			 //현재 시와 비교 시가 같을때
			 if(nowH == compH) {
				 
				 int tempM = nowM-compM;
				 
				 if(tempM > -1 && tempM <= 10 ) {
					 
					 MongoBusTimeFormat busTime = new MongoBusTimeFormat(time, Integer.parseInt(tempTimeArr[0]), compM);
					 choicedBus.add(busTime);
					 
				 	}	 
				 }

			 //현재 시가 더 느릴때, 현재시 12시 /비교시 13시
			 else if(nowH - compH  > 0 ) {
				 

				 int tempM = (60 - compM);
				 if(tempM <= 10) {
					 
					 MongoBusTimeFormat busTime = new MongoBusTimeFormat(time, Integer.parseInt(tempTimeArr[0]), compM);
					 choicedBus.add(busTime);
					 
					 
				 } 
				 
			//현재 시가 더 빠를때,  현재시 11시 /비교시 12시
			 }else if(nowH - compH < 0) {
				 

				 int tempM = (60-nowM) + compM;
				 
				 if(tempM <= 10) {
					 
					 MongoBusTimeFormat busTime = new MongoBusTimeFormat(time, Integer.parseInt(tempTimeArr[0]), compM);
					 choicedBus.add(busTime);
					 
					 
				 } 
				 
				 
			 }
			 
			 
			 
			 }
		 
		 
		 return choicedBus;
		 
		 
		 
	 }
	 
	 
	 //건네 받은 셔틀버스 목록을 메시지로 바꿔주는 메소드
	 public String GetShuttleBusMessage(List<MongoBusTimeFormat> busTable) {
		 
		 String message = "부산외대 셔틀 버스 정보라냥. \n출발시간 제외하고는 도로사정에 따라서 1~3분정도 차이가 날 수 있다냥. \n버스가 안온다고 날 탓하지 말라냥!! \n\n";
		 
		 
		 for(MongoBusTimeFormat time : busTable) {
			 

			 int choicedH = time.getHour();
			 int choicedM = time.getMin();
			 
			 message += "외대에서 출발 " + timeFormatter(choicedH, choicedM) + "\n" +
					 	"외성생활관 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_DOMI) + "\n" +
					 	"범어사역 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_BEOMEOSA) + "\n" +
					 	"남산역 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_NAMSAN) + "\n" +
					 	"남산소방서 " + timeFormatter(choicedH, choicedM+BUFS_SHUTTLE_TERM_FIRESTATION) + "\n" +
					 	"----------------------------------- \n";			 
			 
		 }
		 
		 
		 
		 return message;
		 
		 
		 
	 }
	 
	 
	 public String GetAllShuttleBusMessage(List<MongoBusTimeFormat> busTable) {
		 
		 
		 int compH = 0;
		 int rowJumper = 0;
		 String message ="부산외대 전체 버스 시간표라냥 \n\n";
		 
		 compH = busTable.get(0).getHour();
		 
		 
		 
		 for(MongoBusTimeFormat time : busTable) {
			
			 if(compH != time.getHour()){
				 message += "\n"; 
				 compH = time.getHour();
				 rowJumper = 0;
			 }
			 
			 rowJumper++;
			 
			
			 
			 message += time.getTime() +" ";
			 
			 if(rowJumper % 5 == 0) {
				 
				 message += "\n"; 
			 }
		 }
		 
		 
		 return message;
		 
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
