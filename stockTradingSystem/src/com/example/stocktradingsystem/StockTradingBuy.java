package com.example.stocktradingsystem;

import java.io.IOException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stocktradingsystem.controller.InternetFetcher;
import com.example.stocktradingsystem.controller.StockInfo;
import com.example.stocktradingsystem.controller.StockInfoFetcher;

import android.R.integer;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StockTradingBuy extends Activity implements OnClickListener, OnFocusChangeListener{

	EditText edtStockCode;
	TextView txtSelectedStockName;
	TextView txtSelectedStockLastPos;
	TextView txtSelectedStockHighLow;
	TextView txtSelectedStockBoardLotSize;
	EditText edtBuyingLot;
	TextView txtRequiredFeeToBuyValue;
	Button btnBuyBack;
	Button btnBuyCheckout;
	
	StockInfo selectingStock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set title of c activity
		getActionBar().setTitle("Buy Stock");

		// set views
		edtStockCode = (EditText) findViewById(R.id.edtStockCode);
		edtStockCode.setOnClickListener(this);
		txtSelectedStockName = (TextView) findViewById(R.id.txtSelectedStockName);
		txtSelectedStockLastPos = (TextView) findViewById(R.id.txtSelectedStockLastPos);
		txtSelectedStockHighLow = (TextView) findViewById(R.id.txtSelectedStockHighLow);
		txtSelectedStockBoardLotSize = (TextView) findViewById(R.id.txtSelectedStockBoardLotSize);
		edtBuyingLot = (EditText) findViewById(R.id.edtBuyingLot);
		edtBuyingLot.setOnFocusChangeListener(this);
		txtRequiredFeeToBuyValue = (TextView) findViewById(R.id.txtRequiredFeeToBuyValue);
		btnBuyBack = (Button) findViewById(R.id.btnBuyBack);
		btnBuyBack.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ShowPayResultFromPriceAndLotSize();
			}

			@Override
			public void afterTextChanged(Editable s) { }
		});
		btnBuyCheckout = (Button) findViewById(R.id.btnBuyCheckout);
		btnBuyCheckout.setOnClickListener(this);

		setContentView(R.layout.activity_stock_trading_buy);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v == this.edtStockCode) {
			StockInfoFetcher sif = new StockInfoFetcher() {
				@Override
				protected void onComplete(StockInfo result) {
					selectingStock = result;
					DisplayStockResultOnUI();
				}
			};
			sif.FindFromId(edtStockCode.getText().toString());
		}
	}

	@Override
	public void onClick(View v) {
		if (v == this.btnBuyBack) {
			this.finish();
		} else if (v == this.btnBuyCheckout) {
			
		}
	}
	
	private void DisplayStockResultOnUI() {
		if (selectingStock == null) {
			String errText = "(error)";
			
			txtSelectedStockName.setText(errText);
			txtSelectedStockLastPos.setText(errText);
			txtSelectedStockHighLow.setText(errText);
			txtSelectedStockBoardLotSize.setText(errText);
			txtRequiredFeeToBuyValue.setText(errText);
		} else {
			txtSelectedStockName.setText(selectingStock.getEnglish());
			txtSelectedStockLastPos.setText(selectingStock.getPrice());
			txtSelectedStockHighLow.setText(selectingStock.getHigh() + " / " + selectingStock.getLow());
			txtSelectedStockBoardLotSize.setText(selectingStock.getLot());
		}
	}
	
	private void ShowPayResultFromPriceAndLotSize() {
		// TODO: should correct value type in StockInfo.java 
		double price = Double.parseDouble(selectingStock.getPrice());
		double lotSize = Double.parseDouble(selectingStock.getLot());
		
		txtRequiredFeeToBuyValue.setText("$" + String.format("$.3f", price * lotSize));
	}
}
