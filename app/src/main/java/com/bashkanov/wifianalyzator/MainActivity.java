package com.bashkanov.wifianalyzator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //add a fragment
        RecyclerViewFragment rvFragment = new RecyclerViewFragment() ;
        fragmentTransaction.add(R.id.container, rvFragment);
        fragmentTransaction.commit();
    }
}