/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.support;

/**
 * Method utils.
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-12-04
 */
public class MethodUtils {

    /**
     * Create argument array with specified parameter types and values.
     *
     * @param paramTypes The specified parameter types.
     * @param values     The specified values.
     * @return Arguments
     */
    public static Object[] fillArgs(Class<?>[] paramTypes, Object[] values) {
        final Object[] args = new Object[paramTypes.length];
        final boolean[] used = new boolean[values.length];
        for (int i = 0; i < paramTypes.length; i++) {
            final Class<?> paramType = paramTypes[i];
            for (int j = 0; j < values.length; j++) {
                final Object value = values[j];
                if (!used[j] && ClassTypes.lenientlyEquals(paramType, value.getClass())) {
                    args[i] = value;
                    used[j] = true;
                    break;
                }
            }

            if (paramType.isPrimitive()) {
                if (boolean.class.equals(paramType)) {
                    args[i] = false;
                } else {
                    args[i] = 0;
                }
            }
        }
        return args;
    }
}
