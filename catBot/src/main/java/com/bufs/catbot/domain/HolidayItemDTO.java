package com.bufs.catbot.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class HolidayItemDTO {
		
		private String locdate;
		private String dateKind;
		private String isHoliday;
		private String dateName;
		private int seq;
		
		
		public String getLocdate() {
			return locdate;
		}
		public void setLocdate(String locdate) {
			this.locdate = locdate;
		}
		public String getDateKind() {
			return dateKind;
		}
		public void setDateKind(String dateKind) {
			this.dateKind = dateKind;
		}
		public String getIsHoliday() {
			return isHoliday;
		}
		public void setIsHoliday(String isHoliday) {
			this.isHoliday = isHoliday;
		}
		public String getDateName() {
			return dateName;
		}
		public void setDateName(String dateName) {
			this.dateName = dateName;
		}
		public int getSeq() {
			return seq;
		}
		public void setSeq(int seq) {
			this.seq = seq;
		}
		
		@Override
		public String toString() {
			return "HolidayItemDTO [locdate=" + locdate + ", dateKind=" + dateKind + ", isHoliday=" + isHoliday
					+ ", dateName=" + dateName + ", seq=" + seq + "]";
		}
		
		
		
		

	
}
