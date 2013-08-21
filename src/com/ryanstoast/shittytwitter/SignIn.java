package com.ryanstoast.shittytwitter;

import java.util.HashMap;

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
	
	static String STEP_ONE_TOKEN          = "";
	static String STEP_ONE_TOKEN_SECRET   = "";
	
	static String STEP_TWO_TOKEN          = "";
	static String STEP_TWO_TOKEN_VERIFIER = "";
	
	static String STEP_THREE_TOKEN        = "";
	static String STEP_THREE_TOKEN_SECRET = "";
	
	static String USER_ID                 = "";
	
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
		 
		 String[] process = data.getDataString().split("\\?");
		 String[] results = process[1].split("&");
		 String[] tokens  = {results[0].split("=")[1], results[1].split("=")[1]};
		 
		 STEP_TWO_TOKEN = tokens[0];
		 STEP_TWO_TOKEN_VERIFIER = tokens[1];
		 
		 GetAccessToken get = new GetAccessToken();
		 get.execute(tokens);
	 }
	 
	 protected void onPause() {
		 super.onPause();
		 HashMap<String, String> map = new HashMap<String, String>();
		 
		 map.put("STEP_ONE_TOKEN"         , STEP_ONE_TOKEN);
	     map.put("STEP_ONE_TOKEN_SECRET"  , STEP_ONE_TOKEN_SECRET);
	     map.put("STEP_TWO_TOKEN"         , STEP_TWO_TOKEN);
	     map.put("STEP_TWO_TOKEN_VERIFIER", STEP_TWO_TOKEN_VERIFIER);
	     map.put("STEP_THREE_TOKEN"       , STEP_THREE_TOKEN);
	     map.put("STEP_THREE_TOKEN_SECRET", STEP_THREE_TOKEN_SECRET);
	     map.put("USER_ID"                , USER_ID);
	     
	     MainActivity.setTokenMap(map);
	     
	 }
	 
	 public void onBackPressed() {
		 //Does nothing so the Oauth flow can't be interrupted;
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
			 STEP_ONE_TOKEN = result[0];
			 STEP_ONE_TOKEN_SECRET = result[1];
			 
			 Intent browser = new Intent(Intent.ACTION_VIEW, 
		              Uri.parse(TwitterAPI.BASE_URL + TwitterAPI.AUTHENTICATE +
		            		  "?oauth_token=" + STEP_ONE_TOKEN));
			 
             SignIn.this.startActivityForResult(browser, SIGN_IN_REQUEST_CODE);
		 }
	 }
	 
	 private class GetAccessToken extends AsyncTask<String, Void, String[]> {
		 
		 protected void onPreExecute() {
			 
		 }
		 
		 protected String[] doInBackground(String... s) {
			 
			 
			 return Network.getAccessToken(s);
		 }
		 
		 protected void onPostExecute(String[] result) {
			 
			 if (result[0] != "") {
				 
				 STEP_THREE_TOKEN        = result[0];
				 STEP_THREE_TOKEN_SECRET = result[1];
				 USER_ID                 = result[3];
				 
			 }
			 SignIn.this.onPause();
			 SignIn.this.finish();
		 }
	 }
	 
}
