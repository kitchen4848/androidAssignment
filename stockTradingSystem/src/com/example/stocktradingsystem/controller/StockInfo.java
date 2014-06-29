package com.example.stocktradingsystem.controller;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class StockInfo {
	private String _mode;
	private String symbol;
	private String chinese;
	private String english;
	private boolean sspn;
	private double price;
	private double change;
	private double pct_change;
	private double pexit;
	private double open;
	private double high;
	private double low;
	private String bid;
	private String ask;
	private double year_high;
	private double year_low;
	private long volume;
	private long turnover;
	private double pe;
	private String market_capital;
	private double month_high;
	private double month_low;
	private int lot;
	private double dps;
	private double eps;
	private double rd10;
	private double rd14;
	private double rd20;
	private double md10;
	private double md20;
	private double md50;
	private Date date;

	public String get_mode() {
		return _mode;
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

	public boolean isSspn() {
		return sspn;
	}

	public double getPrice() {
		return price;
	}

	public double getChange() {
		return change;
	}

	public double getPct_change() {
		return pct_change;
	}

	public double getPexit() {
		return pexit;
	}

	public double getOpen() {
		return open;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public String getBid() {
		return bid;
	}

	public String getAsk() {
		return ask;
	}

	public double getYear_high() {
		return year_high;
	}

	public double getYear_low() {
		return year_low;
	}

	public long getVolume() {
		return volume;
	}

	public long getTurnover() {
		return turnover;
	}

	public double getPe() {
		return pe;
	}

	public String getMarket_capital() {
		return market_capital;
	}

	public double getMonth_high() {
		return month_high;
	}

	public double getMonth_low() {
		return month_low;
	}

	public int getLot() {
		return lot;
	}

	public double getDps() {
		return dps;
	}

	public double getEps() {
		return eps;
	}

	public double getRd10() {
		return rd10;
	}

	public double getRd14() {
		return rd14;
	}

	public double getRd20() {
		return rd20;
	}

	public double getMd10() {
		return md10;
	}

	public double getMd20() {
		return md20;
	}

	public double getMd50() {
		return md50;
	}

	public Date getDate() {
		return date;
	}

	public static StockInfo ParseStockInfoFromJson(JSONObject jsonObject) throws JSONException {
		StockInfo ret = new StockInfo();

		JSONObject quote = jsonObject.getJSONObject("quote");
		ret._mode = quote.getString("@mode");
		JSONObject stock = quote.getJSONObject("stock");
		ret.symbol = stock.getString("symbol");
		JSONObject name = stock.getJSONObject("name");
		ret.chinese = name.getString("chinese");
		ret.english = name.getString("english");

		if (ret.english == "null")
			return null;

		ret.sspn = stock.getString("sspn") == "Y" ? true : false;
		ret.price = Double.parseDouble(stock.getString("price"));
		ret.change = Double.parseDouble(stock.getString("change"));
		String pct_chg_str = stock.getString("pct_change");
		ret.pct_change = Double.parseDouble(pct_chg_str.substring(0, pct_chg_str.length() - 1));
		ret.pexit = Double.parseDouble(stock.getString("pexit"));
		ret.open = Double.parseDouble(stock.getString("open"));
		ret.high = Double.parseDouble(stock.getString("high"));
		ret.low = Double.parseDouble(stock.getString("low"));
		ret.bid = stock.getString("bid");
		ret.ask = stock.getString("ask");
		ret.year_high = Double.parseDouble(stock.getString("year_high"));
		ret.year_low = Double.parseDouble(stock.getString("year_low"));
		ret.volume = Long.parseLong(stock.getString("volume"));
		ret.turnover = Long.parseLong(stock.getString("turnover"));
		ret.pe = Double.parseDouble(stock.getString("pe"));
		ret.market_capital = stock.getString("market_capital");
		ret.month_high = Double.parseDouble(stock.getString("month_high"));
		ret.month_low = Double.parseDouble(stock.getString("month_low"));
		ret.lot = (int) Double.parseDouble(stock.getString("lot"));
		ret.dps = Double.parseDouble(stock.getString("dps"));
		ret.eps = Double.parseDouble(stock.getString("eps"));
		JSONObject rsi = stock.getJSONObject("rsi");
		ret.rd10 = Double.parseDouble(rsi.getString("d10"));
		ret.rd14 = Double.parseDouble(rsi.getString("d14"));
		ret.rd20 = Double.parseDouble(rsi.getString("d20"));
		JSONObject ma = stock.getJSONObject("ma");
		ret.md10 = Double.parseDouble(ma.getString("d10"));
		ret.md20 = Double.parseDouble(ma.getString("d20"));
		ret.md50 = Double.parseDouble(ma.getString("d50"));
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm", java.util.Locale.US);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0800"));
		try {
			ret.date = formatter.parse(stock.getString("date"));
		} catch (ParseException e) {
			e.printStackTrace();
			ret.date = new Date();
		}

		return ret;
	}
}
