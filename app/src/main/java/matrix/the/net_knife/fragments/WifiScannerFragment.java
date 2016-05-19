package matrix.the.net_knife.fragments;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.WiFiData;
import matrix.the.net_knife.network.WiFiDataNetwork;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiScannerFragment extends Fragment
{
    private final String TAG = "fragment_wifi_scanner"; // Tag do Fragment WifiScanner
    private WiFiData mWifiData;
    private LineChart chart;
    private View view;
    private Map<String, LineDataSet> wifiDevices = new HashMap<>();

    public WifiScannerFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_wifi_scanner, container, false);

        mWifiData = null;

        loadChart();
        plotData();

        return view;
    }

    private void loadChart()
    {
        chart = (LineChart) view.findViewById(R.id.mp_chart);

        chart.setHardwareAccelerationEnabled(false);
        chart.setBackgroundResource(R.color.white);
        chart.setDrawGridBackground(false);
        chart.setDescription("");
        chart.setNoDataText("");

        chart.setData(new LineData());
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.black));
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.black));
        chart.getAxisLeft().setTextSize(8.0F);

        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setAxisMaxValue(85.0F);
        chart.getAxisLeft().setShowOnlyMinMax(false);
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        chart.getAxisRight().setAxisLineColor(getResources().getColor(R.color.black));
        chart.getAxisRight().setTextColor(getResources().getColor(R.color.black));
        chart.getAxisRight().setTextSize(8.0F);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.getXAxis().setAxisLineColor(getResources().getColor(R.color.red));
        chart.getXAxis().setTextColor(getResources().getColor(R.color.red));
        chart.getXAxis().setTextSize(8.0F);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.getXAxis().setDrawGridLines(false);
        chart.getLegend().setEnabled(false);

        chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(4f, 0));
//        entries.add(new Entry(8f, 1));
//        entries.add(new Entry(6f, 2));
//        entries.add(new Entry(2f, 3));
//        entries.add(new Entry(18f, 4));
//        entries.add(new Entry(9f, 5));


//        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");

//        LineData data = new LineData(labels, dataset);
//        chart.setData(data); // set the data and list of lables into chart

//        chart.invalidate(); // refresh
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
    }
/*
    @Override
    public void onActivityCreated(Bundle savedState)
    {
        super.onActivityCreated(savedState);
        // restore the current data, for instance when changing the screen
        // orientation
        mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
        mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
        mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
        mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
    }
*/
    public void plotData()
    {

    }

    /*
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
    */

    public void UpdateResults(WiFiData data)
    {
        System.out.println("Atualizando redes no Fragment");
        mWifiData = data;

        removeUnreachableDevices();

        for (WiFiDataNetwork net : mWifiData.getNetworks())
        {
//            if (net.getSsid().equals(""))
//                continue;

            Double x = 0.0;
            Double y = 0.0;
            try
            {
                x = Double.parseDouble(String.valueOf(WiFiDataNetwork.convertFrequencyToChannel(net.getFrequency())));
                LineDataSet lineDataSet = null;
//                if (wifiDevices.containsKey(net.getSsid()))
//                {
//                    lineDataSet = wifiDevices.get(net.getSsid());
//                    lineDataSet.getEntryForXIndex(x.intValue()).setVal(net.getLevel()*-1);
//                }
//                else
//                {
                    chart.getData().addXValue(String.valueOf(x));
                    lineDataSet = createDeviceSet();
//                    int xValue = (parabolaWidth/2) + wiFiDevice.getChannel() - 1;
                    int xValue = (2 + (net.getLevel() -1)) * -1;//getXVal(wiFiDevice.frequency);
                    lineDataSet.addEntry(new Entry(0, xValue - (2)));
                    lineDataSet.addEntry(new Entry(0, xValue));
                    lineDataSet.addEntry(new Entry(0, xValue + (2)));
                    chart.getData().addDataSet(lineDataSet);
//                    wifiDevice.put(wiFiDevice, lineDataSet);
                    wifiDevices.put(net.getSsid(), lineDataSet);
//                }
            }
            catch (NumberFormatException e)
            {
                return;
            }
            try
            {
                y = Double.parseDouble(String.valueOf(net.getLevel()));
//                chart.getData().addXValue(String.valueOf());
            }
            catch (NumberFormatException e)
            {
                return;
            }

            System.out.println(net.getSsid());
            System.out.println(String.valueOf(WiFiDataNetwork.convertFrequencyToChannel(net.getFrequency())));
            System.out.println(String.valueOf(net.getLevel()));

            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }

    private void removeUnreachableDevices()
    {
//        System.out.println(wifiDevices.keySet() );
        for (String ssid : wifiDevices.keySet())
        {
            boolean found = false;
            for (WiFiDataNetwork net : mWifiData.getNetworks())
            {
                if (net.getSsid().equals(ssid))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                LineDataSet lineDataSet = wifiDevices.get(ssid);
                chart.getData().removeDataSet(lineDataSet);
                wifiDevices.remove(ssid);
            }
        }
    }

    private LineDataSet createDeviceSet(){
//        int deviceLineColor = WiFiScannerUtils.getColorForBSSID(wiFiDevice.BSSID);
        LineDataSet lineDataSet = new LineDataSet(null, "Signal");
        lineDataSet.setLineWidth(0.6F);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
//        lineDataSet.setColor(deviceLineColor);
        lineDataSet.setDrawFilled(true);
//        lineDataSet.setFillColor(deviceLineColor);
        lineDataSet.setFillAlpha(80);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setCubicIntensity(0.25F);
//        lineDataSet.setValueFormatter(new DeviceValueFormatter(wiFiDevice));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawValues(true);
//        lineDataSet.setValueTextColor(deviceLineColor);
        lineDataSet.setValueTextSize(12);
        return lineDataSet;
    }

}
