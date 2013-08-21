package com.ryanstoast.shittytwitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.oauth.OAuth;

import android.util.Log;

public class Network {

	static String url = TwitterAPI.BASE_URL;
	
	public static String[] getToken() {
		
		String tokenUrl = url + "/oauth/request_token";
				
		HashMap<String,String> map = new HashMap<String, String>();
		
		map.put("oauth_callback"          , TwitterAPI.CALLBACK);
		map.put("oauth_consumer_key"      , TwitterAPI.CONSUMER_KEY);
		map.put("oauth_nonce"             , TwitterAPI.getNONCE());
		map.put("oauth_signature_method"  , TwitterAPI.SIGNATURE_METHOD);
		map.put("oauth_timestamp"         , TwitterAPI.getTime());
		map.put("oauth_version"           , TwitterAPI.OAUTH_VER);
		
		String signature = TwitterAPI.getSignature(tokenUrl, map, TwitterAPI.getKey(), "POST");
		
		map.put("oauth_signature"         , signature);
		
		String headerString = "OAuth ";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("POST", "/oauth/request_token HTTP/1.1");
		client.AddHeader("User-Agent", "HTTP Client");
		client.AddHeader("Host", "api.twitter.com");
		client.AddHeader("Accept", "*/*");
		client.AddHeader("Authorization", headerString + mapToHeaderString(map));
			
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (client.getResponseCode() != 200) {
	        
			Log.d("Response Code", Integer.toString(client.getResponseCode()));
			Log.d("Error Mesg:", client.getErrorMessage());
			Log.d("Network.getToken: ", (client.getResponse() == null) ? "" : client.getResponse());
		    
		
		} else if(client.getResponseCode() == 200) {
			Log.d("Network.getToken: ", (client.getResponse() == null) ? "" : client.getResponse());
			String[] t = client.getResponse().split("&");
			String[]  tokens = {t[0].split("=")[1], t[1].split("=")[1], t[2].split("=")[1]};
			return tokens;
		}
		return new String[]{""};
		
	}
	
	public static String[] getAccessToken(String[] token) {
		
		String tokenUrl = url + TwitterAPI.ACCESS_TOKEN;
		
        HashMap<String,String> map = new HashMap<String, String>();
		
		map.put("oauth_consumer_key"      , TwitterAPI.CONSUMER_KEY);
		map.put("oauth_nonce"             , TwitterAPI.getNONCE());
		map.put("oauth_signature_method"  , TwitterAPI.SIGNATURE_METHOD);
		map.put("oauth_timestamp"         , TwitterAPI.getTime());
		map.put("oauth_token"             , token[0]);
		map.put("oauth_version"           , TwitterAPI.OAUTH_VER);
		
        String signature = TwitterAPI.getSignature(tokenUrl, map, TwitterAPI.getKey(), "POST");
		
		map.put("oauth_signature"         , signature);
		
		String headerString = "OAuth ";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("POST", "/oauth/access_token HTTP/1.1");
		client.AddHeader("User-Agent", "HTTP Client");
		client.AddHeader("Host", "api.twitter.com");
		client.AddHeader("Accept", "*/*");
		client.AddHeader("Authorization", headerString + mapToHeaderString(map));
	    
		
		client.AddParam("oauth_verifier", token[1]);
		String body = "oauth_verifier=" + token[1]; 
		
		client.AddBody(body);
		
		//client.AddHeader("Content-Length", Integer.toString(client.GetBodyLength()));
		client.AddHeader("Content-Type", "application/x-www-form-urlencoded");
		
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        if (client.getResponseCode() != 200) {
	        
			Log.d("Response Code", Integer.toString(client.getResponseCode()));
			//Log.d("Error Mesg:", client.getErrorMessage());
			Log.d("Network.getAccessToken: ", (client.getResponse() == null) ? "" : client.getResponse());
		    
		
		} else if(client.getResponseCode() == 200) {
			Log.d("Network.getAccessToken: ", (client.getResponse() == null) ? "" : client.getResponse());
			String[] t = client.getResponse().split("&");
			String[]  tokens = {t[0].split("=")[1], t[1].split("=")[1], 
					            t[2].split("=")[1], t[3].split("=")[1]};
			return tokens;
		}
		
        return new String[]{""};
	}
	
