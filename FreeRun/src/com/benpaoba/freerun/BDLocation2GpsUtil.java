package com.benpaoba.freerun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import android.util.Base64;
import com.baidu.location.BDLocation;

public class BDLocation2GpsUtil {

    static BDLocation tempBDLocation = new BDLocation();     // ��ʱ�������ٶ�λ��
    static GpsLocation tempGPSLocation = new GpsLocation();  // ��ʱ������gpsλ��

    public static enum Method{
        origin, correct;
    }

    private static final Method method = Method.correct;

    /**
     * λ��ת��
     *
     * @param lBdLocation �ٶ�λ��
     * @return GPSλ��
     */
    public static GpsLocation convertWithBaiduAPI(BDLocation lBdLocation) {
        switch (method) {
        case origin:    //ԭ��
            GpsLocation location = new GpsLocation();
            location.lat = lBdLocation.getLatitude();
            location.lng = lBdLocation.getLongitude();
            return location;

        case correct:   //��ƫ
            //ͬһ����ַ�����ת��
            if (tempBDLocation.getLatitude() == lBdLocation.getLatitude() && tempBDLocation.getLongitude() == lBdLocation.getLongitude()) {
                return tempGPSLocation;
            }
            String url = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&"
                    + "x=" + lBdLocation.getLongitude() + "&y="
                    + lBdLocation.getLatitude();
            String result = executeHttpGet(url);
            if (result != null) {
                GpsLocation gpsLocation = new GpsLocation();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String lngString = jsonObj.getString("x");
                    String latString = jsonObj.getString("y");
                    // ����
                    double lng = Double.parseDouble(new String(Base64.decode(lngString, 0)));
                    double lat = Double.parseDouble(new String(Base64.decode(latString, 0)));
                    // ����
                    gpsLocation.lng = 2 * lBdLocation.getLongitude() - lng;
                    gpsLocation.lat = 2 * lBdLocation.getLatitude() - lat;
                    tempGPSLocation = gpsLocation;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                tempBDLocation = lBdLocation;
                return gpsLocation;
            }else{
                return null;
            }

        default:
            return null;
        }
    }

    private static String executeHttpGet(String requestUrl) {
        String result = null;
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            //�������ӳ�ʱ�Ͷ���ʱ
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
