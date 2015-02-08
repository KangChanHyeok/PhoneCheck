package com.kch.phonecheck.gps;

import com.kch.phonecheck.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class GPSTest extends FragmentActivity implements LocationListener{
	
	private LocationManager locationManager;
	private GoogleMap map;
	private LatLng Seoul = new LatLng(37.5600993, 126.985988);
	private int count=0;
	private TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpstest);
		
		SupportMapFragment mfm=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
				
		map=mfm.getMap();
				
		//zoomControl
		map.getUiSettings().setZoomControlsEnabled(true);
		
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Seoul, 13)); 
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		tv=(TextView)findViewById(R.id.gpstext);
		
			
	}
	
    @Override
    protected void onResume() {
        super.onResume();      
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        //5초단위 거리0으로 갱신
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		count++;
	    double a = location.getLatitude();
	    double b = location.getLongitude();
	    Seoul = new LatLng(a, b);
	    tv.setText("Count : "+count+"\nlatitude : "+a+"\nlatitude:"+b);
  		map.animateCamera(CameraUpdateFactory.newLatLngZoom(Seoul, 13));
  		map.setMyLocationEnabled(true);
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
