package com.kch.phonecheck.temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GetTemp {

	private Runtime operator;
	private Process process;
	private BufferedReader bufir;
    final String cputemp[] = new String[14];
    private int best_count;
    public GetTemp() {
		// TODO Auto-generated constructor stub
	    this.cputemp[0] = "cat /sys/devices/system/cpu/cpu0/cpufreq/cpu_temp";
	    this.cputemp[1] = "cat /sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp";
	    this.cputemp[2] = "cat /sys/class/thermal/thermal_zone1/temp";
	    this.cputemp[3] = "cat /sys/class/i2c-adapter/i2c-4/4-004c/temperature";
	    this.cputemp[4] = "cat /sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature";
	    this.cputemp[5] = "cat /sys/devices/platform/omap/omap_temp_sensor.0/temperature";
	    this.cputemp[6] = "cat /sys/devices/platform/tegra_tmon/temp1_input";
	    this.cputemp[7] = "cat /sys/kernel/debug/tegra_thermal/temp_tj";
	    this.cputemp[8] = "cat /sys/devices/platform/s5p-tmu/temperature";
	    this.cputemp[9] = "cat /sys/class/thermal/thermal_zone0/temp";
	    this.cputemp[10] = "cat /sys/devices/virtual/thermal/thermal_zone0/temp";
	    this.cputemp[11] = "cat /sys/class/hwmon/hwmon0/device/temp1_input";
	    this.cputemp[12] = "cat /sys/devices/virtual/thermal/thermal_zone1/temp";
	    this.cputemp[13] = "cat /sys/devices/platform/s5p-tmu/curr_temp";
	    	
	    best_count=getBestCpuTemp();
	}

	private int getBestCpuTemp() {
		int select=999;
		int count=999;
		try{
			for(int i=0; i<cputemp.length; i++){
				// 먼저 쉘 명령을 만듭니다. 쉘의 위치, 옵션, 그리고 실제 쉘 커맨드
				// cat 는 텍스트파일을 출력하는 명령
				// /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq 파일에 현재 주파수 정보가 기록되어 있습니다.
				   String[] cmd = {"/system/bin/sh", "-c", cputemp[i]};
				// 런타임 클래스를 이용하면 리눅스상의 실행 파일을 실행시킬 수 있습니다.   
				   operator = Runtime.getRuntime();
				// 명령어의 실행
				   process = operator.exec(cmd);
					// 실행 결과 출력되는 결과를 받아오겠습니다.
					   bufir = new BufferedReader (new InputStreamReader(process.getInputStream()));
					   String temp;
					   if((temp = bufir.readLine()) != null){
						   if(select>Integer.parseInt(temp)){
							   select=Integer.parseInt(temp);
							   count=i;
						   }						   
					   }
					   bufir.close();
					   process.destroy();
					   
			}
			if(select==999){
				return 0;
			}
			return count;
		  }catch(IOException e){
			  
		  }
		return 0;
	}	
	
	public int getC_CpuTemp(){
		int select=999;
		try{
			// 먼저 쉘 명령을 만듭니다. 쉘의 위치, 옵션, 그리고 실제 쉘 커맨드
			// cat 는 텍스트파일을 출력하는 명령
			// /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq 파일에 현재 주파수 정보가 기록되어 있습니다.
			String[] cmd = {"/system/bin/sh", "-c", cputemp[best_count]};
			// 런타임 클래스를 이용하면 리눅스상의 실행 파일을 실행시킬 수 있습니다.   
			operator = Runtime.getRuntime();
			// 명령어의 실행
			process = operator.exec(cmd);
			// 실행 결과 출력되는 결과를 받아오겠습니다.
			bufir = new BufferedReader (new InputStreamReader(process.getInputStream()));
			String temp;
			if((temp = bufir.readLine()) != null){
				select=Integer.parseInt(temp);
			}
			bufir.close();
			process.destroy();
			if(select==999){
				return 0;
			}
			return select;
			}catch(IOException e){
				
			}
		return select;
	}
	
	public int getC_BatteryTemp(int bat_temp){
		return bat_temp/10;
	}
	public int getF_CpuTemp(){
		return (int) ((getC_CpuTemp()*1.8)+32);
	}	
	public int getF_BatteryTemp(int bat_temp){
		return (int) ((bat_temp/10)*1.8+32);
	}
}
