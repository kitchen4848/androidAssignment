package com.example.stocktradingsystem.controller;

import java.util.Locale;

import org.json.JSONObject;

import android.R.integer;
import android.os.AsyncTask;

public abstract class StockInfoFetcher {
	private final String providerSearchUrl = "http://www.alanpo.com/itp4501/stock_quote.php?stock_no=%s";
	AsyncTask<String, integer, StockInfo> asyncTask = new AsyncTask<String, integer, StockInfo>() {
		@Override
		protected StockInfo doInBackground(String... urls) {
			try {
				String str = InternetFetcher.FetchStringFromUrl(urls[0]);
				StockInfo ret = StockInfo.ParseStockInfoFromJson(new JSONObject(str));
				// check if every field is null
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(StockInfo result) {
			onComplete(result);
		}
	};

	protected abstract void onComplete(StockInfo result);

	public boolean FindFromId(String id) {
		Long chk = null;
		try {
			chk = Long.parseLong(id);
		} catch (NumberFormatException e) {
			return false;
		}

		if (chk < 0 || chk > 99999)
			return false;

		String reformatted_id = String.format(Locale.US, "%05d", chk);
//		android.util.Log.d("diu", reformatted_id);
		asyncTask.execute(String.format(providerSearchUrl, reformatted_id));
		return true;
	}
}
