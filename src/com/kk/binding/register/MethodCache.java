/*
 * Copyright (C) 2014 kk-team.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kk.binding.register;

import com.kk.binding.util.BindLog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuanjue.hk
 * @date 2014-1-7
 */
public class MethodCache {
    private static final String TAG = "Binding-MethodCache";
    private static HashMap<Class<?>, HashMap<String, Method>> methodCache;

    private static HashMap<Class<?>, HashMap<String, Method>> getMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<Class<?>, HashMap<String, Method>>(256);
        }
        return methodCache;
    }

    public static Method obtain(String methodName, Class<?> clazz) {
        HashMap<String, Method> methodHashMap = getMethodCache().get(clazz);
        BindLog.d(TAG, "obtain method: methodName = " + methodName
                + " Class = " + clazz);
        if (methodHashMap != null) {
            Method method = methodHashMap.get(methodName);
            if (method != null) {
                return method;
            } else {
                // try to find the super class that already has been registered
                for (Map.Entry<Class<?>, HashMap<String, Method>> entry : getMethodCache().entrySet()) {
                    if (entry.getKey() != clazz && entry.getKey().isAssignableFrom(clazz)) {
                        Method method1 = entry.getValue().get(methodName);
                        if (method1 != null) {
                            register(methodName, clazz, method1);
                            return method1;
                        }
                    }
                }
            }
        } else {
            // try to find the super class that already has been registered
            for (Map.Entry<Class<?>, HashMap<String, Method>> entry : getMethodCache().entrySet()) {
                if (entry.getKey() != clazz && entry.getKey().isAssignableFrom(clazz)) {
                    Method method1 = entry.getValue().get(methodName);
                    if (method1 != null) {
                        register(methodName, clazz, method1);
                        return method1;
                    }
                }
            }
        }
        BindLog.e(TAG, "Attempted to obtain an unregister method! methodName = " + methodName + " Class = " + clazz);
        return null;
    }

    public static void register(String methodName, Class<?> clazz, Method method) {
        if (methodName == null || clazz == null || method == null)
            throw new IllegalArgumentException("null argument");
        HashMap<String, Method> clazzHashMap = getMethodCache().get(clazz);
        if (clazzHashMap == null) {
            BindLog.d(TAG, "register method for class: " + clazz);
            clazzHashMap = new HashMap<String, Method>();
            getMethodCache().put(clazz, clazzHashMap);
        }
        clazzHashMap.put(methodName, method);
        BindLog.d(TAG, "register method: methodName = " + methodName + " class = " + clazz);
    }
}
