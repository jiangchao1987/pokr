package com.yanchuanli.games.pokr.model;

import com.yanchuanli.games.pokr.util.Config;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-6
 */

public class Pot {

    private int money;
    private List<Map<String, Integer>> pots;
    private List<Map<String, Integer>> foldPots;
    private Map<String, Integer> currentFoldPlayers;
    private Map<String, Integer> currentPot;
    private List<Record> currentAllInPlayers;
    private RecordComparator comparator;
    private static Logger log = Logger.getLogger(Pot.class);

    public Pot() {
        money = 0;
        pots = new ArrayList<>();
        foldPots = new ArrayList<>();
        currentPot = new HashMap<>();
        currentFoldPlayers = new HashMap<>();
        comparator = new RecordComparator();
        currentAllInPlayers = new ArrayList<>();
    }

    public void addRecord(Record record) {
        log.debug(record);
        money += record.getBet();
        if (currentPot.containsKey(record.getUdid())) {
            int currentBetOfPlayer = currentPot.get(record.getUdid());
            currentBetOfPlayer += record.getBet();
            currentPot.put(record.getUdid(), currentBetOfPlayer);
        } else {
            currentPot.put(record.getUdid(), record.getBet());
        }

        if (record.getActionType() == Config.ACTION_TYPE_ALL_IN) {
            int hisBet = currentPot.get(record.getUdid());
            hisBet += record.getBet();
            Record r = new Record(record.getUdid(), Config.ACTION_TYPE_ALL_IN, hisBet);
            currentAllInPlayers.add(r);
        } else if (record.getActionType() == Config.ACTION_TYPE_FOLD) {
            int hisBet = currentPot.get(record.getUdid());
            currentPot.remove(record.getUdid());
            Iterator<Record> it = currentAllInPlayers.iterator();
            while (it.hasNext()) {
                Record re = it.next();
                if (re.getUdid().equals(record.getUdid())) {
                   it.remove();
                }
            }
            currentFoldPlayers.put(record.getUdid(), hisBet);


            // he would also "fold" in all previous pots and re-calculate the money there
            for (int i = 0; i < pots.size(); i++) {
                Map<String, Integer> thatPot = getPotAtIndex(i);
                Map<String, Integer> thatFoldPot = getFoldPotAtIndex(i);

                if (thatPot.containsKey(record.getUdid())) {
                    thatFoldPot.put(record.getUdid(), thatPot.get(record.getUdid()));
                    thatPot.remove(record.getUdid());
                }
            }


        }

    }

    public void buildPotList() {
        log.debug("buildPotList");
        log.debug("currentPot:" + currentPot);
        Collections.sort(currentAllInPlayers, comparator);
        log.debug("currentAllInPlayers:" + currentAllInPlayers);
        for (int i = 0; i < currentAllInPlayers.size(); i++) {
            String minAllInPlayer = currentAllInPlayers.get(i).getUdid();
            int minBet = currentPot.get(minAllInPlayer);
            Map<String, Integer> smallPot = new HashMap<>();
            Iterator<String> it = currentPot.keySet().iterator();
            while (it.hasNext()) {
                String udid = it.next();
                int hisBet = currentPot.get(udid);
                if (hisBet <= minBet) {
                    smallPot.put(udid, hisBet);
                    it.remove();
                } else {
                    hisBet = hisBet - minBet;
                    smallPot.put(udid, minBet);
                    currentPot.put(udid, hisBet);
                }
            }

            Map<String, Integer> smallFoldPot = new HashMap<>();
            Iterator<String> fit = currentFoldPlayers.keySet().iterator();
            while (fit.hasNext()) {
                String udid = fit.next();
                int hisBet = currentFoldPlayers.get(udid);
                if (hisBet <= minBet) {
                    smallFoldPot.put(udid, hisBet);
                    fit.remove();
                } else {
                    hisBet = hisBet - minBet;
                    smallFoldPot.put(udid, minBet);
                    currentFoldPlayers.put(udid, hisBet);

                }
            }

            pots.add(smallPot);
            foldPots.add(smallFoldPot);
        }

        //在遍历完所有AllIn玩家后，还可能存在在AllIn玩家之后的下一个玩家raise他的情况。
        if (!currentPot.isEmpty()) {
            Map<String, Integer> smallPot = new HashMap<>();
            Iterator<String> it = currentPot.keySet().iterator();
            while (it.hasNext()) {
                String udid = it.next();
                smallPot.put(udid, currentPot.get(udid));
                it.remove();
            }
            pots.add(smallPot);
//            Map<String, Integer> smallFoldPot = new HashMap<>();
            foldPots.add(currentFoldPlayers);
        } else {
        }

        currentPot = pots.get(pots.size() - 1);
        pots.remove(pots.size() - 1);
        currentFoldPlayers = foldPots.get(foldPots.size() - 1);
        foldPots.remove(foldPots.size() - 1);


        // 如果该AllInPlayer进入到下轮的池子，则不需要删除掉，参考Test.testPot2;
        Iterator<Record> rit = currentAllInPlayers.iterator();
        while (rit.hasNext()) {
            Record r = rit.next();
            if (currentFoldPlayers.containsKey(r.getUdid()) || currentPot.containsKey(r.getUdid())) {

            } else {
                rit.remove();
            }
        }

//        currentAllInPlayers.clear();


    }


    public void finish() {
        if (!currentPot.isEmpty()) {
            pots.add(currentPot);
            foldPots.add(currentFoldPlayers);
        }

        for (int i = 0; i < pots.size(); i++) {
            log.debug("===========================");
            Map<String, Integer> m = pots.get(i);
            log.debug(m);
            Map<String, Integer> f = foldPots.get(i);
            log.debug(f);
            int moneyInThisPot = getMoneyAtIndex(i);
            log.debug("money in this pot:" + moneyInThisPot);
        }

        log.debug("total:" + money);
    }

    public int getMoney() {
        return money;
    }


    public List<Map<String, Integer>> getPots() {
        return pots;
    }

    public int potsCount() {
        return pots.size();
    }

    public Map<String, Integer> getPotAtIndex(int index) {
        return pots.get(index);
    }

    public Map<String, Integer> getFoldPotAtIndex(int index) {
        return foldPots.get(index);
    }

    public int getMoneyAtIndex(int index) {
        int result = 0;
        Map<String, Integer> m = getPotAtIndex(index);
//        log.debug(m);
        for (String udid : m.keySet()) {
            Integer t = m.get(udid);
            result += t;
        }

        Map<String, Integer> f = getFoldPotAtIndex(index);
        for (String udid : f.keySet()) {
            Integer t = f.get(udid);
            result += t;
        }
//        result += moneyListOfFoldPlayers.get(index);
        return result;
    }

    public void clear() {
        money = 0;

        pots.clear();
        currentPot.clear();
        currentAllInPlayers.clear();
        currentFoldPlayers.clear();

    }

    public void takeMoneyAway(int amount) {
        money -= amount;
    }

    public boolean hasMoneyLeft() {
        return money == 0;
    }
}
