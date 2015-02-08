package com.kch.phonecheck.battery;

import com.kch.phonecheck.R;

import android.app.ListActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

public class BatteryTest extends ListActivity {

	private BatteryChangeReceiver batteryChangeReceiver;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery_test);
		listview=(ListView)findViewById(android.R.id.list);
		String value[]=getResources().getStringArray(R.array.battery_title);

		//----------------------------------------------------------------------------------
		// 뷰(들)에 접근
		batteryChangeReceiver = new BatteryChangeReceiver(listview, value);

		//----------------------------------------------------------------------------------
		// 변경된 배터리 정보를 받는 브로드캐스트 리시버를 생성
		registerReceiver(
				batteryChangeReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
				);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(batteryChangeReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(
				batteryChangeReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
				);
	}
}
