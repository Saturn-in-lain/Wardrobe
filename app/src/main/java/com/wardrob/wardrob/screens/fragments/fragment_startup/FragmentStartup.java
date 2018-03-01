package com.wardrob.wardrob.screens.fragments.fragment_startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wardrob.wardrob.screens.fragments.fragment_startup_creater.FragmentStartupCreate;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.screens.mainscreen.MainGlobalActivity;

public class FragmentStartup extends Fragment implements FragmentStartupView
{

    View view;
    FragmentStartupPresenter presenter;
    String status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_startup,
                                container,false);
        presenter = new FragmentStartupPresenter(this);
        status = getArguments().getString(ResourcesGetterSingleton.getStr(R.string.bundle_state));

        Button login_button = (Button) view.findViewById(R.id.login_button);

        if(!status.equals("user_available"))
        {
            login_button.setText(ResourcesGetterSingleton.getStr(R.string.login_create));
        }
        else
        {
            ImageView image_logo        = (ImageView) view.findViewById(R.id.image_logo);
            TextView login_welcome_text = (TextView) view.findViewById(R.id.login_welcome_text);

            presenter.setActiveUser(image_logo, login_welcome_text);
            login_button.setText(ResourcesGetterSingleton.getStr(R.string.login_button_text));
        }


        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent;
                if(!status.equals("user_available"))
                {
                    intent = new Intent(view.getContext(), FragmentStartupCreate.class);
                }
                else
                {
                    intent = new Intent(view.getContext(), MainGlobalActivity.class);
                }
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public Context getThis() {
        return view.getContext();
    }
}
