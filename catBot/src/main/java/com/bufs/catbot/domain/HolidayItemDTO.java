package com.bufs.catbot.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
public class HolidayItemDTO {
	

		@XmlElement
		private String locdate;
		@XmlElement
		private String dateKind;
		@XmlElement
		private String isHoliday;
		@XmlElement
		private String dateName;
		@XmlElement
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
