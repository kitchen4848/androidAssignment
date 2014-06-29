package com.example.stocktradingsystem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StockTrading extends Activity implements OnClickListener {
	
	Button btnTradingBuy;
	Button btnTradingSell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set title of c activity
		getActionBar().setTitle("Stock Trading");
		
		// set views
		btnTradingBuy = (Button)findViewById(R.id.btnTradingBuy);
		btnTradingBuy.getBackground().setColorFilter(new LightingColorFilter(0xff6666ff, 0xff9999ff));
		btnTradingBuy.setOnClickListener(this);
		
		btnTradingSell = (Button)findViewById(R.id.btnTradingSell);
		btnTradingSell.getBackground().setColorFilter(new LightingColorFilter(0x66ff66ff, 0x99ff99ff));
		btnTradingSell.setOnClickListener(this);
		
		setContentView(R.layout.activity_stock_trading);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTradingBuy:
			Intent ittStockTradingBuy = new Intent(StockTrading.this, StockTradingBuy.class);
			startActivity(ittStockTradingBuy);
			break;
		case R.id.btnTradingSell:
			Intent ittStockTradingSell = new Intent(StockTrading.this, StockTradingSell.class);
			startActivity(ittStockTradingSell);
			break;
		}
	}
}
