package matrix.the.net_knife.fragments;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.CommonUtil;
import matrix.the.net_knife.utils.CustomEditText;
import matrix.the.net_knife.utils.ProcessStream.ProcessStreamReader;
import matrix.the.net_knife.utils.ShellProcess.OnComplete;

/**
 * Whois Fragment
 */
public class WhoisFragment extends Fragment implements ProcessStreamReader, OnComplete
{

    private String ARG_ITEM_ID = "";
    private NetworkTools.NetworkTool mItem;
    private final Handler mHandler = new Handler();
    private static String sline = "";
    private Button actionButton;
    private CustomEditText inputEditText;
    private Spinner spinnerWhois;
    private TextView consoleTextView;
    private Typeface font;
    private View view;
    private int i = 0;
    private String textBuffer = "";

    public WhoisFragment()
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
        view = inflater.inflate(R.layout.fragment_whois, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        actionButton = (Button) view.findViewById(R.id.whoisButton);
        actionButton.setTypeface(font);
        actionButton.setTextSize(12);
        actionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doAction();
            }
        });

        inputEditText = (CustomEditText) view.findViewById(R.id.whoisEditText);
        inputEditText.addTextChangedListener(CommonUtil.editTextChanged());
        inputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputEditText.setImeActionLabel("WHOIS", 5);
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

        spinnerWhois = (Spinner) view.findViewById(R.id.spinnerWhoisServers);


        consoleTextView = (TextView) view.findViewById(R.id.whoisResultText);
        consoleTextView.setText("Input a valid hostname or IP address, select a server and press the button to query the Whois database");

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
        String whoisServer[] = spinnerWhois.getSelectedItem().toString().split("\\s+");
        String[] args = new String[2];
        args[0] = "-h " + whoisServer[1];
        args[1] = inputEditText.getText().toString();

        consoleTextView.setText("");
        textBuffer = "";

        if ((mItem.worker != null && !mItem.worker.checkArgs(args)) || (mItem.tworker != null && !mItem.tworker.checkArgs(args)))
        {
            consoleTextView.setText("Please enter a valid hostname (like google.com) or IP address (like 8.8.8.8)");
        }
        else
        {
            CommonUtil.hideKeyboardFrom(this.getContext(), view);
            consoleTextView.setText("Querying Whois server about host " + args[0] + ", please wait");
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
            textBuffer += sline + "\n";
        }

        mHandler.post(mUpdateResults);
        System.out.println(sline);
    }

    @Override
    public void onComplete(String results)
    {
        if (textBuffer.contains("domain:"))
        {
            Snackbar.make(view, "Whois completed :)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else
        {
            Snackbar.make(view, "No Whois entry found x(", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        actionButton.setEnabled(true);
    }
}
