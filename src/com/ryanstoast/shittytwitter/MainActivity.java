package com.ryanstoast.shittytwitter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static String token        = "";
	static String token_secret = "";
	static String user_id      = "";
	static public String getTwitterTokenSecret() {
	    return token_secret;
	}
	
	static public String getTwitterToken() {
		return token;
	}
	
	static public String getUserName() {
		return user_id;
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
            	
            	if (resultCode == SignIn.SIGN_IN_OK) {
            	
            		Bundle bundle = data.getExtras();
            		token = bundle.getString("TOKEN");
            		token_secret = bundle.getString("TOKEN_SECRET");
            		user_id      = bundle.getString("USER_ID");
            		
            	    TextView hello     = (TextView) findViewById(R.id.hello);
            	    Button signButton  = (Button) findViewById(R.id.signButton);
                    Button tweetButton = (Button) findViewById(R.id.createTweet);
                    Button feedButton  = (Button) findViewById(R.id.viewFeed);
                
                    String helloText = (String) hello.getText(); 
                    hello.setText(helloText + user_id + "!");
                    hello.setVisibility(View.VISIBLE);
                    signButton.setText("Sign Out");
                    tweetButton.setEnabled(true);
                    feedButton.setEnabled(true);
            	} else if (resultCode == SignIn.SIGN_IN_FAILED) {
            		String error ="There was an error with Authentication.\n" +
            				 "Please clear your browser cookies and try again";
            		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            	}
                
            	break;
            case Tweet.TWEET_REQUEST_CODE:
            	break;
            case Feed.FEED_REQUEST_CODE:
            	break;
        }
    }
    
}
