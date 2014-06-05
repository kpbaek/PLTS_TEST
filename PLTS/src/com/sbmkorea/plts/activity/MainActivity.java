package com.sbmkorea.plts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void startProductionMainActivity(View view) {
		Intent intent = new Intent(this, ProductionMainActivity.class);
		startActivity(intent);
	}
	
	public void startRepairMainActivity(View view) {
		Intent intent = new Intent(this, RepairMainActivity.class);
		startActivity(intent);
	}
	
	public void startSpecChangeActivity(View view) {
		Intent intent = new Intent(this, SpecChangeActivity.class);
		startActivity(intent);
	}
	
	public void startProductHistoryActivity(View view) {
		Intent intent = new Intent(this, ProductHistoryActivity.class);
		startActivity(intent);
	}

	public void startPartHistoryActivity(View view) {
		Intent intent = new Intent(this, PartHistoryActivity.class);
		startActivity(intent);
	}
	
	public void startshipmentHistoryActivity(View view) {
		Intent intent = new Intent(this, ShipmentHistoryActivity.class);
		startActivity(intent);
	}	

}
