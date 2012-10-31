package com.yanchuanli.games.pokr.util;


/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class Config {

    public static final String MQ_EXCHANGE = "texas";
    public static final byte START = '$' & 0xFF;
    public static final byte SPLIT = '|' & 0xFF;
    public static final byte END = '#' & 0xFF;
    public static final boolean offlineDebug = false;

    public static final int TYPE_LOGIN_INGAME = 0;
    public static final int TYPE_REGIST_INGAME = 1;
    public static final int TYPE_LIST_INGAME = 2;
    public static final int TYPE_JOIN_INGAME = 3;
    public static final int TYPE_USERSTANDBY_INGAME = 4;
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
    public static final int TYPE_SHOW2CARDS = 20;
    public static final int TYPE_SITDOWNFAILED = 21;
    public static final int TYPE_BUYIN_INGAME = 22;
    public static final int TYPE_STANDUP_INGAME = 23;
    public static final int TYPE_YOUAREBROKE_INGAME = 24;
    public static final int TYPE_ADDFRIENDREQUEST = 25;
    public static final int TYPE_SHOWDOWN_INGAME = 26;
    public static final int TYPE_YOUAREDISCONNECTEDBYNEWLOGIN = 27;
    public static final int TYPE_GAMESTATUS_INGAME = 28;
    public static final int TYPE_DEALERSAYS_INGAME = 29;



    public static final int RESULT_BUYINSUCCESS = 1;
    public static final int RESULT_SITDOWNSUCEESS = 1;
    public static final int RESULT_SITDOWNFAILED = 0;

    public static final int GAMEINFO_NOTSTARTED = 0;

    public static final int SRC_IPHONE_GUEST = 0;
    public static final int SRC_IPAD_GUEST = 1;
    public static final int SRC_SINA_WEIBO = 2;
    public static final int SRC_BOT = 3;

    public static final int NORMAL_ROOM_LEVEL_BEGINNER = 1;
    public static final int NORMAL_ROOM_LEVEL_PROFESSIONAL = 2;
    public static final int NORMAL_ROOM_LEVEL_MASTER = 3;
    public static final int NORMAL_ROOM_LEVEL_VIP = 4;

    public static final int FAST_ROOM_LEVEL_BEGINNER = 5;
    public static final int FAST_ROOM_LEVEL_PROFESSIONAL = 6;
    public static final int FAST_ROOM_LEVEL_MASTER = 7;
    public static final int FAST_ROOM_LEVEL_VIP = 8;

    public static final int ACTION_TYPE_SMALL_BLIND = 0;
    public static final int ACTION_TYPE_BIG_BLIND = 1;
    public static final int ACTION_TYPE_ALL_IN = 2;
    public static final int ACTION_TYPE_CHECK = 3;
    public static final int ACTION_TYPE_CALL = 4;
    public static final int ACTION_TYPE_BET = 6;
    public static final int ACTION_TYPE_RAISE = 7;
    public static final int ACTION_TYPE_FOLD = 8;
    public static final int ACTION_TYPE_CONTINUE = 9;

    public static final int EVENT_LOGIN = 0;
    public static final int EVENT_DISCONNECTED = 1;

    public static final int STATUS_ONLINE = 1;
    public static final int STATUS_OFFLINE = 0;

    public static final int GAMESTATUS_ACTIVE = 1;
    public static final int GAMESTATUS_WAITING = 0;

    public static final String EMPTY_SEAT = "empty";
    public static final int SEAT_INDEX_NOTSITTED = 0;


}
