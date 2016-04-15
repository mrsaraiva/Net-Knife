package matrix.the.net_knife.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import matrix.the.net_knife.R;
import matrix.the.net_knife.fragments.WifiScannerFragment;
import matrix.the.net_knife.network.WiFiData;
import matrix.the.net_knife.network.WiFiService;
import matrix.the.net_knife.utils.CommonUtil;
import matrix.the.net_knife.utils.Constants;

public class WifiScanner extends AppCompatActivity
{
    private WiFiData mWifiData;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWifiData = null;

        // set receiver
        WifiScannerReceiver mReceiver = new WifiScannerReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(Constants.INTENT_FILTER));

        // launch WiFi service
        intent = new Intent(this, WiFiService.class);
        startService(intent);

        // recover retained object
        mWifiData = (WiFiData) getLastNonConfigurationInstance();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        stopService(intent);
        super.onDestroy();
    }

    public class WifiScannerReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            WiFiData data = (WiFiData) intent.getParcelableExtra(Constants.WIFI_DATA);
            Log.d("Wi-fi", "Uia!");
            System.out.println("Marroia!");

            if (data != null)
            {
                Log.d("Wi-fi", "Rede!");
                mWifiData = data;
                Fragment wifiFragment = CommonUtil.getVisibleFragment(WifiScanner.this);
                if (wifiFragment instanceof WifiScannerFragment)
                {
                    System.out.println("Achei o fragment! :D");
                    ((WifiScannerFragment) wifiFragment).UpdateResults(mWifiData);
                }
                else
                {
                    System.out.println("Fragment is null :(");
                }
            }
        }
    }

}
