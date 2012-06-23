package com.yanchuanli.games.pokr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrowserUtil {

    private static Random ran = new Random();

    private static List<String> useragents = new ArrayList<String>();

    static {
        useragents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/4.0)");
        useragents.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)");
        useragents.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
        useragents.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)");
        useragents.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; Trident/4.0)");
        useragents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; )");
        useragents.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.53 Safari/525.19");
        useragents.add("Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/3.0.195.27 Safari/532.0");
        useragents.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.36 Safari/525.19");
        useragents.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 8.0");
        useragents.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 8.50");
        useragents.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.5) Gecko/20060127 Netscape/8.1");
        useragents.add("Opera/9.20 (Windows NT 6.0; U; en)");
        useragents.add("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; fi-fi) AppleWebKit/420+ (KHTML, like Gecko) Safari/419.3");
        useragents.add("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/312.8 (KHTML, like Gecko) Safari/312.6");
        useragents.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; sv-SE) AppleWebKit/533.19.4 (KHTML, like Gecko) Version/5.0.3 Safari/533.19.4");
    }

    public static String getRandomBrowserUserAgent() {
        return useragents.get(ran.nextInt(useragents.size()));
    }
    
}
