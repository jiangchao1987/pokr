package com.yanchuanli.games.pokr.server;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class SimpleSocketServer {
    private static Logger log = Logger.getLogger(SimpleSocketServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", 9999);
        DatagramSocket datagramSocket = new DatagramSocket(address);

        log.info("start udp server");

        byte[] buffer = new byte[1024];

        for (; ; ) {
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            log.info("receive data:" + new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
            for (int i = 0; i < 5; i++) {
                datagramSocket.send(datagramPacket);
                log.info("msg sent");
                Thread.sleep(1000);
            }


        }


    }
}
