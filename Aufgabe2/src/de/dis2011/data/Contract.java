package de.dis2011.data;

import java.sql.Date;


abstract public class Contract {

	protected int no = -1;
	protected Date cDate;
	protected String place;


	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public Date getcDate() {
		return cDate;
	}

	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

}
