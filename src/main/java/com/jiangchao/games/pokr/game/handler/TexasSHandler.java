package com.jiangchao.games.pokr.game.handler;

import com.jiangchao.games.pokr.util.Memory;
import com.jiangchao.games.pokr.util.Util;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Note: Server Handler 
 * Author: JiangChao 
 * Date: 2012/6/15/13 
 * Email: chaojiang@candou.com
 */
public class TexasSHandler extends IoHandlerAdapter {

	private static Logger log = Logger.getLogger(TexasSHandler.class);
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
            String cmd = Util.extractStringFromIoBuffer(buffer);
//            ServiceCenter.getInstance().processCommand(session, cmd);
            Util.sendMessage(session, "candou915" + cmd);
        }
		log.info("[messageReceived]");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Memory.playersOnServer.remove(player);
		log.info("[sessionClosed]");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		log.info("[sessionCreated]");
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		player = new Player(String.valueOf(session.getId()),
				String.valueOf(session.getId()));
		Memory.playersOnServer.put(player, session);
		log.info("[sessionOpened]" + session.getRemoteAddress() + "; " + player);
	}

}
