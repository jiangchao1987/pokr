package com.yanchuanli.games.pokr.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import java.net.InetSocketAddress;


/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class UDPClient extends IoHandlerAdapter {

    private static Logger log = Logger.getLogger(UDPClient.class);

    private ConnectFuture connFuture;

    public UDPClient() throws InterruptedException {
        NioDatagramConnector connector = new NioDatagramConnector();
        connector.setHandler(new ClientHandler());

        DefaultIoFilterChainBuilder chain = connector.getFilterChain();

        log.debug("connecing to the server...");
        connFuture = connector.connect(new InetSocketAddress(ServerConfig.gameServerAddress, ServerConfig.gameServerPort));
//        connFuture.awaitUninterruptibly();
//        connFuture.await();

        connFuture.addListener(new IoFutureListener<ConnectFuture>() {
            public void operationComplete(ConnectFuture future) {

            }
        });
    }

    public static void main(String[] args) throws InterruptedException {

        UDPClient c = new UDPClient();
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
