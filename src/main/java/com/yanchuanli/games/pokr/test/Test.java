package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Pot;
import com.yanchuanli.games.pokr.model.Record;
import com.yanchuanli.games.pokr.model.Table;
import com.yanchuanli.games.pokr.util.Config;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Test {

    private static Logger log = Logger.getLogger(Test.class);
    private static String AppStoreSandboxVerifyURL = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static String AppStoreVerifyURL = "https://buy.itunes.apple.com/verifyReceipt";
    private static String data = "{\n" +
            "\t\"signature\" = \"AhwqqkESe15lSDEuFtM6NFP26HRcMEyEpJD6nnZH2l5IlPStP1KiTygQwaeaSTGnVFof4rcJn2ycb/wc/q7Z/me8E+MPJ7lKmksNSt7NuZuGUjDUFzc6MCHEPz2QJISqW27YwZ8Q+GAnYonlpXRPg2amI1e2ETMZGrlmGEd7L5X0AAADVzCCA1MwggI7oAMCAQICCGUUkU3ZWAS1MA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA5MDYxNTIyMDU1NloXDTE0MDYxNDIyMDU1NlowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMrRjF2ct4IrSdiTChaI0g8pwv/cmHs8p/RwV/rt/91XKVhNl4XIBimKjQQNfgHsDs6yju++DrKJE7uKsphMddKYfFE5rGXsAdBEjBwRIxexTevx3HLEFGAt1moKx509dhxtiIdDgJv2YaVs49B0uJvNdy6SMqNNLHsDLzDS9oZHAgMBAAGjcjBwMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUNh3o4p2C0gEYtTJrDtdDC5FYQzowDgYDVR0PAQH/BAQDAgeAMB0GA1UdDgQWBBSpg4PyGUjFPhJXCBTMzaN+mV8k9TAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAEaSbPjtmN4C/IB3QEpK32RxacCDXdVXAeVReS5FaZxc+t88pQP93BiAxvdW/3eTSMGY5FbeAYL3etqP5gm8wrFojX0ikyVRStQ+/AQ0KEjtqB07kLs9QUe8czR8UGfdM1EumV/UgvDd4NwNYxLQMg4WTQfgkQQVy8GXZwVHgbE/UC6Y7053pGXBk51NPM3woxhd3gSRLvXj+loHsStcTEqe9pBDpmG5+sk4tw+GK3GMeEN5/+e1QT9np/Kl1nj+aBw7C0xsy0bFnaAd1cSS6xdory/CUvM6gtKsmnOOdqTesbp0bs8sn6Wqs0C9dgcxRHuOMZ2tm8npLUm7argOSzQ==\";\n" +
            "\t\"purchase-info\" = \"ewoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtcHN0IiA9ICIyMDEyLTA3LTE1IDA2OjI3OjAzIEFtZXJpY2EvTG9zX0FuZ2VsZXMiOwoJIm9yaWdpbmFsLXRyYW5zYWN0aW9uLWlkIiA9ICIxMDAwMDAwMDUyODM0NTMwIjsKCSJidnJzIiA9ICIyMDEyMDcxNCI7CgkidHJhbnNhY3Rpb24taWQiID0gIjEwMDAwMDAwNTI4MzQ1MzAiOwoJInF1YW50aXR5IiA9ICIxIjsKCSJvcmlnaW5hbC1wdXJjaGFzZS1kYXRlLW1zIiA9ICIxMzQyMzU4ODIzMjU0IjsKCSJwcm9kdWN0LWlkIiA9ICIxMDAwMiI7CgkiaXRlbS1pZCIgPSAiNTQ1MTA0MjAzIjsKCSJiaWQiID0gImNvbS54eHgucmljaG1hbiI7CgkicHVyY2hhc2UtZGF0ZS1tcyIgPSAiMTM0MjM1ODgyMzI1NCI7CgkicHVyY2hhc2UtZGF0ZSIgPSAiMjAxMi0wNy0xNSAxMzoyNzowMyBFdGMvR01UIjsKCSJwdXJjaGFzZS1kYXRlLXBzdCIgPSAiMjAxMi0wNy0xNSAwNjoyNzowMyBBbWVyaWNhL0xvc19BbmdlbGVzIjsKCSJvcmlnaW5hbC1wdXJjaGFzZS1kYXRlIiA9ICIyMDEyLTA3LTE1IDEzOjI3OjAzIEV0Yy9HTVQiOwp9\";\n" +
            "\t\"environment\" = \"Sandbox\";\n" +
            "\t\"pod\" = \"100\";\n" +
            "\t\"signing-status\" = \"0\";\n" +
            "}";

    public static void main(String[] args) throws IOException {
//        testTable();
//        testPot();
        testSharedPlayer();


    }

    public static void testSharedPlayer() {
        Player player = new Player("1", "yanchuan");
        player.setMoneyInGame(10000);
        Map<String, Player> map = new HashMap<>();
        map.put("1", player);
        List<Player> list = new CopyOnWriteArrayList<>();
        list.add(player);

        Player aplayer = map.get("1");

        aplayer.setMoneyInGame(8000);
        log.debug(aplayer.getMoneyInGame());

        Player bplayer = list.get(0);
        log.debug(bplayer.getMoneyInGame());
        log.debug(list.contains(aplayer));
    }

    public static void testTable() {
        Table table = new Table();
        Player playera = new Player("1", "yanchuan");
        Player playerb = new Player("2", "jiangchao");
        table.joinTable(playera);
        table.joinTable(playerb);
        table.printTableInfo();

        for (int i = 0; i < 5; i++) {
            log.debug(table.nextPlayer().getName());
        }
    }

    public static void testPot() {
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        Pot pot = new Pot();
        Record record1 = new Record(a, Config.ACTION_TYPE_SMALL_BLIND, 5);
        Record record2 = new Record(b, Config.ACTION_TYPE_BIG_BLIND, 10);
        Record record3 = new Record(c, Config.ACTION_TYPE_ALL_IN, 8);
        Record record4 = new Record(d, Config.ACTION_TYPE_CALL, 10);
        Record record5 = new Record(e, Config.ACTION_TYPE_CALL, 10);
        Record record6 = new Record(a, Config.ACTION_TYPE_RAISE, 15);
        Record record7 = new Record(b, Config.ACTION_TYPE_CALL, 10);
        Record record8 = new Record(d, Config.ACTION_TYPE_RAISE, 30);
        Record record9 = new Record(e, Config.ACTION_TYPE_CALL, 30);
        Record record10 = new Record(a, Config.ACTION_TYPE_CALL, 20);
        Record record11 = new Record(b, Config.ACTION_TYPE_CALL, 20);


        pot.addRecord(record1);
        pot.addRecord(record2);
        pot.addRecord(record3);
        pot.addRecord(record4);
        pot.addRecord(record5);
        pot.addRecord(record6);
        pot.addRecord(record7);
        pot.addRecord(record8);
        pot.addRecord(record9);
        pot.addRecord(record10);
        pot.addRecord(record11);
        pot.buildPotList();
        Record record12 = new Record(a, Config.ACTION_TYPE_RAISE, 20);
        Record record13 = new Record(b, Config.ACTION_TYPE_ALL_IN, 10);
        Record record14 = new Record(d, Config.ACTION_TYPE_RAISE, 50);
        Record record15 = new Record(e, Config.ACTION_TYPE_CALL, 50);
        Record record16 = new Record(a, Config.ACTION_TYPE_FOLD, 0);


        pot.addRecord(record12);
        pot.addRecord(record13);
        pot.addRecord(record14);
        pot.addRecord(record15);
        pot.addRecord(record16);
        pot.buildPotList();

        Record record17 = new Record(d, Config.ACTION_TYPE_RAISE, 50);
        Record record18 = new Record(e, Config.ACTION_TYPE_ALL_IN, 300);
        Record record19 = new Record(d, Config.ACTION_TYPE_FOLD, 0);

        pot.addRecord(record17);
        pot.addRecord(record18);
        pot.addRecord(record19);
        pot.buildPotList();


        pot.finish();
    }

    public static void verifyIAP() {
        HttpURLConnection.setFollowRedirects(false);
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedBytes = encoder.encodeBuffer(data.getBytes());


        String json = "{\"receipt-data\":\"" + encodedBytes + "\"}";
        HttpURLConnection con = null;
        try {

            con = (HttpURLConnection) new URL(AppStoreSandboxVerifyURL).openConnection();

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) con;
            httpsURLConnection.setDoOutput(true);

            OutputStreamWriter wr = null;
            wr = new OutputStreamWriter(httpsURLConnection.getOutputStream());
            wr.write(json);
            wr.flush();

            // Get the response
            httpsURLConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    httpsURLConnection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                log.debug(line);
            }
            rd.close();

            System.out.println(httpsURLConnection);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
