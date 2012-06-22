package com.yanchuanli.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.basic.Deck;
import com.yanchuanli.games.pokr.basic.Hand;
import com.yanchuanli.games.pokr.basic.HandEvaluator;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-1
 */
public class StartGame {

    private static Logger log = Logger.getLogger(StartGame.class);

    public static void main(String[] args) {

        Player player1 = new Player("0", "0");
        player1.setMoney(1000);
        Player player2 = new Player("1", "1");
        player2.setMoney(1000);
        Player player3 = new Player("2", "2");
        player3.setMoney(1000);

        GameConfig gc = new GameConfig(1, "123", 20, 40, 0, 10000, 9, Duration.seconds(3), Duration.millis(500));
        Game game = new Game(gc);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.start();
    }


    private static String printCards(List<Card> cardList) {
        String result = "";
        for (Card card : cardList) {
            result += card.toChineseString() + " ";
        }
        return result;
    }

    private static void round(List<Hand> hands, Deck deck, int cardCount) {
        for (Hand hand : hands) {
            for (int i = 0; i < cardCount; i++) {
                Card card = deck.dealCard();
                hand.addCard(card);
            }
        }
    }

    private static void compare(List<Hand> hands, List<Card> cardOnsTable) {
        HandEvaluator handEval = new HandEvaluator();
        for (Hand hand : hands) {
            for (Card card : cardOnsTable) {
                hand.addCard(card);
            }

            Hand bestHand = handEval.getBest5CardHand(hand);
            log.info(bestHand.toChineseString());
            log.info(handEval.nameHand(bestHand));
        }


        Hand hand1 = hands.get(0);
        Hand hand2 = hands.get(1);
        int result = handEval.compareHands(hand1, hand2);
        switch (result) {
            case 1:
                log.debug("hand1 win");
                break;
            case 2:
                log.debug("hand2 win");
                break;
            case 0:
                log.debug("tie");
                break;
        }
    }
}
