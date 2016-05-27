package matrix.the.net_knife.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import matrix.the.net_knife.R;

/**
 * Host Monitor Fragment
 */
public class HostMonitorFragment extends Fragment {

    public HostMonitorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_host_monitor, container, false);
    }
}
