package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.model.Player;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-16
 */
public class NotificationCenter {


    public static void notifyUserForNewLogin(Player player) {
        notifySpecificOnTable(player.getSession(), "", Config.TYPE_YOUAREDISCONNECTEDBYNEWLOGIN);
    }

    /**
     * 从总资产中扣去相应钱数用于继续游戏
     *
     * @param session 当前玩家
     * @param info    是否成功的标志, 1 success
     */
    public static void buyIn(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_BUYIN_INGAME);
    }

    /**
     * 转发聊天信息
     *
     * @param players 所有玩家
     * @param info    转发的消息
     */
    public static void chat(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_CHAT_INGAME);
    }

    /**
     * 转发语音聊天信息
     *
     * @param players 所有玩家
     * @param info    转发的消息
     */
    public static void voicechat(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_VOICECHAT_INGAME);
    }

    /**
     * 通知所有玩家谁是dealer
     *
     * @param players 所有玩家
     * @param info    dealer的id
     */
    public static void markCurrentDealer(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_DEALER_INGAME);
    }

    /**
     * 小盲注
     *
     * @param players 所有玩家
     * @param info
     */
    public static void paySmallBlind(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_SMALLBLIND_INGAME);
    }

    /**
     * 大盲注
     *
     * @param players 所有玩家
     * @param info
     */
    public static void payBigBlind(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_BIGBLIND_INGAME);
    }

    /**
     * 通知所有玩家谁是小盲注
     *
     * @param players 所有玩家
     * @param info    小盲注玩家的id
     */
    public static void markSmallBlind(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_MARKSMALLBLIND_INGAME);
    }

    /**
     * 通知所有玩家谁是大盲注
     *
     * @param players 所有玩家
     * @param info    大盲注玩家的id
     */
    public static void markBigBlind(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_MARKBIGBLIND_INGAME);
    }


    /**
     * 通知所有玩家当前轮到谁了
     *
     * @param players 所有玩家
     * @param info    轮到的玩家id
     */
    public static void otherPlayerStartAction(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_OTHERSTARTACTION_INGAME);
    }

    /**
     * 在所有玩家得到自己的2张牌后，通知所有设备开始发那2张牌, 相当于以前的StartServer！
     *
     * @param players 所有玩家
     * @param info    用户id, 从这个id开始发牌
     */
    public static void deal2CardsOnAllDevices(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_DEAL2CARDSONALLDEVICES_INGAME);
    }

    /**
     * 通知所有玩家谁离开了房间。
     *
     * @param players 所有玩家
     * @param info    提示消息
     */
    public static void leaveRoom(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_LEAVEROOM_INGAME);
    }

    public static void standUp(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_STANDUP_INGAME);
    }

    /*
    * sample msg: id,action:[bet],moneyontable
    */
    public static void forwardAction(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_OTHERSACTION_INGAME);
    }

    public static void list(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_LIST_INGAME);
    }

//    public static void winorlose(IoSession session, String info, int money) {
//        notifySpecificOnTable(session, info, Config.TYPE_WINORLOSE_INGAME);
//    }

    public static void show2cards(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_SHOW2CARDS);
    }

    public static void winorlose(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_WINORLOSE_INGAME);
    }

    public static void showBrokenPlayers(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_SHOWBROKENPLAYERS_INGAME);
    }

    public static void dealRiverCard(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
    }

    public static void dealTurnCard(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
    }

    public static void deal3FlipCards(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
    }

    public static void doBettingRound(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
    }

    public static void showdown(List<Player> players) {
        notifyAllOnTable(players, "", Config.TYPE_SHOWDOWN_INGAME);
    }

    public static void dealCardsOnTableForNewcomers(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_CARD_INGAME);
    }

    public static void act(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_ACTION_INGAME);
    }

    public static void deal2Cards(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_HOLE_INGAME);
    }

    public static void sayHello(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_JOIN_INGAME);
    }

    public static void login(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_LOGIN_INGAME);
    }

    public static void respondToPrepareToEnter(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_JOIN_INGAME);
    }


    public static void sitDownResult(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_SITDOWNFAILED);
    }


    public static void dealerSays(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_DEALERSAYS_INGAME);
    }

    /**
     * 通知桌面上所有玩家。
     */
    private static void notifyAllOnTable(List<Player> players, String info, int type) {
        for (Player player : players) {
            notifySpecificOnTable(player.getSession(), info, type);
        }
    }

    /**
     * 通知指定玩家。
     */
    private static void notifySpecificOnTable(IoSession session, String info,
                                              int type) {
        if (session != null) {
            Util.sendMsg(session, info, type);
        }
    }


    public static void youAreBroke(Player player) {
        notifySpecificOnTable(player.getSession(), "", Config.TYPE_YOUAREBROKE_INGAME);
    }

    public static void forwardAddFriendRequest(Player player, String info) {
        notifySpecificOnTable(player.getSession(), info, Config.TYPE_ADDFRIENDREQUEST);
    }

    public static void notifyUserAboutGameStatus(IoSession session, String info) {
        notifySpecificOnTable(session, info, Config.TYPE_GAMESTATUS_INGAME);
    }

    public static void sendHeartBeat(IoSession session) {
        notifySpecificOnTable(session, "", Config.TYPE_HEARTBEAT_MANAGE);
    }


}
