package com.ryanstoast.shittytwitter;

import android.util.Log;

public class Network {

	static String url = "https://api.twitter.com";
	
	public static String getToken() {
		String tokenUrl = url + "/oauth/request_token";
		Log.d("Here", "I issg");
		
		
		
		String authString = "OAuth ";
		authString = authString + "oauth_callback=\"oop\","; //POOP!
		authString = authString + "oauth_consumer_key=\"" + 
		             Settings.CONSUMER_KEY + "\",";
		authString = authString + "oauth_nonce=\"" + TwitterAPI.getNONCE() + "\",";
		authString = authString + "oauth_timestamp=\"" + 
		             Integer.toString((int) (System.currentTimeMillis() / 1000L)) +
		             "\",";
		authString = authString + "oauth_version=\"1.0\"";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("Authorization", authString);
		
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.d("APICalls.getEventsNearLocation", (client.getResponse() == null) ? "" : client.getResponse());
		
		
		return "";
	}
	
	
	
}
