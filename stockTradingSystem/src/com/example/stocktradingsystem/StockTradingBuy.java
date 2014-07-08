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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StockTradingBuy extends Activity implements OnClickListener, OnFocusChangeListener, android.content.DialogInterface.OnClickListener {

	private EditText edtBuyStockStockCode;
	private TextView txtBuyStockSelectedStockName;
	private TextView txtBuyStockSelectedStockLastPos;
	private TextView txtBuyStockSelectedStockHighLow;
	private TextView txtBuyStockSelectedStockBoardLotSize;
	private EditText edtBuyingPrice;
	private EditText edtBuyingLot;
	private TextView txtRequiredFeeToBuyValue;
	private Button btnBuyBack;
	private Button btnBuyCheckout;
	private AlertDialog dlgBuyConfirm;
	private AlertDialog dlgBuyingSuccess;

	private StockInfo selectingStock = null;
	private PortfolioItem selectingPortfolioItem = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_trading_buy);

		// add back button?
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// set views
		edtBuyStockStockCode = (EditText) findViewById(R.id.edtBuyStockStockCode);
		edtBuyStockStockCode.setOnFocusChangeListener(this);
		txtBuyStockSelectedStockName = (TextView) findViewById(R.id.txtBuyStockSelectedStockName);
		txtBuyStockSelectedStockLastPos = (TextView) findViewById(R.id.txtBuyStockSelectedStockLastPos);
		txtBuyStockSelectedStockHighLow = (TextView) findViewById(R.id.txtBuyStockSelectedStockHighLow);
		txtBuyStockSelectedStockBoardLotSize = (TextView) findViewById(R.id.txtBuyStockSelectedStockBoardLotSize);
		edtBuyingPrice = (EditText) findViewById(R.id.edtBuyingPrice);
		edtBuyingPrice.addTextChangedListener(new TextWatcher() {

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
		if (v.getId() == R.id.edtBuyStockStockCode) {
			StockInfoFetcher sif = new StockInfoFetcher() {
				@Override
				protected void onComplete(StockInfo result) {
					selectingStock = result;
					DisplayStockResultOnUI();
					ShowPayResultFromPriceAndLotSize();
				}
			};
			sif.FindFromId(edtBuyStockStockCode.getText().toString());
		}
	}
	
//	private void GetPortfolioItemFromSelectingStock() {
//		//AsyncTask at = new Async
//		
//		if (selectingStock != null) {
//			try	{
//				SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(this);
//				selectingPortfolioItem = DatabaseCommunicate.getportfolioItemByStockCode(db, Integer.parseInt(selectingStock.getSymbol()));
//				db.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				Toast.makeText(this, "Cannot fetch your portfolio records. Rejected.", Toast.LENGTH_SHORT).show();
//			}
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBuyBack:
			this.finish();
			break;
		case R.id.btnBuyCheckout:
			if (selectingStock == null) {
				Toast.makeText(this, "No current stock info. Rejected.", Toast.LENGTH_SHORT).show();
				return;
			} else if (selectingPortfolioItem == null) {
				Toast.makeText(this, "Cannot fetch your portfolio records. Rejected.", Toast.LENGTH_SHORT).show();
				return;
			} else if (edtBuyingPrice.getText().toString().length() == 0) {
				Toast.makeText(this, "What is your selling price?", Toast.LENGTH_SHORT).show();
				return;
			} else if (edtBuyingLot.getText().toString().length() == 0) {
				Toast.makeText(this, "How may lots do you want to sell?", Toast.LENGTH_SHORT).show();
				return;
			} else if (Integer.parseInt(edtBuyingLot.getText().toString()) < 1) {
				Toast.makeText(this, "\"0\" is not a valid number of lot to sell.", Toast.LENGTH_SHORT).show();
				return;
			} else if (Integer.parseInt(edtBuyingLot.getText().toString()) > selectingPortfolioItem.getQuantityOnHand()) {
				Toast.makeText(this, "You don't have that many lots to sell.", Toast.LENGTH_SHORT).show();
				return;
			}

			dlgBuyConfirm = new AlertDialog.Builder(this).create();
			dlgBuyConfirm.setCancelable(false); // This blocks the 'BACK' button
			String msg = "Are you sure to buy:\n";
			msg += "Stock: " + selectingStock.getSymbol() + ".HK (" + selectingStock.getEnglish() + ")\n";
			msg += "Price: $" + this.edtBuyingPrice.getText().toString() + "\n";
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

			txtBuyStockSelectedStockName.setText(errText);
			txtBuyStockSelectedStockLastPos.setText(errText);
			txtBuyStockSelectedStockHighLow.setText(errText);
			txtBuyStockSelectedStockBoardLotSize.setText(errText);
			txtRequiredFeeToBuyValue.setText(errText);
		} else {
			txtBuyStockSelectedStockName.setText(selectingStock.getEnglish());
			txtBuyStockSelectedStockLastPos.setText(String.format("%.3f", selectingStock.getPrice()));
			txtBuyStockSelectedStockHighLow.setText(String.format("%.3f / %.3f", selectingStock.getHigh(), selectingStock.getHigh()));
			txtBuyStockSelectedStockBoardLotSize.setText(String.format("%d", selectingStock.getLot()));
		}
	}

	private void ShowPayResultFromPriceAndLotSize() {
		if (edtBuyingPrice.getText().toString().length() == 0 || edtBuyingLot.getText().toString().length() == 0) {
			txtRequiredFeeToBuyValue.setText("(none)");
		} else if (selectingStock != null) {
			double price = Double.parseDouble(this.edtBuyingPrice.getText().toString());
			int buyingLot = Integer.parseInt(edtBuyingLot.getText().toString()); 
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
				double tradeAtPrice = Double.parseDouble(this.edtBuyingPrice.getText().toString());
				int lotSize = selectingStock.getLot();
				int tradingLotAmount = Integer.parseInt(this.edtBuyingLot.getText().toString());
				boolean isBuying = true;
				TradingRecord tr = new TradingRecord(momentOfTrading, stockCode, stockNameAtTheMoment, tradeAtPrice, tradingLotAmount, isBuying);
				int newLotOnHand = selectingPortfolioItem.getQuantityOnHand() + tradingLotAmount;
				PortfolioItem pi = new PortfolioItem(stockCode, stockNameAtTheMoment, lotSize, newLotOnHand);
				
				SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(getBaseContext());
				DatabaseCommunicate.addNewTradingRecord(db, tr);
				DatabaseCommunicate.addOrUpdatePortfolioItem(db, pi, true);
				db.close();

				dialog.dismiss();
				
				// showing dialog telling bought ok
			    dlgBuyingSuccess = new AlertDialog.Builder(this).create();  
			    dlgBuyingSuccess.setCancelable(false); // This blocks the 'BACK' button  
			    dlgBuyingSuccess.setMessage("Buying stock success.");  
			    dlgBuyingSuccess.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", this);  
			    dlgBuyingSuccess.show();  
			}
		} else if (dialog == dlgBuyingSuccess) {
			this.finish();
			dialog.dismiss();
		}
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
