package com.example.stocktradingsystem.controller;

public class PortfolioItem {
	private final int stockCode;
	private final String stockName;
	private final int lotSize;
	private final int quantityOnHand;
	
	public int getStockCode() {
		return stockCode;
	}
	public String getStockName() {
		return stockName;
	}
	public int getLotSize() {
		return lotSize;
	}
	public int getQuantityOnHand() {
		return quantityOnHand;
	}
	
	public PortfolioItem(int stockCode, String stockName, int lotSize, int quantityOnHand) {
		this.stockCode = stockCode;
		this.stockName = stockName;
		this.lotSize = lotSize;
		this.quantityOnHand = quantityOnHand;
	}
}
