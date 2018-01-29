package com.wardrob.wardrob.I_startup_login_chooser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wardrob.wardrob.I_startup_new_member.LoginCreateActivity;
import com.wardrob.wardrob.R;


public class LoginChooserActivity extends AppCompatActivity implements LoginChooserView
{

    private LoginChooserPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_chooser);

        presenter = new LoginChooserPresenter(this);
    }



    /**
     * Function: onCreateNewMember
     * @param view {@link View}
     */
    public void onCreateNewMember(View view)
    {
        Intent intent = new Intent(this, LoginCreateActivity.class);
        this.startActivity(intent);
    }


    /**
     * Function: onSaveAvatarButton
     * @param view {@link View}
     */
    public void onSaveAvatarButton(View view)
    {
        finish();
    }

    /**
     * Function: getThis
     * @return Context
     */
    @Override
    public Context getThis()
    {
        return this;
    }


    /**
     * Function: getAvailableUsersLayout
     * @return LinearLayout
     */
    @Override
    public LinearLayout getAvailableUsersLayout()
    {
        LinearLayout lAvatartWithFamilyMembers =
                                    (LinearLayout) findViewById(R.id.lAvatartWithFamilyMembers);
        return  lAvatartWithFamilyMembers;
    }
}
