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
			//		Toast.makeText(getApplicationContext(), "��ġ��",
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
				 * params.gravity�� ���� ��ȣ ����
				 * 
				 * LEFT : x�� +
				 * 
				 * RIGHT : x�� -
				 * 
				 * TOP : y�� +
				 * 
				 * BOTTOM : y�� -
				 */
				params.x = mViewX + x;
				params.y = mViewY + y;

				wm.updateViewLayout(layview, params);

				break;
			}

			return true;
		}
	};
	//�׻� ���̰� �� ��
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
		//�ֻ��� �����쿡 �ֱ� ���� ����
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, //�׻� �ֻ���
				1288, //��Ŀ���� ���� ����
				PixelFormat.RGBA_8888);			//����					
		
		/*
		 *  TYPE�� TYPE_PHONE���� �����ϸ� ��ġ �̺�Ʈ�� ���� �� �ִ�. 
		 *  ������ Status bar �����θ� Ȱ�밡���ϰ� �䰡 Focus�� ������ �־� ���� �ǵ���� �� �̿��� �κп� ��ġ�� �ϸ� 
		 *  �ٸ� ���� ��ġ�� ����ؾ� �ϴµ� �̰��� �Ұ��� �ϰ� Ű �̺�Ʈ ���� �Ծ� ������.
		 *   �̰��� �ذ��ϱ� ���� FLAG ������ FLAG_NOT_FOCUSABLE�� �ָ� �䰡 ��Ŀ���� ������ �ʾ� �� �̿��� 
		 *   �κ��� ��ġ �̺�Ʈ�� Ű �̺�Ʈ�� ���� �ʾƼ� �ڿ������� ������ �� �ְ� �ȴ�.
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
		
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);	//������ �Ŵ��� �ҷ���.
		wm.addView(layview, params);		//�ֻ��� �����쿡 �� �ֱ�. *�߿� : ���⿡ �۹̼���  
									//������ �ξ�� �Ѵ�. �Ŵ��佺Ʈ�� 
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cir != null)		//���� ����� �� ����. *�߿� : �並 �� ���� �ؾ���.
		{
			
			unregisterReceiver(receiver);
			((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(layview);
			cir = null;
		}
	}
}