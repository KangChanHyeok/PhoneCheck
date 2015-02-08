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
		// ��(��)�� ����
		batteryChangeReceiver = new BatteryChangeReceiver(listview, value);

		//----------------------------------------------------------------------------------
		// ����� ���͸� ������ �޴� ��ε�ĳ��Ʈ ���ù��� ����
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
