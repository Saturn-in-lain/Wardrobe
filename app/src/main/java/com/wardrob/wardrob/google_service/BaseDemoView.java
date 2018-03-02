package com.wardrob.wardrob.google_service;


import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.tasks.Task;

public interface BaseDemoView
{

    GoogleApiClient getGoogleApiClient();

    Context getThis();

    DriveResourceClient getDriveResourceClient();

    Task<DriveId> pickItem(OpenFileActivityOptions openOptions);
}
