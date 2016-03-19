package matrix.the.net_knife.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import matrix.the.net_knife.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiScannerFragment extends Fragment {

    public WifiScannerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_scanner, container, false);
    }
}
