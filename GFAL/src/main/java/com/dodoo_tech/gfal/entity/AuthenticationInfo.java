package com.dodoo_tech.gfal.entity;

public class AuthenticationInfo {
	private String sn;
	private String macAddress;
	private String code;
	private boolean isAuthentication=false;
	private String time;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isAuthentication() {
		return isAuthentication;
	}
	public void setAuthentication(boolean isAuthentication) {
		this.isAuthentication = isAuthentication;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	
}
