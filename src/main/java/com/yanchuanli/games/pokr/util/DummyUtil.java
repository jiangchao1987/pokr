package com.yanchuanli.games.pokr.util;

import java.io.File;
import java.util.List;

import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.model.Dummy;

/**
 * @author jiangchao
 */
public class DummyUtil {
	
	private static final String SEEDS_PATH = "conf/seeds.txt";
	private static final String SAVE_AVATAR_PATH = "/upload";
	private static final String SAVE_BASE_PATH = "/var/lib/tomcat6/webapps/texasbackend";
	private static final String SAVE_PATH = SAVE_BASE_PATH + SAVE_AVATAR_PATH;

	public static void main(String[] args) {
		List<List<String>> seeds = FileUtil.getSeedList(FileUtil.getSeedFile(SEEDS_PATH));
		for (List<String> seed : seeds) {
			Dummy dummy = new Dummy();
			dummy.setUdid(seed.get(0));
			dummy.setSource(randomInteger(2));
			dummy.setName(seed.get(1));
			dummy.setPassword(seed.get(2));
			dummy.setMoney(randomInteger(20000) + 20000);
			dummy.setExp(Integer.parseInt(seed.get(3)));
			dummy.setWinCount(randomInteger(200));
			dummy.setLoseCount(randomInteger(300));
			dummy.setHistoricalBestHandRank(Integer.parseInt(seed.get(4)));
			dummy.setHistoricalBestHand(seed.get(5));
			dummy.setMaxWin(randomInteger(100000) + 3000);
			dummy.setAvatar("");
			dummy.setCustomAvatar(0);
			dummy.setSex(randomInteger(2));
			dummy.setAddress(seed.get(6));
			dummy.setLevel(Integer.parseInt(seed.get(7)));
			dummy.setOnline(0);
			dummy.setElapsedTimeToday(0);
			dummy.setTimeLevelToday(0);
			dummy.setRoomId(0);

			File avatar = new File(seed.get(8));
			dummyPlayer(avatar, dummy);
		}
	}
	
	public static int randomInteger(int range) {
		return ((int) (Math.random() * range));
	}

	public static void dummyPlayer(File avatar, Dummy dummy) {
		String path = FileUtil.uploadAvatar(dummy, SAVE_PATH, avatar);
		if (path != null) {
			dummy.setAvatar(SAVE_AVATAR_PATH + path);
			PlayerDao.insert(dummy);
		}
	}

}
