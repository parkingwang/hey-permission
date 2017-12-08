/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.invoker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

class ActivityPermissionInvoker extends BasePermissionInvoker<Activity> {

    private final Class<? extends Activity> mSuperClass;

    ActivityPermissionInvoker(Activity delegate) {
        super(delegate);
        if (delegate instanceof AppCompatActivity) {
            mSuperClass = AppCompatActivity.class;
        } else if (delegate instanceof FragmentActivity) {
            mSuperClass = FragmentActivity.class;
        } else {
            mSuperClass = Activity.class;
        }
    }

    @Override
    public Context getContext() {
        return getDelegate();
    }

    @Override
    public final Class<? extends Activity> delegateSuperClass() {
        return mSuperClass;
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getDelegate(), permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getDelegate().startActivityForResult(intent, requestCode);
    }

    @Override
    public void executeRequestPermissions(int code, @NonNull String... permissions) {
        ActivityCompat.requestPermissions(getDelegate(), permissions, code);
    }

    @Override
    public boolean needHandleThisRequestCode(int requestCode) {
        if (getDelegate() instanceof AppCompatActivity) {
            // Handle only when support fragment request this permission.
            return ((requestCode >> 16) & 0xffff) == 0;
        } else {
            return true;
        }
    }
}