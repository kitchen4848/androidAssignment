package com.example.stocktradingsystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.stocktradingsystem.controller.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StockRecording extends Activity {

	private TextView txtValueCount;
	private ListView lsvTradingRecordMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_recording);

		// add back button?
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// set views
		txtValueCount = (TextView) findViewById(R.id.txtValueCount);
		lsvTradingRecordMain = (ListView) findViewById(R.id.lsvTradingRecordMain);
		
		new AsyncTask<Object, Integer, ArrayList<HashMap<String, String>>>() {
			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Object... params) {
				return getData();
			}
			
			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				ShowingDataOnUI(result);
			}
		}.execute();
	}

	private ArrayList<HashMap<String, String>> getData() {
		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		TradingRecordCollection trc = new TradingRecordCollection();

		try {
			SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(this);
			if (DatabaseCommunicate.isTradingRecordExist(db))
				trc = DatabaseCommunicate.getAllTradingRecords(db);
			db.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
			Toast.makeText(this, "Error fetching trading records.", Toast.LENGTH_LONG).show();
			return ret;
		}
		
		java.util.Collections.reverse(trc);
		
		for (TradingRecord r : trc) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("title", (r.isBuying() ? "Buying" : "Selling") + " " + String.format("%05d", r.getStockCode()) + ".HK (" + r.getStockNameAtTheMoment() + ")");
			item.put("desc", "at $" + r.getTradeAtPrice() + " with " + r.getTradingLotAmount() + " lot(s), at " + r.getMomentOfTrading() + ".");
			ret.add(item);
		}

		return ret;
	}
	
	private void ShowingDataOnUI(ArrayList<HashMap<String, String>> result) {
		lsvTradingRecordMain.setAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_list_item_2, new String[] { "title", "desc" }, new int[] { android.R.id.text1,
			android.R.id.text2 }));
		txtValueCount.setText(" " + result.size());
	}

	// https://developer.android.com/training/implementing-navigation/ancestral.html
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
