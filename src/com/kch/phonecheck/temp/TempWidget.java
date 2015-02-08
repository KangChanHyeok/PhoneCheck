package com.kch.phonecheck.temp;

import com.kch.phonecheck.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class TempWidget extends PreferenceActivity{

	private Boolean TempChk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		CheckBoxPreference checktemp = (CheckBoxPreference)findPreference("tempchk");
		checktemp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				TempChk = preference.getSharedPreferences().getBoolean("tempchk", false);
                if(TempChk){
                	startService(new Intent(TempWidget.this, TempAlwaysTop.class));
                }else{
                	stopService(new Intent(TempWidget.this, TempAlwaysTop.class));
                }
                return false;
			}
		});
	}
}
