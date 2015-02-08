package com.kch.phonecheck.battery;

import java.util.ArrayList;

import com.kch.phonecheck.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BatteryChangeReceiver extends BroadcastReceiver {

	private ListView listview;
    //----------------------------------------------------------------------------------
    // 브로드캐스트 리시버로 받을 배터리 정보들

    private int health;
    private int plugged;
    private int level;
    private int status;
    private int temperature;
    private int voltage;
	private String Tech;
	private Context context;
	private ArrayList<String> list;
	private String value[];
	

    //----------------------------------------------------------------------------------
    // 생성자. 액티비티와 텍스트 뷰는 반드시 설정하도록 하였습니다.

    public BatteryChangeReceiver(ListView listview, String value[]) {
        this.listview=listview;        
        this.value=value;
    }

    //----------------------------------------------------------------------------------
    // 배터리 정보 수신을 처리

    @Override
    public void onReceive(Context context, Intent intent) {
    	this.context=context;
        //------------------------------------------------------------------------------
        // 인텐트트로부터 수행할 액션이 ACTION_BATTERY_CHANGED인지 확인합니다.
        if (intent.getAction() != Intent.ACTION_BATTERY_CHANGED) {
        	return;
        }
            //------------------------------------------------------------------------------
            // 인텐트로부터 배터리 정보를 구합니다.

            health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);            
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            Tech=intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);            

            //------------------------------------------------------------------------------
            // 메인 액티비티에 보여줄 텍스트의 내용을 만듭니다.
	        list=getBatteryStatus();
	        BatteryAdapter adapter = new BatteryAdapter(context, R.layout.info_content, list, value);
	        listview.setAdapter(adapter);
            
	        
            //------------------------------------------------------------------------------
            // 메인 액티비티에 배터리 정보를 보여 줍니다.

    }
    
	private class BatteryAdapter extends ArrayAdapter<String>{
		
		private ArrayList<String> list;
		private String battery_title[];
		public BatteryAdapter(Context context, int textViewResourceId,
				ArrayList<String> list, String[] battery_title) {
			super(context, textViewResourceId, list);
			// TODO Auto-generated constructor stub
			this.battery_title=battery_title;
			this.list=list;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
	        View v = convertView;
	        TextView txt_title;
	        TextView txt_content;
	        if (v == null) {	        	
	            LayoutInflater vi = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.info_content, null);
	        }
	        txt_title=(TextView)v.findViewById(R.id.sys_title);
	        txt_content=(TextView)v.findViewById(R.id.sys_content);
	        txt_title.setText(battery_title[position]);	        
	        txt_content.setText(list.get(position));
	        return v;
		}		
	}

    public ArrayList<String> getBatteryStatus(){
    	list=new ArrayList<String>();
    	list.add(getBatteryHealth(health));
    	list.add(level+"%");
    	list.add(getBatteryPlugged(plugged));
    	list.add(getBatteryStatus(status));
    	list.add(temperature/10+"℃");
    	list.add(Tech);
    	list.add(voltage+"mv");
    	list.add(getBatteryCapacity()+" mAh");
    	return list;
    }
    
    public int getBatteryCapacity() {

        Object mPowerProfile_ = null;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(context);

        } catch (Exception e) {

            // Class not found?
            e.printStackTrace();
        }

        try {

            // Invoke PowerProfile method "getAveragePower" with param
            // "battery.capacity"
            batteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

        } catch (Exception e) {

            // Something went wrong
            e.printStackTrace();
        }

        return (int) batteryCapacity;
    }

    //----------------------------------------------------------------------------------
    // health 필드의 값을 사람이 이해할 수 있는 문자열을 리턴

    private String getBatteryHealth(int health) {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return "Good";
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return "Dead";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return "Over voltage";
            case BatteryManager.BATTERY_HEALTH_COLD:
                return "Cold";
        }
        
        return "Unknown";
    }

    //----------------------------------------------------------------------------------
    // plugged 필드의 값을 사람이 이해할 수 있는 문자열을 리턴
    
    private String getBatteryPlugged(int plugged) {
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                return "AC";
            case BatteryManager.BATTERY_PLUGGED_USB:
                return "USB";
        }
        
        return "Not plugged";
    }

    //----------------------------------------------------------------------------------
    // present 필드의 값을 사람이 이해할 수 있는 문자열을 리턴


    //----------------------------------------------------------------------------------
    // status 필드의 값을 사람이 이해할 수 있는 문자열을 리턴

    private String getBatteryStatus(int status) {
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "Charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return "Discharging";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "Full";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "Not charging";
        }

        return "Unknown";
    }

	@Override
	protected void finalize() throws Throwable {
		this.listview = null;

		//------------------------------------------------------------------------------

		super.finalize();
	}

}
