/**
 *
 */
package com.kk.binding.util;

/**
 * @author xuanjue.hk
 * @date 2013-5-28
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean compare(String str1, String str2) {
        return (str1 == null || str2 == null) ? str1 == str2 : str1.equals(str2);
    }
}