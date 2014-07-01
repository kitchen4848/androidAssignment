package com.example.stocktradingsystem.controller;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseCommunicate {
	public static SQLiteDatabase getOpeningDatabaseObject(Context context) {
		return context.openOrCreateDatabase("stockSystemDB", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

	private static boolean isTableExist(SQLiteDatabase database, String tableName) {
		Cursor cursor = database.rawQuery("SELECT 1 FROM sqlite_master WHERE type = ? AND name = ?", new String[] { "table", tableName });
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	public static boolean isTradingRecordExist(SQLiteDatabase database) {
		return isTableExist(database, "TradingRecord");
	}

	public static void createTradingRecordStore(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE `TradingRecord` (`momentOfTrading` INTEGER NOT NULL, `stockCode` INTEGER NOT NULL, `stockNameAtTheMoment` TEXT NOT NULL, `tradeAtPrice` REAL NOT NULL, `tradingLotAmount` INTEGER NOT NULL, `isBuying` INTEGER NOT NULL, PRIMARY KEY(momentOfTrading,stockCode));");
	}

	public static void addNewTradingRecord(SQLiteDatabase database, TradingRecord tradingRecord) {
		if (!isTradingRecordExist(database))
			createTradingRecordStore(database);

		ContentValues values = new ContentValues();
		values.put("momentOfTrading", tradingRecord.getMomentOfTrading().getTime());
		values.put("stockCode", tradingRecord.getStockCode());
		values.put("stockNameAtTheMoment", tradingRecord.getStockNameAtTheMoment());
		values.put("tradeAtPrice", tradingRecord.getTradeAtPrice());
		values.put("tradingLotAmount", tradingRecord.getTradingLotAmount());
		values.put("isBuying", tradingRecord.isBuying());

		database.insert("TradingRecord", null, values);
	}

	public static TradingRecordCollection getAllTradingRecords(SQLiteDatabase database) {
		TradingRecordCollection ret = new TradingRecordCollection();

		if (!isTradingRecordExist(database))
			return ret;

		Cursor cursor = database.rawQuery("SELECT `momentOfTrading`, `stockCode`, `stockNameAtTheMoment`, `tradeAtPrice`, `tradingLotAmount`, `isBuying` FROM TradingRecord", null);
		while (cursor.moveToNext()) {
			Date momentOfTrading = new Date(cursor.getLong(cursor.getColumnIndex("momentOfTrading")) * 1000);
			int stockCode = cursor.getInt(cursor.getColumnIndex("stockCode"));
			String stockNameAtTheMoment = cursor.getString(cursor.getColumnIndex("stockNameAtTheMoment"));
			double tradeAtPrice = cursor.getDouble(cursor.getColumnIndex("tradeAtPrice"));
			int tradingLotAmount = cursor.getInt(cursor.getColumnIndex("tradingLotAmount"));
			boolean isBuying = cursor.getInt(cursor.getColumnIndex("isBuying")) > 0;

			TradingRecord newTradingRecord = new TradingRecord(momentOfTrading, stockCode, stockNameAtTheMoment, tradeAtPrice, tradingLotAmount, isBuying);
			ret.add(newTradingRecord);
		}
		
		cursor.close();

		return ret;
	}

	public static boolean isPortfolioExist(SQLiteDatabase database) {
		return isTableExist(database, "Portfolio");
	}

	public static void createPortfolio(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE `Portfolio` (`stockCode` INTEGER NOT NULL, `stockName` TEXT NOT NULL, `lotSize` INTEGER NOT NULL, `quantityOnHand` INTEGER NOT NULL, PRIMARY KEY(stockCode));");
	}

	private static void addNewPortfolioItem(SQLiteDatabase database, PortfolioItem portfolioItem) {
		ContentValues values = new ContentValues();
		values.put("stockCode", portfolioItem.getStockCode());
		values.put("stockName", portfolioItem.getStockName());
		values.put("lotSize", portfolioItem.getLotSize());
		values.put("quantityOnHand", portfolioItem.getQuantityOnHand());

		database.insert("Portfolio", null, values);
	}

	private static void updatePortfolioItem(SQLiteDatabase database, PortfolioItem portfolioItem, boolean isBuying) {
		ContentValues values = new ContentValues();
		values.put("stockCode", portfolioItem.getStockCode());
		values.put("stockName", portfolioItem.getStockName());
		values.put("lotSize", portfolioItem.getLotSize());
		values.put("quantityOnHand", portfolioItem.getQuantityOnHand());

		database.update("Portfolio", values, "stockCode=?", new String[] { portfolioItem.getStockCode() + "" });
	}

	public static void addOrUpdatePortfolioItem(SQLiteDatabase database, PortfolioItem portfolioItem, boolean isBuying) {
		if (!isPortfolioExist(database))
			createPortfolio(database);
		
		if (isPortfolioItemExist(database, portfolioItem.getStockCode()))
			updatePortfolioItem(database, portfolioItem, isBuying);
		else
			addNewPortfolioItem(database, portfolioItem);
	}

	private static boolean isPortfolioItemExist(SQLiteDatabase database, int stockCode) {
		if (!isPortfolioExist(database))
			return false;

		Cursor cursor = database.rawQuery("SELECT 1 FROM Portfolio WHERE stockCode = ?", new String[] { stockCode + "" });
		if(cursor.moveToFirst()) {
			cursor.close();
			return true;
		} else {
			return false;
		}
	}

	public static PortfolioItemCollection getAllPortfolioItems(SQLiteDatabase database) {
		PortfolioItemCollection ret = new PortfolioItemCollection();

		if (!isPortfolioExist(database))
			return ret;

		Cursor cursor = database.rawQuery("SELECT `stockCode`, `stockName`, `lotSize`, `quantityOnHand` FROM Portfolio", null);
		while (cursor.moveToNext()) {
			int stockCode = cursor.getInt(cursor.getColumnIndex("stockCode"));
			String stockName = cursor.getString(cursor.getColumnIndex("stockName"));
			int lotSize = cursor.getInt(cursor.getColumnIndex("lotSize"));
			int quantityOnHand = cursor.getInt(cursor.getColumnIndex("quantityOnHand"));

			PortfolioItem newPortfolioItem = new PortfolioItem(stockCode, stockName, lotSize, quantityOnHand);
			ret.add(newPortfolioItem);
		}
		
		cursor.close();

		return ret;
	}
	
	public static PortfolioItem getportfolioItemByStockCode(SQLiteDatabase database, int stockCode) {
		if (!isPortfolioExist(database))
			return null;

		Cursor cursor = database.rawQuery("SELECT `stockCode`, `stockName`, `lotSize`, `quantityOnHand` FROM Portfolio WHERE stockCode = ?", new String[] { String.format("%d", stockCode) });
		if (!cursor.moveToNext())
			return null;
		
		int _stockCode = cursor.getInt(cursor.getColumnIndex("stockCode"));
		String stockName = cursor.getString(cursor.getColumnIndex("stockName"));
		int lotSize = cursor.getInt(cursor.getColumnIndex("lotSize"));
		int quantityOnHand = cursor.getInt(cursor.getColumnIndex("quantityOnHand"));
		
		cursor.close();

		return new PortfolioItem(_stockCode, stockName, lotSize, quantityOnHand);
	}
}
