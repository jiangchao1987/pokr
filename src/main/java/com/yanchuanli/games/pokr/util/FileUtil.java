package com.yanchuanli.games.pokr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.yanchuanli.games.pokr.model.Dummy;

public class FileUtil {

	public static String uploadAvatar(Dummy dummy, String savePath, File avatar) {
		String avatarPath = null;
		try {
			// generate new avatar name
			String suffix = avatar.getName().substring(avatar.getName().lastIndexOf("."), avatar.getName().length());
			String newFilename = File.separator + dummy.getUdid() + TimeUtil.unixtime() + suffix;

			// create avatar folder if exists
			String tempPath = File.separator + TimeUtil.year() + File.separator + TimeUtil.month() + File.separator
					+ TimeUtil.day();
			String folderPath = savePath + tempPath;
			newFolder(folderPath); // new folder

			// copy file from inputstream to destination
			FileOutputStream fos = new FileOutputStream(folderPath + newFilename);
			FileInputStream fis = new FileInputStream(avatar);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
			avatarPath = tempPath + newFilename;
		} catch (Exception e) {
			ExceptionUtils.getStackTrace(e);
			avatarPath = null;
		}
		return avatarPath;
	}

	private static void newFolder(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	public static InputStream getSeedFile(String file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static List<List<String>> getSeedList(InputStream io) {
		List<List<String>> seeds = new ArrayList<>();
		List<String> seed = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(io));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] splited = line.split("\\|");
				if (!line.startsWith("#")) {
					seed = Arrays.asList(splited[0], splited[1], splited[2], splited[3], splited[4], splited[5],
							splited[6], splited[7], splited[8]);
					seeds.add(seed);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return seeds;
	}

}
