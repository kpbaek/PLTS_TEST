package com.sbmkorea.plts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class BottomHitDetectScrollView extends ScrollView {
	protected Callback __func = null;
	protected boolean isListeningToScrollChange = false;
	
	public boolean isListeningToScrollChange() {
		return isListeningToScrollChange;
	}

	public void setListeningToScrollChange(boolean isListeningToScrollChange) {
		this.isListeningToScrollChange = isListeningToScrollChange;
	}

	public interface Callback {
		void call();
	}
	
	public BottomHitDetectScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BottomHitDetectScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BottomHitDetectScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setCallback(Callback func) {
		this.__func = func;
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getBottom()-(getHeight() + getScrollY())); // Calculate the scrolldiff
        if(diff == 0 && isListeningToScrollChange()){
        	setListeningToScrollChange(false);
        	// if difference is zero, then the bottom has been reached
            Log.e("SCC", "MyScrollView: Bottom has been reached" );
            if (__func != null) {
            	__func.call();
            }
        }
        super.onScrollChanged(x, y, oldx, oldy);
	}
}
