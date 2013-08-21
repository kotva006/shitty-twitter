package com.ryanstoast.shittytwitter;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static String token = "";
	static String token_secret = "";
	
	private static HashMap<String, String> tokenMap = new HashMap<String, String>();
	public static void setTokenMap(HashMap<String, String> map) {
		tokenMap = map;
	}
	
	
	static public String getTwitterTokenSecret() {
		String result = tokenMap.get("STEP_THREE_TOKEN_SECRET");
		if (result == null) {
			return "";
		}
		return result;
	}
	
	static public String getTwitterToken() {
		String result = tokenMap.get("STEP_THREE_TOKEN");
		if (result == null) {
			return "";
		}
		return result;
	}
	
	static public String getUserName() {
		String result = tokenMap.get("USER_ID");
		if (result == null) {
			return "";
		}
		return result;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button signButton = (Button) findViewById(R.id.signButton);
        Button tweetButton = (Button) findViewById(R.id.createTweet);
        Button feedButton = (Button) findViewById(R.id.viewFeed);
        
        if (token.equals("")) {
        	signButton.setText("Sign In");
        	signButton.setEnabled(true);
        	tweetButton.setEnabled(false);
        	feedButton.setEnabled(false);        	
        } else {
        	signButton.setText("Sign Out");
        	tweetButton.setEnabled(true);
    	    feedButton.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void signAction(View v) {
    	
    	Button signButton = (Button) findViewById(R.id.signButton);
    	Button tweetButton = (Button) findViewById(R.id.createTweet);
        Button feedButton = (Button) findViewById(R.id.viewFeed);
    	
    	if (signButton.getText().equals("Sign Out")) {
    		tokenMap.clear();
    		signButton.setText("Sign In");
        	signButton.setEnabled(true);
        	tweetButton.setEnabled(false);
        	feedButton.setEnabled(false);
    	}
    	    	
    	Intent signIn = new Intent(MainActivity.this, SignIn.class);
    	startActivityForResult(signIn,SignIn.SIGN_IN_REQUEST_CODE);
    	
    	
    }
    
    public void tweetAction(View v) {
    	
    	Intent tweet = new Intent(MainActivity.this, Tweet.class);
    	startActivityForResult(tweet, Tweet.TWEET_REQUEST_CODE);
    	
    }
    
    public void feedAction(View v) {
    	
    	Intent feed = new Intent(MainActivity.this, Feed.class);
    	startActivityForResult(feed, Feed.FEED_REQUEST_CODE);
    	
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SignIn.SIGN_IN_REQUEST_CODE:
            	
            	TextView hello     = (TextView) findViewById(R.id.hello);
            	Button signButton  = (Button) findViewById(R.id.signButton);
                Button tweetButton = (Button) findViewById(R.id.createTweet);
                Button feedButton  = (Button) findViewById(R.id.viewFeed);
                
                String helloText = (String) hello.getText(); 
                hello.setText(helloText + tokenMap.get("USER_ID") + "!");
                hello.setVisibility(View.VISIBLE);
                signButton.setText("Sign Out");
                tweetButton.setEnabled(true);
                feedButton.setEnabled(true);
                
                
            	break;
            case Tweet.TWEET_REQUEST_CODE:
            	break;
            case Feed.FEED_REQUEST_CODE:
            	break;
        }
    }
    
}
