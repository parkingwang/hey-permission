/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.parkingwang.heypermission.support;

/**
 * Reference from https://github.com/yoojia/NextEvents
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-12-1
 */
public class ClassTypes {

    /**
     * 获取指定类型的包装类型。包装类型只对Java基础类型生效，其它类型返回本身类型。
     *
     * @param type 指定类型
     * @return 包装类型或者本身类型
     */
    private static Class<?> wrap(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        if (byte.class.equals(type)) {
            return Byte.class;
        } else if (short.class.equals(type)) {
            return Short.class;
        } else if (int.class.equals(type)) {
            return Integer.class;
        } else if (long.class.equals(type)) {
            return Long.class;
        } else if (float.class.equals(type)) {
            return Float.class;
        } else if (double.class.equals(type)) {
            return Double.class;
        } else if (char.class.equals(type)) {
            return Character.class;
        } else if (boolean.class.equals(type)) {
            return Boolean.class;
        } else {
            return type;
        }
    }

    /**
     * 宽泛的比较类型是否相等。如果是Java基础类型，则包装类型与原类型也相等。
     *
     * @param a 需要比较的类型
     * @param b 需要比较的类型
     * @return 宽泛的比较类型是否相等
     */
    public static boolean lenientlyEquals(Class<?> a, Class<?> b) {
        return wrap(a).equals(wrap(b));
    }
}
