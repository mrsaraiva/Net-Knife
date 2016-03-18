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
                String dnsCommand = "su -c /data/data/matrix.the.net_knife/busybox nslookup";
                String hostName = dnsEditText.getText().toString();
                String cmd = dnsCommand + " " + hostName;
                dnsResultText.setText("");

                try
                {
                    System.out.println(cmd);
                    Process p = Runtime.getRuntime().exec(cmd);
                    p.waitFor();

                    int len;
                    if ((len = p.getErrorStream().available()) > 0)
                    {
                        System.out.println(p.exitValue());
                        byte[] buf = new byte[len];
                        p.getErrorStream().read(buf);
                        System.out.println("Command error:\t\"" + new String(buf) + "\"");
                    }

                    InputStream input = p.getInputStream();
                    System.out.println(input.read());
                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    System.out.println(in.readLine());
                    while ((line = in.readLine()) != null)
                    {
                        //System.out.println(in.readLine());
                        //buffer.append(line);
                        //buffer.append("\n");
                        dnsResultText.append(line);
                        dnsResultText.append("\n");
                    }

                    //String bufferStr = buffer.toString();
                    //dnsResultText.setText(bufferStr);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
}
