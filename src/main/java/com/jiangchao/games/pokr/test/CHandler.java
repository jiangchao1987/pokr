package com.jiangchao.games.pokr.test;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.jiangchao.games.pokr.util.Memory;

public class CHandler extends IoHandlerAdapter {

	private static Logger log = Logger.getLogger(CHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		Memory.sessionsOnClient.put(String.valueOf(session.getId()), session);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		super.messageReceived(session, message);
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			List<Map<Integer, String>> list = Helper.ioBufferToString(buffer);
			for (Map<Integer, String> map : list) {
				for (Integer key : map.keySet()) {
					String info = map.get(key);
					log.debug("[messageReceived] status code: [" + key + "] "
							+ info);
				}
			}
		} else {
			log.info("[messageReceived]illegal");
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		Memory.sessionsOnClient.remove(String.valueOf(session.getId()));
	}

}
