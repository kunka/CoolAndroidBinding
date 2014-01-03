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
package com.kk.binding.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import android.widget.AdapterView;
import com.kk.binding.converter.FalseToVisibleConverter;
import com.kk.binding.converter.NotNullToVisibleConverter;
import com.kk.binding.converter.NullToVisibleConverter;
import com.kk.binding.converter.TrueToVisibleConverter;
import com.kk.binding.kernel.Binding;
import com.kk.binding.kernel.CommandBinding;
import com.kk.binding.kernel.DependencyProperty;
import com.kk.binding.kernel.ICommand;
import com.kk.binding.listener.ListenerToCommand;
import com.kk.binding.listener.OnClickListenerImp;
import com.kk.binding.listener.OnFocusChangeListenerImp;
import com.kk.binding.listener.OnItemClickListenerImp;
import com.kk.binding.register.BindablePropertyRegister;
import com.kk.binding.register.CommandRegister;
import com.kk.binding.util.BindDesignLog;

import java.lang.reflect.Constructor;

/**
 * @author xuanjue.hk
 * @date 2013-2-26
 */
public class ViewFactory implements Factory {
    public static String TAG = "Binding-ViewFactory";
    // public static final String BINDING_NAMESPACE = "http://schemas.android.com/apk/res/binding";
    public static String BINDING_NAMESPACE = "http://schemas.android.com/apk/res-auto";
    public static final String BINDING_NAME = "binding";
    public static final String BINDING_DATA_CONTEXT = "dataContext";
    public static final String BINDING_PATH = "path";
    public static final String BINDING_PROPERTY = "property";
    public static final String BINDING_CONVERTER = "converter";
    public static final String BINDING_COMMAND = "command";
    public static final String BINDING_EVENT = "event";
    private LayoutInflater mInflater;

