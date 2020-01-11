package com.github.chiangho.nat_service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Test {

	
	 public static int bytesToInt(byte[] b) {
	    	short a  = 0;
	        a = (short) (a|(b[0] & 0xff ));
	        a = (short) (a<<8);
	        a = (short) (a|(b[1] & 0xff ));
	    	return a;
	    }
	
	public static void main(String[] args) throws ParseException {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		System.out.println(format.parse("2020-01-11 12:05:11:839").getTime());
		
		byte[] array =new  byte[4];
		int tag = 443;
		array[0] = (byte)((tag >> 24) & 0xff);
		array[1] = (byte)((tag >> 16) & 0xff) ;
		array[2] = (byte)((tag >> 8) & 0xff);
		array[3] = (byte)(tag & 0xff);
		
		System.out.println(array[0]);
		System.out.println(array[1]);
		System.out.println(array[2]);
		System.out.println(array[3]);
		
		
		System.out.println(bytesToInt(new byte[] {array[2],array[3]}));
		
	}
	
}
