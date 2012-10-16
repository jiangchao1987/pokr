package com.yanchuanli.games.pokr.ai;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 9/28/12
 */
public class Bot implements Runnable {
    private int id;
    private String username;
    private String password;
    private int roomId;
    private boolean stop;

    public Bot(String username, String password, int roomId) {
        this.username = username;
        this.password = password;
        this.roomId = roomId;
        stop = false;
    }

    public Bot(String username, String password) {
        this(username, password, Integer.MIN_VALUE);
    }

    @Override
    public void run() {

    }

    private void login() {

    }

    private void buyIn() {

    }

    private void standBy() {

    }

    public static void main(String[] args) {
        Bot bot = new Bot("a", "a123");
        bot.run();
    }
}
