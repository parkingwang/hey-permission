/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.invoker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.parkingwang.heypermission.callback.PermissionRequestExecutor;

/**
 * Delegate class to make permission calls.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-11-30
 */
public abstract class BasePermissionInvoker<T> implements PermissionRequestExecutor {

    private final T mDelegate;

    BasePermissionInvoker(T delegate) {
        mDelegate = delegate;
    }

    public final T getDelegate() {
        return mDelegate;
    }

    /**
     * Return the calling context
     *
     * @return The calling context
     */
    public abstract Context getContext();

    /**
     * The class object of T
     *
     * @return T.class
     */
    public abstract <SubType extends T> Class<SubType> delegateSuperClass();

    /**
     * Gets whether you should show UI with rationale for requesting a permission.
     *
     * @param permissions The permissions your app wants to request
     * @return Whether you should show permission rationale UI.
     * @see Activity#shouldShowRequestPermissionRationale(String)
     */
    public abstract boolean shouldShowRequestPermissionRationale(@NonNull String... permissions);

    /**
     * Same as calling {@link Activity#startActivityForResult(Intent, int)}
     *
     * @param intent      The intent to start.
     * @param requestCode If >= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public abstract void startActivityForResult(Intent intent, int requestCode);

    /**
     * Whether the invoker should handle the special request code.
     *
     * @param requestCode The request code.
     * @return True if should handle, otherwise false.
     */
    public boolean needHandleThisRequestCode(int requestCode) {
        return true;
    }

    @NonNull
    public static BasePermissionInvoker newInstance(Activity activity) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return new LowApiPermissionInvoker(activity);
//        }
        return new ActivityPermissionInvoker(activity);
    }

    @NonNull
    public static BasePermissionInvoker newInstance(Fragment fragment) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return new LowApiPermissionInvoker(fragment);
//        }
        return new FragmentPermissionInvoker(fragment);
    }

    @NonNull
    public static BasePermissionInvoker newInstance(android.support.v4.app.Fragment fragment) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return new LowApiPermissionInvoker(fragment);
//        }
        return new SupportFragmentPermissionInvoker(fragment);
    }
}
