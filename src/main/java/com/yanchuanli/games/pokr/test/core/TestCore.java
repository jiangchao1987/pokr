package com.yanchuanli.games.pokr.test.core;

import com.yanchuanli.games.pokr.core.Hand;
import com.yanchuanli.games.pokr.core.HandEvaluator;
import org.apache.log4j.Logger;

/**
 * Author: Yanchuan Li
 * Date: 5/18/12
 * Email: mail@yanchuanli.com
 */
public class TestCore {

    private static Logger log = Logger.getLogger(TestCore.class);

    public static void main(String[] args) {
        Hand hand1 = new Hand("Kc 8c 9c Tc Jc Qc Ac");
        Hand hand2 = new Hand("Kc 8c 9c Tc Jc Qc Ad");

        HandEvaluator handEval = new HandEvaluator();

        log.info("Hand1: " + hand1.toChineseString());
        log.info("Hand2: " + hand2.toChineseString());

        log.info(handEval.getBest5CardHand(hand1));
        log.info(handEval.getBest5CardHand(hand2));

        Hand besthand1 = handEval.getBest5CardHand(hand1);
        Hand besthand2 = handEval.getBest5CardHand(hand2);

        int score = handEval.compareHands(hand1,hand2);


        log.info("Best Hand1[" + String.valueOf(score) + "]:" + besthand1.toChineseString());
        log.info("Best Hand2[" + String.valueOf(score) + "]:" + besthand2.toChineseString());

        if(score==1){
            log.info("Hand1 won!");
        }else if(score==2){
            log.info("Hand2 won!");
        }else{
            log.info("draw!");
        }

        log.info(handEval.nameHand(hand1));
        log.info(handEval.nameHand(hand2));
    }
}
