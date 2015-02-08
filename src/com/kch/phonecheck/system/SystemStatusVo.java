package com.kch.phonecheck.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SystemStatusVo {
	private DecimalFormat deci = new DecimalFormat("0.00");

	public String getMaxCPUFreqMHz() {

		String str = "";
	    try {
	    	RandomAccessFile localRandomAccessFile = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
	        str = localRandomAccessFile.readLine();
	        localRandomAccessFile.close();

	        
	        if(str!=""){
	        	if(Float.parseFloat(str)>1000000){
	        		str=deci.format(Float.parseFloat(str)/1000000)+" Ghz";
	        	}else{
	        		str=deci.format(Float.parseFloat(str)/1000)+" Mhz";
	        	}
	        	return str;
	        }
	       return "unknown";

	    } catch ( IOException ex ) {
	        ex.printStackTrace();
	    }
	    return "unknown";
	}
	public String getFormattedKernelVersion() {
		String TAG = "DeviceInfoSettings";
        String procVersionStr;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
            try {
                procVersionStr = reader.readLine();
            } finally {
                reader.close();
            }

            final String PROC_VERSION_REGEX =
            	"\\w+\\s+" + /* ignore: Linux */
                "\\w+\\s+" + /* ignore: version */
                "([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
                "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
                "\\([^)]+\\)\\s+" + /* ignore: (gcc ..) */
                "([^\\s]+)\\s+" + /* group 3: #26 */
                "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
                "(.+)"; /* group 4: date */

            Pattern p = Pattern.compile(PROC_VERSION_REGEX);
            Matcher m = p.matcher(procVersionStr);

            if (!m.matches()) {
                Log.e(TAG, "Regex did not match on /proc/version: " + procVersionStr);
                return "Unavailable";
            } else if (m.groupCount() < 4) {
                Log.e(TAG, "Regex match on /proc/version only returned " + m.groupCount()
                        + " groups");
                return "Unavailable";
            } else {
                return (new StringBuilder(m.group(1))).toString();
            }
            
	        }catch (IOException e) {  
	            Log.e(TAG,
	                "IO Exception when getting kernel version for Device Info screen",
	                e);
	
	            return "Unavailable";
	        }
    }
	
	
	public String getTotalMemSize(String Type) {
		File path;
		StatFs stat;
		long blockSize;
		long totalBlocks;		
		float totalMemory;
		if(Type.equals("Internal")){
			path = Environment.getDataDirectory();  
			stat = new StatFs(path.getPath());  
		}else{
			path = Environment.getExternalStorageDirectory();
			stat = new StatFs(path.getPath());
		}
		blockSize = stat.getBlockSize();
		totalBlocks = stat.getBlockCount();
		totalMemory = totalBlocks * blockSize/1024/1024;
		if(totalMemory>1024){
			return deci.format(totalMemory/1024)+" GB";
		}else{
			return deci.format(totalMemory)+" MB";
		}
	}
	
	public String getAvailableMemSize(String Type) {
		File path;
		StatFs stat;
		long blockSize;
		long availableBlocks;		
		long totalBlocks;
		float totalMemory;
		float availableMemory;		
		if(Type.equals("Internal")){
			path = Environment.getDataDirectory();  
			stat = new StatFs(path.getPath());  
		}else{
			path = Environment.getExternalStorageDirectory();
			stat = new StatFs(path.getPath());
		}		
		totalBlocks = stat.getBlockCount();
		blockSize = stat.getBlockSize();
		availableBlocks = stat.getAvailableBlocks();
		availableMemory = availableBlocks * blockSize/1024/1024;
		totalMemory = totalBlocks * blockSize/1024/1024;
		if(availableMemory>1024){
			return deci.format(availableMemory/1024)+" GB"+" ("+deci.format((availableMemory*100)/totalMemory)+"%)";
		}else{
			return deci.format(availableMemory)+" MB"+" ("+deci.format((availableMemory*100)/totalMemory)+"%)";
		}
	}
}
