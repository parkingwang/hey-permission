/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.callback;

/**
 * Callback when you should show UI with rationale for requesting a permission.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-11-30
 */
public interface RationaleCallback {

    /**
     * Call when you should show UI with rationale for requesting a permission.
     *
     * @param executor    The permission request executor
     * @param requestCode The request code
     * @param permissions One or more permission
     * @param onResult    Whether be call on request permissions result.
     * @return Whether has show UI with rationale.
     */
    boolean onShowRationale(PermissionRequestExecutor executor, int requestCode,
                            String[] permissions, boolean onResult);
}
