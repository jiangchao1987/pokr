package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Config;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;


/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class Client extends IoHandlerAdapter {

    private static Logger log = Logger.getLogger(Client.class);

    private ConnectFuture connFuture;

    public Client() {
        NioDatagramConnector connector = new NioDatagramConnector();
        connector.setHandler(new ClientHandler());

        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
        // chain.addLast("logger", new LoggingFilter());
        chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.NUL, LineDelimiter.NUL)));
        log.debug("connecing to the server...");
        connFuture = connector.connect(new InetSocketAddress(Config.serverAddress, Config.port));
        connFuture.awaitUninterruptibly();

        connFuture.addListener(new IoFutureListener<ConnectFuture>() {
            public void operationComplete(ConnectFuture future) {

            }
        });
    }

    public static void main(String[] args) {

        Client c = new Client();
        if (c.connFuture.isConnected()) {
            log.info("...connected");
            IoSession session = c.connFuture.getSession();
            session.write("hallo");
            session.getConfig().setUseReadOperation(true);
            session.getCloseFuture().awaitUninterruptibly();
            log.info("msg sent");

         //   session.close(true);
        } else {
            log.error("Not connected...exiting");
        }


    }

}
