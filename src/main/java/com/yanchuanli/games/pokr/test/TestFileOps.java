package com.yanchuanli.games.pokr.test;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 10/17/12
 */
public class TestFileOps {
    private static Logger log = Logger.getLogger(TestFileOps.class);

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/gillbates/Desktop/163acc.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String content = "";
        FileWriter fw = new FileWriter("/Users/gillbates/Desktop/new163acc.txt");

        BufferedWriter bw = new BufferedWriter(fw);

        while ((line = br.readLine()) != null) {
            String[] infos = line.split("\t");
            if (infos.length == 2) {
                String username = infos[0];
                String password = infos[1];
                bw.write(username + " " + password + "\n");
            } else {
                log.info(line);
            }

        }

        bw.close();

    }
}
