package matrix.the.net_knife.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcos Saraiva on 08/04/2016.
 */
public class WiFiData implements Parcelable
{
    private List<WiFiDataNetwork> mNetworks;

    public WiFiData()
    {
        mNetworks = new ArrayList<WiFiDataNetwork>();
    }

    public WiFiData(Parcel in)
    {
        in.readTypedList(mNetworks, WiFiDataNetwork.CREATOR);
    }

    public static final Parcelable.Creator<WiFiData> CREATOR = new Parcelable.Creator<WiFiData>()
    {
        public WiFiData createFromParcel(Parcel in)
        {
            return new WiFiData(in);
        }

        public WiFiData[] newArray(int size)
        {
            return new WiFiData[size];
        }
    };

    /**
     * Stores the last WiFi scan performed by {@link
     * WifiManager.getScanResults()} creating a {@link WiFiDataNetwork ()} object
     * for each network detected.
     *
     * @param results list of networks detected
     */
    public void addNetworks(List<ScanResult> results)
    {
        mNetworks.clear();
        for (ScanResult result : results)
        {
            mNetworks.add(new WiFiDataNetwork(result));
        }
        Collections.sort(mNetworks);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeTypedList(mNetworks);
    }

    /**
     * @return Returns a string containing a concise, human-readable description
     * of this object.
     */
    @Override
    public String toString()
    {
        if (mNetworks == null || mNetworks.size() == 0)
        {
            return "Empty data";
        }
        else
        {
            return mNetworks.size() + " networks data";
        }
    }

    /**
     * @return Returns the list of scanned networks
     */
    public List<WiFiDataNetwork> getNetworks()
    {
        return mNetworks;
    }
}
