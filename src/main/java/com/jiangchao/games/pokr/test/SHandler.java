package com.jiangchao.games.pokr.test;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.jiangchao.games.pokr.util.Memory;

public class SHandler extends IoHandlerAdapter {
	
	private static Logger log = Logger.getLogger(SHandler.class);

	public void sessionOpened(IoSession session) throws Exception {
		log.info(session.getId() + "[" + session.getRemoteAddress() + "] incomming!");
		Memory.sessionsOnServer.put(String.valueOf(session.getId()), session);
	}

	public void sessionClosed(IoSession session) {
		log.info(session.getId() + "[" + session.getRemoteAddress() + "] disconnected!");
		Memory.sessionsOnServer.remove(String.valueOf(session.getId()));
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			log.debug("remaining:" + buffer.remaining());

			SocketAddress remoteAddress = session.getRemoteAddress();
			log.info(remoteAddress + ":" + new String(buffer.array()));

			List<Map<Integer, String>> list = Helper.ioBufferToString(buffer);
			for (Map<Integer, String> map : list) {
				// ServiceCenter.getInstance().processCommand(session, map);
				log.info(map);
			}
		}
	}

}
