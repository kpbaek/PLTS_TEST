package com.sbmkorea.plts.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbmkorea.plts.activity.R;
import com.sbmkorea.plts.data.ShipmentHistoryData;

public class ShipmentHistoryContentView extends LinearLayout {
	
	protected Context context;

	private ImageView borderView;
	public LinearLayout row;
	public LinearLayout repairRow;
	public ImageView taskTypeImageView1;
	public ImageView taskTypeImageView2;
	public TextView enterpriseTextView;
	public TextView shipmentBarcodeTextView;
	public TextView productBarcodeTextView;
	public TextView regDateTextView;
	public TextView workerTextView;
	public TextView etcTextView;
	
	
	// Initialize custom LinearLayout
	
	public ShipmentHistoryContentView(Context context) {
		super(context);
		this.context = context;
		initialize();
	}
	
	public ShipmentHistoryContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initialize();
	}

	@SuppressLint("NewApi")
	public ShipmentHistoryContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initialize();
	}
	
	public int getDimension(int id) {
		return getResources().getDimensionPixelOffset(id);
	}

	private final int MATCH_PARENT = LayoutParams.MATCH_PARENT;
	private final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
	private final int ROW_HEIGHT = getDimension(R.dimen.ShipmentHistoryActivity_Row_HEIGHT);
	private final int TEXTVIEW_WIDTH_SHORT = getDimension(R.dimen.ShipmentHistoryActivity_TextView_WIDTH_SHORT);
	private final int TEXTVIEW_WIDTH_MEDIUM = getDimension(R.dimen.ShipmentHistoryActivity_TextView_WIDTH_MEDIUM);
	private final int TEXTVIEW_WIDTH_LONG = getDimension(R.dimen.ShipmentHistoryActivity_TextView_WIDTH_LONG);
	
	private final int IMAGE_FIELD_WIDTH = getDimension(R.dimen.ShipmentHistoryActivity_IMAGE_FIELD_WIDTH);
	private final int BORDER_HEIGHT = getDimension(R.dimen.ShipmentHistoryActivity_BORDER_HEIGHT);
	//private final int INNER_HORIZONTAL_MARGIN = 10;
	//private final int INNER_VERTICAL_MARGIN = 0;
	
	public void initialize(){
		
		// Since ScrollView can only have a child element,
		// therefore set another LinearLayout inside of ScrollView
		// AND then set other elements inside of the LinearLayout.
		
		// Initialize HistoryContentView
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.parseColor("#cfcfcf"));
	}
	
	private void inflateChildViews(int rowHeight) {
		LayoutParams params;
		
		borderView = new ImageView(context);
		borderView.setBackgroundColor(Color.WHITE);
		params = new LayoutParams(MATCH_PARENT, BORDER_HEIGHT);
		this.addView(borderView, params);
		
		row = new LinearLayout(context);
		row.setOrientation(LinearLayout.HORIZONTAL);
		params = new LayoutParams(MATCH_PARENT, rowHeight);
		row.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(row, params);
		
		// Initialize ImageView for indicating TaskType as Image, Textview for Tasktype as text,
		// product barcode, partbarcode, Task-time, worker respectively.
		
		// Initialize ImageView & TextView for TaskType
		taskTypeImageView1 = new ImageView(context);
		params = new LayoutParams(IMAGE_FIELD_WIDTH, MATCH_PARENT);
		row.addView(taskTypeImageView1, params);
		
		enterpriseTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_MEDIUM - IMAGE_FIELD_WIDTH+10, WRAP_CONTENT);
		enterpriseTextView.setGravity(Gravity.CENTER);
		row.addView(enterpriseTextView, params);
		
		// Initialize Shipment barcode TextView
		shipmentBarcodeTextView = new TextView(context);
		params = new LayoutParams(95, WRAP_CONTENT);
		shipmentBarcodeTextView.setGravity(Gravity.CENTER);
		row.addView(shipmentBarcodeTextView, params);
		
		// Initialize Product barcode TextView
		productBarcodeTextView = new TextView(context);
		params = new LayoutParams(165, WRAP_CONTENT);
		productBarcodeTextView.setGravity(Gravity.CENTER);
		row.addView(productBarcodeTextView, params);
		
		// Initialize Reg date TextView
		regDateTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_LONG, WRAP_CONTENT);
		regDateTextView.setGravity(Gravity.CENTER);
		row.addView(regDateTextView, params);
		
		// Initialize Worker TextView
		workerTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_SHORT, WRAP_CONTENT);
		workerTextView.setGravity(Gravity.CENTER);
		row.addView(workerTextView, params);
		
		// Initialize etc TextView
		etcTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_MEDIUM , WRAP_CONTENT);
		etcTextView.setGravity(Gravity.CENTER);
		row.addView(etcTextView, params);		
		
		borderView = new ImageView(context);
		params = new LayoutParams(MATCH_PARENT, BORDER_HEIGHT);
		this.addView(borderView, params);
	}
	
	public void setRowValues(ShipmentHistoryData historyData){				
	
		inflateChildViews(ROW_HEIGHT);
		
		if (historyData.getEnterpriser().equals("[ASA]")) {
			taskTypeImageView1.setBackgroundColor(0xFFCC723D);
		}else if (historyData.getEnterpriser().equals("[GAMMA]")) {
			taskTypeImageView1.setBackgroundColor(0xFF050099);
		}else if (historyData.getEnterpriser().equals("Amro")) {
			taskTypeImageView1.setBackgroundColor(0xFF747474);
		}else if (historyData.getEnterpriser().equals("[Abacus]")) {
			taskTypeImageView1.setBackgroundColor(0xFF47C83E);
		}else if (historyData.getEnterpriser().equals("[CDM]")) {
			taskTypeImageView1.setBackgroundColor(0xFF99004C);
		}else if (historyData.getEnterpriser().equals("[Gemsys]")) {
			taskTypeImageView1.setBackgroundColor(0xFFF2CB61);
		}else if (historyData.getEnterpriser().equals("[KOTAS]")) {
			taskTypeImageView1.setBackgroundColor(0xFFB2EBF4);
		}else if (historyData.getEnterpriser().equals("[SCS]")) {
			taskTypeImageView1.setBackgroundColor(0xFFA566FF);
		}else if (historyData.getEnterpriser().equals("[Master Team]")) {
			taskTypeImageView1.setBackgroundColor(0xFF005766);
		}else if (historyData.getEnterpriser().equals("[PageAuto]")) {
			taskTypeImageView1.setBackgroundColor(0xFF6B9900);
		}else if (historyData.getEnterpriser().equals("[STREMA]")) {
			taskTypeImageView1.setBackgroundColor(0xFFF15F5F);
		}else if (historyData.getEnterpriser().equals("(NONE)")) {
			taskTypeImageView1.setBackgroundColor(0xFFFF0000);
		}else{
			taskTypeImageView1.setBackgroundResource(R.drawable.recordmanager_specchange_icon);
		}	
		enterpriseTextView.setText(historyData.getEnterpriser().toString());
		this.removeView(repairRow);
		
		shipmentBarcodeTextView.setText(historyData.getShipmentBarcode());
		productBarcodeTextView.setText(historyData.getProudctBarcode());
		regDateTextView.setText(historyData.getRegDate());
		workerTextView.setText(historyData.getWorkerName());
		etcTextView.setText(historyData.getEtc());
	}
}
