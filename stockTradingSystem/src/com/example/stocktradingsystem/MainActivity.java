package com.example.stocktradingsystem;

import com.example.stocktradingsystem.controller.DatabaseCommunicate;
import com.example.stocktradingsystem.controller.PortfolioItem;
import com.example.stocktradingsystem.controller.PortfolioItemCollection;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	Button btnCancel;
	TextView txt1;
	TextView txt2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtUserName = (EditText) findViewById(R.id.txtUserName);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		//load the saved user login information
		load();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		//check the user and password and login into the application when the user clicked the login button
		case R.id.btnLogin:
			if (txtUserName.getText().toString().equals("admin") && txtPassword.getText().toString().equals("admin")) {
				// hard-code to put portfolio items into database
//				new AsyncTask<Context, Object, Object>() {
//					@Override
//					protected Object doInBackground(Context... params) {
						/* create portfolio objects
						 * 
						 * portfolioId	stockCode	stockName		lotSize	quantityOnHand
						 * 1001			00001		CHEUNG KONG		1000	2000
						 * 1002			00002		CLP HOLDINGS	500		5000
						 * 1003			00003		HK & CHINA GAS	1000	3000
						 * 1004			00005		HSBC HOLDINGS	400		2000
						 * 1005			00066		MTR COOPOARTION	500		1000
						 */
						PortfolioItemCollection pic = new PortfolioItemCollection();
						pic.add(new PortfolioItem(00001, 	"CHEUNG KONG", 		1000, 	2000));
						pic.add(new PortfolioItem(00002, 	"CLP HOLDINGS", 	500, 	5000));
						pic.add(new PortfolioItem(00003, 	"HK & CHINA GAS", 	1000, 	3000));
						pic.add(new PortfolioItem(00005, 	"HSBC HOLDINGS", 	400, 	2000));
						pic.add(new PortfolioItem(00066, 	"MTR COOPOARTION", 	500, 	1000));
						
//						SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(params[0]);
						SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(this);
						if (DatabaseCommunicate.isPortfolioExist(db))
							DatabaseCommunicate.dropPortfolio(db);
						DatabaseCommunicate.createPortfolio(db);
						for (PortfolioItem i : pic) {
							DatabaseCommunicate.addNewPortfolioItem(db, i);
						}
						db.close();
//						return null;
//					}
//				}.execute(this);
				
				//go to the meun
				Intent intent = new Intent(MainActivity.this, StockMeun.class);
				startActivity(intent);
				
				//save the user login information
				SharedPreferences settings = getApplicationContext().getSharedPreferences("userDetails", 0);
				SharedPreferences.Editor edit = settings.edit();
				// edit.clear();
				edit.putString("username", txtUserName.getText().toString());
				edit.putString("password", txtPassword.getText().toString());
				edit.commit();

				this.finish();
			} else {
				//show the alert to user when input wrong password or username
				ShowAlert();
			}
			break;
		//quit the application if the user clicked the cancel button
		case R.id.btnCancel:
			this.finish();
			break;
		}
	}

	public void ShowAlert() {
		Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Warning");
		alert.setMessage("The username or password incorrect.");
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		};
		alert.setNeutralButton("OK", OkClick);
		alert.show();
	}

	public void load() {
		SharedPreferences setting = getApplicationContext().getSharedPreferences("userDetails", 0);
		String name = setting.getString("username", "");
		String password = setting.getString("password", "");
		txtUserName.setText(name);
		txtPassword.setText(password);
	}

}
