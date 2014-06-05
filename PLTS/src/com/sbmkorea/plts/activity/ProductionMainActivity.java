package com.sbmkorea.plts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

public class ProductionMainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_production_main);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	public void startProductionOneToOneActivity(View view) {
		Intent intent = new Intent(this, ProductionOneToOneActivity.class);
		startActivity(intent);
	}

	public void startProductionOneToManyActivity(View view) {
		Intent intent = new Intent(this, ProductionOneToManyActivity.class);
		startActivity(intent);
	}

	public void startProductionShipmentActivity(View view) {
		Intent intent = new Intent(this, ProductionShipmentActivity.class);
		startActivity(intent);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
