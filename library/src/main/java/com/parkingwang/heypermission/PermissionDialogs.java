/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.parkingwang.heypermission.callback.PermissionRequestExecutor;
import com.parkingwang.heypermission.invoker.BasePermissionInvoker;


/**
 * Provide some default dialogs for permission requesting.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-12-01
 */
public class PermissionDialogs {

    public static void showDefaultRationaleDialog(
            @NonNull Context context, @NonNull final PermissionRequestExecutor executor,
            @IntRange(from = 0) final int code,
            @NonNull @Size(min = 1) final String... permissions) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.hey_permission_request_title)
                .setMessage(R.string.hey_permission_request_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.executeRequestPermissions(code, permissions);
                    }
                })
                .setCancelable(false)
                .show();
    }

    public static void showDefaultAppSettingsDialog(@NonNull final Activity activity,
                                                    final int requestCode) {
        showDefaultAppSettingsDialog(BasePermissionInvoker.newInstance(activity), requestCode);
    }

    public static void showDefaultAppSettingsDialog(@NonNull final Fragment fragment,
                                                    int requestCode) {
        showDefaultAppSettingsDialog(BasePermissionInvoker.newInstance(fragment), requestCode);
    }

    public static void showDefaultAppSettingsDialog(
            @NonNull final android.support.v4.app.Fragment fragment, int requestCode) {
        showDefaultAppSettingsDialog(BasePermissionInvoker.newInstance(fragment), requestCode);
    }

    private static void showDefaultAppSettingsDialog(@NonNull final BasePermissionInvoker invoker,
                                                     final int requestCode) {
        final Context context = invoker.getContext();
        final DialogInterface.OnClickListener settingListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invoker.startActivityForResult(
                                PermissionDialogs.appSettingsIntent(context), requestCode);
                    }
                };
        showAppSettingsDialog(context, R.string.hey_permission_permission_denied,
                settingListener, null);
    }

    public static void showAppSettingsDialogAndFinishWhenDismissed(
            @NonNull final Activity activity, @StringRes int msgId) {
        showAppSettingsDialog(activity, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(
                        PermissionDialogs.appSettingsIntent(activity));
            }
        }, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activity.finish();
            }
        });
    }

    public static void showAppSettingsDialog(
            @NonNull final Context context, @StringRes int msgId,
            @NonNull DialogInterface.OnClickListener settingListener,
            @Nullable DialogInterface.OnDismissListener dismissListener) {
        new AlertDialog.Builder(context)
                .setMessage(msgId)
                .setPositiveButton(R.string.hey_permission_go_to_setting, settingListener)
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .setOnDismissListener(dismissListener)
                .show();
    }

    /**
     * @param context The context of the app
     * @return The intent to show system settings
     */
    public static Intent appSettingsIntent(@NonNull Context context) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.getPackageName(), null));
    }
}
