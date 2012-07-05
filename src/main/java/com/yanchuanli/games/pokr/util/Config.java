package com.yanchuanli.games.pokr.util;

/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class Config {

    public static final int port = 9999;
    public static final String serverAddress = "localhost";
    public static final String webServerBase = "http://192.168.1.177:8080/restpokr/";

    public static final byte START = '$' & 0xFF;
    public static final byte SPLIT = '|' & 0xFF;
    public static final byte END = '#' & 0xFF;
    public static final boolean offlineDebug = false;

    public static final int TYPE_LOGIN_INGAME = 0;
    public static final int TYPE_REGIST_INGAME = 1;
    public static final int TYPE_LIST_INGAME = 2;
    public static final int TYPE_JOIN_INGAME = 3;
    public static final int TYPE_USER_INGAME = 4;
    public static final int TYPE_ACTION_INGAME = 5;
    public static final int TYPE_HOLE_INGAME = 6;
    public static final int TYPE_CARD_INGAME = 7;
    public static final int TYPE_GAMEOVER_INGAME = 8;
    public static final int TYPE_WINORLOSE_INGAME = 9;
    public static final int TYPE_LEAVEROOM_INGAME = 10;
    public static final int TYPE_OTHERSACTION_INGAME = 11;
    public static final int TYPE_DEAL2CARDSONALLDEVICES_INGAME = 12;
    public static final int TYPE_OTHERSTARTACTION_INGAME = 13;
    public static final int TYPE_BIGBLIND_INGAME = 14;
    public static final int TYPE_SMALLBLIND_INGAME = 15;
    public static final int TYPE_DEALER_INGAME = 16;
    public static final int TYPE_CHAT_INGAME = 17;
    public static final int TYPE_MARKBIGBLIND_INGAME = 18;
    public static final int TYPE_MARKSMALLBLIND_INGAME = 19;

    public static final int SRC_IPHONE_GUEST = 0;
    public static final int SRC_IPAD_GUEST = 1;

    public static final int ROOM_LEVEL_BEGINNER = 1;
    public static final int ROOM_LEVEL_PROFESSIONAL = 2;
    public static final int ROOM_LEVEL_MASTER = 3;
    public static final int ROOM_LEVEL_VIP = 4;
}
