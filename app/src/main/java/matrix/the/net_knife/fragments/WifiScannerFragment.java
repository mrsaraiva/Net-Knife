package matrix.the.net_knife.fragments;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.WiFiData;
import matrix.the.net_knife.network.WiFiDataNetwork;
import matrix.the.net_knife.utils.CommonUtil;

/**
 * WifiScanner Fragment
 */
public class WifiScannerFragment extends Fragment
{
    private final String TAG = "fragment_wifi_scanner";
    private WiFiData mWifiData;
    private View view;

    public WifiScannerFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_wifi_scanner, container, false);

        mWifiData = null;

        plotData();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
    }

    public void plotData()
    {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.scanningResultBlock);
        linearLayout.removeAllViews();

        if (mWifiData == null)
        {
            Log.d(TAG, "Plotting data: no networks");
            TextView noDataView = new TextView(getContext());
            noDataView.setText(getResources().getString(R.string.wifi_no_data));
            noDataView.setGravity(Gravity.CENTER_HORIZONTAL);
            noDataView.setPadding(0, 50, 0, 0);
            noDataView.setTextSize(18);
            linearLayout.addView(noDataView);
        }
        else
        {
            Log.d(TAG, "Plotting data");

            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);

            TableLayout tableLayout = new TableLayout(getContext());
            tableLayout.setLayoutParams(tableParams);
            tableLayout.setStretchAllColumns(true);

            // row header
            TableRow tableRowHeader = new TableRow(getContext());
            tableRowHeader.setLayoutParams(rowParams);

            TextView ssidText = new TextView(getContext());
            ssidText.setLayoutParams(rowParams);
            ssidText.setText(getResources().getString(R.string.ssid_text));
            ssidText.setTypeface(null, Typeface.BOLD);

            TextView chText = new TextView(getContext());
            chText.setText(getResources().getString(R.string.ch_text));
            chText.setTypeface(null, Typeface.BOLD);

            TextView rxText = new TextView(getContext());
            rxText.setText(getResources().getString(R.string.rx_text));
            rxText.setTypeface(null, Typeface.BOLD);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                System.out.println("Landscape");
                TextView bssidText = new TextView(getContext());
                bssidText.setText(getResources().getString(R.string.bssid_text));
                bssidText.setTypeface(null, Typeface.BOLD);

                tableRowHeader.addView(ssidText);
                tableRowHeader.addView(bssidText);
                tableRowHeader.addView(chText);
                tableRowHeader.addView(rxText);
            }
            else
            {
                System.out.println("Portrait");
                tableRowHeader.addView(ssidText);
                tableRowHeader.addView(chText);
                tableRowHeader.addView(rxText);
            }

            tableLayout.addView(tableRowHeader);

            // rows data
            for (WiFiDataNetwork net : mWifiData.getNetworks())
            {
                TextView ssidVal = new TextView(getContext());
                ssidVal.setText(net.getSsid());

                TextView chVal = new TextView(getContext());
                chVal.setText(String.valueOf(WiFiDataNetwork.convertFrequencyToChannel(net.getFrequency())));

                TextView rxVal = new TextView(getContext());
                rxVal.setText(String.valueOf(net.getLevel()));

                TableRow tableRow = new TableRow(getContext());
                tableRow.setLayoutParams(rowParams);

                if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    TextView bssidVal = new TextView(getContext());
                    bssidVal.setText(net.getBssid());

                    rxVal.setText(String.valueOf(net.getLevel()) + " dBm");

                    tableRow.addView(ssidVal);
                    tableRow.addView(bssidVal);
                    tableRow.addView(chVal);
                    tableRow.addView(rxVal);
                }
                else
                {
                    tableRow.addView(ssidVal);
                    tableRow.addView(chVal);
                    tableRow.addView(rxVal);
                }

                tableLayout.addView(tableRow);
            }

            linearLayout.addView(tableLayout);
        }
    }

    public void UpdateResults(WiFiData data)
    {
        System.out.println("Atualizando redes no Fragment");
        mWifiData = data;


        for (WiFiDataNetwork net : mWifiData.getNetworks())
        {
            double x = 0;
            double y = 0;
            try
            {
                x = Double.parseDouble(String.valueOf(WiFiDataNetwork.convertFrequencyToChannel(net.getFrequency())));
            }
            catch (NumberFormatException e)
            {
                return;
            }
            try
            {
                y = Double.parseDouble(String.valueOf(net.getLevel()));
            }
            catch (NumberFormatException e)
            {
                return;
            }

            System.out.println(net.getSsid());
            System.out.println(String.valueOf(WiFiDataNetwork.convertFrequencyToChannel(net.getFrequency())));
            System.out.println(String.valueOf(net.getLevel()));

            plotData();
        }
    }

}
