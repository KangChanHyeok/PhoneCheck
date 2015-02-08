package com.kch.phonecheck.temp;

import com.kch.phonecheck.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TempTab extends TabActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery_tab);
		
		TabHost.TabSpec spec ;
        // 탭호스트를 참조한다
        TabHost tabhost = this.getTabHost();

        //배터리 상태
        spec = tabhost.newTabSpec("Tab1").setIndicator("Temperature").setContent(new Intent(this, TempMain.class));
        tabhost.addTab(spec);
        
        //배터리 로그
        spec = tabhost.newTabSpec("Tab2").setIndicator("Setting").setContent(new Intent(this, TempWidget.class));
        tabhost.addTab(spec);
        
        
   //     intent = new  Intent( ).setClass(this, BatteryTest.class);
    //    spec = tabhost.newTabSpec("DBSearch").setIndicator("고객조회",res.getDrawable(R.drawable.a3)).setContent(intent);
     //   tabhost.addTab(spec);
        
        tabhost.setCurrentTab(0) ; 
	}
}
