package com.yanchuanli.games.pokr.test.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-8-1
 */
public class SegmentedHttpDownloader implements Runnable {

    private static String ipaurl = "http://dl.appvv.com/95f998f56979d09e3a4eba2f9c2f6faeb47a373e.ipa";
    private static Logger log = Logger.getLogger(SegmentedHttpDownloader.class);
    private String url;
    private boolean acceptRanges = false;
    private static int threadCount = 10;

    public SegmentedHttpDownloader(String url) {
        this.url = url;
    }


    @Override
    public void run() {
        HttpUriRequest httpget = new HttpGet(url);
        httpget.addHeader("Host", "dl.appvv.com");
        httpget.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SIMBAR={DC83FBA1-CFD9-11E1-9858-000B6B67C82B}; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; MASM; .NET4.0C; .NET4.0E)");
        httpget.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        httpget.addHeader("Accept-Language", "zh-cn");
        httpget.addHeader("Accept-Encoding", "gzip, deflate");
        httpget.addHeader("Keep-Alive", "115");
        httpget.addHeader("Connection", "keep-alive");
        HttpClient hc = new DefaultHttpClient();
        try {
            HttpResponse response = hc.execute(httpget);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new Exception("资源不存在!");
            }

            long contentLength = 0;
            Header[] headers = response.getHeaders("Content-Length");
            if (headers.length > 0) {
                contentLength = Long.valueOf(headers[0].getValue());
            }
            httpget.abort();
            log.info("contentlength:"+contentLength);

            httpget = new HttpGet(url);
            httpget.addHeader("Range", "bytes=0-" + (contentLength - 1));
            response = hc.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 206) {
                acceptRanges = true;
            }
            httpget.abort();

            File file = new File("123.ipa");
            file.createNewFile();

            ExecutorService pool = Executors.newFixedThreadPool(threadCount);
            long perThreadLength = contentLength / threadCount + 1;
            long startPosition = 0;
            long endPosition = perThreadLength;
            do {
                if (endPosition >= contentLength) {
                    endPosition = contentLength - 1;
                }
                DownloadThread dt = new DownloadThread(file, startPosition, endPosition, url);
                pool.submit(dt);
                startPosition = endPosition + 1;
                endPosition += perThreadLength;

            } while (startPosition < contentLength);

            pool.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SegmentedHttpDownloader dt = new SegmentedHttpDownloader(ipaurl);
        Thread t = new Thread(dt);
        t.start();
    }
}
