package com.bashkanov.wifianalyzator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements RecyclerViewAdapter.OnCardViewClickListener{

    boolean mBound = false;
    WifiListenerService mService;

    private Handler mHandler = new Handler();
    private List<Wifi> mWifiList;
    private List<ScanResult> mWifiResult;
    RecyclerViewAdapter adapter;
    private LinearLayoutManager llm;
    private RecyclerView mRecyclerView;
    private WifiManager mWifiManager;
    private static final String TAG = "myWifi";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Intent intent = new Intent(getActivity().getApplicationContext(), WifiListenerService.class);
        getActivity().startService(intent);
        llm = new LinearLayoutManager(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = new Intent(getActivity().getApplicationContext(), WifiListenerService.class);
        getActivity().bindService(intent, mConnection, 0);
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView =(RecyclerView)getView().findViewById(R.id.rv);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mWifiList = new ArrayList<Wifi>();
        this.mWifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);


    }


    @Override
    public void onResume() {
        super.onResume();

        /*try{
            this.mWifiList = mService.detectWifi(this.mWifiList);
            mService.test();
        }catch (NullPointerException e) {
            Log.d(TAG, "mService is null");
        }*/

        detectWifi();
        initializeAdapter();
        mHandler.postDelayed(mUpdateWifi, 2000);

    }

    private void initializeAdapter(){
        adapter = new RecyclerViewAdapter(mWifiList);
        adapter.setOnCardViewClickListener(this);
        mRecyclerView.swapAdapter(adapter,false);
    }

    @Override
    public void onStop() {
        mHandler.removeCallbacks(mUpdateWifi);
        if (mBound) {
            getActivity().unbindService(mConnection);
            Log.d(TAG, "(onStop) Unbind from service");
            mBound = false;
        }
        super.onStop();
    }


    public void detectWifi(){
        this.mWifiManager.startScan();
        this.mWifiResult = this.mWifiManager.getScanResults();
        if (this.mWifiList != null) {this.mWifiList.clear();}

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
    }
    @Override
    public void OnSubscribeClicked(Wifi wifi) {
        Log.d(TAG, "OnChartClicked");
    }

    @Override
    public void OnChartClicked(Wifi wifi) {

    }

    @Override
    public void OnNotifyClicked(Wifi wifi) {

    }

    private Runnable mUpdateWifi = new Runnable(){
        @Override
        public void run() {

            /*try{
                mWifiList = mService.detectWifi(mWifiList);
            } catch (NullPointerException e) {
                Log.d(TAG, "mService is null");
            }*/
            //adapter.swap(mWifiList);

            detectWifi();
            adapter.notifyDataSetChanged();
            mHandler.postDelayed(this, 3000);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WifiListenerService.LocalBinder binder = (WifiListenerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
