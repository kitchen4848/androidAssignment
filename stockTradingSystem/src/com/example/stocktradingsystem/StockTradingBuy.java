package com.example.stocktradingsystem;

import java.util.Date;

import com.example.stocktradingsystem.controller.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StockTradingBuy extends Activity implements OnClickListener, OnFocusChangeListener, android.content.DialogInterface.OnClickListener {

	EditText edtStockCode;
	TextView txtSelectedStockName;
	TextView txtSelectedStockLastPos;
	TextView txtSelectedStockHighLow;
	TextView txtSelectedStockBoardLotSize;
	EditText edtBuyingLot;
	TextView txtRequiredFeeToBuyValue;
	Button btnBuyBack;
	Button btnBuyCheckout;
	AlertDialog dlgBuyConfirm;

	StockInfo selectingStock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_trading_buy);

		// set views
		edtStockCode = (EditText) findViewById(R.id.edtStockCode);
		edtStockCode.setOnFocusChangeListener(this);
		txtSelectedStockName = (TextView) findViewById(R.id.txtSelectedStockName);
		txtSelectedStockLastPos = (TextView) findViewById(R.id.txtSelectedStockLastPos);
		txtSelectedStockHighLow = (TextView) findViewById(R.id.txtSelectedStockHighLow);
		txtSelectedStockBoardLotSize = (TextView) findViewById(R.id.txtSelectedStockBoardLotSize);
		edtBuyingLot = (EditText) findViewById(R.id.edtBuyingLot);
		edtBuyingLot.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (selectingStock != null)
					ShowPayResultFromPriceAndLotSize();
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		txtRequiredFeeToBuyValue = (TextView) findViewById(R.id.txtRequiredFeeToBuyValue);
		btnBuyBack = (Button) findViewById(R.id.btnBuyBack);
		btnBuyBack.setOnClickListener(this);
		btnBuyCheckout = (Button) findViewById(R.id.btnBuyCheckout);
		btnBuyCheckout.setOnClickListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.edtStockCode) {
			StockInfoFetcher sif = new StockInfoFetcher() {
				@Override
				protected void onComplete(StockInfo result) {
					selectingStock = result;
					DisplayStockResultOnUI();
					ShowPayResultFromPriceAndLotSize();
				}
			};
			sif.FindFromId(edtStockCode.getText().toString());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBuyBack:
			this.finish();
			break;
		case R.id.btnBuyCheckout:
			if (selectingStock == null) {
				Toast.makeText(this, "No current stock info. Rejected.", Toast.LENGTH_SHORT);
				break;
			}

			dlgBuyConfirm = new AlertDialog.Builder(this).create();
			dlgBuyConfirm.setCancelable(false); // This blocks the 'BACK' button
			String msg = "Are you sure to buy:\n";
			msg += "Stock: " + selectingStock.getSymbol() + ".HK (" + selectingStock.getEnglish() + ")\n";
			msg += "Price: %" + String.format("%.3f", selectingStock.getPrice()) + "\n";
			msg += "Buying Lot Amount: " + this.edtBuyingLot.getText().toString() + "\n";
			msg += "Paying fee: $" + String.format("%.1f", selectingStock.getPrice() * selectingStock.getLot() * Integer.parseInt(this.edtBuyingLot.getText().toString())) + "\n";
			msg += "\nConfirm?";
			dlgBuyConfirm.setMessage(msg);
			dlgBuyConfirm.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", this);
			dlgBuyConfirm.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
			dlgBuyConfirm.show();
			break;
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
			txtSelectedStockLastPos.setText(String.format("%.3f", selectingStock.getPrice()));
			txtSelectedStockHighLow.setText(String.format("%.3f / %.3f", selectingStock.getHigh(), selectingStock.getHigh()));
			txtSelectedStockBoardLotSize.setText(String.format("%d", selectingStock.getLot()));
		}
	}

	private void ShowPayResultFromPriceAndLotSize() {
		if (edtBuyingLot.getText().toString().length() == 0) {
			txtRequiredFeeToBuyValue.setText("(none)");
		} else if (selectingStock != null) {
			int buyingLot = Integer.parseInt(edtBuyingLot.getText().toString()); 
			double price = selectingStock.getPrice();
			double lotSize = selectingStock.getLot();

			txtRequiredFeeToBuyValue.setText("$" + String.format("%.3f", buyingLot * price * lotSize));
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (dialog == dlgBuyConfirm) {
			if (which == DialogInterface.BUTTON_NEGATIVE) {
				dialog.dismiss();
			} else if (which == DialogInterface.BUTTON_POSITIVE) {
				Date momentOfTrading = new Date();
				int stockCode = Integer.parseInt(selectingStock.getSymbol());
				String stockNameAtTheMoment = selectingStock.getEnglish();
				double tradeAtPrice = selectingStock.getPrice();
				int tradingLotAmount = Integer.parseInt(this.edtBuyingLot.getText().toString());
				boolean isBuying = true;
				TradingRecord tr = new TradingRecord(momentOfTrading, stockCode, stockNameAtTheMoment, tradeAtPrice, tradingLotAmount, isBuying);

				SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(getBaseContext());
				DatabaseCommunicate.addNewTradingRecord(db, tr);

				dialog.dismiss();
				
			}
		}
	}
}
