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
		setContentView(R.layout.activity_stock_trading);
		
		// set views
		btnTradingBuy = (Button)findViewById(R.id.btnTradingBuy);
		btnTradingBuy.getBackground().setColorFilter(new LightingColorFilter(0xff0000ff, 0xff0000ff));
		btnTradingBuy.setOnClickListener(this);
		
		btnTradingSell = (Button)findViewById(R.id.btnTradingSell);
		btnTradingSell.getBackground().setColorFilter(new LightingColorFilter(0x00ff00ff, 0x00ff00ff));
		btnTradingSell.setOnClickListener(this);
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
