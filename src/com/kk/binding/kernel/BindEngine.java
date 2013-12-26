/*
 * Copyright (C) 2013 kk-team.com.
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
package com.kk.binding.kernel;

import android.content.Context;

import com.kk.binding.util.BindDesignLog;
import com.kk.binding.util.StringUtil;

/**
 * Created by hk on 13-12-10.
 */
public class BindEngine {
    private static final String TAG = "BindEngine";
    private static BindEngine bindEngine;
    private static IBindValueSetter bindValueSetter;
    private static String propertyDeclareClass = "";
    private Context mContext;

    public static BindEngine instance() {
        if (bindEngine == null) {
            bindEngine = new BindEngine();
        }
        return bindEngine;
    }

    private BindEngine() {
    }

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public static void setBindValueSetter(IBindValueSetter bindValueSetter) {
        BindEngine.bindValueSetter = bindValueSetter;
    }

    public static IBindValueSetter getBindValueSetter() {
        return bindValueSetter;
    }

    /**
     * @param propertyDeclareClassName
     */
    public static void registerPropertyDeclareClass(String propertyDeclareClassName) {
        if (!StringUtil.compare(propertyDeclareClass, propertyDeclareClassName)) {
            propertyDeclareClass = propertyDeclareClassName;
            BindDesignLog.d(TAG, "propertyDeclareClass =  " + propertyDeclareClass);
            if (!StringUtil.isNullOrEmpty(propertyDeclareClass)) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(propertyDeclareClass);
                    BindDesignLog.d(TAG, "parse propertyDeclareClass success " + clazz.toString());
                } catch (ClassNotFoundException e) {
                    if (BindDesignLog.isInDesignMode())
                        throw new RuntimeException("parse propertyDeclareClass failed " + e.toString());
                    BindDesignLog.e(TAG, "parse propertyDeclareClass failed " + e.toString());
                }
                if (clazz != null) {
                    try {
                        Object instance = clazz.newInstance();
                        if (instance != null)
                            BindDesignLog.d(TAG, "create propertyDeclareClass instance success " + instance.toString());
                    } catch (Exception e) {
                        if (BindDesignLog.isInDesignMode())
                            throw new RuntimeException("create propertyDeclareClass instance failed " + e.toString());
                        BindDesignLog.e(TAG, "create propertyDeclareClass instance failed " + e.toString());
                    }
                }
            }
        }
    }

}
