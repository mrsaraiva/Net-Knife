package matrix.the.net_knife.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import matrix.the.net_knife.R;
import matrix.the.net_knife.utils.PingTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class PingFragment extends Fragment{

    Button pingButton;
    EditText pingEditText;
    TextView pingResultText;
    View view;
    Typeface font;

    public PingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_ping, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        pingButton = (Button) view.findViewById(R.id.pingButton);
        pingButton.setTypeface(font);
        pingButton.setTextSize(20);

        pingEditText = (EditText)view.findViewById(R.id.pingEditText);
        pingResultText = (TextView)view.findViewById(R.id.pingResultText);

        setEventos();

        return view;
    }

    private void setEventos()
    {
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pingResultText.setText("");
                PingTask ping = new PingTask(pingEditText.getText().toString(), pingResultText, getActivity().getBaseContext());
                ping.execute();
            }
        });
    }
}