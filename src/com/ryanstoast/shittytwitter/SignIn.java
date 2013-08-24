package com.ryanstoast.shittytwitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class SignIn extends Activity{

	final static int SIGN_IN_REQUEST_CODE = 425093;
    final static int SIGN_IN_FAILED       = 352;
    final static int SIGN_IN_OK           = 0;
	
	protected static String STEP_ONE_TOKEN          = "";
	protected static String STEP_ONE_TOKEN_SECRET   = "";
	
	protected static String STEP_TWO_TOKEN          = "";
	protected static String STEP_TWO_TOKEN_VERIFIER = "";
		
	protected void clearStrings() {
		STEP_ONE_TOKEN          = "";
		STEP_ONE_TOKEN_SECRET   = "";
		STEP_TWO_TOKEN          = "";
		STEP_TWO_TOKEN_VERIFIER = "";
	}
	
	 protected void onCreate(Bundle savedInstanceState) {
		 
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.sign_in);
		 
	   Button signIn = (Button) findViewById(R.id.signInAction);
	   if (STEP_ONE_TOKEN == "") {
		    	signIn.setEnabled(true);
	    }
	    
	 }
	 
	 public void signInAction(View v) {
		 
		 Button signIn = (Button) findViewById(R.id.signInAction);
		 signIn.setEnabled(false);
		 ProgressBar bar = (ProgressBar) findViewById(R.id.signInProgressBar);
		 bar.setVisibility(View.VISIBLE);
		 bar.setProgress(0);
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
		 
		 ProgressBar bar = (ProgressBar) findViewById(R.id.signInProgressBar);
		 bar.setProgress(66);
		 
		 GetAccessToken get = new GetAccessToken();
		 get.execute(tokens);
	 }
	 
	 protected void onPause() {
		 super.onPause();
	 }
	 
	 private void finishActivity(String[] result) {
		 
		 ProgressBar bar = (ProgressBar) findViewById(R.id.signInProgressBar);
		 bar.setVisibility(View.INVISIBLE);
		 bar.setProgress(0);
		 
		 Intent finishIntent = this.getIntent();
		 finishIntent.putExtra("TOKEN", result[0]);
		 finishIntent.putExtra("TOKEN_SECRET", result[1]);
		 finishIntent.putExtra("USER_ID", result[3]);
		 setResult(SignIn.SIGN_IN_OK, finishIntent);
		 finish();
		 
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
			 
			 ProgressBar bar = (ProgressBar) findViewById(R.id.signInProgressBar);
			 bar.setProgress(33);
			 
			 if (result[0] == "") {
				 Log.d("signInCaller:", "Recieved Failed login");
				 setResult(SignIn.SIGN_IN_FAILED);
				 clearStrings();
				 finish();
			 }
			 Log.d("signInCaller Result", result[0]);
			 STEP_ONE_TOKEN = result[0];
			 STEP_ONE_TOKEN_SECRET = result[1];
			 
			 Intent browser = new Intent(Intent.ACTION_VIEW, 
		              Uri.parse(TwitterAPI.BASE_URL + TwitterAPI.AUTHENTICATE +
		            		  "?oauth_token=" + STEP_ONE_TOKEN));
			 
             startActivityForResult(browser, SIGN_IN_REQUEST_CODE);
		 }
	 }
	 
	 private class GetAccessToken extends AsyncTask<String, Void, String[]> {
		 
		 protected void onPreExecute() {
			 
		 }
		 
		 protected String[] doInBackground(String... s) {
			 
			 
			 return Network.getAccessToken(s);
		 }
		 
		 protected void onPostExecute(String[] result) {
			 
			 ProgressBar bar = (ProgressBar) findViewById(R.id.signInProgressBar);
			 bar.setProgress(100);
			 
			 if (result[0] == "") {
				 
				 clearStrings();
				 setResult(SIGN_IN_FAILED);
				 finish();
				 
			 }
			 
			 finishActivity(result);
		
		 }
	 }
	 
}
