package com.example.stocktradingsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
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
		tvdata = (TextView)findViewById(R.id.tvdata);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		txtSearch = (EditText)findViewById(R.id.txtSearch);


	}
	
	private class FetchPageTask extends AsyncTask<String,Void,String>{
		
		protected String doInBackground(String...url){
			String result ="";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url[0]);
				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent())); 
				String reply = "";
				String line = "";
				while ((line=rd.readLine()) != null)
					reply += line;
				rd.close();
				JSONObject jObject = new JSONObject(reply);
				JSONObject quote = jObject.getJSONObject("quote");
					String mode = quote.getString("@mode");
					JSONObject stock = quote.getJSONObject("stock");
					String symbol = stock.getString("symbol");
					JSONObject name = stock.getJSONObject("name");
						String chinese = name.getString("chinese");
						String english = name.getString("english");
					String sspn = stock.getString("sspn");
					String price = stock.getString("price");
					String change = stock.getString("change");
					String pct_change = stock.getString("pct_change");
					String pexit = stock.getString("pexit");
					String open = stock.getString("open");
					String high = stock.getString("high");
					String low = stock.getString("low");
					String bid = stock.getString("bid");
					String ask = stock.getString("ask");
					String year_high = stock.getString("year_high");
					String year_low = stock.getString("year_low");
					String volume = stock.getString("volume");
					String turnover = stock.getString("turnover");
					String pe = stock.getString("pe");
					String market_capital = stock.getString("market_capital");
					String month_high = stock.getString("month_high");
					String month_low = stock.getString("month_low");
					String lot = stock.getString("lot");
					String dps = stock.getString("dps");
					String eps = stock.getString("eps");
					JSONObject rsi = stock.getJSONObject("rsi");
						String rd10 = rsi.getString("d10");
						String rd14 = rsi.getString("d14");
						String rd20 = rsi.getString("d20");
					JSONObject ma = stock.getJSONObject("ma");
						String md10 = ma.getString("d10");
						String md20 = ma.getString("d20");
						String md50 = ma.getString("d50");
					String date = stock.getString("date");
				result += "Mode: "+mode+"\nSymbol: "+symbol+"\nName: "+chinese+"("+english+")"+"\nsspn: "+sspn+"\nPrice: "+price+"\npct_change: "+pct_change;
				result += "\nPexit: "+pexit+"\nOpen: "+open+"\nHigh: "+high+"\nLow: "+low+"\nBid: "+bid+"\nAsk: "+ask+"\nYear_high: "+year_high+"\nYear_low: "+year_low;
				result += "\nVolume: "+volume+"\nTurnover: "+turnover+"\nPe: "+pe+"\nMarket_capital: "+market_capital+"\nMonth_high: "+month_high+"\nMonth_low: "+month_low;
				result += "\nLot: "+lot+"\ndps: "+dps+"\neps: "+eps+"\nrsi: "+String.format("d10:%s \n%8s:"+rd14+" \n%8s:"+rd20+"", rd10,"d14","d20");
				result += "\nma: "+String.format("d10:%s \n%7s:"+md10+" \n%7s:"+md20+"", md50,"d14","d20")+"\nDate: "+date;
				
			}catch (Exception e) {
				tvdata.setText(e.getMessage());
			}
				return result;
		}
		
		protected void onPostExecute(String result){
			tvdata.setText(result);
		}
	}
	
	public void clickedSearch(View view){
		symbol = txtSearch.getText().toString();
		
		if (task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)) {
					task = new FetchPageTask();
					task.execute("http://www.alanpo.com/itp4501/stock_quote.php?stock_no="+symbol);
				}
	}
}

/*
	
	
	
			JSONObject quote = jObject.getJSONObject("quote");
					String mode = quote.getString("@mode");
					
			
	

	dataStr +=(chinese);
			
	
} */
