package matrix.the.net_knife.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import matrix.the.net_knife.R;

/**
 * Created by Marcos Saraiva on 21/03/2016.
 */
public class CustomEditText extends LinearLayout
{
    protected EditText editText;
    protected ImageButton clearTextButton;

    public interface TextChangedListener extends TextWatcher
    {
    }

    TextChangedListener editTextListener = null;

    public void addTextChangedListener(TextChangedListener listener)
    {
        this.editTextListener = listener;
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener)
    {
        this.editText.setOnEditorActionListener(onEditorActionListener);
    }

    public Editable getText()
    {
        return this.editText.getText();
    }

    public void setImeOptions(int imeOptions)
    {
        this.editText.setImeOptions(imeOptions);
    }

    public int getImeOptions()
    {
        return this.editText.getImeOptions();
    }

    public void setImeActionLabel(CharSequence label, int actionId)
    {
        this.editText.setImeActionLabel(label, actionId);
    }


    public CustomEditText(Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.activity_main, this);
    }

    public CustomEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle)
    {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0);
        String hintText;
        int deleteButtonRes;
        try
        {
            // get the text and colors specified using the names in attrs.xml
            hintText = a.getString(R.styleable.CustomEditText_hintText);
            deleteButtonRes = a.getResourceId(R.styleable.CustomEditText_deleteButtonRes, R.drawable.ic_text_field_clear_btn);

        }
        finally
        {
            a.recycle();
        }
        editText = createEditText(context, hintText);
        clearTextButton = createImageButton(context, deleteButtonRes);

        this.addView(editText);
        this.addView(clearTextButton);


        editText.addTextChangedListener(txtEntered());


        editText.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && editText.getText().toString().length() > 0)
                {
                    buttonAnimation(0);
                }
                else
                {
                    buttonAnimation(1);
                }

            }
        });
        clearTextButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                editText.setText("");
            }
        });
    }

    public TextWatcher txtEntered()
    {
        return new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (editTextListener != null)
                {
                    editTextListener.onTextChanged(s, start, before, count);
                }

                if (editText.getText().toString().length() > 0)
                {
                    if (editText.getText().toString().length() == 1 && !(clearTextButton.getVisibility() == View.VISIBLE))
                    {
                        buttonAnimation(0);
                    }
                }
                else
                {
                    buttonAnimation(1);
                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (editTextListener != null)
                {
                    editTextListener.afterTextChanged(s);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if (editTextListener != null)
                {
                    editTextListener.beforeTextChanged(s, start, count, after);
                }

            }

        };
    }

    @SuppressLint("NewApi")
    private EditText createEditText(Context context, String hintText)
    {
        editText = new EditText(context);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
        //editText.setHorizontallyScrolling(false);
        //editText.setVerticalScrollBarEnabled(true);
        editText.setHint(hintText);
        editText.setGravity(Gravity.LEFT);
        editText.setBackground(null);
        editText.setSingleLine();
        editText.setPadding(30, 0, 0, 0);
        return editText;
    }

    private ImageButton createImageButton(Context context, int deleteButtonRes)
    {
        clearTextButton = new ImageButton(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.CENTER_VERTICAL;
        clearTextButton.setLayoutParams(params);
        clearTextButton.setBackgroundResource(deleteButtonRes);
        clearTextButton.setVisibility(View.GONE);
        return clearTextButton;
    }

    private void buttonAnimation(int effect)
    {
        AlphaAnimation fade_in = new AlphaAnimation(0.0f, 1.0f);
        fade_in.setDuration(500);
        fade_in.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationStart(Animation arg0)
            {
            }

            public void onAnimationRepeat(Animation arg0)
            {
            }

            public void onAnimationEnd(Animation arg0)
            {
                clearTextButton.setVisibility(View.VISIBLE);
            }
        });

        AlphaAnimation fade_out = new AlphaAnimation(1.0f, 0.0f);
        fade_out.setDuration(500);
        fade_out.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationStart(Animation arg0)
            {
            }

            public void onAnimationRepeat(Animation arg0)
            {
            }

            public void onAnimationEnd(Animation arg0)
            {
                clearTextButton.setVisibility(View.GONE);
            }
        });

        if (effect == 0)
        {
            clearTextButton.setVisibility(View.INVISIBLE);
            clearTextButton.startAnimation(fade_in);
        }
        else
        {
            clearTextButton.startAnimation(fade_out);
        }
    }
}
