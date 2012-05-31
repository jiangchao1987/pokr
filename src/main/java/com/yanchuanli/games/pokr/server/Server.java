package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Config;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class Server {

    private static Logger log = Logger.getLogger(Server.class);

    public Server() {

    }

    public void setup() {

    }

    public void start() {
        NioDatagramAcceptor acceptor = new NioDatagramAcceptor();

        // 创建接收数据的过滤器
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

        // 设定这个过滤器将一行一行(/r/n)的读取数据
        //			chain.addLast("logger", new LoggingFilter());
        chain.addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory(Charset.forName("UTF-8"),
                        LineDelimiter.NUL, LineDelimiter.NUL)));

        // 设定服务器端的消息处理器:一个SamplMinaServerHandler对象,
        acceptor.setHandler(new NetworkServerHandler());

        DatagramSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);

        // 绑定端口,启动服务器
        try {
            acceptor.bind(new InetSocketAddress(Config.port));
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,1000000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Mina Server is Listing on: " + Config.port);
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.start();
    }
}
