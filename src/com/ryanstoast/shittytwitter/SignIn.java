package com.ryanstoast.shittytwitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignIn extends Activity{

	final static int SIGN_IN_REQUEST_CODE = 425093;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.sign_in);
	 }
	 
	 public void signInAction(View v) {
		 //EditText pin = (EditText) findViewById(R.id.pin_box);
		 GetRequestToken caller = new GetRequestToken();
		 caller.execute();

	 }
	 
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {}
	 
	 // Information about handling results from the browser
	 // http://stackoverflow.com/questions/7150493/how-does-twitter-oauth-work-in-android
	 @Override
	 protected void onNewIntent(Intent data) {
		 Log.d("Data string", data.getDataString());
		 
		 String[] process = data.getDataString().split("?");
		 String[] results = process[1].split("=");
	 }
	 
	 private class GetRequestToken extends AsyncTask<Void, Void, String[]> {
		 
		 
		 
		 protected void onPreExecute() {
			 //dialog = ProgressDialog.show(getApplicationContext(), "Signing In...", "");
		 }
		 
		 protected String[] doInBackground(Void... v) {
			 
			 return Network.getToken();
		 }
		 
		 protected void onPostExecute(String[] result) {
			 
			 
			 if (result[0] == "") {
				 Log.d("signInCaller:", "Recieved Failed login");
				 return;
			 }
			 Log.d("signInCaller Result", result[0]);
			 MainActivity.token = result[0];
			 MainActivity.token_secret = result[1];
			 
			 Intent browser = new Intent(Intent.ACTION_VIEW, 
		              Uri.parse(TwitterAPI.BASE_URL + TwitterAPI.AUTHENTICATE +
		            		  "?oauth_token=" + MainActivity.getTwitterToken()));
			 
             SignIn.this.startActivityForResult(browser, SIGN_IN_REQUEST_CODE);
		 }
	 }
	 
}
