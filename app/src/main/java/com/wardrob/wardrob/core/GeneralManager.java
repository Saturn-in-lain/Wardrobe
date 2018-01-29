package com.wardrob.wardrob.core;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

public class GeneralManager
{

    public GeneralManager(){}

    /**
     * Function: getScreenSize()
     * @Description:
     * @param activity {@link Activity}
     * @Note
     */
    public static Point getScreenSize(Activity activity)
    {
        Point size = new Point();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size;
    }

}
