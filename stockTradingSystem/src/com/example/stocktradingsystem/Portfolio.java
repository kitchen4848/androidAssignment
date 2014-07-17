package com.example.stocktradingsystem;

import com.example.stocktradingsystem.controller.*;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Portfolio extends Activity {
	TextView txtPortfolioData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portfolio);

		// add back button?
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		txtPortfolioData = (TextView) findViewById(R.id.txtPortfolioData);
		
		String dataStr = String.format("%6s %-15s %7s %8s \n", "sCode", "stockName", "lotSize", "quantity");
		
		try {
			//connect the database
			SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(this);
			PortfolioItemCollection pic = DatabaseCommunicate.getAllPortfolioItems(db);
			db.close();
			
			for (PortfolioItem pi : pic) {
				dataStr += String.format("\n%6s %-15s %7s %8s \n", pi.getStockCode(), pi.getStockName(), pi.getLotSize(), pi.getQuantityOnHand());
			}
		} catch (SQLiteException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		//display the record in database
		txtPortfolioData.setText(dataStr);
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
