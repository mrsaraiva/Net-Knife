package matrix.the.net_knife.utils;

import android.content.Context;

/**
 * Created by Marcos Saraiva on 20/03/2016.
 */

/*
 * This will help you to use application context in any file with in project.
 */
public final class ContextBean
{
    private static Context localContext;

    public static Context getLocalContext()
    {
        return localContext;
    }

    public static void setLocalContext(Context localContext)
    {
        ContextBean.localContext = localContext;
    }
}
