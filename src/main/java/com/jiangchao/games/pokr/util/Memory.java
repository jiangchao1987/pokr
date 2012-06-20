package com.jiangchao.games.pokr.util;

import org.apache.mina.core.session.IoSession;

import com.yanchuanli.games.pokr.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Note: Memory
 * Author: JiangChao 
 * Date: 2012/6/15/15 
 * Email: chaojiang@candou.com
 */
public class Memory {
    public static Map<String, IoSession> sessionsOnServer = new HashMap<String, IoSession>();
    public static Map<String, IoSession> sessionsOnClient = new HashMap<String, IoSession>();
	public static Map<String, Player> playersOnServer = new HashMap<String, Player>();
	public static Map<String, Player> playersOnClient = new HashMap<String, Player>();
	
}
