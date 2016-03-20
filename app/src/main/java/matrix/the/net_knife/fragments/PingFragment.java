package matrix.the.net_knife.fragments;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.ProcessStream.ProcessStreamReader;
import matrix.the.net_knife.utils.ShellProcess.OnComplete;

/**
 * A placeholder fragment containing a simple view.
 */
public class PingFragment extends Fragment implements OnClickListener, ProcessStreamReader, OnComplete
{

    private String ARG_ITEM_ID = "";
    private NetworkTools.NetworkTool mItem;
    private final Handler mHandler = new Handler();
    private static String sline = "";
    private Button actionButton;
    private EditText inputEditText;
    private TextView consoleTextView;
    private Typeface font;
    private View view;
    private int i;
    private String textBuffer = "";

    public PingFragment()
    {
    }

    final Runnable mUpdateResults = new Runnable()
    {
        public void run()
        {
            consoleTextView.setText(textBuffer);
            i++;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ARG_ITEM_ID = getActivity().getIntent().getStringExtra("ARG_ITEM_ID");

        mItem = NetworkTools.ITEM_MAP.get(ARG_ITEM_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_ping, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        actionButton = (Button) view.findViewById(R.id.pingButton);
        actionButton.setOnClickListener(this);
        actionButton.setTypeface(font);
        actionButton.setTextSize(11);

        inputEditText = (EditText) view.findViewById(R.id.pingEditText);
        consoleTextView = (TextView) view.findViewById(R.id.pingResultText);
        consoleTextView.setText("Input a valid hostname or IPv4 address and press the button to probe the host using ICMP packets");

        if (mItem != null)
        {
            actionButton.setText(mItem.content);
        }
        else
        {
            actionButton.setText("Start");
        }

        return view;
    }

    @Override
    public void onClick(View arg0)
    {
        String[] args = new String[2];
        args[0] = "-c 4";
        args[1] = inputEditText.getText().toString();

        consoleTextView.setText("");
        textBuffer = "";
        i = 0;

        if ((mItem.worker != null && !mItem.worker.checkArgs(args)) || (mItem.tworker != null && !mItem.tworker.checkArgs(args)))
        {
            consoleTextView.setText("Please enter a valid hostname (like google.com) or IPv4 address (like 8.8.8.8)");
        }
        else
        {
            consoleTextView.setText("Probing host " + args[1]);
            actionButton.setEnabled(false);

            mItem.start(args, this, this);
        }
    }

    @Override
    public void onLineRead(String line)
    {
        sline = line;

        if (sline.contains("bad address"))
        {
            textBuffer = "Unknown host '" + inputEditText.getText() + "'";
        }
        else
        {
            if (sline != "\n" && sline != null && !textBuffer.contains("Unknown host"))
            {
                textBuffer += sline + "\n";
            }
        }

        i++;

        mHandler.post(mUpdateResults);
        System.out.println(sline);
    }

    @Override
    public void onComplete(String results)
    {
        consoleTextView.append("\n" + results);
        actionButton.setEnabled(true);
    }
}
