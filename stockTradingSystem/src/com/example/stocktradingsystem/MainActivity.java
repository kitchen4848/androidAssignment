package com.example.stocktradingsystem;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	Button btnCancel;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtUserName = (EditText)findViewById(R.id.txtUserName);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		//loadtextbox();
	}
	
	
	public void onClick(View view){
		switch (view.getId()) {
			case R.id.btnLogin:
				if(txtUserName.getText().toString().equals("admin") && txtPassword.getText().toString().equals("admin")){
				Intent intent = new Intent(MainActivity.this, StockMeun.class);
				startActivity(intent);
				
				SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
				Editor edit = userDetails.edit();
				edit.clear();
				edit.putString("username", txtUserName.getText().toString());
				edit.putString("password", txtPassword.getText().toString());
				edit.commit();
				
				this.finish();
				} else {
					ShowAlert();
				}
			break;
			case R.id.btnCancel:
				this.finish();
			break;
		}
	}
	
	/*public void loadtextbox(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		txtUserName.setText(prefs.getString("UserName", null));
		txtUserName.setText(prefs.getString("Password", null));
	}*/
	
	public void ShowAlert(){
		Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Warning");
		alert.setMessage("The username or password incorrect.");
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {

			}
		};
		alert.setNeutralButton("OK",OkClick );
		alert.show();
	}
	

	
	

}
