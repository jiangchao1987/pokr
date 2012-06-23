package com.yanchuanli.games.pokr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class URLFetchUtil {
	private static Logger logger = Logger.getLogger(URLFetchUtil.class);

	public static String fetch(String url) {
		logger.info("Connection " + url);

		String content = null;
		try {
			URL request = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) request.openConnection();

			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", BrowserUtil.getRandomBrowserUserAgent());
			connection.setRequestProperty("Accept-Language", "zh-cn, zh;q=0.75, en-us;q=0.50, en;q=0.25");

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = in.readLine()) != null) {
					sb.append(line).append(System.getProperty("line.separator"));
				}
				content = sb.toString();
			} else {
				logger.error(String.format("Response status error: %d - %s", connection.getResponseCode(), connection.getResponseMessage()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
