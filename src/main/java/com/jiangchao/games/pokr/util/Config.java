package com.jiangchao.games.pokr.util;

/**
 * Note: Config
 * Author: JiangChao 
 * Date: 2012/6/15/13 
 * Email: chaojiang@candou.com
 */
public class Config {
    public static int port = 9999;
    public static String serverAddress = "localhost";
    
    public static final byte HEAD = '#' & 0xFF;
    public static final byte START = '$' & 0xFF;
    public static final byte SPLIT = '|' & 0xFF;
    public static boolean offlineDebug = false;
    public static final int TYPE_LOGIN_INGAME = 0;
    public static final int TYPE_REGIST_INGAME = 1;
    public static final int TYPE_USER_INGAME = 4;
    public static final int TYPE_ACTION_INGAME = 5;
    public static final int TYPE_HOLE_INGAME = 6;
    public static final int TYPE_CARD_INGAME = 7;
    public static final int TYPE_GAMEOVER_INGAME = 8;
    public static final int TYPE_WINORLOSE_INGAME = 9;
}
