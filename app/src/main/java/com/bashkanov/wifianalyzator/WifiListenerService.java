package com.bashkanov.wifianalyzator;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class WifiListenerService extends Service {

    private static final String TAG = "myLogs";
    private final IBinder mBinder = new LocalBinder();
    private WifiManager mWifiManager;
    private List<ScanResult> mWifiResult;

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId){
        //TODO
        Log.d(TAG, "onStartCommand");
        return(START_NOT_STICKY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        WifiListenerService getService() {

            // Return this instance of LocalService so clients can call public methods
            return WifiListenerService.this;
        }
    }

    /** method for clients */

    public void test(){
        Log.d(TAG, "test");
    }

    public List<Wifi> detectWifi(List<Wifi> mWifiList){

        this.mWifiManager.startScan();
        this.mWifiResult = this.mWifiManager.getScanResults();
        if (mWifiList != null) {mWifiList.clear();}

        for (int i = 0; i < mWifiResult.size(); i++){
            String item = mWifiResult.get(i).toString();
            Log.d(TAG, "items =" + item);
            String [] vector_item = item.split(",");
            String item_ssid = vector_item[0];
            String item_bssid = vector_item[1];
            String item_level = vector_item[3];
            String item_frequency = vector_item[4];

            String ssid = item_ssid.split(": ")[1];
            String bssid = item_bssid.split(": ")[1];
            Integer level = Integer.parseInt((item_level.split(": ")[1]));
            Integer frequency = Integer.parseInt(item_frequency.split(": ")[1]);

            mWifiList.add(new Wifi(ssid, frequency, level, bssid));
        }
        Log.d(TAG, "detectWifi");
        return mWifiList;
    }
}
