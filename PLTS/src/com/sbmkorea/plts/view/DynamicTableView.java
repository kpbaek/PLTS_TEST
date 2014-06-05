package com.sbmkorea.plts.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sbmkorea.plts.activity.R;
import com.sbmkorea.plts.activity.listener.SynchronizedScrollViewListener;

public class DynamicTableView extends LinearLayout {

	protected Context context;
	
	protected SynchronizedScrollView statusFieldScrollView;
	protected LinearLayout statusFieldLayout;
	protected HorizontalScrollView baseTableHorizontalScrollView;
	protected LinearLayout baseTableLayout;
	protected LinearLayout fieldNameLayout;
	protected SynchronizedScrollView recordsScrollView;
	protected LinearLayout recordsLayout;
	
	private SynchronizedScrollViewListener scrollViewSynchronizer;
	
	private ArrayList<String> fieldNameList = new ArrayList<String>();
	
	protected int rowCount = 0;
	protected int colCount = 0;
	protected RowLayout currentRow;
	protected EditText currentEntry;
	
	protected int successCount = 0;
	
	public EditText getCurrentEntry() {
		return currentEntry;
	}
	
	public int getCurrentRowIndex() {
		return rowCount;
	}
	
	public int getCurrentColumnIndex() {
		return currentRow.getEntryCount();
	}
	
	public LinearLayout getRecordsLayout() {
		return recordsLayout;
	}
	
	public boolean hasNoErrors() {
		return successCount == rowCount - 1;
	}
	
	public int getDimension(int id) {
		return getResources().getDimensionPixelOffset(id);
	}

	private static final int MATCH_PARENT = LayoutParams.MATCH_PARENT;
	private static final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
	private final int ROW_HEIGHT = getDimension(R.dimen.DynamicTable_Row_HEIGHT);
	private final int ENTRY_WIDTH = getDimension(R.dimen.DynamicTable_ENTRY_WIDTH);
	private final int ID_FIELD_WIDTH = getDimension(R.dimen.DynamicTable_ID_FIELD_WIDTH);
	private final int STATUS_FIELD_WIDTH = getDimension(R.dimen.DynamicTable_STATUS_FEILD_WIDTH);
	private final int STATUS_ICON_WIDTH = getDimension(R.dimen.DynamicTable_STATUS_ICON_WIDTH);
	private final int STATUS_ICON_HEIGHT = getDimension(R.dimen.DynamicTable_STATUS_ICON_HEIGHT);
	private final int STATUS_ICON_VERTICAL_MARGIN = getDimension(R.dimen.DynamicTable_STATUS_ICON_VERTICAL_MARGIN);
	
	private final int INNER_HORIZONTAL_MARGIN = getDimension(R.dimen.DynamicTable_STATUS_ICON_VERTICAL_MARGIN);
	private final int INNER_VERTICAL_MARGIN = getDimension(R.dimen.DynamicTable_STATUS_ICON_VERTICAL_MARGIN);

	public static class Status {
		public static final int SUCCESS = 1;
		public static final int ERROR = 2;
		public static final int PROCESSING = 3;
	}

	public DynamicTableView(Context context) {
		super(context);
		this.context = context;
	}

	public DynamicTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@SuppressLint("NewApi")
	public DynamicTableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	public void initialize(ArrayList<String> fieldNameList) {
		this.fieldNameList = fieldNameList;
		
		setChildViews();
		synchronizeScrollView();
	}

	public RowLayout addRow() {
		if (rowCount == 0) {
			addNewColumnFieldWithWidth(ID_FIELD_WIDTH);
		}
		RowLayout row = new RowLayout(context);
		row.initialize(this, ++rowCount);
		LayoutParams params = new LayoutParams(WRAP_CONTENT, ROW_HEIGHT);
		params.setMargins(0, 0, 0, INNER_VERTICAL_MARGIN);
		recordsLayout.addView(row, params);
		
		// add status image
		ImageView iv = new ImageView(context);
		params = new LayoutParams(STATUS_ICON_WIDTH, STATUS_ICON_HEIGHT);
		params.setMargins(0, STATUS_ICON_VERTICAL_MARGIN, 0, STATUS_ICON_VERTICAL_MARGIN);
		statusFieldLayout.addView(iv, params);
		
		currentRow = row;
		return row;
	}
	
	public EditText addEntry() {
		if (currentRow != null) {
			EditText e = currentRow.addEntry();
			if (currentRow.getEntryCount() > colCount - 1) {
				currentRow.setLongest(true);
				addNewColumnField();
			}
			currentEntry = e;
			e.requestFocus();
			return e;
		} else {
			return null;
		}
	}
	
	private void addNewColumnField() {
		addNewColumnFieldWithWidth(ENTRY_WIDTH);
	}
	
	private void addNewColumnFieldWithWidth(int width) {
		TextView tv = new TextView(context);
		LayoutParams params = new LayoutParams(width, MATCH_PARENT);
		params.setMargins(0, 0, INNER_HORIZONTAL_MARGIN, 0);
		tv.setText(fieldNameList.get(colCount++));
		tv.setGravity(Gravity.CENTER);
		fieldNameLayout.addView(tv, params);
	}
	
	public void removeLastEntry() {
		currentRow.removeLastEntry();
		if (currentRow.isLongest()) {
			fieldNameLayout.removeViewAt(fieldNameLayout.getChildCount() - 1);
		}
	}
	
