package com.yanchuanli.games.pokr.util;

import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Memory {
    public static Map<String, IoSession> sessionsOnServer = new HashMap<String, IoSession>();
    public static Map<String, IoSession> sessionsOnClient = new HashMap<String, IoSession>();
}
