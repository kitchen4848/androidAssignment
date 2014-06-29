package com.example.stocktradingsystem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Portfolio extends Activity {
	SQLiteDatabase db;
	TextView tvData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portfolio);
		String sql;
		String columns [] = {"portfolioId", "stockCode", "stockName", "lotSize", "quantityOnHand"};
		Cursor cursor = null;
		tvData = (TextView)findViewById(R.id.tvdata);
		//String dataStr = String.format("%4s %5s %20s %5 %5 \n", "portfolioId", "stockCode","stockName", "lotSize", "quantityOnHand");
		String dataStr = String.format("%4s %6s %-15s %7s %8s \n","PID", "sCode", "stockName", "lotSize", "quantity");
		
		//create database
		try{
			//db = SQLiteDatabase.openDatabase("/data/data/stock.System/stockDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
			db = openOrCreateDatabase("stockSystemDB", MODE_PRIVATE, null);
			
		//Drop table if it exist
		sql = "Drop table if exists Portfolio;";
		db.execSQL(sql);
		
		//create table if table does not exist
		sql = "Create Table Portfolio(portfolioId int Primary key, stockCode text, stockName text, lotSize, quantityOnHand int);";
		db.execSQL(sql);
		
		//initialising database
		sql = "Insert into Portfolio(portfolioId, stockCode, stockName, lotSize, quantityOnHand) values (1001, '00001', 'CHEUNG KONG', 1000, 2000);";
		db.execSQL(sql);
		sql = "Insert into Portfolio(portfolioId, stockCode, stockName, lotSize, quantityOnHand) values (1002, '00002', 'CLP HOLDINGS', 500, 5000);";
		db.execSQL(sql);
		sql = "Insert into Portfolio(portfolioId, stockCode, stockName, lotSize, quantityOnHand) values (1003, '00003', 'HK & CHINA GAS', 1000, 3000);";
		db.execSQL(sql);
		sql = "Insert into Portfolio(portfolioId, stockCode, stockName, lotSize, quantityOnHand) values (1004, '00005', 'HSBC HOLDINGS', 400, 2000);";
		db.execSQL(sql);
		sql = "Insert into Portfolio(portfolioId, stockCode, stockName, lotSize, quantityOnHand) values (1005, '00006', 'MTR COOPOARTION', 500, 1000);";
		db.execSQL(sql);
		
		Toast.makeText(this, "Database Portfolio is created and initialised.", Toast.LENGTH_LONG).show();
		
		cursor = db.rawQuery("select * from Portfolio", null);
		while (cursor.moveToNext()) {
		        int id = cursor.getInt(cursor.getColumnIndex("portfolioId")); 
		        String code = cursor.getString(cursor.getColumnIndex("stockCode")); 
		        String name = cursor.getString(cursor.getColumnIndex("stockName"));
		        int lotsize = cursor.getInt(cursor.getColumnIndex("lotSize"));
		        int quantity = cursor.getInt(cursor.getColumnIndex("quantityOnHand"));
		        
		       //dataStr += String.format("%4s %5s %20s %5 %5 \n", id, code, name, lotsize, quantity);
		        dataStr += String.format("%4s %6s %-15s %7s %8s\n", id,code,name,lotsize,quantity);
		      }
		      tvData.setText(dataStr);
		
		db.close();
		} catch (SQLiteException e) {
			Toast.makeText(this, e.getMessage(), 1).show();
		}
	}

}
