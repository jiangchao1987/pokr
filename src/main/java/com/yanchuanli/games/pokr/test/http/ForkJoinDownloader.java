package com.yanchuanli.games.pokr.test.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Author: Yanchuan Li
 * Date: 8/1/12
 * Email: mail@yanchuanli.com
 */
public class ForkJoinDownloader extends RecursiveAction {

    private File file;
    private long startPosition;
    private long endPosition;
    private String url;
    private static Logger log = Logger.getLogger(ForkJoinDownloader.class);
    private int THRESHOLD = 100000;

    public ForkJoinDownloader(File file, long startPosition, long endPosition, String url) {
        this.file = file;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.url = url;
    }

    @Override
    protected void compute() {
        if (endPosition - startPosition < THRESHOLD) {
            download();
        } else {

            long length = endPosition - startPosition;
            log.info("splitting into " + length);
            long pivot = length / 2;
            invokeAll(new ForkJoinDownloader(file, startPosition, startPosition + pivot, url), new ForkJoinDownloader(file, pivot + 1, endPosition, url));
        }
    }

    private void download() {
        log.info("downloading ...");
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

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("123.ipa");
        file.createNewFile();
        String ipaurl = "http://dl.appvv.com/95f998f56979d09e3a4eba2f9c2f6faeb47a373e.ipa";
        ForkJoinTask sort = new ForkJoinDownloader(file, 0, 1433975, ipaurl);
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(sort);
        pool.shutdown();
        pool.awaitTermination(1000, TimeUnit.SECONDS);
    }
}
