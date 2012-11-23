package com.yanchuanli.games.pokr.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 11/23/12
 */
public class Sentences {
    private static List<String> botSentencesInGame;
    private static List<String> dealyerSentencesWhileWaiting;
    private static List<String> dealyerSentencesInGame;
    private static Logger log = Logger.getLogger(Sentences.class);

    private static List<String> getBotSentencesInGame() {
        if (botSentencesInGame == null) {
            botSentencesInGame = loadSentencesFromFile("botsaysingame.txt");
        }
        return botSentencesInGame;
    }

    private static List<String> getDealyerSentencesWhileWaiting() {
        if (dealyerSentencesWhileWaiting == null) {
            dealyerSentencesWhileWaiting = loadSentencesFromFile("dealersayswhilewaiting.txt");
        }
        return dealyerSentencesWhileWaiting;
    }

    private static List<String> getDealyerSentencesInGame() {
        if (dealyerSentencesInGame == null) {
            dealyerSentencesInGame = loadSentencesFromFile("dealersaysingame.txt");
        }
        return dealyerSentencesInGame;
    }

    private static List<String> loadSentencesFromFile(String filename) {

        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/conf/" + filename)));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    result.add(line);
                    log.debug(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) {
        getBotSentencesInGame();
        getDealyerSentencesInGame();
        getDealyerSentencesWhileWaiting();
    }

}
