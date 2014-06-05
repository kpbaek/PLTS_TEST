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
import com.sbmkorea.plts.data.ProductHistoryData;

public class ProductHistoryContentView extends LinearLayout {
	
	protected Context context;

	private ImageView borderView;
	public LinearLayout row;
	public LinearLayout repairRow;
	public ImageView taskTypeImageView1;
	public ImageView taskTypeImageView2;
	public TextView taskTypeTextView;
	public TextView productBarcodeTextView;
	public TextView partBarcodeTextView;
	public TextView regDateTextView;
	public TextView workerTextView;
	private TextView errorTypeTextView;
	private TextView remarkTextView;
	
	// Initialize custom LinearLayout
	
	public ProductHistoryContentView(Context context) {
		super(context);
		this.context = context;
		initialize();
	}
	
	public ProductHistoryContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initialize();
	}

	@SuppressLint("NewApi")
	public ProductHistoryContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initialize();
	}
	
	public int getDimension(int id) {
		return getResources().getDimensionPixelOffset(id);
	}

	private final int MATCH_PARENT = LayoutParams.MATCH_PARENT;
	private final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
	private final int ROW_HEIGHT = getDimension(R.dimen.ProductHistoryActivity_Row_HEIGHT);
	private final int TEXTVIEW_WIDTH_SHORT = getDimension(R.dimen.ProductHistoryActivity_TextView_WIDTH_SHORT);
	private final int TEXTVIEW_WIDTH_MEDIUM = getDimension(R.dimen.ProductHistoryActivity_TextView_WIDTH_MEDIUM);
	private final int TEXTVIEW_WIDTH_LONG = getDimension(R.dimen.ProductHistoryActivity_TextView_WIDTH_LONG);
	
	private final int IMAGE_FIELD_WIDTH = getDimension(R.dimen.ProductHistoryActivity_IMAGE_FIELD_WIDTH);
	private final int REMARK_FIELD_WIDTH = getDimension(R.dimen.ProductHistoryActivity_REMARK_FIELD_WIDTH);
	private final int BORDER_HEIGHT = getDimension(R.dimen.ProductHistoryActivity_BORDER_HEIGHT);
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
		
		taskTypeTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_MEDIUM - IMAGE_FIELD_WIDTH, WRAP_CONTENT);
		taskTypeTextView.setGravity(Gravity.CENTER);
		row.addView(taskTypeTextView, params);
		
		// Initialize product barcode TextView
		productBarcodeTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_LONG, WRAP_CONTENT);
		productBarcodeTextView.setGravity(Gravity.CENTER);
		row.addView(productBarcodeTextView, params);
		
		// Initialize Part barcode TextView
		partBarcodeTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_LONG, WRAP_CONTENT);
		partBarcodeTextView.setGravity(Gravity.CENTER);
		row.addView(partBarcodeTextView, params);
		
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
		
		// Initialize second record row - record Repair ONLY DATA
		repairRow = new LinearLayout(context);
		repairRow.setOrientation(LinearLayout.HORIZONTAL);
		params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		this.addView(repairRow, params);
		
		taskTypeImageView2 = new ImageView(context);
		params = new LayoutParams(IMAGE_FIELD_WIDTH, MATCH_PARENT);
		repairRow.addView(taskTypeImageView2, params);
		
		// Initialize ErrorType textView
		errorTypeTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_MEDIUM, WRAP_CONTENT);
		errorTypeTextView.setGravity(Gravity.CENTER);
		repairRow.addView(errorTypeTextView, params);
		
		remarkTextView = new TextView(context);
		remarkTextView.setBackgroundColor(Color.WHITE);
		params = new LayoutParams(REMARK_FIELD_WIDTH, WRAP_CONTENT);
		remarkTextView.setGravity(Gravity.CENTER_VERTICAL);
		repairRow.addView(remarkTextView, params);
		
		borderView = new ImageView(context);
		params = new LayoutParams(MATCH_PARENT, BORDER_HEIGHT);
		this.addView(borderView, params);
	}
	
	public void setRowValues(ProductHistoryData historyData){
		if (historyData.getTaskType().equals("production")) {
			inflateChildViews(ROW_HEIGHT);
			taskTypeImageView1.setBackgroundColor(0xFF95D127);
			taskTypeTextView.setText(R.string.ProductHistoryActivity_Production_taskTypeText);
			this.removeView(repairRow);
		}else if (historyData.getTaskType().equals("repair")) {
			inflateChildViews(WRAP_CONTENT);
			taskTypeImageView1.setBackgroundResource(R.drawable.recordmanager_repair_icon);
			taskTypeImageView2.setBackgroundResource(R.drawable.recordmanager_repair_icon);
			taskTypeTextView.setText(R.string.ProductHistoryActivity_Repair_taskTypeText);
			// TODO: errortype handling
			errorTypeTextView.setText(historyData.getErrorType());
			remarkTextView.setText(historyData.getRemarks());
		}else{
			inflateChildViews(ROW_HEIGHT);
			taskTypeImageView1.setBackgroundResource(R.drawable.recordmanager_specchange_icon);
			taskTypeTextView.setText(R.string.ProductHistoryActivity_SpecChange_taskTypeText);
			this.removeView(repairRow);
		}
		
		productBarcodeTextView.setText(historyData.getProudctBarcode());
		partBarcodeTextView.setText(historyData.getPartBarcode());
		regDateTextView.setText(historyData.getRegDate());
		workerTextView.setText(historyData.getWorkerName());
	}
}
