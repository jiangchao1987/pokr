package com.yanchuanli.games.pokr.basic;

import com.yanchuanli.games.pokr.model.Player;

import java.util.Comparator;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-13
 */
public class PlayerRankComparator implements Comparator<Player> {

    private HandEvaluator handEval;

    public PlayerRankComparator() {
        handEval = new HandEvaluator();
    }


    @Override
    public int compare(Player player1, Player player2) {

        if (player1.getBestHand() == null) {
            player1.setBestHand(handEval.getBest5CardHand(player1.getHand()));
            player1.setNameOfHand(HandEvaluator.nameHandInChinese(player1.getBestHand()));
        }

        if (player2.getBestHand() == null) {
            player2.setBestHand(handEval.getBest5CardHand(player2.getHand()));
            player2.setNameOfHand(HandEvaluator.nameHandInChinese(player2.getBestHand()));
        }

        if (player1.getBestHandRank() == Integer.MIN_VALUE) {
            player1.setBestHandRank(HandEvaluator.rankHand(player1.getBestHand()));
        }

        if (player2.getBestHandRank() == Integer.MIN_VALUE) {
            player2.setBestHandRank(HandEvaluator.rankHand(player2.getBestHand()));
        }
        return handEval.compareHands(player1.getBestHand(), player2.getBestHand());
    }
}
