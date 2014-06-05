package com.sbmkorea.plts.activity.listener;

import android.text.TextWatcher;

public abstract class BarcodeTextWatcher implements TextWatcher {
	public static final String PRODUCT_ID_PREFIX = "2";
	public static final String PART_ID_PREFIX = "1";
	
	public static final int BARCODE_LENGTH = 13;
	public static final int SHIPMENT_LENGTH = 7;
}
