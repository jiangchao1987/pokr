package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Memory;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 11/6/12
 */
public class FireWall {

    private static Logger log = Logger.getLogger(FireWall.class);

    public static boolean validate(IoSession session) {
        boolean result = Memory.sessionsOnServer.containsKey(String.valueOf(session.getId())) || Memory.adminSessionsOnServer.containsKey(String.valueOf(session.getId()));

        if (result) {
            result = validateIP(session);
        }

        if (!result) {
            log.debug("illegal access from " + session.getRemoteAddress());
            session.close(true);
        }
        return result;
    }

    private static boolean validateIP(IoSession session) {
        //log.info("incomming client : " + session.getRemoteAddress());
        return true;
    }

}
