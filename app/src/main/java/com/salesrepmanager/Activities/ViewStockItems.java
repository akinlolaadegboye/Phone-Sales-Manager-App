package com.salesrepmanager.Activities;

public class ViewStockItems {
	String model;
	String price;
	String numberAvailable;
	byte [] image;

	public ViewStockItems(String model, String price,String numberAvailable, byte[] image) {
		this.model = model;
		this.price = price;
		this.numberAvailable = numberAvailable;
		this.image = image;
	}
	public String getModel() {
		return model;
	}
	public String getprice() {
		return price;
	}
	public String getNumberAvailable() {
		return numberAvailable;


	}

	public byte[] getImage() {
		return image;
	}

}
