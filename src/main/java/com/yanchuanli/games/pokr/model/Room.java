package com.yanchuanli.games.pokr.model;

import java.util.List;

/**
 * Copyright Candou.com 
 * Author: Yanchuan Li 
 * Email: mail@yanchuanli.com 
 * Date: 12-5-31
 */
public class Room {

	private int id;
	private String name;
	private int smallBlindAmount;
	private int bigBlindAmount;
	private int minHolding;
	private int maxHolding;
	private int maxPlayersCount;
	private int currentPlayerCount;
	private int level;
	private int bettingDuration;

	private List<Player> players;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSmallBlindAmount() {
		return smallBlindAmount;
	}

	public void setSmallBlindAmount(int smallBlindAmount) {
		this.smallBlindAmount = smallBlindAmount;
	}

	public int getBigBlindAmount() {
		return bigBlindAmount;
	}

	public void setBigBlindAmount(int bigBlindAmount) {
		this.bigBlindAmount = bigBlindAmount;
	}

	public int getMinHolding() {
		return minHolding;
	}

	public void setMinHolding(int minHolding) {
		this.minHolding = minHolding;
	}

	public int getMaxHolding() {
		return maxHolding;
	}

	public void setMaxHolding(int maxHolding) {
		this.maxHolding = maxHolding;
	}

	public int getMaxPlayersCount() {
		return maxPlayersCount;
	}

	public void setMaxPlayersCount(int maxPlayersCount) {
		this.maxPlayersCount = maxPlayersCount;
	}

	public int getCurrentPlayerCount() {
		return currentPlayerCount;
	}

	public void setCurrentPlayerCount(int currentPlayerCount) {
		this.currentPlayerCount = currentPlayerCount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getBettingDuration() {
		return bettingDuration;
	}

	public void setBettingDuration(int bettingDuration) {
		this.bettingDuration = bettingDuration;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

}
