package com.kch.phonecheck;



import java.util.ArrayList;

import com.kch.phonecheck.battery.BatteryTest;
import com.kch.phonecheck.gps.GPSTest;
import com.kch.phonecheck.lcd.LCDTest;
import com.kch.phonecheck.multitouch.MultiTouch;
import com.kch.phonecheck.system.SystemInformation;
import com.kch.phonecheck.temp.TempAlwaysTop;
import com.kch.phonecheck.temp.TempTab;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView listview=(ListView)findViewById(android.R.id.list);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ad=pref.getBoolean("tempchk", false);	
        if(ad){
        	startService(new Intent(MainActivity.this, TempAlwaysTop.class));
        }else{
        	stopService(new Intent(MainActivity.this, TempAlwaysTop.class));
        }
        
		String title[]=getResources().getStringArray(R.array.title);
		String content[]=getResources().getStringArray(R.array.content);
		int image[]={R.drawable.screen, R.drawable.multi_touch, R.drawable.gps, R.drawable.battery, R.drawable.temp, R.drawable.device};

		ArrayList<MainListVo> list=new ArrayList<MainListVo>();
		for(int i=0; i<getResources().getStringArray(R.array.title).length; i++){			
			list.add(new MainListVo(image[i], title[i], content[i]));
		}	
		MainListAdapter m_adapter = new MainListAdapter(this, R.layout.main_view, list); 
	   // 어댑터를 생성합니다.
	    listview.setAdapter(m_adapter);
	    listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent;
					switch (position) {
					case 0:
						intent = new Intent(MainActivity.this, LCDTest.class);		
						startActivity(intent);
						break;
					case 1:
						intent = new Intent(MainActivity.this, MultiTouch.class);
						startActivity(intent);
						break;
					case 2:
						LocationManager LocMan = (LocationManager)getSystemService(LOCATION_SERVICE);
						if (!LocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)){
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  
							builder.setMessage("위치정보를 찾을수 없습니다\nGPS 설정창으로 이동하시겠습니까?");
							builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {								
									
								}
							});
							builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
									
								}
							});
							builder.create();
							builder.show();
						}else{
							intent=new Intent(MainActivity.this, GPSTest.class);
							startActivity(intent);
						}
						break;
					case 3:
						intent = new Intent(parent.getContext(), BatteryTest.class);		
						startActivity(intent);						
						break;
					case 4:
						intent = new Intent(parent.getContext(), TempTab.class);		
						startActivity(intent);						
						break;		
					case 5:
						intent = new Intent(parent.getContext(), SystemInformation.class);		
						startActivity(intent);		
					}
				}
	        	
			});	        	           
	}
}
