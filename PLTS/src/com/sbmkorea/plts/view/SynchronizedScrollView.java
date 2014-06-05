package com.sbmkorea.plts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.sbmkorea.plts.activity.listener.SynchronizedScrollViewListener;

public class SynchronizedScrollView extends ScrollView {
	
	private SynchronizedScrollViewListener scrollViewListener;

	public SynchronizedScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SynchronizedScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SynchronizedScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setScrollViewListener(SynchronizedScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}
