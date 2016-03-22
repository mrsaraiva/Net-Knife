package matrix.the.net_knife.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Marcos Saraiva on 20/03/2016.
 */
public class CommonUtil
{
    public static void hideKeyboardFrom(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static CustomEditText.TextChangedListener editTextChanged()
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

    public static String getDataDir()
    {
        try
        {
            return ContextBean.getLocalContext().getPackageManager().getPackageInfo(
                    ContextBean.getLocalContext().getPackageName(), 0).applicationInfo.dataDir;
        }
        catch (Exception e)
        {
            Log.w("Your Tag", "Data Directory error:", e);
            return null;
        }
    }

    // read more about Environment class : http://developer.android.com/reference/android/os/Environment.html
    public static String getDownloadFolder()
    {
        return ContextBean.getLocalContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString();
    }

    public static String getBusyboxBin()
    {
        String arch = SystemProperties.get("ro.product.cpu.abilist");

        if (arch.contains("arm") || arch.contains("arch64"))
        {
            return "libbusybox_arm.so";
        }
        else if (arch.contains("i686") || arch.contains("x86"))
        {
            return "libbusybox_intel.so";
        }

        return "libbusybox_arm.so";
    }
}
