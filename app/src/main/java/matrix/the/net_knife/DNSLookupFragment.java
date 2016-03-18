package matrix.the.net_knife;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import matrix.the.net_knife.utils.DNSLookupTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class DNSLookupFragment extends Fragment {

    Button dnsButton;
    EditText dnsEditText;
    TextView dnsResultText;
    View view;
    
    public DNSLookupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dnslookup, container, false);
        dnsButton = (Button) view.findViewById(R.id.dnsButton);
        dnsEditText = (EditText)view.findViewById(R.id.dnsEditText);
        dnsResultText = (TextView)view.findViewById(R.id.dnsResultText);
        dnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dnsResultText.setText("");
                DNSLookupTask dnslookup = new DNSLookupTask(dnsEditText.getText().toString(), dnsResultText, getActivity().getApplicationContext());
                dnslookup.execute();
            }
        });
        return view;
    }
}
