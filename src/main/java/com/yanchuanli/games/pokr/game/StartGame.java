package com.yanchuanli.games.pokr.game;

import com.yanchuanli.games.pokr.core.Card;
import com.yanchuanli.games.pokr.core.Deck;
import com.yanchuanli.games.pokr.core.Hand;
import com.yanchuanli.games.pokr.core.HandEvaluator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
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
        Deck deck = new Deck();
        deck.shuffle();

        List<Card> cardsOnDesk = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Card card = deck.dealCard();
            cardsOnDesk.add(card);
        }
        printCards(cardsOnDesk);

        Hand hand1 = new Hand();
        Hand hand2 = new Hand();
        List<Hand> hands = new ArrayList<>();
        hands.add(hand1);
        hands.add(hand2);

        round(hands, deck, 2);
        log.debug("起手牌 ...");
        log.debug("hand1:" + hand1.toChineseString());
        log.debug("hand2:" + hand2.toChineseString());

        Card card4 = deck.dealCard();
        cardsOnDesk.add(card4);
        log.debug("table:" + printCards(cardsOnDesk));

        Card card5 = deck.dealCard();
        cardsOnDesk.add(card5);
        log.debug("table:" + printCards(cardsOnDesk));

        compare(hands, cardsOnDesk);
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

            Hand bestHand=handEval.getBest5CardHand(hand);
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
