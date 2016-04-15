package matrix.the.net_knife.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.WiFiData;
import matrix.the.net_knife.network.WiFiDataNetwork;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiScannerFragment extends Fragment
{
    private final String TAG = "fragment_wifi_scanner";
    private WiFiData mWifiData;
    private View view;

    /**
     * The main dataset that includes all the series that go into a chart.
     */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /**
     * The main renderer that includes all the renderers customizing a chart.
     */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /**
     * The most recently added series.
     */
    private XYSeries mCurrentSeries;
    /**
     * The most recently created renderer, customizing the current series.
     */
    private XYSeriesRenderer mCurrentRenderer;
    /**
     * The chart view that displays the data.
     */
    private GraphicalView mChartView;

    public WifiScannerFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_wifi_scanner, container, false);

        // set some properties on the main renderer
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[]{20, 30, 15, 0});
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(5);
        mRenderer.setXAxisMin(-90);
        mRenderer.setXAxisMax(-30);
        mRenderer.setYAxisMin(1);
        mRenderer.setYAxisMax(14);

        // Chart view
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.chart);

        mWifiData = null;

        mChartView = ChartFactory.getLineChartView(view.getContext(), mDataset, mRenderer);
        mCurrentSeries = new XYSeries("chucochuco");

        TextView textView = new TextView(getActivity());
        textView.setText("Cadê esse textview?!");
        textView.setTextSize(50);

        //linearLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        plotData();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
        outState.putSerializable("dataset", mDataset);
        outState.putSerializable("renderer", mRenderer);
        outState.putSerializable("current_series", mCurrentSeries);
        outState.putSerializable("current_renderer", mCurrentRenderer);
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
        String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
        // create a new series of data
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // set some renderer properties
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(false);
        renderer.setDisplayChartValues(true);
        renderer.setDisplayChartValuesDistance(10);
        mCurrentRenderer = renderer;
        mChartView.repaint();
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

            // add a new data point to the current series
            mCurrentSeries.add(x, y);
            // repaint the chart such as the newly added point to be visible
            mChartView.repaint();

            System.out.println(net.getSsid());
            System.out.println(String.valueOf(WiFiDataNetwork.convertFrequencyToChannel(net.getFrequency())));
            System.out.println(String.valueOf(net.getLevel()));

            plotData();
        }
    }

}
