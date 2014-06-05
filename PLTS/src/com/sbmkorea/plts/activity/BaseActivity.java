package com.sbmkorea.plts.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	public static final String DEFAULT_USER_ID = "SBM_KOREA";
	public static final String DEFAULT_USER_BARCODE = "9010001000009";
	protected static String USER_ID = "SBM_KOREA";
	protected static String USER_BARCODE = "9010001000009";

	protected Menu menu = null;

	public static void setUser(String userId, String userBarcode) {
		USER_ID = userId;
		USER_BARCODE = userBarcode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (menu != null) {
			changeUser();		
		}
	}

	// Notice
	// when you extend this class, you need to delete child Activity's onCreateOptionsMenu method 
	// in Order to maintain Log-in module.
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.base, menu);
		changeUser();
		
		return true;
	}
	
	protected void changeUser() {
		MenuItem item = menu.findItem(R.id.user_id);
		item.setTitle(USER_ID);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.user_id:
			if (USER_ID.equals(DEFAULT_USER_ID)) {
				login();
			}
			else {
				logOutChecker();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void login() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	private void logout() {
		USER_ID = DEFAULT_USER_ID;
		USER_BARCODE = DEFAULT_USER_BARCODE;
		changeUser();
	}
	
	private void logOutChecker(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(R.string.logout_dialogue_msg)
		.setCancelable(false)
		.setPositiveButton(R.string.logout_dialogue_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				logout();
			}})
		.setNegativeButton(R.string.logout_dialogue_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				return;
			}});
		
	    AlertDialog alert = alt_bld.create();
	    // Title for AlertDialog
	    alert.setTitle(R.string.logout_dialogue_title);
	    // Icon for AlertDialog
	    alert.setIcon(R.drawable.ic_launcher);
	    alert.show();
	}

}
