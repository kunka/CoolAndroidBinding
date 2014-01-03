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
package com.kk.binding.register;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.kk.binding.kernel.DependencyProperty;
import com.kk.binding.util.BindDesignLog;
import com.kk.binding.view.ViewFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuanjue.hk
 * @date 2013-2-28
 */
public class BindablePropertyRegister {
    private static final String TAG = "Binding-BindablePropertyRegister";
    private static HashMap<Class<?>, HashMap<String, DependencyProperty>> propertyHashMapOfClass;

    private static HashMap<Class<?>, HashMap<String, DependencyProperty>> getPropertyHashMap() {
        if (propertyHashMapOfClass == null) {
            propertyHashMapOfClass = new HashMap<Class<?>, HashMap<String, DependencyProperty>>(128);
            initPropertyHashMapInner();
        }
        return propertyHashMapOfClass;
    }

    private static void initPropertyHashMapInner() {
        //****************************************************************************************//
        // View
        //****************************************************************************************//

        register("clickable", boolean.class, View.class, true);
        register("selected", boolean.class, View.class, true);
        register("enable", boolean.class, View.class, true);
        register("focusable", boolean.class, View.class, true);
        register("pressed", boolean.class, View.class, false);
        register("focusable", boolean.class, View.class, true);
        register("focusable", boolean.class, View.class, true);

//        register("pivotX", float.class, View.class, 0f);
//        register("pivotY", float.class, View.class, 0f);
//        register("scaleX", float.class, View.class, 1f);
//        register("scaleY", float.class, View.class, 1f);
//        register("translationX", float.class, View.class, 0f);
//        register("translationY", float.class, View.class, 0f);
//        register("x", float.class, View.class, 0f);
//        register("y", float.class, View.class, 0f);

        register("visibility", int.class, View.class, View.VISIBLE);
        register("alpha", float.class, View.class, 1.0f);
        register("backgroundColor", int.class, View.class, 0x000000);

        //****************************************************************************************//
        // TextView
        //****************************************************************************************//

        register("text", CharSequence.class, TextView.class, "");
        register("textColor", int.class, TextView.class, 0x000000);
        register("hintTextColor", int.class, TextView.class, 0x000000);
        register("textSize", float.class, TextView.class, 0f);
        register("lines", int.class, TextView.class, 0f);
        register("maxLines", int.class, TextView.class, 0f);

        //****************************************************************************************//
        // ImageView
        //****************************************************************************************//

        register("url", String.class, ImageView.class, "");

        //****************************************************************************************//
        // RatingBar
        //****************************************************************************************//

        register("rating", float.class, RatingBar.class, 5);

        // TODO
    }

    public static DependencyProperty obtain(String propertyName, Class<?> ownerType) {
        if (ViewFactory.BINDING_DATA_CONTEXT.equals(propertyName)) {
            return new DependencyProperty(propertyName, Object.class, Object.class, "{}");
        }

        BindDesignLog.d(TAG, "obtain DependencyProperty: propertyName = " + propertyName
                + " ownerType = " + ownerType);

        HashMap<String, DependencyProperty> propertyHashMap = getPropertyHashMap().get(ownerType);
        if (propertyHashMap != null) {
            DependencyProperty dp = propertyHashMap.get(propertyName);
            if (dp != null) {
                return dp.clone();
            } else {
                // try to find the super class that already has been registered
                for (Map.Entry<Class<?>, HashMap<String, DependencyProperty>> entry : getPropertyHashMap().entrySet()) {
                    if (entry.getKey() != ownerType && entry.getKey().isAssignableFrom(ownerType)) {
                        DependencyProperty dp0 = entry.getValue().get(propertyName);
                        if (dp0 != null) {
                            return register(propertyName, dp0.getPropertyType()
                                    , ownerType, dp0.getDefaultValue()).clone();
                        }
                    }
                }
            }
        } else {
            // try to find the super class that already has been registered
            for (Map.Entry<Class<?>, HashMap<String, DependencyProperty>> entry : getPropertyHashMap().entrySet()) {
                if (entry.getKey() != ownerType && entry.getKey().isAssignableFrom(ownerType)) {
                    DependencyProperty dp0 = entry.getValue().get(propertyName);
                    if (dp0 != null) {
                        return register(propertyName, dp0.getPropertyType()
                                , ownerType, dp0.getDefaultValue()).clone();
                    }
                }
            }
        }
        BindDesignLog.e(TAG, "Attempted to obtain an unregister DependencyProperty! propertyName = " + propertyName + " ownerType = " + ownerType);
        return null;
    }

    public static DependencyProperty register(String propertyName, Class<?> propertyType, Class<?> ownerType,
                                              Object defaultValue) {
        HashMap<String, DependencyProperty> propertyHashMap = getPropertyHashMap().get(ownerType);
        if (propertyHashMap == null) {
            propertyHashMap = new HashMap<String, DependencyProperty>();
            BindDesignLog.d(TAG, "register DependencyProperty for class:  " + ownerType);
            getPropertyHashMap().put(ownerType, propertyHashMap);
        }
        DependencyProperty dp = propertyHashMap.get(propertyName);
        if (dp == null) {
            dp = new DependencyProperty(propertyName, propertyType, ownerType, defaultValue);
            BindDesignLog.d(TAG, "register DependencyProperty: propertyName = " + propertyName
                    + " propertyType = " + propertyType
                    + " ownerType = " + ownerType
                    + " defaultValue = " + defaultValue);
            propertyHashMap.put(propertyName, dp);
        }
        return dp;
    }

    // TODO
    // register BindValueSetter filter to optimize performance
}
