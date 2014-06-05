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
import com.sbmkorea.plts.data.PartHistoryData;

public class PartHistoryContentView extends LinearLayout {
	
	protected Context context;

	private ImageView borderView;
	public LinearLayout row;
	public TextView partBarcodeTextView;
	public TextView regDateTextView;
	public TextView workerTextView;
	private TextView remarkTextView;
	
	// Initialize custom LinearLayout
	
	public PartHistoryContentView(Context context) {
		super(context);
		this.context = context;
		initialize();
	}
	
	public PartHistoryContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initialize();
	}

	@SuppressLint("NewApi")
	public PartHistoryContentView(Context context, AttributeSet attrs, int defStyle) {
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
	private final int TEXTVIEW_WIDTH_LONG = getDimension(R.dimen.ProductHistoryActivity_TextView_WIDTH_LONG);
	
	private final int REMARK_FIELD_WIDTH = getDimension(R.dimen.PartHistoryActivity_REMARK_FIELD_WIDTH);
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
		
		// Initialize Part barcode TextView
		partBarcodeTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_LONG, WRAP_CONTENT);
		partBarcodeTextView.setGravity(Gravity.CENTER);
		row.addView(partBarcodeTextView, params);
		
		remarkTextView = new TextView(context);
		params = new LayoutParams(REMARK_FIELD_WIDTH, WRAP_CONTENT);
		remarkTextView.setGravity(Gravity.CENTER_VERTICAL);
		row.addView(remarkTextView, params);
		
		// Initialize Worker TextView
		workerTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_SHORT, WRAP_CONTENT);
		workerTextView.setGravity(Gravity.CENTER);
		row.addView(workerTextView, params);
		
		// Initialize Reg date TextView
		regDateTextView = new TextView(context);
		params = new LayoutParams(TEXTVIEW_WIDTH_LONG, WRAP_CONTENT);
		regDateTextView.setGravity(Gravity.CENTER);
		row.addView(regDateTextView, params);
	}
	
	public void setRowValues(PartHistoryData historyData){
		inflateChildViews(ROW_HEIGHT);
		partBarcodeTextView.setText(historyData.getPartBarcode());
		remarkTextView.setText(historyData.getRemarks());
		workerTextView.setText(historyData.getWorkerName());
		regDateTextView.setText(historyData.getRegDate());
	}
}
