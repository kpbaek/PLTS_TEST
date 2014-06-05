package com.sbmkorea.plts.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;

public class GetNetworkTask extends NetworkTask {
	public static final String BASE_API_URL = "http://222.121.121.77/plts/api/v1";
	
	public GetNetworkTask(Context context, Callback func) {
		super(context, func);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		String url = params[0];
		try {
			if (isNetworkAvailable()) {
				result = request(url);
			} else {
				result = NETWORK_ERROR;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (result == null) ? "" : result;
	}
	
	@Override
	protected void onPostExecute(String result){
		if (result.equals(NETWORK_ERROR)) {
    	   showSettingsAlert();
        }
		else {
        	if (super.__func != null) {
	    		// does not use id parameter. just set it to 1
	            super.__func.call(1, result);
	        }
		}
	}
	
	private String request(String urlString) throws IOException {
		InputStream inStream = null;
		int bufferSize = 1024 * 10;

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(1000);
			conn.setConnectTimeout(1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("charset", "utf-8");
			conn.setDoInput(true);
			
			// starts the request
			conn.connect();
			int response = conn.getResponseCode();
			Log.e("Net Response", "The response is: " + response);
			inStream = conn.getInputStream();
			// Convert the InputStream into a String.
			String contentAsString = super.readIt(inStream, bufferSize);
			return contentAsString;
		}
		finally {
			if(inStream != null) {
			    inStream.close();
			}
		}
	}
}
