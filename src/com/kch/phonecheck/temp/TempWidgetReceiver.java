package com.kch.phonecheck.temp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.preference.PreferenceManager;

public class TempWidgetReceiver extends BroadcastReceiver{
	private CircularProgressBar progress;
    private SharedPreferences pref;
    private GetTemp getTemp;
    //해당 브로드캐스트는 부팅과 동시에 시작되는 리시버도 함께하므로 생성자에 파라미터가 없는것도 만들어줘야함.
    public TempWidgetReceiver() {
		// TODO Auto-generated constructor stub
	}
	public TempWidgetReceiver(CircularProgressBar progress) {
		// TODO Auto-generated constructor stub
		this.progress = progress;
		getTemp = new GetTemp();
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean ad=pref.getBoolean("tempchk", false);	
		String CF=pref.getString("CF", "C");
		String CB=pref.getString("CB", "Cpu Temp");
		
		// TODO Auto-generated method stub        
		if(!ad){
			return;
		}else{
	        if(context.getClass().getName().equals("com.kch.phonecheck.temp.TempAlwaysTop")){	 
	        	int bat_temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
	        	int temp=0;
	        	if(CF.equals("F")){
	        		if(CB.equals("Cpu Temp")){	        			
	        			temp=getTemp.getF_CpuTemp();	        			
	        		}else{
	        			temp=getTemp.getF_BatteryTemp(bat_temp);
	        		}
	        	}else{
	        		if(CB.equals("Cpu Temp")){
	        			temp=getTemp.getC_CpuTemp();
	        		}else{
	        			temp=getTemp.getC_BatteryTemp(bat_temp);
	        		}
	        	}
	        	progress.setProgress(temp);
	        	progress.setTitle(temp+"");	        	
	        	int prefColor=Integer.parseInt(pref.getString("preList", "0"));
	        	switch (prefColor) {
				case 0:
					progress.setTitleColor(Color.WHITE);
					break;
				case 1:
					progress.setTitleColor(Color.RED);
					break;
				case 2:
					progress.setTitleColor(Color.parseColor("#bada55"));
					break;	
				case 3:
					progress.setTitleColor(Color.BLUE);
					break;			
				case 4:
					progress.setTitleColor(Color.BLACK);
					break;					
				}
	        	TempMax(CF, temp, prefColor);
	        }

			if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && ad){
				context.startService(new Intent(context, TempAlwaysTop.class));			
			}
		}
	}
	
	private void TempMax(String CF, int temp, int prefColor){
		if(CF.equals("F")){
			progress.setMax(212);
			if(temp>140){
				progress.setDrawColor(Color.RED);
			}else{
				progress.setDrawColor(Color.parseColor("#bada55"));
			}
		}else{
			progress.setMax(100);
			if(temp>60){
				progress.setDrawColor(Color.RED);
			}else{
				progress.setDrawColor(Color.parseColor("#bada55"));
			}
		}
	}
}
