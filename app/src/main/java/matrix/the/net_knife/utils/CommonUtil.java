package matrix.the.net_knife.utils;

import android.os.Environment;
import android.util.Log;

/**
 * Created by Marcos Saraiva on 20/03/2016.
 */
public class CommonUtil
{
    public static String getDataDir()
    {
        try
        {
            return ContextBean.getLocalContext().getPackageManager().getPackageInfo(
                    ContextBean.getLocalContext().getPackageName(), 0).applicationInfo.dataDir;
        } catch (Exception e)
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
        String arch = System.getProperty("os.arch");

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
