package com.benpaoba.freerun;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.content.ActivityNotFoundException;

public class GpsManager {

	public final static int GPS_BAD = 1;
	public final static int GPS_NORMAL = 2;
	public final static int GPS_GOOD = 3;
	public static boolean isGpsOpen(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩         
		boolean gpsOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (gpsOpen) {
			return true;
		}
		return false;  
	}
	
	public static void startIntentForGpsSetting(Context context) {
		Intent intent = new Intent().setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		try 
        {
            context.startActivity(intent);
        } catch(ActivityNotFoundException ex) {            
            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}
	
	public static int getGpsStatus(int numberOfUsedSatellites) {
		if(numberOfUsedSatellites < 2) {
			return GPS_BAD;
		} else if(numberOfUsedSatellites < 5){
			return GPS_NORMAL;
		} else {
			return GPS_GOOD;
		}
	}
}
