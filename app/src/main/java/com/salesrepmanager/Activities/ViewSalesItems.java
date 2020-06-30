package com.salesrepmanager.Activities;

public class ViewSalesItems {
	String model;
	String price,time,date;
	String numberAvailable;
	byte[] image;

	public ViewSalesItems(String model, String price, String time,String date, byte[] image) {
		this.model = model;
		this.price = price;
		this.time = time;
		this.date = date;
		this.image = image;
	}
	public String getModel() {
		return model;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}
	public String getprice() {
		return price;
	}
	public byte[] getImage() {
		return image;
	}

}
