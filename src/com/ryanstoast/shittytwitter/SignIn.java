package com.ryanstoast.shittytwitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignIn extends Activity{

	final static int SIGN_IN_REQUEST_CODE = 425093;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	 }
	 
	 public void signInAction(View v) {
		 EditText username = (EditText) findViewById(R.id.userName);
		 EditText password = (EditText) findViewById(R.id.password);
	 }
	 
	 private class signInCaller extends AsyncTask<String, Void, String> {
		 
		 ProgressDialog dialog;
		 
		 protected void onPreExecute() {
			 dialog = ProgressDialog.show(getApplicationContext(), "Signing In...", "");
		 }
		 
		 protected String doInBackground(String... string) {
			 
			 return Network.signIn(string[0],string[1]);
		 }
		 
		 protected void onPostExecute(String result) {
			 
		 }
	 }
	
}
