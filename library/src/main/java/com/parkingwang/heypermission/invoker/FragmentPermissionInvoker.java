/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.invoker;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

class FragmentPermissionInvoker extends BasePermissionInvoker<Fragment> {

    FragmentPermissionInvoker(Fragment delegate) {
        super(delegate);
    }

    @Override
    public Context getContext() {
        return getDelegate().getActivity();
    }

    @Override
    public final Class<Fragment> delegateSuperClass() {
        return Fragment.class;
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getDelegate().requestPermissions(permissions, code);
        }
    }
}
