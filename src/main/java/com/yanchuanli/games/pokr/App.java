package com.yanchuanli.games.pokr;

import com.yanchuanli.games.pokr.core.GameEngine;
import com.yanchuanli.games.pokr.server.TCPServer;
import org.apache.log4j.Logger;

/**
 * Hello world!
 */
public class App {
    private static Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        GameEngine.start();
        TCPServer.start();
    }
}
