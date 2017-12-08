/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.parkingwang.heypermission.annotations.PermissionsDenied;
import com.parkingwang.heypermission.annotations.PermissionsGranted;
import com.parkingwang.heypermission.annotations.PermissionsNeverAskAgain;
import com.parkingwang.heypermission.annotations.PermissionsResult;
import com.parkingwang.heypermission.callback.RationaleCallback;
import com.parkingwang.heypermission.invoker.BasePermissionInvoker;
import com.parkingwang.heypermission.support.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility to check and request runtime permissions for app targeting Android M.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-11-30 0.1
 */
public class HeyPermission {

    private static final String TAG = "HeyPermission";

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     The calling context
     * @param permissions One or more permission.
     * @return True if all permissions are already granted, false if at least one permission is not
     * yet granted.
     * @see android.Manifest.permission
     */
    public static boolean hasPermissions(@NonNull Context context,
                                         @NonNull @Size(min = 1) String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        final AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        final String packageName = context.getPackageName();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            String op = AppOpsManager.permissionToOp(permission);
            if (!TextUtils.isEmpty(op) && appOpsManager != null
                    && appOpsManager.noteProxyOp(op, packageName) != AppOpsManager.MODE_ALLOWED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(@NonNull Activity host,
                                          @IntRange(from = 0) int requestCode,
                                          @Size(min = 1) @NonNull String[]... permissions) {
        requestPermissions(BasePermissionInvoker.newInstance(host), requestCode, permissions);
    }

    public static void requestPermissions(@NonNull Fragment fragment,
                                          @IntRange(from = 0) int requestCode,
                                          @Size(min = 1) @NonNull String[]... permissions) {
        requestPermissions(BasePermissionInvoker.newInstance(fragment), requestCode, permissions);
    }

    public static void requestPermissions(@NonNull android.support.v4.app.Fragment fragment,
                                          @IntRange(from = 0) int requestCode,
                                          @Size(min = 1) @NonNull String[]... permissions) {
        requestPermissions(BasePermissionInvoker.newInstance(fragment), requestCode, permissions);
    }

    private static void requestPermissions(@NonNull BasePermissionInvoker invoker,
                                           @IntRange(from = 0) int requestCode,
                                           @Size(min = 1) @NonNull String[]... permissionSets) {
        final List<String> permissionList = new ArrayList<>();
        for (String[] permissionSet : permissionSets) {
            permissionList.addAll(Arrays.asList(permissionSet));
        }
        final String[] permissions = permissionList.toArray(new String[permissionList.size()]);
        if (hasPermissions(invoker.getContext(), permissions)) {
            notifyAlreadyHasPermissions(invoker, requestCode, permissions);
            return;
        }
        if (invoker.shouldShowRequestPermissionRationale(permissions)) {
            if (invokeShowRationaleMethod(false, invoker, requestCode, permissions)) {
                return;
            }
        }
        invoker.executeRequestPermissions(requestCode, permissions);
    }

    private static void notifyAlreadyHasPermissions(
            @NonNull BasePermissionInvoker invoker, @IntRange(from = 0) int requestCode,
            @Size(min = 1) @NonNull String... permissions) {
        final int[] grantResults = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            grantResults[i] = PackageManager.PERMISSION_GRANTED;
        }
        onRequestPermissionsResult(invoker, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(
            Activity activity, @IntRange(from = 0) int requestCode,
            @Size(min = 1) @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(BasePermissionInvoker.newInstance(activity),
                requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(
            Fragment fragment, @IntRange(from = 0) int requestCode,
            @Size(min = 1) @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(BasePermissionInvoker.newInstance(fragment),
                requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(
            android.support.v4.app.Fragment fragment, @IntRange(from = 0) int requestCode,
            @Size(min = 1) @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(BasePermissionInvoker.newInstance(fragment),
                requestCode, permissions, grantResults);
    }

    private static void onRequestPermissionsResult(
            @NonNull BasePermissionInvoker invoker, @IntRange(from = 0) int requestCode,
            @Size(min = 1) @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!invoker.needHandleThisRequestCode(requestCode)) {
            return;
        }

        final List<String> granted = new ArrayList<>();
        final List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permissions[i]);
            } else {
                denied.add(permissions[i]);
            }
        }

        if (denied.isEmpty()) {
            // all permissions were granted
            invokePermissionsResultMethod(PermissionsGranted.class, invoker, requestCode, granted);
            invokePermissionsResultMethod(PermissionsResult.class, invoker, requestCode, denied);
            return;
        }
        final String[] deniedPermissions = denied.toArray(new String[denied.size()]);
        boolean neverAskAgain = true;
        if (invoker.shouldShowRequestPermissionRationale(deniedPermissions)) {
            neverAskAgain = false;
            if (invokeShowRationaleMethod(true, invoker, requestCode, deniedPermissions)) {
                return;
            }
        }
        if (neverAskAgain) {
            invokePermissionsResultMethod(PermissionsNeverAskAgain.class,
                    invoker, requestCode, denied);
        } else {
            invokePermissionsResultMethod(PermissionsDenied.class, invoker, requestCode, denied);
        }
        invokePermissionsResultMethod(PermissionsResult.class, invoker, requestCode, denied);
    }

    private static void invokePermissionsResultMethod(
            Class<? extends Annotation> annotationClass, @NonNull BasePermissionInvoker invoker,
            @IntRange(from = 0) int requestCode, List<String> permissions) {
        final Object receiver = invoker.getDelegate();
        final Class topSuperClass = invoker.delegateSuperClass();
        Class cls = receiver.getClass();
        while (cls != null && cls != topSuperClass) {
            for (Method method : cls.getDeclaredMethods()) {
                if (isThePermissionResultMethod(method, annotationClass, requestCode)) {
                    try {
                        final Class<?>[] paramTypes = method.getParameterTypes();
                        final Object[] values = new Object[]{requestCode, permissions};
                        method.invoke(receiver, MethodUtils.fillArgs(paramTypes, values));
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, "Invoke permissions granted method failed:", e);
                    } catch (InvocationTargetException e) {
                        Log.e(TAG, "Invoke permissions granted method failed:", e);
                    }
                    return;
                }
            }
            cls = cls.getSuperclass();
        }
        Log.i(TAG, "Could not find any method with @" + annotationClass.getSimpleName()
                + " annotation to invoke");
    }

    private static boolean isThePermissionResultMethod(
            Method method, Class<? extends Annotation> cls, @IntRange(from = 0) int requestCode) {
        final Annotation annotation = method.getAnnotation(cls);
        if (annotation == null || annotation.annotationType() != cls) {
            return false;
        }
        if (annotation instanceof PermissionsGranted) {
            return ((PermissionsGranted) annotation).requestCode() == requestCode;
        } else if (annotation instanceof PermissionsDenied) {
            return checkRequestCodes(requestCode, ((PermissionsDenied) annotation).requestCodes());
        } else if (annotation instanceof PermissionsResult) {
            return checkRequestCodes(requestCode, ((PermissionsResult) annotation).requestCodes());
        } else if (annotation instanceof PermissionsNeverAskAgain) {
            return checkRequestCodes(requestCode, ((PermissionsNeverAskAgain) annotation).requestCodes());
        }
        return false;
    }

    private static boolean checkRequestCodes(@IntRange(from = 0) int requestCode, int[] codes) {
        if (codes.length == 0) {
            return true;
        }
        for (int code : codes) {
            if (code == requestCode) {
                return true;
            }
        }
        return false;
    }

    private static boolean invokeShowRationaleMethod(
            boolean callOnResult, @NonNull BasePermissionInvoker invoker, @IntRange(from = 0) int requestCode,
            @Size(min = 1) @NonNull String... permissions) {
        final Object receiver = invoker.getDelegate();
        return receiver instanceof RationaleCallback
                && ((RationaleCallback) receiver).onShowRationale(invoker, requestCode, permissions,
                callOnResult);
    }
}
