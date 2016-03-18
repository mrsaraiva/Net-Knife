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
public class TracerouteFragment extends Fragment {

    Button traceButton;
    EditText traceEditText;
    TextView traceResultText;
    View view;

    public TracerouteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_traceroute, container, false);
        traceButton = (Button) view.findViewById(R.id.traceButton);
        traceEditText = (EditText)view.findViewById(R.id.traceEditText);
        traceResultText = (TextView)view.findViewById(R.id.traceResultText);
        traceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String traceCommand = "su -c /data/data/matrix.the.net_knife/busybox traceroute -w 1 -n -m 20";
                String hostName = traceEditText.getText().toString();
                String cmd = traceCommand + " " + hostName;
                traceResultText.setText("");

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
                        traceResultText.append(line);
                        traceResultText.append("\n");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
}
