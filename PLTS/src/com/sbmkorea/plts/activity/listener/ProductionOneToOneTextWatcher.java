package com.sbmkorea.plts.activity.listener;

import android.text.Editable;
import android.widget.EditText;

import com.sbmkorea.plts.activity.ProductionOneToOneActivity;
import com.sbmkorea.plts.view.DynamicTableView;

public class ProductionOneToOneTextWatcher implements android.text.TextWatcher {
	public static final int BARCODE_LENGTH = 13;
	public static final String PRODUCT_ID_PREFIX = "2";
	public static final String PART_ID_PREFIX = "1";
	
	private ProductionOneToOneActivity activity;
	private DynamicTableView table;
	private EditText current;

	private EnterDisablingOnKeyListener enterDisablingOnKeyListener;
	
	public ProductionOneToOneTextWatcher(ProductionOneToOneActivity activity, DynamicTableView table) {
		this.table = table;
		this.activity = activity;
		current = new EditText(activity);	// make a temporary current EditText
		enterDisablingOnKeyListener = new EnterDisablingOnKeyListener();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String barcodeAsString = s.toString();
		boolean isValid = false;
		boolean isEndOfRow = false;
		int columnIndex = table.getCurrentColumnIndex();
		
		if (barcodeAsString.length() == BARCODE_LENGTH) {
			if (barcodeAsString.startsWith(PRODUCT_ID_PREFIX)) {
				if (columnIndex == 1) {
					isValid = true;
				} else {
					isValid = false;
				}
			}
			else if (barcodeAsString.startsWith(PART_ID_PREFIX)) {
				if (columnIndex == 1) {
					isValid = false;
				} else {
					isValid = true;
					isEndOfRow = true;
				}
			}
			
			if (isValid) {
				if (isEndOfRow) {
					// order here is very important.
					int rowId = table.getCurrentRowIndex();
					table.setStatus(rowId, DynamicTableView.Status.PROCESSING);
					activity.uploadBarcodeInfo(rowId);
					
					table.addRow();
				}
				table.addEntry();
				watch(table.getCurrentEntry());
			} else {
				current.setText("");
				current.requestFocus();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	public void watch(EditText nextEditText) {
		current.setEnabled(false);
		current.removeTextChangedListener(this);
		current.setOnKeyListener(null);

		current = nextEditText;
		current.addTextChangedListener(this);
		current.setOnKeyListener(enterDisablingOnKeyListener);
		current.requestFocus();
	}
}
