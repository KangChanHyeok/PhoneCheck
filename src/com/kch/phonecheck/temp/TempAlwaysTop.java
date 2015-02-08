package com.kch.phonecheck.temp;

import com.kch.phonecheck.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class TempAlwaysTop extends Service {
	
	private WindowManager wm;
	private WindowManager.LayoutParams params;
	private float mTouchX, mTouchY;
	private int mViewX, mViewY;

	private CircularProgressBar cir;
	private boolean isMove = false;
	private TempWidgetReceiver receiver;
	private LinearLayout layview;
	private View view;
	private SharedPreferences pref;
	private SharedPreferences.Editor edit;
	private OnTouchListener mViewTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isMove = false;

				mTouchX = event.getRawX();
				mTouchY = event.getRawY();
				mViewX = params.x;
				mViewY = params.y;
				break;

			case MotionEvent.ACTION_UP:
				if (!isMove) {
			//		Toast.makeText(getApplicationContext(), "터치됨",
			//				Toast.LENGTH_SHORT).show();
				}
				edit.putInt("moveX", params.x);
				edit.putInt("moveY", params.y);
				edit.commit();
				break;

			case MotionEvent.ACTION_MOVE:
				isMove = true;

				int x = (int) (event.getRawX() - mTouchX);
				int y = (int) (event.getRawY() - mTouchY);

				final int num = 5;
				if ((x > -num && x < num) && (y > -num && y < num)) {
					isMove = false;
					break;
				}

				/**
				 * params.gravity에 따른 부호 변경
				 * 
				 * LEFT : x가 +
				 * 
				 * RIGHT : x가 -
				 * 
				 * TOP : y가 +
				 * 
				 * BOTTOM : y가 -
				 */
				params.x = mViewX + x;
				params.y = mViewY + y;

				wm.updateViewLayout(layview, params);

				break;
			}

			return true;
		}
	};
	//항상 보이게 할 뷰
	@Override
	public IBinder onBind(Intent arg0) { return null; }

	@Override
	public void onCreate() {
		super.onCreate();
		//int statusBarHeight = (int) Math.ceil(25 * getResources().getDisplayMetrics().density);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_progress, null);        
        layview=(LinearLayout)view.findViewById(R.id.circular_layout);
        cir = (CircularProgressBar)view.findViewById(R.id.circularprogressbar);
        cir.setTitleSize(30);
		receiver=new TempWidgetReceiver(cir);
		registerReceiver(
				receiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
				);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		layview.setOnTouchListener(mViewTouchListener);
		//최상위 윈도우에 넣기 위한 설정
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, //항상 최상위
				1288, //포커스를 갖지 않음
				PixelFormat.RGBA_8888);			//투명					
		
		/*
		 *  TYPE을 TYPE_PHONE으로 설정하면 터치 이벤트를 받을 수 있다. 
		 *  하지만 Status bar 밑으로만 활용가능하고 뷰가 Focus를 가지고 있어 원래 의도대로 뷰 이외의 부분에 터치를 하면 
		 *  다른 앱이 터치를 사용해야 하는데 이것이 불가능 하고 키 이벤트 까지 먹어 버린다.
		 *   이것을 해결하기 위해 FLAG 값으로 FLAG_NOT_FOCUSABLE을 주면 뷰가 포커스를 가지지 않아 뷰 이외의 
		 *   부분의 터치 이벤트와 키 이벤트를 먹지 않아서 자연스럽게 동작할 수 있게 된다.
		 */
		
		//	
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		edit=pref.edit();
		int moveX=pref.getInt("moveX", 0);
		int moveY=pref.getInt("moveY", 0);
		params.x=moveX;
		params.y=moveY;
		if(moveX==0 && moveY==0){
			params.x=-550;
			params.y=-950;
		}
		
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);	//윈도우 매니저 불러옴.
		wm.addView(layview, params);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 퍼미션을  
									//설정해 두어야 한다. 매니페스트에 
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cir != null)		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
		{
			
			unregisterReceiver(receiver);
			((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(layview);
			cir = null;
		}
	}
}