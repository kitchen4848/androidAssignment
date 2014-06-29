package com.example.stocktradingsystem.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class InternetFetcher {
	public static String FetchStringFromUrl(String urls) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(urls);
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String ret = "";
		String line = "";

		while ((line = rd.readLine()) != null)
			ret += line;
		rd.close();
		
		return ret;
	}
}
