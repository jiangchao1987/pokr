package com.yanchuanli.games.pokr.server.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-2
 */
public class NettyServerHandler extends SimpleChannelHandler {

    private static Logger log = Logger.getLogger(NettyServerHandler.class);

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        Server.chanels.put(String.valueOf(ctx.getChannel().getId()), e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        log.debug(e);
        if (e instanceof BigEndianHeapChannelBuffer) {
            BigEndianHeapChannelBuffer buffer = (BigEndianHeapChannelBuffer) e;
            e.getChannel().getId();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
}
