package com.jiangchao.games.pokr.game.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.jiangchao.games.pokr.util.Memory;
import com.jiangchao.games.pokr.util.Util;
import com.yanchuanli.games.pokr.model.Player;

/**
 * Note: Client Handler 
 * Author: JiangChao 
 * Date: 2012/6/15/14 
 * Email: chaojiang@candou.com
 */
public class TexasCHandler extends IoHandlerAdapter {
    private static Logger log = Logger.getLogger(TexasCHandler.class);

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
//	        String info = Util.extractStringFromIoBuffer(buffer);
//	        log.info("[messageReceived]" + info);
//			List<String> infos = Util.extractStringFromIoBuffer(buffer);
			List<String> infos = Util.byteToString(buffer);
			for (String info : infos) {
				log.info("[messageReceived]" + info);
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Memory.playersOnClient.remove(String.valueOf(session.getId()));
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		Player player = new Player(String.valueOf(session.getId()),
				String.valueOf(session.getId()));
		player.setSession(session);
		Memory.playersOnClient.put(String.valueOf(session.getId()), player);
	}

}
