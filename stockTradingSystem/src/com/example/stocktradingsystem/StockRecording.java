package com.example.stocktradingsystem;

import java.util.ArrayList;
import java.util.List;

import com.example.stocktradingsystem.controller.*;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StockRecording extends Activity {

	private ListView lsvMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set title of c activity
		getActionBar().setTitle("Stock Trading Record");

		// add back button?
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// set views
		lsvMain = new ListView(this);
		lsvMain.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));

		setContentView(R.layout.activity_stock_recording);
	}

	private List<String> getData() {
		List<String> ret = new ArrayList<String>();
		TradingRecordCollection trc = new TradingRecordCollection();
		
		try {
			SQLiteDatabase db = StockSystemDatabaseCommunicator.getOpeningDatabaseObject(this);
			trc = StockSystemDatabaseCommunicator.getAllTradingRecords(db);
			db.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
			Toast.makeText(this, "Error fetching trading records.", Toast.LENGTH_LONG).show();
			return ret;
		}

		for (TradingRecord r : trc) {
			String str = "";
			str += r.getMomentOfTrading() + " ";
			str += (r.isBuying() ? "Buying" : "Selling") + "\n";
			str += r.getStockCode() + " (" + r.getStockNameAtTheMoment() + ") ";
			str += "at " + r.getTradeAtPrice() + " with " + r.getTradingLotAmount() + " lot(s).";
			
			ret.add(str);
		}
		return ret;
	}

	// https://developer.android.com/training/implementing-navigation/ancestral.html
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
