package com.ryanstoast.shittytwitter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import android.widget.Button;

public class MainActivity extends Activity {
	
	static String requestToken = "";
	
	public static String getTwitterToken() {
		return requestToken;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button signButton = (Button) findViewById(R.id.signButton);
        Button tweetButton = (Button) findViewById(R.id.createTweet);
        Button feedButton = (Button) findViewById(R.id.viewFeed);
        
        if (requestToken.equals("")) {
        	signButton.setText("Sign In");
        	signButton.setClickable(true);
        	tweetButton.setClickable(false);
        	feedButton.setClickable(false);        	
        } else {
        	signButton.setText("Sign Out");
        	tweetButton.setClickable(true);
    	    feedButton.setClickable(true);
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
    	
    	if (requestToken.equals("") && signButton.getText().toString().equals("Sign In")) {
    	    String url = "https://api.twitter.com";
    	    //Settings.random();
    	    Intent signIn = new Intent(MainActivity.this, SignIn.class);
    	    startActivityForResult(signIn,SignIn.SIGN_IN_REQUEST_CODE);
    	} else {
    		requestToken = "";
    		signButton.setText("Sign In");
        	tweetButton.setClickable(false);
        	feedButton.setClickable(false);
    	}
    	
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
            	break;
            case Tweet.TWEET_REQUEST_CODE:
            	break;
            case Feed.FEED_REQUEST_CODE:
            	break;
        }
    }
    
}
