package com.ryanstoast.shittytwitter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class Tweet extends Activity {

	final static int TWEET_REQUEST_CODE = 54345;
	
	private static String body = "";
	
	protected void onCreate(Bundle saved) {
		super.onCreate(saved);
		setContentView(R.layout.tweet);
		
		EditText tweetBox = (EditText) findViewById(R.id.tweetBody);
		
		tweetBox.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				body = s.toString();
			}
			// Unused interface methods of TextWatcher.
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
	}
	
	public void post(View v) {
		
		PostTweet post = new PostTweet();
		post.execute(body);
		
	}
	
	private class PostTweet extends AsyncTask<String, Void, String> {
		 
		 protected void onPreExecute() {
			 
		 }
		 
		 protected String doInBackground(String... s) {
			 
			 
			 return Network.postTweet(s[0]);
		 }
		 
		 protected void onPostExecute(String[] result) {
			 
			 finish();
	     }
	}

}
