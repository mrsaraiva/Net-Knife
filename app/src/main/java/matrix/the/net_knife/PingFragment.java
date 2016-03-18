package matrix.the.net_knife;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.Process;
import java.lang.Runtime;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * A placeholder fragment containing a simple view.
 */
public class PingFragment extends Fragment{

    Button pingButton;
    EditText pingEditText;
    TextView pingResultText;
    View view;


    public PingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_ping, container, false);
        pingButton = (Button) view.findViewById(R.id.pingButton);
        pingEditText = (EditText)view.findViewById(R.id.pingEditText);
        pingResultText = (TextView)view.findViewById(R.id.pingResultText);
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String pingCommand = "su -c /data/data/matrix.the.net_knife/busybox ping -c 5";
                String hostName = pingEditText.getText().toString();
                String cmd = pingCommand + " " + hostName;
                pingResultText.setText("");

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
                        pingResultText.append(line);
                        pingResultText.append("\n");
                    }

                    //String bufferStr = buffer.toString();
                    //pingResultText.setText(bufferStr);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
}
