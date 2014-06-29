package com.example.stocktradingsystem.controller;

import java.util.Date;

public class TradingRecord {
	private final Date momentOfTrading;
	private final int stockCode;
	private final String stockNameAtTheMoment;
	private final double tradeAtPrice;
	private final int tradingLotAmount;
	private final boolean isBuying;
	
	public Date getMomentOfTrading() {
		return momentOfTrading;
	}
	public int getStockCode() {
		return stockCode;
	}
	public String getStockNameAtTheMoment() {
		return stockNameAtTheMoment;
	}
	public double getTradeAtPrice() {
		return tradeAtPrice;
	}
	public int getTradingLotAmount() {
		return tradingLotAmount;
	}
	public boolean isBuying() {
		return isBuying;
	}
	
	public TradingRecord(Date momentOfTrading, int stockCode, String stockNameAtTheMoment, double tradeAtPrice, int tradingLotAmount, boolean isBuying) {
		this.momentOfTrading = momentOfTrading;
		this.stockCode = stockCode;
		this.stockNameAtTheMoment = stockNameAtTheMoment;
		this.tradeAtPrice = tradeAtPrice;
		this.tradingLotAmount = tradingLotAmount;
		this.isBuying = isBuying;
	}
}
