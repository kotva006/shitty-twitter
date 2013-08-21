package com.ryanstoast.shittytwitter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Feed extends Activity{

	final static int FEED_REQUEST_CODE = 576834;
	@Override
	protected void onCreate(Bundle saved) {
		super.onCreate(saved);
		setContentView(R.layout.feed);
		GetTimeline request = new GetTimeline();
		request.execute();
	}
	
	private void populateView(ArrayList<TimeLine> line) {
		
		ScrollView root = (ScrollView) findViewById(R.id.scroll);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.feed);
		TextView     header = (TextView) findViewById(R.id.header);
		header.setText("Timeline for: " + MainActivity.getUserName());
	    
		for (TimeLine item : line) {
			
			TextView user   = new TextView(this);
			TextView tweet  = new TextView(this);
			TextView date   = new TextView(this);
			ImageView image = new ImageView(this);
			
			user.setText(item.getUser() + ": ");
			tweet.setText(item.getStatus());
			date.setText(item.getDate());
			
			user.setTextSize(20);
			tweet.setTextSize(15);
			date.setTextSize(10);
			
			user.setTextColor(Color.BLACK);
			tweet.setTextColor(Color.parseColor("#2E2E2F"));
			date.setTextColor(Color.parseColor("#2E2E2F"));
			
			/*ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(0,0);
			params.setMargins(10, 0, 10, 1);
			Log.d("Params are", "made");
			user.setLayoutParams(params);
			tweet.setLayoutParams(params);
			image.setLayoutParams(params);
			
			params.setMargins(10, 0, 10, 10);
			
			date.setLayoutParams(params); */
		
			try {
				if (item.getPicture() == null) {
					Log.d("Null image", "Skipping");
				}
				URL url = new URL(item.getPicture());
				
				Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				image.setImageBitmap(bmp);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Log.d("Bad URL", e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("Bad IO", e.getMessage());
			}
			
			layout.addView(user);
			layout.addView(tweet);
			layout.addView(image);
			layout.addView(date);
			
		}
		setContentView(root);
	}
	
	
	
	private class GetTimeline extends AsyncTask<Void, Void, ArrayList<TimeLine>> {
		 
		 protected void onPreExecute() {
			 
		 }
		 
		 protected ArrayList<TimeLine> doInBackground(Void... v) {
			 
			 
			 return Network.getTimeline();
		 }
		 
		 protected void onPostExecute(ArrayList<TimeLine> result) {
			 
			 if (result == null) {
				 Toast.makeText(Feed.this, "There was an error with Auth", Toast.LENGTH_LONG).show();
				 return;
			 }
			 
			 populateView(result);
			 
		 }
	 }
	
}
