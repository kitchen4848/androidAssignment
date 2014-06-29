package com.example.stocktradingsystem.controller;

import org.json.JSONException;
import org.json.JSONObject;

public class StockInfo {
	private String mode;
	private String symbol;
	private String chinese;
	private String english;
	private String sspn;
	private String price;
	private String change;
	private String pct_change;
	private String pexit;
	private String open;
	private String high;
	private String low;
	private String bid;
	private String ask;
	private String year_high;
	private String year_low;
	private String volume;
	private String turnover;
	private String pe;
	private String market_capital;
	private String month_high;
	private String month_low;
	private String lot;
	private String dps;
	private String eps;
	private String rd10;
	private String rd14;
	private String rd20;
	private String md10;
	private String md20;
	private String md50;
	private String date;
	
	public StockInfo() {
	}
	
	public String getMode() {
		return mode;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getChinese() {
		return chinese;
	}

	public String getEnglish() {
		return english;
	}

	public String getSspn() {
		return sspn;
	}

	public String getPrice() {
		return price;
	}

	public String getChange() {
		return change;
	}

	public String getPct_change() {
		return pct_change;
	}

	public String getPexit() {
		return pexit;
	}

	public String getOpen() {
		return open;
	}

	public String getHigh() {
		return high;
	}

	public String getLow() {
		return low;
	}

	public String getBid() {
		return bid;
	}

	public String getAsk() {
		return ask;
	}

	public String getYear_high() {
		return year_high;
	}

	public String getYear_low() {
		return year_low;
	}

	public String getVolume() {
		return volume;
	}

	public String getTurnover() {
		return turnover;
	}

	public String getPe() {
		return pe;
	}

	public String getMarket_capital() {
		return market_capital;
	}

	public String getMonth_high() {
		return month_high;
	}

	public String getMonth_low() {
		return month_low;
	}

	public String getLot() {
		return lot;
	}

	public String getDps() {
		return dps;
	}

	public String getEps() {
		return eps;
	}

	public String getRd10() {
		return rd10;
	}

	public String getRd14() {
		return rd14;
	}

	public String getRd20() {
		return rd20;
	}

	public String getMd10() {
		return md10;
	}

	public String getMd20() {
		return md20;
	}

	public String getMd50() {
		return md50;
	}

	public String getDate() {
		return date;
	}

	public static StockInfo ParseStockInfoFromJson(JSONObject jsonObject) throws JSONException {
		StockInfo ret = new StockInfo();
		
		JSONObject quote = jsonObject.getJSONObject("quote");
		ret.mode = quote.getString("@mode");
		JSONObject stock = quote.getJSONObject("stock");
		ret.symbol = stock.getString("symbol");
		JSONObject name = stock.getJSONObject("name");
		ret.chinese = name.getString("chinese");
		ret.english = name.getString("english");
		ret.sspn = stock.getString("sspn");
		ret.price = stock.getString("price");
		ret.change = stock.getString("change");
		ret.pct_change = stock.getString("pct_change");
		ret.pexit = stock.getString("pexit");
		ret.open = stock.getString("open");
		ret.high = stock.getString("high");
		ret.low = stock.getString("low");
		ret.bid = stock.getString("bid");
		ret.ask = stock.getString("ask");
		ret.year_high = stock.getString("year_high");
		ret.year_low = stock.getString("year_low");
		ret.volume = stock.getString("volume");
		ret.turnover = stock.getString("turnover");
		ret.pe = stock.getString("pe");
		ret.market_capital = stock.getString("market_capital");
		ret.month_high = stock.getString("month_high");
		ret.month_low = stock.getString("month_low");
		ret.lot = stock.getString("lot");
		ret.dps = stock.getString("dps");
		ret.eps = stock.getString("eps");
		JSONObject rsi = stock.getJSONObject("rsi");
		ret.rd10 = rsi.getString("d10");
		ret.rd14 = rsi.getString("d14");
		ret.rd20 = rsi.getString("d20");
		JSONObject ma = stock.getJSONObject("ma");
		ret.md10 = ma.getString("d10");
		ret.md20 = ma.getString("d20");
		ret.md50 = ma.getString("d50");
		ret.date = stock.getString("date");
		
		return ret;
	}
}
