package com.example.stocktradingsystem.controller;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StockSystemDatabaseCommunicator {
	public static SQLiteDatabase getOpeningDatabaseObject(Context context) {
		return context.openOrCreateDatabase("stockSystemDB", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

	public static boolean isTableExist(SQLiteDatabase database, String tableName) {
		Cursor cursor = database.rawQuery("SELECT 1 FROM sqlite_master WHERE type = ? AND name = ?", new String[] { "table", "TradingRecord" });
		return cursor.moveToFirst();
	}
	
	public static void createTradingRecordStore(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE `TradingRecord` (" + 
		                 "`momentOfTrading` INTEGER NOT NULL, " + 
		                 "`stockCode` INTEGER NOT NULL, " + 
		                 "`stockNameAtTheMoment` TEXT NOT NULL, " + 
		                 "`tradeAtPrice` REAL NOT NULL, " + 
		                 "`tradingLotAmount` INTEGER NOT NULL, " + 
		                 "`isBuying` INTEGER NOT NULL, " + 
		                 "PRIMARY KEY(momentOfTrading,stockCode)" + 
		                 ");");
	}
	
	public static TradingRecordCollection getAllTradingRecords(SQLiteDatabase database) {
		TradingRecordCollection ret = new TradingRecordCollection();
		
		Cursor cursor = database.rawQuery("SELECT momentOfTrading, stockCode, stockNameAtTheMoment, tradeAtPrice, tradingLotAmount, isBuying FROM TradingRecord", null);
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
		
		return ret;
	}
}
