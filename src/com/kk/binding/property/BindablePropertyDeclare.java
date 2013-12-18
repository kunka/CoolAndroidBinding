/**
 *
 */
package com.kk.binding.property;

import android.widget.ImageView;
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
public class BindablePropertyDeclare {
    private static final String TAG = "Binding-BindablePropertyDeclare";
    private static HashMap<Class<?>, HashMap<String, DependencyProperty>> propertyHashMapOfClass;

    private static HashMap<Class<?>, HashMap<String, DependencyProperty>> getPropertyHashMap() {
        if (propertyHashMapOfClass == null) {
            propertyHashMapOfClass = new HashMap<Class<?>, HashMap<String, DependencyProperty>>(128);
            initPropertyHashMapInner();
        }
        return propertyHashMapOfClass;
    }

    private static void initPropertyHashMapInner() {
        register("text", CharSequence.class, TextView.class, "");
        register("url", String.class, ImageView.class, "");
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
        BindDesignLog.d(TAG, "Attempted to obtain an unregister DependencyProperty! propertyName = " + propertyName + " ownerType = " + ownerType);
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
}
