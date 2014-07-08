package com.example.stocktradingsystem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class StockMeun extends Activity implements OnClickListener {

	Button btnPortfolio;
	Button btnRealTimeStock;
	Button btnTrading;
	Button btnRecord;
	Button btnPieChart;
	Button btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_meun);

		btnPortfolio = (Button) findViewById(R.id.btnPortfolio);
		btnPortfolio.setOnClickListener(this);
		btnRealTimeStock = (Button) findViewById(R.id.btnRealTimeStock);
		btnRealTimeStock.setOnClickListener(this);
		btnTrading = (Button) findViewById(R.id.btnTrading);
		btnTrading.setOnClickListener(this);
		btnRecord = (Button) findViewById(R.id.btnRecord);
		btnRecord.setOnClickListener(this);
		btnPieChart = (Button) findViewById(R.id.btnPieChart);
		btnPieChart.setOnClickListener(this);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnPortfolio:
			Intent ittPortfolio = new Intent(StockMeun.this, Portfolio.class);
			startActivity(ittPortfolio);
			break;
		case R.id.btnRealTimeStock:
			Intent ittRealTimeStock = new Intent(StockMeun.this, RealTimeStock.class);
			startActivity(ittRealTimeStock);
			break;
		case R.id.btnTrading:
			Intent ittStockTrading = new Intent(StockMeun.this, StockTrading.class);
			startActivity(ittStockTrading);
			break;
		case R.id.btnRecord:
			Intent ittStockRecording = new Intent(StockMeun.this, StockRecording.class);
			startActivity(ittStockRecording);
			break;
		case R.id.btnPieChart:
			Intent ittPicChart = new Intent(StockMeun.this, PieChart.class);
			startActivity(ittPicChart);
			break;
		case R.id.btnLogout:
			this.finish();
			break;
		}
	}

}
