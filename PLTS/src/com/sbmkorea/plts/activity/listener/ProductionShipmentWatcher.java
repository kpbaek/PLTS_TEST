package com.sbmkorea.plts.activity.listener;

import android.text.Editable;
import android.widget.EditText;

import com.sbmkorea.plts.activity.ProductionShipmentActivity;
import com.sbmkorea.plts.view.DynamicTableView;

public class ProductionShipmentWatcher implements android.text.TextWatcher {
	public static final int BARCODE_LENGTH = 13;
	public static final int SHIPMENT_LENGTH = 7;
	public static final String PRODUCT_ID_PREFIX = "2";
	
	private ProductionShipmentActivity activity;
	private DynamicTableView table;
	private EditText current;
	private boolean isshipmentflag;

	private EnterDisablingOnKeyListener enterDisablingOnKeyListener;
	
	public ProductionShipmentWatcher(ProductionShipmentActivity activity, DynamicTableView table) {
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
		
		if(isshipmentflag == false)
		{
			if (barcodeAsString.length() == BARCODE_LENGTH) {
				if (barcodeAsString.startsWith(PRODUCT_ID_PREFIX)) {
					if (columnIndex == 1) {
						isValid = true;
						isshipmentflag = true;
					} else {
						isValid = false;
					}
				}
				
				System.out.printf("%d\n",columnIndex);
				
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
		else if(isshipmentflag == true)
		{
			if (barcodeAsString.length() == SHIPMENT_LENGTH) {				
				if (columnIndex == 1) {
					isValid = false;
				} else {
					isValid = true;
					isEndOfRow = true;
					isshipmentflag = false;
				}
				
				System.out.printf("%d\n",columnIndex);
				
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

	public boolean isIsshipmentflag() {
		return isshipmentflag;
	}

	public void setIsshipmentflag(boolean isshipmentflag) {
		this.isshipmentflag = isshipmentflag;
	}
}
