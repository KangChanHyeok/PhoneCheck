package com.kch.phonecheck.temp;

import com.kch.phonecheck.R.color;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TempReceiver extends BroadcastReceiver{
	private CircularProgressBar cpu_progress, battery_progress;
    private GetTemp getTemp;
    private SharedPreferences pref;
    private TextView cf, cb;
    private ImageView img;
    //해당 브로드캐스트는 부팅과 동시에 시작되는 리시버도 함께하므로 생성자에 파라미터가 없는것도 만들어줘야함.
    public TempReceiver() {
		// TODO Auto-generated constructor stub
	}
	public TempReceiver(CircularProgressBar cpu_progress, CircularProgressBar battery_progress,
			TextView cf, TextView cb, ImageView img) {
		// TODO Auto-generated constructor stub
		this.cpu_progress = cpu_progress;
		this.battery_progress=battery_progress;
		this.cf=cf;
		this.cb=cb;
		this.img=img;
		getTemp=new GetTemp();
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        if(context.getClass().getName().equals("com.kch.phonecheck.temp.TempMain")){
        	
        	int bat_temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        	cpu_progress.setTitle(getTemp.getC_CpuTemp()+"℃");
        	cpu_progress.setSubTitle(getTemp.getF_CpuTemp()+"℉");
        	cpu_progress.setProgress(getTemp.getC_CpuTemp());
        	
        	battery_progress.setTitle(getTemp.getC_BatteryTemp(bat_temp)+"℃");
        	battery_progress.setSubTitle(getTemp.getF_BatteryTemp(bat_temp)+"℉");
        	battery_progress.setProgress(getTemp.getC_BatteryTemp(bat_temp));
        	
        	pref = PreferenceManager.getDefaultSharedPreferences(context);
    		String CF=pref.getString("CF", "C");
    		String CB=pref.getString("CB", "Cpu Temp");
    		if(CF.equals("C")){
    			cf.setText("℃");
    		}else{
    			cf.setText("℉");
    		}
    		cb.setText(CB);
    		int prefColor=Integer.parseInt(pref.getString("preList", "0"));
    		setColor(img, prefColor);
    		
        }
	}
	private void setColor(ImageView img, int prefColor){
    	switch (prefColor) {
		case 0:
			img.setImageResource(color.white);
			break;
		case 1:
			img.setImageResource(color.red);
			break;
		case 2:
			img.setImageResource(color.green);
			break;	
		case 3:
			img.setImageResource(color.blue);
			break;			
		case 4:
			img.setImageResource(color.black);
			break;					
		}
	}
}
