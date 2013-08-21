package com.ryanstoast.shittytwitter;

public class TimeLine {
	
	private String status;
	private String user_name;
	private String date;
	private String picture_url;
	
	public TimeLine(String s, String u, String d, String p) {
		this.status      = s;
		this.user_name   = u;
		this.date        = d;
		this.picture_url = p;
	}
	
	public TimeLine() {
		
	}

	public void setStatus(String s) {
		this.status = s;
	}
	public String getStatus() {
		return this.status;
	}
	
	public void setUser(String u) {
		this.user_name = u;
	}
	public String getUser() {
		return this.user_name;
	}
	
	public void setDate(String d) {
		this.date = d;
	}
	public String getDate() {
		return this.date;
	}
	
	public void setPicture(String u) {
		this.picture_url = u;
	}
	public String getPicture() {
		return this.picture_url;
	}

}
