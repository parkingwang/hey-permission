/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.invoker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

class SupportFragmentPermissionInvoker extends BasePermissionInvoker<Fragment> {

    SupportFragmentPermissionInvoker(android.support.v4.app.Fragment delegate) {
        super(delegate);
    }

    @Override
    public Context getContext() {
        return getDelegate().getContext();
    }

    @Override
    public Class<android.support.v4.app.Fragment> delegateSuperClass() {
        return android.support.v4.app.Fragment.class;
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String... permissions) {
        for (String permission : permissions) {
            if (getDelegate().shouldShowRequestPermissionRationale(permission)) {
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
        getDelegate().requestPermissions(permissions, code);
    }

}