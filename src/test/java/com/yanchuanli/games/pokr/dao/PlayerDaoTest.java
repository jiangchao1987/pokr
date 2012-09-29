package com.yanchuanli.games.pokr.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yanchuanli.games.pokr.model.Player;

public class PlayerDaoTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPlayer() {
		Player player = PlayerDao.getPlayer("jiangchao", "jiangchao123", 0);
		System.out.println(player);
	}

}
