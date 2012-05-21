package com.yanchuanli.games.pokr;

import com.yanchuanli.games.pokr.core.Hand;
import com.yanchuanli.games.pokr.core.HandEvaluator;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;

/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class HandEvaluatorTest {

    private HandEvaluator handEval;
    private static Logger log = Logger.getLogger(HandEvaluatorTest.class);


    public void setUp() throws Exception {
        handEval = new HandEvaluator();
    }

    @Test
    public void testWin() throws Exception {
        Hand hand1 = new Hand("Kc 8c 9c Tc Jc Qc Ac");
        Hand hand2 = new Hand("Kc 8c 9c Tc Jc Qc Ad");

        log.info("Hand1: " + hand1.toChineseString());
        log.info("Hand2: " + hand2.toChineseString());

        log.info(handEval.getBest5CardHand(hand1));
        log.info(handEval.getBest5CardHand(hand2));

        Hand besthand1 = handEval.getBest5CardHand(hand1);
        Hand besthand2 = handEval.getBest5CardHand(hand2);

        int score = handEval.compareHands(hand1, hand2);


        log.info("Best Hand1[" + String.valueOf(score) + "]:" + besthand1.toChineseString());
        log.info("Best Hand2[" + String.valueOf(score) + "]:" + besthand2.toChineseString());
        int result = handEval.compareHands(hand1, hand2);
        assertEquals(result, 1);

    }


}
