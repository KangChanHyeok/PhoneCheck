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
    // ��ε�ĳ��Ʈ ���ù��� ���� ���͸� ������

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
    // ������. ��Ƽ��Ƽ�� �ؽ�Ʈ ��� �ݵ�� �����ϵ��� �Ͽ����ϴ�.

    public BatteryChangeReceiver(ListView listview, String value[]) {
        this.listview=listview;        
        this.value=value;
    }

    //----------------------------------------------------------------------------------
    // ���͸� ���� ������ ó��

    @Override
    public void onReceive(Context context, Intent intent) {
    	this.context=context;
        //------------------------------------------------------------------------------
        // ����ƮƮ�κ��� ������ �׼��� ACTION_BATTERY_CHANGED���� Ȯ���մϴ�.
        if (intent.getAction() != Intent.ACTION_BATTERY_CHANGED) {
        	return;
        }
            //------------------------------------------------------------------------------
            // ����Ʈ�κ��� ���͸� ������ ���մϴ�.

            health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);            
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            Tech=intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);            

            //------------------------------------------------------------------------------
            // ���� ��Ƽ��Ƽ�� ������ �ؽ�Ʈ�� ������ ����ϴ�.
	        list=getBatteryStatus();
	        BatteryAdapter adapter = new BatteryAdapter(context, R.layout.info_content, list, value);
	        listview.setAdapter(adapter);
            
	        
            //------------------------------------------------------------------------------
            // ���� ��Ƽ��Ƽ�� ���͸� ������ ���� �ݴϴ�.

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
    	list.add(temperature/10+"��");
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
    // health �ʵ��� ���� ����� ������ �� �ִ� ���ڿ��� ����

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
    // plugged �ʵ��� ���� ����� ������ �� �ִ� ���ڿ��� ����
    
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
    // present �ʵ��� ���� ����� ������ �� �ִ� ���ڿ��� ����


    //----------------------------------------------------------------------------------
    // status �ʵ��� ���� ����� ������ �� �ִ� ���ڿ��� ����

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
