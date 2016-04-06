package matrix.the.net_knife.fragments;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.CommonUtil;
import matrix.the.net_knife.utils.CustomEditText;
import matrix.the.net_knife.utils.ProcessStream.ProcessStreamReader;
import matrix.the.net_knife.utils.ShellProcess.OnComplete;

/**
 * A placeholder fragment containing a simple view.
 */
public class DNSLookupFragment extends Fragment implements ProcessStreamReader, OnComplete
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

    public DNSLookupFragment()
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
        view = inflater.inflate(R.layout.fragment_dnslookup, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        actionButton = (Button) view.findViewById(R.id.dnsButton);
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

        inputEditText = (CustomEditText) view.findViewById(R.id.dnsEditText);
        inputEditText.addTextChangedListener(CommonUtil.editTextChanged());
        inputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputEditText.setImeActionLabel("LOOKUP", 5);
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

        consoleTextView = (TextView) view.findViewById(R.id.dnsResultText);
        consoleTextView.setText("Input a valid hostname and press the button to query the DNS server");

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

    private CustomEditText.TextChangedListener editTextChanged()
    {
        return new CustomEditText.TextChangedListener()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };
    }

    public void doAction()
    {
        String[] args = new String[2];
        args[0] = inputEditText.getText().toString();
        args[1] = null;

        consoleTextView.setText("");
        textBuffer = "";
        i = 0;

        if ((mItem.worker != null && !mItem.worker.checkArgs(args)) || (mItem.tworker != null && !mItem.tworker.checkArgs(args)))
        {
            consoleTextView.setText("Please enter a valid hostname (like google.com)");
        }
        else
        {
            CommonUtil.hideKeyboardFrom(this.getContext(), view);
            consoleTextView.setText("Querying nameserver about hostname " + args[0] + ", please wait");
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

        i++;

        mHandler.post(mUpdateResults);
        System.out.println(sline);
    }

    @Override
    public void onComplete(String results)
    {
        if (textBuffer.contains("Name:"))
        {
            Snackbar.make(view, "DNS Lookup finished :)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else
        {
            Snackbar.make(view, "No DNS A entry found x(", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        actionButton.setEnabled(true);
    }
}
