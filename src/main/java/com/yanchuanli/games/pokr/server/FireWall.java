package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Memory;
import org.apache.mina.core.session.IoSession;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 11/6/12
 */
public class FireWall {
    public static boolean validate(IoSession session) {
        boolean result = Memory.sessionsOnServer.containsKey(String.valueOf(session.getId())) || Memory.adminSessionsOnServer.containsKey(String.valueOf(session.getId()));
        if (!result) {
            session.close(true);
        }
        return result;
    }
}
