package matrix.the.net_knife;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import matrix.the.net_knife.utils.PingTask;

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

                PingTask ping = new PingTask(pingEditText.getText().toString(), pingResultText, getActivity().getApplicationContext());
                ping.execute();

            }
        });
        return view;
    }
}