    public ViewFactory(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    protected View CreateViewByInflater(String name, Context context, AttributeSet attrs) {
        String viewFullName = "";
        try {
            if (name.equals("View") || name.equals("ViewGroup") || name.equals("ViewStub"))
                viewFullName = "android.view." + name;
            else if (name.contains("."))
                viewFullName = name;
            else
                viewFullName = "android.widget." + name;

            BindDesignLog.d(TAG, "onCreateView viewFullName = " + viewFullName);
            return mInflater.createView(viewFullName, null, attrs);
        } catch (Exception e) {
            // design mode cannot find custom class, inflate will failed, shit!
            // so we use the class name to create instance.
            BindDesignLog.e(TAG, "onCreateView exception = " + e.toString());
            Class<?> clazz = null;
            try {
                clazz = Class.forName(viewFullName);
            } catch (ClassNotFoundException e0) {
                BindDesignLog.e(TAG, " class not found " + e0.toString());
            }
            if (clazz != null) {
                BindDesignLog.d(TAG, "onCreateView class founded = " + clazz.toString());
                Constructor<?> constructor = null;
                try {
                    constructor = clazz.getDeclaredConstructor(Context.class, AttributeSet.class);
                } catch (Exception e1) {
                    BindDesignLog.e(TAG, "onCreateView getDeclaredConstructor failed  = " + e1.toString());
                }

                if (constructor != null) {
                    BindDesignLog.d(TAG, "onCreateView getDeclaredConstructor success = " + constructor.toString());
                    try {
                        return (View) constructor.newInstance(context, attrs);
                    } catch (Exception e1) {
                        BindDesignLog.e(TAG, "onCreateView newInstance failed  = " + e1.toString());
                    }
                }
            }
            return null;
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        BindDesignLog.d(TAG, "onCreateView name = " + name);
        View view = CreateViewByInflater(name, context, attrs);
        if (view == null)
            return null;
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(BINDING_NAMESPACE, attrName);
            if (attrValue != null && attrName != null) {
                BindDesignLog.d(TAG, "onCreateView parseBindingAttribute: attrName = " + attrName + " attrValue = " + attrValue);
                if (attrName.startsWith(BINDING_NAME)) {
                    parseAttribute(view, attrName, attrValue);
                } else if (attrName.equals(BINDING_DATA_CONTEXT)) {
                    parseAttributeDataContext(view, attrName, attrValue);
                }
            }
        }
        return view;
    }

    /**
     * TODO
     * begin with "on"
     */
    public void parseCommand(View view, String attrName, String attrValue) {
        ListenerToCommand ltc = null;
        if (view instanceof View) {
            if ("onClick".equalsIgnoreCase(attrName)) {
                ltc = new OnClickListenerImp();
            }
        } else if (view instanceof AdapterView<?>) {
            if ("onItemClick".equalsIgnoreCase(attrName)) {
                ltc = new OnItemClickListenerImp();
            }
        }
        if (ltc != null) {
            CommandBinding cmdb = new CommandBinding();
            cmdb.setCommandName(attrValue);
            ltc.registerToView(view);
            BindViewUtil.getDependencyObject(view).setCmdBindings(ltc, cmdb);
        }
    }

    private void parseAttributeDataContext(View view, String attrName, String attrValue) {
        // remove '{' and '}'
        // "{bindValue}" --> "bindValue"
        attrValue = parseBindingSyntactic(view, attrValue);
        if (attrValue == null) return;
        Binding bd = new Binding();
        bd.setPath(attrValue);
        DependencyProperty dp = BindablePropertyRegister.obtain(BINDING_DATA_CONTEXT, view.getClass());
        BindViewUtil.getDependencyObject(view).setBindings(dp, bd);
    }

    public void parseAttribute(View view, String attrName, String attrValue) {
        // remove '{' and '}'
        // "{bindValue}" --> "bindValue"
        attrValue = parseBindingSyntactic(view, attrValue);
        if (attrValue == null) return;

        // "path=data.bindValue,property=pro,converter=stringToVisibleConverter",command="cmd",event="onClick"
        // -->
        // path=data.bindValue | property=pro | converter=stringToVisibleConverter
        String[] values = attrValue.split(",");
        if (values.length < 2) {
            if (view.isInEditMode())
                throw new IllegalArgumentException("binding expression illegal, at least specify the property and path");
            BindDesignLog.e(TAG, "binding expression illegal, at least specify the property and path");
            return;
        }
        Bind bind = new Bind();
        for (String v : values) {
            parseBinding(bind, view, v);
        }
        doBind(view, bind);
    }

    private void doBind(View view, Bind bind) {
        if (bind.property != null && bind.path != null) {
            Binding bd = new Binding();
            bd.setPath(bind.path);
            if (bind.converter != null) {
                // TODO: parseConverter
                if ("TrueToVisibleConverter".equals(bind.converter)) {
                    bd.setValueConverter(new TrueToVisibleConverter());
                } else if ("FalseToVisibleConverter".equals(bind.converter)) {
                    bd.setValueConverter(new FalseToVisibleConverter());
                } else if ("NullToVisibleConverter".equals(bind.converter)) {
                    bd.setValueConverter(new NullToVisibleConverter());
                } else if ("NotNullToVisibleConverter".equals(bind.converter)) {
                    bd.setValueConverter(new NotNullToVisibleConverter());
                }
            }
            DependencyProperty dp = BindablePropertyRegister.obtain(bind.property, view.getClass());
            BindViewUtil.getDependencyObject(view).setBindings(dp, bd);
        }

        if (bind.event != null && bind.command != null) {
            ListenerToCommand ltc = null;
            // TODO: parseCommand
            if ("OnClick".equals(bind.event)) {
                ltc = new OnClickListenerImp();
            } else if ("OnItemClick".equals(bind.event)) {
                ltc = new OnItemClickListenerImp();
            } else if ("OnFocusChange".equals(bind.event)) {
                ltc = new OnFocusChangeListenerImp();
            } else {
            }

            if (ltc != null) {
                CommandBinding cmdbd = new CommandBinding();
                // TODO custom define cmd
                Class<?> commandType = CommandRegister.getCommandsHashMap().get(bind.command);
                if (commandType != null) {
                    try {
                        ltc.setCommand((ICommand) commandType.newInstance());
                    } catch (Exception e) {
                        BindDesignLog.e(TAG, "create command failed. commandName = " + bind.command
                                + " commandType = " + commandType + " e = " + e.toString());
                    }
                }
//                if ("urlNavCommand".equals(bind.command)) {
//                    ltc.setCommand(new UrlNavCommand());
//                }
                cmdbd.setCommandName(bind.command);
                cmdbd.setCommandParamPath(bind.path);
                ltc.registerToView(view);
                BindViewUtil.getDependencyObject(view).setCmdBindings(ltc, cmdbd);
            }
        }
    }

    private void parseBinding(Bind bind, View view, String value) {
        // path=data.bindValue
        // -->
        // path | data.bindValue
        String[] vs = value.split("=");
        if (vs.length != 2) {
            if (view.isInEditMode())
                throw new IllegalArgumentException("binding expression illegal");
            BindDesignLog.e(TAG, "binding expression illegal");
            return;
        }
        String arg1 = vs[0], arg2 = vs[1];
        if (arg1.equals(BINDING_PATH)) {
            bind.path = arg2;
        } else if (arg1.equals(BINDING_PROPERTY)) {
            bind.property = arg2;
        } else if (arg1.equals(BINDING_CONVERTER)) {
            bind.converter = arg2;
        } else if (arg1.equals(BINDING_COMMAND)) {
            bind.command = arg2;
        } else if (arg1.equals(BINDING_EVENT)) {
            bind.event = arg2;
        }
    }

    public static String parseBindingSyntactic(View view, String attrValue) {
        // remove '{' and '}'
        // "{bindValue}" --> "bindValue"
        if (!attrValue.startsWith("{") || !attrValue.endsWith("}")) {
            if (view.isInEditMode()) {
                throw new IllegalArgumentException("binding expression must start with '{' and end with '}'");
            } else {
                // ignore
                BindDesignLog.e(TAG, "binding expression must start with '{' and end with '}'");
                return null;
            }
        }
        int length = attrValue.length();
        attrValue = attrValue.substring(1, length - 1);
        return attrValue;
    }

    class Bind {
        String path;
        String property;
        String converter;
        String command;
        String event;
    }
}
