/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.callback;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

/**
 * PermissionRequestExecutor is an interface that defines an executor request permissions actually.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-12-01
 */
public interface PermissionRequestExecutor {

    /**
     * Execute permission request
     *
     * @param code        The request code
     * @param permissions The permissions your app wants to request
     */
    void executeRequestPermissions(@IntRange(from = 0) int code,
                                   @NonNull @Size(min = 1) String... permissions);

}
