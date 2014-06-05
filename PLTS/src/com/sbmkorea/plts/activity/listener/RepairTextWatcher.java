package com.sbmkorea.plts.activity.listener;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sbmkorea.plts.activity.R;
import com.sbmkorea.plts.activity.RepairMainActivity;

public class RepairTextWatcher extends BarcodeTextWatcher {
	
	RepairMainActivity repairMainActivity;
	private EnterDisablingOnKeyListener enterDisablingOnKeyListener;
	private EditText current;
	private View next;
	
	public String getBarcode() {
		return current.getText().toString();
	}
	
	public RepairTextWatcher(RepairMainActivity repairMainActivity, EditText current, View next) {
		this.repairMainActivity = repairMainActivity;
		this.current = current;
		this.next = next;
		enterDisablingOnKeyListener = new EnterDisablingOnKeyListener();
		
		current.setOnKeyListener(enterDisablingOnKeyListener);
		
	}
	public String PrefixChecker(EditText current){
		switch (current.getId()){
			case R.id.RepairMainActivity_product_EditText:
				return "2";
		case R.id.RepairMainActivity_part_EditText:
				return "1";	
		}
		return null;	
	}
	
	
	@Override
	public void afterTextChanged(Editable s) {
		String barcodeAsString = s.toString();
		if(barcodeAsString.length()==BARCODE_LENGTH) {
			if(barcodeAsString.startsWith(PrefixChecker(current))) {

				current.setEnabled(false);
				if (PrefixChecker(current)=="1"){
					// For case of part-Barcode validation, Also need to activate spinner
					this.repairMainActivity.findViewById(R.id.RepairMainActivity_errortype_Spinner).setEnabled(true);
				}
				next.setEnabled(true);
				next.requestFocus();
				current.removeTextChangedListener(this);
				
			}
			else {
				String errorMessage = this.repairMainActivity.getString(R.string.RepairMainActivity_toastMsg_invalidBarcode);
				Toast toast = Toast.makeText(this.repairMainActivity, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
				current.setText("");
				current.requestFocus();			
			}
		}				
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