	public ImageView setStatus(int rowId, int status) {
		ImageView iv = (ImageView) statusFieldLayout.getChildAt(rowId - 1);
		switch (status) {
			case Status.SUCCESS:
				iv.setBackgroundResource(R.drawable.status_icon_complete);
				successCount++;
				break;
			case Status.ERROR:
				iv.setBackgroundResource(R.drawable.status_icon_error);
				break;
			case Status.PROCESSING:
				iv.setBackgroundResource(R.drawable.status_icon_processing);
				break;
		}
		return iv;
	}
	
	
	private class RowLayout extends LinearLayout {
		private Context context;
		private int rowId;
		private boolean isLongest = false;
		private int entryCount = 0;
		
		public RowLayout(Context context) {
			super(context);
			this.context = context;
		}

		public RowLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.context = context;
		}

		@SuppressLint("NewApi")
		public RowLayout(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			this.context = context;
		}
		
		public void initialize(DynamicTableView table, int rowId) {
			this.setOrientation(HORIZONTAL);
			this.rowId = rowId;
			
			addIdField();
		}
		
		private void addIdField() {
			EditText e = new EditText(context);
			LayoutParams params = new LayoutParams(ID_FIELD_WIDTH, MATCH_PARENT);
			params.setMargins(0, 0, INNER_HORIZONTAL_MARGIN, 0);
			e.setText(rowId + "");
			e.setEnabled(false);
			e.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
			this.addView(e, params);
		}
		
		public EditText addEntry() {
			EditText e = new EditText(context);
			LayoutParams params = new LayoutParams(ENTRY_WIDTH, MATCH_PARENT);
			params.setMargins(0, 0, INNER_HORIZONTAL_MARGIN, 0);
			e.setPrivateImeOptions("defaultInputmode=english;");
			e.setInputType(InputType.TYPE_CLASS_TEXT);
			e.setGravity(Gravity.BOTTOM);
			this.addView(e, params);
			
			entryCount++;
			return e;
		}
		
		public void removeLastEntry() {
			super.removeViewAt(super.getChildCount() - 1);
			entryCount--;
		}
		
		public boolean isLongest() {
			return isLongest;
		}
		
		public void setLongest(boolean isLongest) {
			this.isLongest = isLongest;
		}
		
		public int getEntryCount() {
			return entryCount;
		}
	}
	
	private void setChildViews() {
		this.setOrientation(LinearLayout.HORIZONTAL);
		
		setStatusFieldScrollView();
		setBaseTableHorizontalScrollView();
	}
	
	private void setStatusFieldScrollView() {
		statusFieldScrollView = new SynchronizedScrollView(context);
		statusFieldScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		statusFieldScrollView.setVerticalScrollBarEnabled(false);
		
		LayoutParams params = new LayoutParams(STATUS_FIELD_WIDTH, MATCH_PARENT);
		params.setMargins(0, ROW_HEIGHT + INNER_VERTICAL_MARGIN, INNER_HORIZONTAL_MARGIN, 0);
		this.addView(statusFieldScrollView, params);
		
		// set inner LinearLayout
		statusFieldLayout = new LinearLayout(context);
		statusFieldLayout.setOrientation(VERTICAL);
		params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
		statusFieldScrollView.addView(statusFieldLayout, params);
	}
	
	private void setBaseTableHorizontalScrollView() {
		baseTableHorizontalScrollView = new HorizontalScrollView(context);
		baseTableHorizontalScrollView.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER);
		LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		this.addView(baseTableHorizontalScrollView, params);
		
		// set inner LinearLayout
		baseTableLayout = new LinearLayout(context);
		baseTableLayout.setOrientation(VERTICAL);
		baseTableHorizontalScrollView.addView(baseTableLayout, params);
		
		setFieldNameLayout();
		setRecordsScrollView();
	}
	
	private void setFieldNameLayout() {
		fieldNameLayout = new LinearLayout(context);
		fieldNameLayout.setOrientation(HORIZONTAL);
		LayoutParams params = new LayoutParams(WRAP_CONTENT, ROW_HEIGHT);
		baseTableLayout.addView(fieldNameLayout, params);
	}
	
	private void setRecordsScrollView() {
		recordsScrollView = new SynchronizedScrollView(context);
		recordsScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		
		LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		baseTableLayout.addView(recordsScrollView, params);
		
		// set inner LinearLayout
		recordsLayout = new LinearLayout(context);
		recordsLayout.setOrientation(VERTICAL);
		recordsScrollView.addView(recordsLayout, params);
	}
	
	private void synchronizeScrollView() {
		scrollViewSynchronizer = new SynchronizedScrollViewListener() {
			
			@Override
			public void onScrollChanged(SynchronizedScrollView synchronizedScrollView,
					int x, int y, int oldx, int oldy) {
				// TODO Auto-generated method stub
				if (synchronizedScrollView == statusFieldScrollView) {
					recordsScrollView.scrollTo(x, y);
				} else if (synchronizedScrollView == recordsScrollView) {
					statusFieldScrollView.scrollTo(x, y);
				}
			}
		};
		statusFieldScrollView.setScrollViewListener(scrollViewSynchronizer);
		recordsScrollView.setScrollViewListener(scrollViewSynchronizer);
	}
	
	
}
