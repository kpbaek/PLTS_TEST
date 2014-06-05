package com.sbmkorea.plts.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;

public class PostNetworkTask extends NetworkTask {
	public static final String BASE_API_URL = "http://222.121.121.77/plts/upload_manufacturing_history/";
	public static final String ONE_TO_MANY_API_URL = "http://222.121.121.77/plts/upload_manufacturing_history/bulk_production/";
	public static final String SHIPMENT_API_URL = "http://222.121.121.77/plts/report_shipment/";
	public static final String DEFAULT_TYPE = "default";
	public static final String ONE_TO_MANY_TYPE = "1-N";
	public static final String SHIPMENT_TYPE = "1-S";
	public static final String SUCCESS_STRING = "success";
	public static final String MANUFACTURING_TYPE_PRODUCTION = "production";
	public static final String MANUFACTURING_TYPE_REPAIR = "repair";
	public static final String MANUFACTURING_TYPE_SPEC_CHANGE = "spec_change";
	public static final String MANUFACTURING_TYPE_SHIPMENT = "shipment";
	
	public PostNetworkTask(Context context, int id, Callback func) {
		super(context, id, func);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		String url;
		String matchingType = params[0];
		String data = params[1];
		//Log.d("matchingType",matchingType);
		try {
			if (isNetworkAvailable()) {
				if (matchingType.equals(ONE_TO_MANY_TYPE)) {
					url = ONE_TO_MANY_API_URL;
				}else if (matchingType.equals(SHIPMENT_TYPE)) {
					url = SHIPMENT_API_URL;
				} else {
					url = BASE_API_URL;
				}
				Log.e("do in back",data);
				result = request(url, data);
			} else {
				result = NETWORK_ERROR;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result){
		if (result == null) {
			result = "null";
		}
		Log.e("onPost", result);
		
		if (result.equals(NETWORK_ERROR)) {
			showSettingsAlert();
        }
        if (super.__func != null) {
        	// does not use id parameter. just set it to 1
	        super.__func.call(super.__id, result);
	    }
	}
	
	private String request(String urlString, String data) throws IOException {
		InputStream inStream = null;
		int bufferSize = 100;

		try {
			String urlParameters = "data=" + data;
			
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(1000);
			conn.setConnectTimeout(1000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("charset", "utf-8");
			conn.setDoInput(true);
			
			OutputStreamWriter request = new OutputStreamWriter(conn.getOutputStream());
            request.write(urlParameters);
            request.flush();
            request.close();
            
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
