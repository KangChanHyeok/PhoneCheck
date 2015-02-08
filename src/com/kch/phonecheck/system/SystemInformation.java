package com.kch.phonecheck.system;

import com.kch.phonecheck.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SystemInformation extends ListActivity{

	private ActivityManager activityManager;
	private MemoryInfo memoryInfo;
	private DisplayMetrics display;
	private double screenInch;
	private DecimalFormat localDecimalFormat = new DecimalFormat("0.00");
	private SystemStatusVo vo=new SystemStatusVo();	
	private SystemInfoAdapter adapter;
	private long totalRam, availRam, usageRam=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sysinfo);
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		memoryInfo = new MemoryInfo();		
		activityManager.getMemoryInfo(memoryInfo);
		display=new DisplayMetrics();		
		getWindowManager().getDefaultDisplay().getMetrics(display);
		double x = Math.pow(display.widthPixels/display.xdpi,2);
		double y = Math.pow(display.heightPixels/display.ydpi,2);
		screenInch=Math.sqrt(x+y);
	    totalRam=(memoryInfo.totalMem / (1024 * 1024));
	    availRam=(memoryInfo.availMem / (1024 * 1024));
	    usageRam=(availRam * 100) / totalRam;
	    CharSequence[] array_title=getResources().getTextArray(R.array.sysinfo_title);
	    CharSequence[] array_content=getResources().getTextArray(R.array.sysinfo_content);
	    ArrayList<String> list = new ArrayList<String>();
	    list = arraysum(list);
	    adapter=new MySectionedAdapter(this, array_title, array_content, list);
	    setListAdapter(adapter);
	    
	}
    private class MySectionedAdapter extends SystemInfoAdapter {

    	CharSequence[] array_title;
    	CharSequence[] array_content;
    	ArrayList<String> list;
		public MySectionedAdapter(Context context, CharSequence[] array_title, 
				CharSequence[] array_content, ArrayList<String> list) {
			super(context);
			this.array_title=array_title;
			this.array_content=array_content;
			this.list=list;
		}
    
		@Override
		protected int getNumberOfSections() {
			return 3;
		}
		
		@Override
		protected int getNumberOfRowsInSection(int section) {
			switch (section) {
				case 0:
					return 8;
				case 1:
					return 4;					
				default:
					return 5;
			}
		}
		
		@Override
		public Object getItemInSectionAndRow(int section, int row) {
			return null;
		}

		@Override
		protected View newSectionView(Context context, int section, ViewGroup parent) {
			return ((LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_title, parent, false);
		}
		
		@Override
		protected void bindSectionView(Context context, int section, View convertView) {
			((TextView)convertView.findViewById(R.id.textSeparator)).setText(array_title[section]);
		}
		
		@Override
		protected View newRowViewInSection(Context context, int section, int row, ViewGroup parent) {
			return ((LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_content, parent, false);
		}
		
		@Override
		protected void bindRowViewInSection(Context context, int section, int row, View convertView) {
			((TextView)convertView.findViewById(R.id.sys_title)).setText(array_content[row]);
			((TextView)convertView.findViewById(R.id.sys_content)).setText(list.get(row));
		}
    }
	public ArrayList<String> arraysum(ArrayList<String> list){
		list.add(Build.MANUFACTURER); //Brand
		list.add(Build.PRODUCT); //Model Name
		list.add(vo.getMaxCPUFreqMHz());	//MaxCpu
		list.add(Build.BOARD);	//Board(MSM8974)		
		list.add(totalRam+" MB"); // RAM
		list.add(availRam+" MB ("+usageRam+"%)"); // RAM
		list.add(vo.getTotalMemSize("Internal")); //Internal Storage
		list.add(vo.getAvailableMemSize("Internal")); //Internal Storage
		
		list.add(Build.VERSION.RELEASE); //Version
		list.add(Build.ID);	//ID(JDQ39)
		list.add(vo.getFormattedKernelVersion()); //Kernal	
		list.add(Build.VERSION.SDK_INT+""); //SDK(17)
		
		list.add(localDecimalFormat.format(screenInch)+" (Inch)");	//Screen Size
		list.add(display.widthPixels+" x "+display.heightPixels+" (Pixels)"); //Resolution		
		list.add(display.densityDpi+"");	//Destiny(480)
		list.add(display.xdpi+"");
		list.add(display.ydpi+"");
		
		return list;
	}
	

}
