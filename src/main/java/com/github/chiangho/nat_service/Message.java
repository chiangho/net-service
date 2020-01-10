package com.github.chiangho.nat_service;

public class Message {

	private String sip;
	private String dip;
	private String sport;
	private String dport;
	
	
	private String packet;
	
	
	
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getDport() {
		return dport;
	}
	public void setDport(String dport) {
		this.dport = dport;
	}
	public String getSip() {
		return sip;
	}
	public void setSip(String sip) {
		this.sip = sip;
	}
	public String getDip() {
		return dip;
	}
	public void setDip(String dip) {
		this.dip = dip;
	}
	public String getPacket() {
		return packet;
	}
	public void setPacket(String packet) {
		this.packet = packet;
	}
	
	
	
	
	
}
