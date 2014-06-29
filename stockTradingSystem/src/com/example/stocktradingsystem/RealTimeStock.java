package com.example.stocktradingsystem;

import com.example.stocktradingsystem.controller.StockInfo;
import com.example.stocktradingsystem.controller.StockInfoFetcher;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RealTimeStock extends Activity {
	TextView tvdata;
	Button btnSearch;
	EditText txtSearch;
	FetchPageTask task = null;
	String symbol;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_time_stock);
		tvdata = (TextView) findViewById(R.id.tvdata);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		txtSearch = (EditText) findViewById(R.id.txtSearch);

	}

	private class FetchPageTask extends StockInfoFetcher {
		@Override
		protected void onComplete(StockInfo result) {
			String ret = "(error fetching data)";

			if (result != null) {
				ret = "Mode: " + result.getMode() + "\n";
				ret += "Symbol: " + result.getSymbol() + "\n";
				ret += "Name: " + result.getChinese() + "(" + result.getEnglish() + ")" + "\n";
				ret += "sspn: " + result.getSspn() + "\n";
				ret += "Price: " + result.getPrice() + "\n";
				ret += "pct_change: " + result.getPct_change() + "\n";
				ret += "Pexit: " + result.getPexit() + "\n";
				ret += "Open: " + result.getOpen() + "\n";
				ret += "High: " + result.getHigh() + "\n";
				ret += "Low: " + result.getLow() + "\n";
				ret += "Bid: " + result.getBid() + "\n";
				ret += "Ask: " + result.getAsk() + "\n";
				ret += "Year_high: " + result.getYear_high() + "\n";
				ret += "Year_low: " + result.getYear_low() + "\n";
				ret += "Volume: " + result.getVolume() + "\n";
				ret += "Turnover: " + result.getTurnover() + "\n";
				ret += "Pe: " + result.getPe() + "\n";
				ret += "Market_capital: " + result.getMarket_capital() + "\n";
				ret += "Month_high: " + result.getMonth_high() + "\n";
				ret += "Month_low: " + result.getMonth_low() + "\n";
				ret += "Lot: " + result.getLot() + "\n";
				ret += "dps: " + result.getDps() + "\n";
				ret += "eps: " + result.getEps() + "\n";
				ret += "rsi: " + String.format("d10:%s \n%8s:" + result.getRd14() + " \n%8s:" + result.getRd20() + "", result.getRd10(), "d14", "d20");
				ret += "ma: " + String.format("d10:%s \n%7s:" + result.getMd10() + " \n%7s:" + result.getMd20() + "", result.getMd50(), "d14", "d20") + "\nDate: " + result.getDate();
			}

			tvdata.setText(ret);
		}
	}

	public void clickedSearch(View view) {
		symbol = txtSearch.getText().toString();

		FetchPageTask fetchPageTask = new FetchPageTask();
		if (!fetchPageTask.FindFromId(symbol)) {
			Toast.makeText(getApplicationContext(), "Format of symbol is incorrect.", Toast.LENGTH_SHORT).show();
		}
	}
}

/*
 * 
 * 
 * 
 * JSONObject quote = jObject.getJSONObject("quote"); String mode =
 * quote.getString("@mode");
 * 
 * 
 * 
 * 
 * dataStr +=(chinese);
 * 
 * 
 * }
 */
