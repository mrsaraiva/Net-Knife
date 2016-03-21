package matrix.the.net_knife.fragments;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.CommonUtil;
import matrix.the.net_knife.utils.ProcessStream.ProcessStreamReader;
import matrix.the.net_knife.utils.ShellProcess.OnComplete;

/**
 * A placeholder fragment containing a simple view.
 */
public class PortScannerFragment extends Fragment implements ProcessStreamReader, OnComplete
{

    private String ARG_ITEM_ID = "";
    private NetworkTools.NetworkTool mItem;
    private final Handler mHandler = new Handler();
    private static String sline = "";
    private Button actionButton;
    private EditText inputEditText, startPortEditText, endPortEditText;
    private TextView consoleTextView;
    private Typeface font;
    private View view;
    private int i;
    private String textBuffer = "";

    public PortScannerFragment()
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
        view = inflater.inflate(R.layout.fragment_port_scanner, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        actionButton = (Button) view.findViewById(R.id.pscanButton);
        actionButton.setTypeface(font);
        actionButton.setTextSize(11);
        actionButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doAction();
            }
        });

        inputEditText = (EditText) view.findViewById(R.id.pscanEditText);
        inputEditText.setOnEditorActionListener(new CustomInputTextHandler());

        startPortEditText = (EditText) view.findViewById(R.id.pscanStartPortEditText);
        startPortEditText.setOnEditorActionListener(new CustomInputTextHandler());

        endPortEditText = (EditText) view.findViewById(R.id.pscanEndPortEditText);
        endPortEditText.setOnEditorActionListener(new CustomInputTextHandler());

        consoleTextView = (TextView) view.findViewById(R.id.pscanResultText);
        consoleTextView.setText("Input a valid hostname and optionally input TCP start port and TCP end port to be scanned. If no ports are specified, 1 and 1024 will be set, respectively.");

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

    public void doAction()
    {
        String[] args = new String[3];

        // Get start port to scan
        if (startPortEditText.getText().toString().trim().length() == 0)
        {
            args[0] = "-p 1";
        }
        else
        {
            args[0] = "-p " + startPortEditText.getText().toString();
            // Get end port to scan
        }

        if (endPortEditText.getText().toString().trim().length() == 0)
        {
            if (Integer.parseInt(args[0].substring(3)) > 1024)
            {
                args[1] = "-P" + startPortEditText.getText().toString();
            }
            else
            {
                args[1] = "-P 1024";
            }
        }
        else
        {
            args[1] = "-P " + endPortEditText.getText().toString();
        }

        // Get host to scan
        args[2] = inputEditText.getText().toString();

        consoleTextView.setText("");
        textBuffer = "";
        i = 0;

        if ((mItem.worker != null && !mItem.worker.checkArgs(args)) || (mItem.tworker != null && !mItem.tworker.checkArgs(args)))
        {
            consoleTextView.setText("Please enter a valid hostname (like google.com) and port numbers (1-65535)");
        }
        else
        {
            CommonUtil.hideKeyboardFrom(this.getContext(), view);
            consoleTextView.setText("Scanning hostname " + args[2] + "\nPorts " + args[0].substring(3) + " to " + args[1].substring(3) +"\nPlease wait");
            actionButton.setEnabled(false);

            mItem.start(args, this, this);
        }
    }

    @Override
    public void onLineRead(String line)
    {
        sline = line;

        if (sline != "\n" && sline != null)
        {
            sline = sline.replaceAll("\t","    ");
            textBuffer += sline + "\n";
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

    class CustomInputTextHandler implements OnEditorActionListener
    {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            System.out.println("Action ID: " + actionId);

            if (actionId == 5)
            {
                doAction();
                return true;
            }
            return false;
        }
    }
}
