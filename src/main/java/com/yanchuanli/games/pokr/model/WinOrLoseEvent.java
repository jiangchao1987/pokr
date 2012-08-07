package com.yanchuanli.games.pokr.model;

public class WinOrLoseEvent extends Event {

	private String roomName;
	private String nameOfBestHand;
	private int money;

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getNameOfBestHand() {
		return nameOfBestHand;
	}

	public void setNameOfBestHand(String nameOfBestHand) {
		this.nameOfBestHand = nameOfBestHand;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}
