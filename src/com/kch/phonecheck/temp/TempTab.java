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
        // ��ȣ��Ʈ�� �����Ѵ�
        TabHost tabhost = this.getTabHost();

        //���͸� ����
        spec = tabhost.newTabSpec("Tab1").setIndicator("Temperature").setContent(new Intent(this, TempMain.class));
        tabhost.addTab(spec);
        
        //���͸� �α�
        spec = tabhost.newTabSpec("Tab2").setIndicator("Setting").setContent(new Intent(this, TempWidget.class));
        tabhost.addTab(spec);
        
        
   //     intent = new  Intent( ).setClass(this, BatteryTest.class);
    //    spec = tabhost.newTabSpec("DBSearch").setIndicator("����ȸ",res.getDrawable(R.drawable.a3)).setContent(intent);
     //   tabhost.addTab(spec);
        
        tabhost.setCurrentTab(0) ; 
	}
}
