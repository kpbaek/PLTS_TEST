package com.sbmkorea.plts.activity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sbmkorea.plts.task.PostNetworkTask;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SpecChangeActivity extends ProductionOneToOneActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void finishSpecChange(View view) {
		super.finishProduction(view);
	}
	
	@Override
	protected String convertDataToJson(int rowId) {
		LinearLayout recordsLayout = table.getRecordsLayout();
		LinearLayout row = (LinearLayout) recordsLayout.getChildAt(rowId - 1);
		EditText e;
		
		e = (EditText) row.getChildAt(1);
		String productId = e.getText().toString();
		
		e = (EditText) row.getChildAt(2);
		String partId = e.getText().toString();
		
		Gson gson = new Gson();
		JsonObject data = new JsonObject();
		data.addProperty("manufacturing_type_name", PostNetworkTask.MANUFACTURING_TYPE_SPEC_CHANGE);
		data.addProperty("product_barcode", productId);
		data.addProperty("part_barcode", partId);
		data.addProperty("worker_barcode", USER_BARCODE);
		
		// add null repair_error_type and null remark
		// to use same code in server side.
		data.addProperty("repair_error_type", "");
		data.addProperty("remark", "");
		
		return gson.toJson(data);
	}
}
