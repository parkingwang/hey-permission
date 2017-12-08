/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.compat;

import android.annotation.SuppressLint;
import android.hardware.Camera;

/**
 * Camera permission compat checker for device that OS version below Android M.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-12-04
 */
public class CameraPermission {
    public static final String FAIL_TO_CONNECT = "Fail to connect to camera service";
    public static final String INITIALIZATION_FAILED = "Camera initialization failed";

    /**
     * Check whether the exception was cause by permission on device that version below M.
     *
     * @param camera {@link android.hardware.Camera} object
     * @param e      The camera exception
     * @return Whether cause by permission.
     */
    @SuppressLint("deprecation")
    public static boolean maybePermissionProblem(Camera camera, Exception e) {
        return camera == null || FAIL_TO_CONNECT.equals(e.getMessage());
    }
}
