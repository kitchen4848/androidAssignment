package com.example.stocktradingsystem;

import com.example.stocktradingsystem.controller.*;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.TextView;
import android.widget.Toast;

public class Portfolio extends Activity {
	SQLiteDatabase db;
	TextView tvData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portfolio);
		
		try {
			SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(this);
			PortfolioItemCollection pic = DatabaseCommunicate.getAllPortfolioItems(db);
			
			String dataStr = String.format("%6s %-15s %7s %8s \n", "sCode", "stockName", "lotSize", "quantity");;
			for (PortfolioItem pi : pic) {
				String.format("%6s %-15s %7s %8s \n", pi.getStockCode(), pi.getStockName(), pi.getLotSize(), pi.getQuantityOnHand());
			}
			tvData.setText(dataStr);
		} catch (SQLiteException e) {
			Toast.makeText(this, e.getMessage(), 1).show();
		}
	}

}
