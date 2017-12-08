/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that a method to call after the permissions was denied forever.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-11-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionsDenied {
    /**
     * @return The request code. It it default empty that means receiving all request codes.
     */
    int[] requestCodes() default {};
}
