package com.wardrob.wardrob.screens.fragments.fragment_startup_creater;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.GeneralActivity;
import com.wardrob.wardrob.core.TakeImageHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;


public class FragmentStartupCreate extends Fragment implements FragmentStartupCreateView
{
    View view;

    private boolean change_fragment=false;

    private int DEFAULT_IMG_SIZE = 250;

    private FragmentStartupCreatePresenter presenter;
    public  String gender      = null;
    public  String avatarName  = null;

    ImageView img_avatar;
    EditText edt;
    RadioGroup rgb;
    private String path_to_picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_startup_creater,
                container,
                false);
        presenter = new FragmentStartupCreatePresenter(this);

        img_avatar = (ImageView) view.findViewById(R.id.img_avatar);
        edt = (EditText) view.findViewById(R.id.edtName);
        rgb = (RadioGroup) view.findViewById(R.id.rbgGender);
        Button bntSaveCreatedUser = (Button) view.findViewById(R.id.bntSaveCreatedUser);

        if(null != getArguments())
        {
            final Bitmap imageBitmap = BitmapFactory.decodeFile(getArguments().getString("img"));
            img_avatar.setImageBitmap(imageBitmap);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(DEFAULT_IMG_SIZE,
                                                                            DEFAULT_IMG_SIZE);
            parms.gravity = Gravity.CENTER;
            img_avatar.setLayoutParams(parms);

            edt.setText(getArguments().getString("name"));

            Integer radioButtonID = Integer.valueOf(getArguments().getString("gender"));
            RadioButton radioButton = (RadioButton) rgb.findViewById(radioButtonID);
            if(null != radioButton)
                radioButton.setChecked(true);
        }

        bntSaveCreatedUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText edtName  = (EditText) view.findViewById(R.id.edtName);
                avatarName = edtName.getText().toString();
                RadioGroup rbgGender = (RadioGroup) view.findViewById(R.id.rbgGender);
                int checkedRadioButtonId = rbgGender.getCheckedRadioButtonId();
                RadioButton mRdBtnGenderSelection = (RadioButton) view.findViewById(checkedRadioButtonId);
                gender = mRdBtnGenderSelection.getText().toString();


                if (presenter.object != null) {
                    presenter.saveNewMemberInDataBase(avatarName, gender);
                }
                else
                {
                    presenter.setAlarmDialog();
                }
            }
        });


        img_avatar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(presenter.createNewImage(getActivity()),
                        TakeImageHelper.REQUEST_IMAGE_SELECT);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        change_fragment=true;

        if (resultCode == RESULT_OK)
        {
            if (requestCode == TakeImageHelper.REQUEST_IMAGE_SELECT)
            {
                final boolean isCamera;
                if (data == null || data.getData() == null)
                {
                    isCamera = true;
                }
                else
                {
                    final String action = data.getAction();
                    if (action == null)
                    {
                        isCamera = false;
                    }
                    else
                    {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;

                if (isCamera)
                {
                    try
                    {
                        byte[] imgData = FileManagement.getImageFileInByteArray(presenter.object.destination.getPath());
                        Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
                        img_avatar.setImageBitmap(imageBitmap);
                    }
                    catch (NullPointerException e){
                        Timber.e(e.toString());}
                }
                else // -------------------------------- GALLERY ------------------------------------
                {
                    selectedImageUri = data == null ? null : data.getData();
                    Timber.d("selectedImageUri: %s",
                            GeneralActivity.getPath(getActivity().getBaseContext(), selectedImageUri));

                    String filePath = GeneralActivity.getPath(getActivity().getBaseContext(), selectedImageUri);
                    //final String fileName = new File(filePath).getName();
                    File imgFile = new  File(filePath);


                    long length = imgFile.length();
                    byte[] imgData = new byte[(int) length];

                    FileInputStream pdata = null;
                    try {pdata = new FileInputStream(imgFile);}
                    catch (FileNotFoundException e1){e1.printStackTrace();}

                    try {pdata.read(imgData);}
                    catch (IOException e) {e.printStackTrace();}

                    if(imgFile.exists())
                    {
                        if(null != presenter.object)
                        {
                            FileManagement.createFile(presenter.object.destination.getPath(), imgData);
                            File file = new File(presenter.object.destination.getPath());
                            presenter.object.destination = file;

                            final Bitmap imageBitmap = BitmapFactory.decodeFile(presenter.object.destination.getAbsolutePath());
                            img_avatar.setImageBitmap(imageBitmap);

                            Timber.d("\n\nImage path to draw: %s", presenter.object.destination.getAbsolutePath());

                            path_to_picture = presenter.object.destination.getAbsolutePath();

                        }
                    }
                    else
                    {
                        Timber.e("Cant not create file: %s",
                                GeneralActivity.getPath(getActivity().getBaseContext(), selectedImageUri));
                    }
                }
            }

            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(DEFAULT_IMG_SIZE,
                                                                            DEFAULT_IMG_SIZE);
            parms.gravity = Gravity.CENTER;
            img_avatar.setLayoutParams(parms);
            Timber.d("\n\nImage was set to layout here");
        }
//       super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    /**
     * Function: getThis
     * @return Context
     */
    @Override
    public Context getThis()
    {
        return getContext();
    }

    @Override
    public void closeActivity()
    {
        Timber.d("\n\n!!!!!!!!!!!!!closeActivity!!!!!!!!!!");
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onStop()
    {
        if(change_fragment)
        {
            change_fragment=false;
            Fragment fragment = new FragmentStartupCreate();
            //-------------------------------------------------------------------------------------
            Bundle args = new Bundle();
            args.putString("img", path_to_picture);
            args.putString("name", edt.getText().toString());

            Integer index = rgb.getCheckedRadioButtonId();
            args.putString("gender", index.toString());
            fragment.setArguments(args);
            //-------------------------------------------------------------------------------------
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.startup_container, fragment).commit();
        }
        super.onStop();
    }
}
