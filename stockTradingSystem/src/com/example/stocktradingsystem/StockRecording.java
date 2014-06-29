package com.example.stocktradingsystem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StockRecording extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_recording);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_recording, menu);
		return true;
	}

}
