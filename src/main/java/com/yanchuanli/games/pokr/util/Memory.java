package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.model.Player;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Memory {
    public static Map<String, Player> sessionsOnServer = new HashMap<String, Player>();
    public static Map<String, IoSession> sessionsOnClient = new HashMap<String, IoSession>();
    public static List<Player> playersOnServer = new ArrayList<Player>();
    public static List<Player> playersOnClient = new ArrayList<Player>();
}
