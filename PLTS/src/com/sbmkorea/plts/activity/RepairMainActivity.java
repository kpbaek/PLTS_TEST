package com.sbmkorea.plts.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sbmkorea.plts.activity.listener.RepairTextWatcher;
import com.sbmkorea.plts.activity.listener.SpinnerWatcher;
import com.sbmkorea.plts.task.PostNetworkTask;

public class RepairMainActivity extends BaseActivity {
	private RepairTextWatcher productTextWatcher;
	private RepairTextWatcher partTextWatcher;
	private SpinnerWatcher spinnerWatcher;
	
	private EditText productEditText;
	private EditText partEditText;
	private Spinner spinner;
	private EditText remarksEditText;
	
	private ImageView statusImageView;
	
	private ReUploadOnClickListener reUploadOnClickListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair_main);
		// Show the Up button in the action bar.
		setupActionBar();
		
		setupViews();
		setupBarcodeWatcher();
		setupSpinnerWatcher();
		reUploadOnClickListener = new ReUploadOnClickListener();
	}
	
	private void setupViews() {
		productEditText = (EditText) findViewById(R.id.RepairMainActivity_product_EditText);
		partEditText = (EditText) findViewById(R.id.RepairMainActivity_part_EditText);
		spinner = (Spinner) findViewById(R.id.RepairMainActivity_errortype_Spinner);
		remarksEditText = (EditText) findViewById(R.id.RepairMainActivity_remarks_EditText);
		statusImageView = (ImageView) findViewById(R.id.RepairMainActivity_status_ImageView);
		
		partEditText.setEnabled(false);
		spinner.setEnabled(false);
		remarksEditText.setEnabled(false);
	}
	private void setupBarcodeWatcher(){
		// TO prevent Error Barcode reading. (POKA-YOKE)
		productTextWatcher = new RepairTextWatcher(this, productEditText, partEditText);
		partTextWatcher = new RepairTextWatcher(this, partEditText, remarksEditText);
		productEditText.addTextChangedListener(productTextWatcher);
		partEditText.addTextChangedListener(partTextWatcher);
	}
	
	private void setupSpinnerWatcher() {
		spinner = (Spinner) findViewById(R.id.RepairMainActivity_errortype_Spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.RepairMainActivity_errortype_Spinner_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinnerWatcher = new SpinnerWatcher();
		spinner.setOnItemSelectedListener(spinnerWatcher);
	}
	
	public void uploadRepair() {
		PostNetworkTask.Callback onPostExecuteFunction = new PostNetworkTask.Callback() {
			
			@Override
			public void call(int id, String result) {
				// TODO Auto-generated method stub
				afterUpload(result);
			}
		};
		PostNetworkTask postNetworkTask = new PostNetworkTask(this, 0, onPostExecuteFunction);
		String data = convertDataToString();
		postNetworkTask.execute(PostNetworkTask.DEFAULT_TYPE, data);
	}
	
	private void afterUpload(String result) {
		if (result.equals(PostNetworkTask.SUCCESS_STRING)) {
			
			if (partTextWatcher.getBarcode().equals("")) {
				String message = getResources().getString(R.string.ReapirMainActivity_toastMsg_NullBarcode);
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				return;
			}
			else {
				statusImageView.setBackgroundResource(R.drawable.status_icon_complete);
				String message = getResources().getString(R.string.ReapirMainActivity_toastMsg_Done);
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			statusImageView.setBackgroundResource(R.drawable.status_icon_error);
			statusImageView.setOnClickListener(reUploadOnClickListener);
		}
	}
		
   class ReUploadOnClickListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			statusImageView.setBackgroundResource(R.drawable.status_icon_processing);
			uploadRepair();
		}
	}
	
	public void finishRepair(View view) {
		uploadRepair();
	}
	
	public String convertDataToString() {
		String productId = productTextWatcher.getBarcode();
		String partId = partTextWatcher.getBarcode();
 		int spinnerValue = spinner.getSelectedItemPosition()+1;
 		String remarksValue = remarksEditText.getText().toString();

 		Gson gson = new Gson();
 		JsonObject data = new JsonObject();
 		
 		data.addProperty("manufacturing_type_name", PostNetworkTask.MANUFACTURING_TYPE_REPAIR);
		data.addProperty("product_barcode", productId);
		data.addProperty("part_barcode", partId);
		data.addProperty("worker_barcode", USER_BARCODE);
		data.addProperty("repair_error_type", spinnerValue);
		data.addProperty("remark", remarksValue);
		
 		return gson.toJson(data);
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
