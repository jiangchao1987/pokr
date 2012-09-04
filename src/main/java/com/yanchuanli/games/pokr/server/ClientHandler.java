package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class ClientHandler extends IoHandlerAdapter {

    private static Logger log = Logger.getLogger(ClientHandler.class);
    
    private boolean hasLoginedIn = false;
    private List<String> cardsOnTable = null;
    private String holeCardsStr = null;
    private int counter = 0;

    public ClientHandler() {

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        Memory.sessionsOnClient.put(String.valueOf(session.getId()), session);
//        log.info("sessionCreated ...");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        if (message instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer) message;


            List<Map<Integer, String>> list = Util.ioBufferToString(session.getId(), buffer);
            for (Map<Integer, String> map : list) {
                for (Integer key : map.keySet()) {

                    String info = map.get(key);
                    log.debug("[messageReceived] status code: [" + key + "] " + info);
                    switch (key) {
                        case Config.TYPE_ACTION_INGAME:
                            String[] infos = info.split(",");
                            String username = infos[1];
                            log.debug(username + ":" + infos[2]);
                            log.info(String.format("%s 可使用的操作: %s 奖池: %s", username, Util.parseCmdsInGame(infos[2]), infos[3]));
                            break;
                        case Config.TYPE_HOLE_INGAME:
                            infos = info.split(",");
                            username = infos[1];
                            String[] pokers = infos[2].split("_");
                            String debuginfo = "";
                            for (String s : pokers) {
                                Card c = new Card(Integer.parseInt(s));
                                debuginfo = debuginfo + " " + c.toChineseString();
                            }
                            log.debug(username + ":" + debuginfo);
                            log.info(String.format("%s 收到的牌: %s", username, debuginfo));
                            break;
                        case Config.TYPE_JOIN_INGAME:	//3
                        	if (!hasLoginedIn) {
                        		log.info("进入游戏房间成功");
                            	log.info("请输入携带的金币");
                            	hasLoginedIn = true;
                        	}
                            break;
                        case Config.TYPE_CARD_INGAME:
                            infos = info.split(",");
                            pokers = infos[0].split("_");
                            debuginfo = "";
                            for (String s : pokers) {
                                Card c = new Card(Integer.parseInt(s));
                                debuginfo = debuginfo + " " + c.toChineseString();
                            }
                            log.debug("ontable" + ":" + debuginfo);
                            log.info("桌子上的牌" + ":" + debuginfo);
                            
                            counter++;
                            if (counter == 3) {
                            	cardsOnTable = Util.parseCardsInGame(debuginfo.trim());
                            }
                            break;
                        case Config.TYPE_CHAT_INGAME:
                            log.debug(info);
                            break;
                        case Config.TYPE_LOGIN_INGAME:	//1
                        	log.info("登录成功");
                        	log.info("请加入某个房间");
                        	break;
                        case Config.TYPE_BUYIN_INGAME: //22
                        	if (info.trim().equals("1")) {
                        		log.info("操作成功");
                        		log.info("请就坐");
                        	} else {
                        		log.info("操作失败");
                        	}
                        	break;
                        case Config.TYPE_WINORLOSE_INGAME:	//9
                        	log.info(String.format("本轮游戏结束\n %s", Util.parseCardsGameOver(info, cardsOnTable, holeCardsStr)));
                        	cardsOnTable = null;
                            holeCardsStr = null;
                            counter = 0;
                        	break;
                        case Config.TYPE_SHOW2CARDS:	//20
                        	holeCardsStr = info;
                        	break;
                    }
                }
            }
        } else {
            log.info("[messageReceived]illegal");
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("sessionClosed");
        super.sessionClosed(session);
        Memory.sessionsOnClient.remove(String.valueOf(session.getId()));
        System.exit(1);
    }
    
}
