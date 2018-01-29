package com.wardrob.wardrob.core;

import android.content.Context;

public class ResourcesGetterSingleton
{

    public static volatile Context ctx = null;

    public static Context getInstance(Context context)
    {
        if (null == ctx)
        {
            ctx = context;
        }
        return ctx;
    }

    public static String getStr(int id)
    {
        return ctx.getResources().getText(id).toString();
    }


    public static void killInstance()
    {
        ctx = null;
    }
}
