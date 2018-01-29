package com.wardrob.wardrob.I_startup_new_member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wardrob.wardrob.I_startup.GeneralActivity;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.SpeechRecognitionHelper;
import com.wardrob.wardrob.core.TakeImageHelper;


public class LoginCreateActivity extends GeneralActivity implements LoginCreateView
{

    private int DEFAULR_IMG_SIZE = 250;

    private LoginCreatePresenter presenter;
    public  String gender      = null;
    public  String avatarName  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_create);
        presenter = new LoginCreatePresenter(this);
    }


    /**
     * Function: onCreateFamilyMemberButton
     * @param view
     */
    public void onCreateFamilyMemberButton(View view)
    {
        EditText edtName  = (EditText) findViewById(R.id.edtName);
        this.avatarName = edtName.getText().toString();

        RadioGroup rbgGender = (RadioGroup) findViewById(R.id.rbgGender);
        int checkedRadioButtonId = rbgGender.getCheckedRadioButtonId();
        RadioButton mRdBtnGenderSelection = (RadioButton) findViewById(checkedRadioButtonId);
        this.gender = mRdBtnGenderSelection.getText().toString();

        presenter.saveNewMemberInDataBase(this.avatarName, this.gender);
    }

    /**
     * Function: closeActivity
     */
    public void closeActivity()
    {
        finish();
    }

    /**
     * Function: onMakeImageForAvatarButton
     * @param view
     */
    public void onMakeImageForAvatarButton(View view)
    {
        startActivityForResult(presenter.createNewImage(this),
                                TakeImageHelper.REQUEST_IMAGE_SELECT);
    }

    /**
     * Function: onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ImageView img_avatar = (ImageView) findViewById(R.id.img_avatar);

        onResponseActions(requestCode,
                          resultCode,
                          data,
                          presenter.object,
                          this,
                          img_avatar);

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(DEFAULR_IMG_SIZE,
                                                                        DEFAULR_IMG_SIZE);
        parms.gravity = Gravity.CENTER;
        img_avatar.setLayoutParams(parms);

        if (resultCode == RESULT_OK && null != data)
        {
            if (requestCode == SpeechRecognitionHelper.REQ_CODE_SPEECH_INPUT)
            {
                // ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // EditText edtTxtDownloadReportAdditionalComments = (EditText) findViewById(R.id.edtTxtDownloadReportAdditionalComments);
                // edtTxtDownloadReportAdditionalComments.setText(matches.get(0).toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
