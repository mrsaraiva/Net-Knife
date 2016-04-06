package matrix.the.net_knife.fragments;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.CommonUtil;
import matrix.the.net_knife.utils.CustomEditText;
import matrix.the.net_knife.utils.ProcessStream.ProcessStreamReader;
import matrix.the.net_knife.utils.ShellProcess.OnComplete;

/**
 * Ping Fragment
 */
public class PingFragment extends Fragment implements ProcessStreamReader, OnComplete
{

    private String ARG_ITEM_ID = "";
    private NetworkTools.NetworkTool mItem;
    private final Handler mHandler = new Handler();
    private static String sline = "";
    private Button actionButton;
    private CustomEditText inputEditText;
    private TextView consoleTextView;
    private Typeface font;
    private View view;
    private int i;
    private String textBuffer = "";

    // Patterns for ping util result
    private static Pattern pingTTLExcPtn = Pattern.compile("From (.+): icmp_seq=(\\d+) Time to live exceeded");
    private static Pattern pingUnknHostPtn = Pattern.compile("ping: unknown host (.+)");
    private static Pattern pingConnNetUnreachPtn = Pattern.compile("connect: Network is unreachable");
    private static Pattern pingSmsgNetUnreachPtn = Pattern.compile("ping: sendmsg: Network is unreachable");
    private static Pattern pingBcastPtn = Pattern.compile("Do you want to ping broadcast\\? Then -b");
    private static Pattern pingSckNotPtn = Pattern.compile("ping: icmp open socket: Operation not permitted");
    private static Pattern pingFromNetUnreachPtn = Pattern.compile("From (.+): icmp_seq=(\\d+) Destination Host Unreachable");
    private static Pattern pingResultPtn = Pattern.compile("(\\d+) bytes from (.+?)(\\s\\((.+)\\))?: icmp_seq=(\\d+) ttl=(\\d+)( time=(\\d+.\\d+) ms)?");
    private static Pattern pingBfFinalResultPtn = Pattern.compile("(\\d+) packets transmitted, (\\d+) received, (\\d+)% packet loss, time (\\d+)ms");
    private static Pattern pingBfFinalResultErrPtn = Pattern.compile("(\\d+) packets transmitted, (\\d+) received, \\+(\\d+) errors, (\\d+)% packet loss, time (\\d+)ms");
    private static Pattern pingFinalResultPtn = Pattern.compile("rtt min/avg/max/mdev = (.+)/(.+)/(.+)/(.+) ms");

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

        inputEditText = (CustomEditText) view.findViewById(R.id.pingEditText);
        inputEditText.addTextChangedListener(CommonUtil.editTextChanged());
        inputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputEditText.setImeActionLabel("PING", 5);
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
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
        });

        consoleTextView = (TextView) view.findViewById(R.id.pingResultText);
        consoleTextView.setText("Input a valid hostname or IP address and press the button to probe the host using ICMP packets");


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

    private void doAction()
    {
        String[] args = new String[2];
        args[0] = "-c 4";
        args[1] = inputEditText.getText().toString();

        consoleTextView.setText("");
        textBuffer = "";
        i = 0;

        if ((mItem.worker != null && !mItem.worker.checkArgs(args)) || (mItem.tworker != null && !mItem.tworker.checkArgs(args)))
        {
            consoleTextView.setText("Please enter a valid hostname (like google.com) or IP address (like 8.8.8.8)");
        }
        else
        {
            CommonUtil.hideKeyboardFrom(this.getContext(), view);
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
        if (pingFinalResultPtn.matcher(textBuffer).find())
        {
            Snackbar.make(view, "Ping completed :)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else
        {
            Snackbar.make(view, "Ping failed x(", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        actionButton.setEnabled(true);
    }
}
