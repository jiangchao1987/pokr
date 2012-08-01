package com.yanchuanli.games.pokr.test.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-8-1
 */
public class DownloadThread implements Runnable {

    private File file;
    private long startPosition;
    private long endPosition;
    private String url;
    private static Logger log = Logger.getLogger(DownloadThread.class);

    public DownloadThread(File file, long startPosition, long endPosition, String url) {
        this.file = file;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.url = url;
    }

    @Override
    public void run() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            HttpClient hc = new DefaultHttpClient();
            HttpResponse response;
            HttpGet httpget;
            int fixSize = 500000;
            while (startPosition < endPosition - 1) {
                httpget = new HttpGet(url);
                long next = startPosition + fixSize;
                if (next > endPosition - 1) {
                    next = endPosition - 1;
                }
                httpget.addHeader("Range", "bytes=" + startPosition + "-" + next);
                log.info("downloading " + startPosition + "-" + next);
                response = hc.execute(httpget);
                InputStream inputStream = response.getEntity().getContent();
                RandomAccessFile outputStream = new RandomAccessFile(file, "rw");
                outputStream.seek(startPosition);
                int count = 0;
                byte[] buffer = new byte[1024];
                while ((count = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
                httpget.abort();
                startPosition = next;
                log.info("download finished ...");
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
