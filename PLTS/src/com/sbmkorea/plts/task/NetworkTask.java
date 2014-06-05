package com.sbmkorea.plts.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;

import com.sbmkorea.plts.activity.R;

public abstract class NetworkTask extends AsyncTask<String, Void, String> {
	protected static final String NETWORK_ERROR = "NETWORK_ERROR"; 
	
	protected Context __context;
	protected final Callback __func;
	public final int __id;
	
	public interface Callback {
		void call(int id, String result);
	}
	
	public NetworkTask (Context context) {
		__context = context;
		__func = new Callback() {
			@Override
			public void call(int id, String result) {
				// TODO Auto-generated method stub
			}
		};
		__id = 1;
	}
	
	public NetworkTask (Context context, Callback func) {
		__context = context;
		__func = func;
		__id = 1;
	}
	
	public NetworkTask (Context context, int id, Callback func) {
		__context = context;
		__func = func;
		__id = id;
	}
	
	protected String readIt(InputStream inStream, int len) throws IOException, UnsupportedEncodingException {
		StringWriter writer = new StringWriter();
    	IOUtils.copy(inStream, writer, "utf-8");
    	return writer.toString();
    }
    
    public boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) __context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    
    public void showSettingsAlert() {
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(__context);
		
		alertDialog.setTitle(R.string.NetworkTask_alert_title);
		alertDialog.setMessage(R.string.NetworkTask_alert_msg);
		// On pressing Settings Button
		alertDialog.setPositiveButton(R.string.NetworkTask_alert_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				__context.startActivity(intent);
			}
		});
		
		alertDialog.setNegativeButton(R.string.NetworkTask_alert_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		alertDialog.show();
	}

}
