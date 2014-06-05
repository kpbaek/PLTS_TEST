package com.sbmkorea.plts.activity.listener;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class EnterDisablingOnKeyListener implements OnKeyListener {

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
			return true;
		return false;
	}

}