	private static String mapToHeaderString(HashMap<String,String> map) {
		
		String result = "";
        TreeMap<String, String> sortedMap = new TreeMap<String, String>(map);
		
		for(String key : sortedMap.keySet()) {
			result += OAuth.percentEncode(key) + "=\"" + OAuth.percentEncode(sortedMap.get(key)) + "\", ";
		}
		return result.substring(0, result.length() - 2);
	}
	
	public static String postTweet(String t) {
				
        String tokenUrl = url + TwitterAPI.POST_TWEET;
		
        HashMap<String,String> map = new HashMap<String, String>();
		
		map.put("oauth_consumer_key"      , TwitterAPI.CONSUMER_KEY);
		map.put("oauth_nonce"             , TwitterAPI.getNONCE());
		map.put("oauth_signature_method"  , TwitterAPI.SIGNATURE_METHOD);
		map.put("oauth_timestamp"         , TwitterAPI.getTime());
		map.put("oauth_token"             , MainActivity.getTwitterToken());
		map.put("oauth_version"           , TwitterAPI.OAUTH_VER);
		map.put("status"                  , t);
		
        String signature = TwitterAPI.getSignature(tokenUrl, map, TwitterAPI.getKey(), "POST");
		
		map.put("oauth_signature"         , signature);
		
		String headerString = "OAuth ";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("POST", "/1.1/statuses/update.json HTTP/1.1");
		client.AddHeader("User-Agent", "HTTP Client");
		client.AddHeader("Host", "api.twitter.com");
		client.AddHeader("Accept", "*/*");
		client.AddHeader("Authorization", headerString + mapToHeaderString(map));
		
		client.AddParam("status", t);
		
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.POST);
		} catch (Exception e) {
			Log.d("Tweet Post Error", e.getMessage());
		}
		
		Log.d("Network.postTweet: ", (client.getResponse() == null) ? "" : client.getResponse());
		
		return "";
	}
	
	public static ArrayList<TimeLine> getTimeline() {
		
        String tokenUrl = url + TwitterAPI.GET_TIMELINE;
		
        HashMap<String,String> map = new HashMap<String, String>();
		
		map.put("oauth_consumer_key"      , TwitterAPI.CONSUMER_KEY);
		map.put("oauth_nonce"             , TwitterAPI.getNONCE());
		map.put("oauth_signature_method"  , TwitterAPI.SIGNATURE_METHOD);
		map.put("oauth_timestamp"         , TwitterAPI.getTime());
		map.put("oauth_token"             , MainActivity.getTwitterToken());
		map.put("oauth_version"           , TwitterAPI.OAUTH_VER);
		
		
        String signature = TwitterAPI.getSignature(tokenUrl, map, TwitterAPI.getKey(), "GET");
		
		map.put("oauth_signature"         , signature);
		
		String headerString = "OAuth ";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("GET", "/1.1/statuses/home_timeline.json HTTP/1.1");
		client.AddHeader("User-Agent", "HTTP Client");
		client.AddHeader("Host", "api.twitter.com");
		client.AddHeader("Accept", "*/*");
		client.AddHeader("Authorization", headerString + mapToHeaderString(map));
		
		//client.AddParam("count", "50");
		
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.GET);
		} catch (Exception e) {
			Log.d("Tweet Post Error", e.getMessage());
		}
		
		Log.d("Network.postTweet: ", (client.getResponse() == null) ? "" : client.getResponse());
		
		ArrayList<TimeLine> timeLine = new ArrayList<TimeLine>();
		
		try {
			JSONArray response = new JSONArray(client.getResponse());
			
			if(response != null) {
				int i = 0;
			    for (i = 0; i < response.length(); i++) {
			    	JSONObject object = response.getJSONObject(i);
			    	TimeLine line = new TimeLine();
			    	line.setStatus(object.getString("text"));
			    	line.setDate(object.getString("created_at"));
			    	JSONObject user = object.getJSONObject("user");
			    	line.setUser(user.getString("name"));
			    	line.setPicture(user.getString("profile_image_url"));
			    	timeLine.add(line);
			    }
			} 
			
		} catch (JSONException e) {
			JSONObject error;
			try {
				error = new JSONObject(client.getResponse());
				if (error.has("errors")){
					return null;
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		} 
		
		return timeLine;
	}
	
	
}
