package com.wardrob.wardrob.I_startup_login_chooser;

import android.content.Context;
import android.widget.LinearLayout;

public interface LoginChooserView
{
    Context getThis();

    LinearLayout getAvailableUsersLayout();
}
