package com.github.chiangho.nat_service;

import java.io.UnsupportedEncodingException;

import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

public class PacketParse {

	private Ip4 ip = new Ip4();
    private Icmp icmp = new Icmp();
    private Tcp tcp = new Tcp();
    private Udp udp = new Udp();
	
    private Message message;
	
	private void handleIp(PcapPacket packet) {
        packet.getHeader(ip);
        byte[] sIP = new byte[4], dIP = new byte[4];
        sIP = ip.source();
        dIP = ip.destination();
        String srcIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
        String dstIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);
        message.setSip(srcIP);
        message.setDip(dstIP);
    }
     
    private void handleIcmp(PcapPacket packet) {
        packet.getHeader(icmp);
         
        byte[] buff = new byte[packet.getTotalSize()];
        packet.transferStateAndDataTo(buff);
        JBuffer jb = new JBuffer(buff);
        String content = jb.toHexdump();
        //for(int i = 0; i < icmpRules.size(); i++) {
            //if(content.contains(icmpRules.get(i).getContent())) {
                //message.setMsg(icmpRules.get(i).getMsg());
                message.setPacket(content);
                sendMessage();
            //}
        //}
    }
     
    public static int bytesToInt(byte[] b) {
    	int a  = 0;
        
        a = (int) (a|(b[0] & 0xff ));
        a = (int) (a<<8);
        
        a = (int) (a|(b[1] & 0xff ));
        
        return a;
    }
    
    private void handleTcp(PcapPacket packet) {
        packet.getHeader(tcp);
        String srcPort = String.valueOf(tcp.source());
        String dstPort = String.valueOf(tcp.destination());
        message.setSport(srcPort);
        message.setDport(dstPort);
         
        byte[] buff = new byte[packet.getTotalSize()];
        packet.transferStateAndDataTo(buff);
        System.out.println("----------------------------------"); 
       
        
		System.out.println("@@@@@@"+bytesToInt(new byte[] {tcp.getHeader()[0],tcp.getHeader()[1],0,0}));
        
        JBuffer jb = new JBuffer(buff);
        String content = jb.toHexdump();
        //for(int i = 0; i < tcpRules.size(); i++) {
            //if(content.contains(tcpRules.get(i).getContent())) {
                //message.setMsg(tcpRules.get(i).getMsg());
                message.setPacket(content);
                sendMessage();
            //}
        //}
    }
     
    private void handleUdp(PcapPacket packet) {
        packet.getHeader(udp);
        String srcPort = String.valueOf(udp.source());
        String dstPort = String.valueOf(udp.destination());
        message.setSport(srcPort);
        message.setDport(dstPort);
         
        byte[] buff = new byte[packet.getTotalSize()];
        packet.transferStateAndDataTo(buff);
        JBuffer jb = new JBuffer(buff);
        String content = jb.toHexdump();
        //for(int i = 0; i < udpRules.size(); i++) {
            //if(content.contains(udpRules.get(i).getContent())) {
                //message.setMsg(udpRules.get(i).getMsg());
                message.setPacket(content);
                sendMessage();
            //}
        //}
    }
	
	public void parseProtocol(PcapPacket packet) {
		message = new Message();
		if (packet.hasHeader(ip)) {
            handleIp(packet);
        }
        if (packet.hasHeader(icmp)) {
            handleIcmp(packet);
        }
        if (packet.hasHeader(tcp)) {
            handleTcp(packet);
        }
        if (packet.hasHeader(udp)) {
            handleUdp(packet);
        }
	}

	
	private void sendMessage() {
//	        MessageCenter.sendMessage(message);
		//System.out.println(this.message.getPacket());
	}

	public Object getString() {
		System.out.printf("sip %s \n",message.getSip());
		System.out.printf("sport %s \n",message.getSport());
		System.out.printf("dip %s \n",message.getDip());
		System.out.printf("dport %s \n",message.getDport());
		System.out.println("++++++++++++++++++++++++++++++++++++"); 
		return "";//this.message.getPacket();
	}
}
