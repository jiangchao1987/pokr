package com.jiangchao.games.pokr.game.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.jiangchao.games.pokr.util.Util;
import com.yanchuanli.games.pokr.model.Player;
import com.jiangchao.games.pokr.util.Memory;

/**
 * Note: Client Handler 
 * Author: JiangChao 
 * Date: 2012/6/15/14 
 * Email: chaojiang@candou.com
 */
public class TexasCHandler extends IoHandlerAdapter {
    private static Logger log = Logger.getLogger(TexasCHandler.class);
    private Player player;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
	        String info = Util.extractStringFromIoBuffer(buffer);
	        log.info("[messageReceived]" + info);
		} else {
			log.info("[messageReceived]illegal");
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Memory.playersOnClient.remove(player);
		log.info("[sessionClosed]");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		log.info("[sessionCreated]");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		player = new Player(String.valueOf(session.getId()),
				String.valueOf(session.getId()));
		Memory.playersOnClient.put(player, session);
		log.info("[sessionOpened]");
	}

}
