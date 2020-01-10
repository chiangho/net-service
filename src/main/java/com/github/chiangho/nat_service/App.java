package com.github.chiangho.nat_service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.winpcap.WinPcap;
 
public class App {
 
	public static void main(String[] args) {
		
		StringBuilder errbuf = new StringBuilder();
		 
		List<PcapIf> alldevs = new ArrayList<PcapIf>();
 
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			return;
		}
		
		// 输出网卡信息
		System.out.println("Network devices found:");
 
		int i = 0;
		for (PcapIf device : alldevs) {
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), device.getDescription());
		}

		
		PcapIf device = alldevs.get(0);

		int snaplen = 64 * 1024;
		int flags = Pcap.MODE_PROMISCUOUS;// 混杂模式 接受所有经过网卡的帧
		int timeout = 10 * 1000;
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

		
		
		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			return;
		}
 
		PcapBpfProgram filter = new PcapBpfProgram();
		String expression = "tcp";
		WinPcap winPcap = WinPcap.openDead(1, snaplen);
		int res = winPcap.compile(filter, expression, 1, 0);
		if (res != 0) {
			System.out.println("Filter error：" + winPcap.getErr());
		}
		
		// 创建一个数据包处理器
		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
 
			public void nextPacket(PcapPacket packet, String user) {
 
				int flag = WinPcap.offlineFilter(filter, packet.getCaptureHeader(), packet);
 
				if (flag != 0) {
					PacketParse parse = new PacketParse();
					parse.parseProtocol(packet);
					parse.getString() ;
					/*
					 * System.out.printf("%s Received at %s caplen=%-4d len=%-4d %s\n",parse.
					 * getString() , new Date(packet.getCaptureHeader().timestampInMillis()),
					 * packet.getCaptureHeader().caplen(), // Length // actually // captured
					 * packet.getCaptureHeader().wirelen(), // Original length user // User supplied
					 * object );
					 */
				}
			}
		};
		// 循环监听
		pcap.loop(Integer.MAX_VALUE, jpacketHandler, "jNetPcap rocks!");
		// 监听结束后关闭
		pcap.close();


		

	}
}
