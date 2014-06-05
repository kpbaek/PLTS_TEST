package com.sbmkorea.plts.activity.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class SpinnerWatcher implements OnItemSelectedListener {
		
	private String SpinnerValue;
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		// TODO Auto-generated method stub
		SpinnerValue = (String) parent.getItemAtPosition(pos);
	}
	
	public String getSpinnerValue() {
		return SpinnerValue;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}