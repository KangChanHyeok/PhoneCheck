package com.kch.phonecheck.temp;

import com.kch.phonecheck.R;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TempMain extends Activity{

	private TempReceiver receiver;
	private TextView cf, cb;
	private ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tempmain);
		
		CircularProgressBar cpu=(CircularProgressBar)findViewById(R.id.temp_main_cputemp);
		CircularProgressBar bat=(CircularProgressBar)findViewById(R.id.temp_main_battemp);
		cf=(TextView)findViewById(R.id.temp_main_cf);
		cb=(TextView)findViewById(R.id.temp_main_cb);
		img=(ImageView)findViewById(R.id.temp_main_imgcolor);
		receiver = new TempReceiver(cpu, bat, cf, cb, img);
		registerReceiver(
				receiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
				);
	}
	@Override
	protected void onPause(){
		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(
				receiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
				);
	}
}
