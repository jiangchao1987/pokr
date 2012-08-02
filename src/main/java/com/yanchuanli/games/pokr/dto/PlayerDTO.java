package com.yanchuanli.games.pokr.dto;

public class PlayerDTO {

	private String udid;
	private String name;
	private int moneyInGame;
	private int customAvatar;
	private String avatar;
	private int sex;
	private String address;
	private int seatIndex;

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoneyInGame() {
		return moneyInGame;
	}

	public void setMoneyInGame(int moneyInGame) {
		this.moneyInGame = moneyInGame;
	}

	public int getCustomAvatar() {
		return customAvatar;
	}

	public void setCustomAvatar(int customAvatar) {
		this.customAvatar = customAvatar;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSeatIndex() {
		return seatIndex;
	}

	public void setSeatIndex(int seatIndex) {
		this.seatIndex = seatIndex;
	}

}
