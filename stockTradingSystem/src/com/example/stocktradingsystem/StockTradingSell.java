package com.example.stocktradingsystem;

import java.util.Date;

import com.example.stocktradingsystem.controller.DatabaseCommunicate;
import com.example.stocktradingsystem.controller.PortfolioItem;
import com.example.stocktradingsystem.controller.StockInfo;
import com.example.stocktradingsystem.controller.StockInfoFetcher;
import com.example.stocktradingsystem.controller.TradingRecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StockTradingSell extends Activity implements OnFocusChangeListener, OnClickListener, android.content.DialogInterface.OnClickListener {
	private AutoCompleteTextView atxSellStockStockCode;
	private TextView txtSellStockSelectedStockName;
	private TextView txtSellStockSelectedStockLastPos;
	private TextView txtSellStockSelectedStockHighLow;
	private EditText edtSellingPrice;
	private EditText edtSellingLot;
	private TextView txtMoneyAfterSoldValue;
	private Button btnSellBack;
	private Button btnSellCheckout;
	private AlertDialog dlgSellConfirm;
	private AlertDialog dlgSellingSuccess;

	private StockInfo selectingStock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_trading_sell);

		// set views
		atxSellStockStockCode = (AutoCompleteTextView) findViewById(R.id.atxSellStockStockCode);
		atxSellStockStockCode.setOnFocusChangeListener(this);
		
		txtSellStockSelectedStockName = (TextView) findViewById(R.id.txtSellStockSelectedStockName);
		txtSellStockSelectedStockLastPos = (TextView) findViewById(R.id.txtSellStockSelectedStockLastPos);
		txtSellStockSelectedStockHighLow = (TextView) findViewById(R.id.txtSellStockSelectedStockHighLow);
		edtSellingPrice = (EditText) findViewById(R.id.edtSellingPrice);
		edtSellingPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (selectingStock != null)
					ShowGainResultFromPriceAndLotSize();
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		edtSellingLot = (EditText) findViewById(R.id.edtSellingLot);
		edtSellingLot.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (selectingStock != null)
					ShowGainResultFromPriceAndLotSize();
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		txtMoneyAfterSoldValue = (TextView) findViewById(R.id.txtMoneyAfterSoldValue);
		btnSellBack = (Button) findViewById(R.id.btnSellBack);
		btnSellBack.setOnClickListener(this);
		btnSellCheckout = (Button) findViewById(R.id.btnSellCheckout);
		btnSellCheckout.setOnClickListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.atxSellStockStockCode) {
			StockInfoFetcher sif = new StockInfoFetcher() {
				@Override
				protected void onComplete(StockInfo result) {
					selectingStock = result;
					DisplayStockResultOnUI();
					ShowGainResultFromPriceAndLotSize();
				}
			};
			sif.FindFromId(atxSellStockStockCode.getText().toString());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSellBack:
			this.finish();
			break;
		case R.id.btnSellCheckout:
			if (selectingStock == null) {
				Toast.makeText(this, "No current stock info. Rejected.", Toast.LENGTH_SHORT);
				break;
			}

			dlgSellConfirm = new AlertDialog.Builder(this).create();
			dlgSellConfirm.setCancelable(false); // This blocks the 'BACK'
													// button
			String msg = "Are you sure to sell:\n";
			msg += "Stock: " + selectingStock.getSymbol() + ".HK (" + selectingStock.getEnglish() + ")\n";
			msg += "Price: $" + this.edtSellingPrice.getText().toString() + "\n";
			msg += "Selling Lot Amount: " + this.edtSellingLot.getText().toString() + "\n";
			msg += "Gaining money: $"
					+ String.format("%.1f", Double.parseDouble(this.edtSellingPrice.getText().toString()) * selectingStock.getLot() * Integer.parseInt(this.edtSellingLot.getText().toString()))
					+ "\n";
			msg += "\nConfirm?";
			dlgSellConfirm.setMessage(msg);
			dlgSellConfirm.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", this);
			dlgSellConfirm.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
			dlgSellConfirm.show();
			break;
		}
	}

	private void DisplayStockResultOnUI() {
		if (selectingStock == null) {
			String errText = "(error)";

			txtSellStockSelectedStockName.setText(errText);
			txtSellStockSelectedStockLastPos.setText(errText);
			txtSellStockSelectedStockHighLow.setText(errText);
			txtMoneyAfterSoldValue.setText(errText);
		} else {
			txtSellStockSelectedStockName.setText(selectingStock.getEnglish());
			txtSellStockSelectedStockLastPos.setText(String.format("%.3f", selectingStock.getPrice()));
			txtSellStockSelectedStockHighLow.setText(String.format("%.3f / %.3f", selectingStock.getHigh(), selectingStock.getHigh()));
		}
	}

	private void ShowGainResultFromPriceAndLotSize() {
		if (edtSellingPrice.getText().toString().length() == 0 || edtSellingLot.getText().toString().length() == 0) {
			txtMoneyAfterSoldValue.setText("(none)");
		} else if (selectingStock != null) {
			double price = Double.parseDouble(this.edtSellingPrice.getText().toString());
			int sellingLot = Integer.parseInt(edtSellingLot.getText().toString());
			double lotSize = selectingStock.getLot();

			txtMoneyAfterSoldValue.setText("$" + String.format("%.3f", sellingLot * price * lotSize));
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (dialog == dlgSellConfirm) {
			if (which == DialogInterface.BUTTON_NEGATIVE) {
				dialog.dismiss();
			} else if (which == DialogInterface.BUTTON_POSITIVE) {
				Date momentOfTrading = new Date();
				int stockCode = Integer.parseInt(selectingStock.getSymbol());
				String stockNameAtTheMoment = selectingStock.getEnglish();
				double tradeAtPrice = Double.parseDouble(this.edtSellingPrice.getText().toString());
				int lotSize = selectingStock.getLot();
				int tradingLotAmount = Integer.parseInt(this.edtSellingLot.getText().toString());
				boolean isBuying = true;
				TradingRecord tr = new TradingRecord(momentOfTrading, stockCode, stockNameAtTheMoment, tradeAtPrice, tradingLotAmount, isBuying);
				PortfolioItem pi = new PortfolioItem(stockCode, stockNameAtTheMoment, lotSize, tradingLotAmount);

				SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(getBaseContext());
				DatabaseCommunicate.addNewTradingRecord(db, tr);
				DatabaseCommunicate.addOrUpdatePortfolioItem(db, pi, false);

				dialog.dismiss();

				// showing dialog telling bought ok
				dlgSellingSuccess = new AlertDialog.Builder(this).create();
				dlgSellingSuccess.setCancelable(false); // This blocks the
														// 'BACK' button
				dlgSellingSuccess.setMessage("Selling stock success.");
				dlgSellingSuccess.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", this);
				dlgSellingSuccess.show();
			}
		} else if (dialog == dlgSellingSuccess) {
			this.finish();
			dialog.dismiss();
		}
	}
}
