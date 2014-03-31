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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import com.kk.binding.command.ICommand;
import com.kk.binding.converter.IValueConverter;
import com.kk.binding.kernel.*;
import com.kk.binding.listener.ListenerToCommand;
import com.kk.binding.register.CommandRegister;
import com.kk.binding.register.ConverterRegister;
import com.kk.binding.register.ListenerImpRegister;
import com.kk.binding.register.PropertyRegister;
import com.kk.binding.util.BindLog;
import com.kk.binding.view.BindViewUtil;

import java.lang.reflect.Constructor;

/**
 * @author xuanjue.hk
 * @date 2013-2-26
 */
public class ViewFactory implements Factory {
    class BindMeta {
        String path;
        String property;
        String converter;
        String command;
        String event;
    }

    public static String TAG = "Binding-ViewFactory";
    // public static final String BINDING_NAMESPACE = "http://schemas.android.com/apk/res/binding";
    public static final String BINDING_NAMESPACE = "http://schemas.android.com/apk/res-auto";
    public static final String BINDING_NAME_PREFIX = "binding";
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

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        BindLog.d(TAG, "onCreateView name = " + name);
        View view = createViewByInflater(name, context, attrs);
        if (view == null)
            return null;
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(BINDING_NAMESPACE, attrName);
            if (attrValue != null && attrName != null) {
//                BindLog.d(TAG, "onCreateView parseBindingAttribute: attrName = " + attrName + " attrValue = " + attrValue);
                if (attrName.startsWith(BINDING_NAME_PREFIX)) {
                    parseAttribute(view, attrName, attrValue);
                } else if (attrName.equals(BINDING_DATA_CONTEXT)) {
                    parseAttributeDataContext(view, attrName, attrValue);
                }
            }
        }
        return view;
    }

    protected View createViewByInflater(String name, Context context, AttributeSet attrs) {
        String viewFullName = "";
        try {
            if (name.equals("View") || name.equals("ViewGroup") || name.equals("ViewStub"))
                viewFullName = "android.view." + name;
            else if (name.contains("."))
                viewFullName = name;
            else
                viewFullName = "android.widget." + name;

//            BindLog.d(TAG, "onCreateView viewFullName = " + viewFullName);
            return mInflater.createView(viewFullName, null, attrs);
        } catch (Exception e) {
            // design mode cannot find custom class?! inflate will failed, shit! :(
            // so we use the class name to create instance.
            BindLog.e(TAG, "onCreateView exception = " + e.toString());
            Class<?> clazz = null;
            try {
                clazz = Class.forName(viewFullName);
            } catch (ClassNotFoundException e0) {
                BindLog.e(TAG, " class not found " + e0.toString());
            }
            if (clazz != null) {
                BindLog.d(TAG, "onCreateView class founded = " + clazz.toString());
                Constructor<?> constructor = null;
                try {
                    constructor = clazz.getDeclaredConstructor(Context.class, AttributeSet.class);
                } catch (Exception e1) {
                    BindLog.e(TAG, "onCreateView getDeclaredConstructor failed  = " + e1.toString());
                }

                if (constructor != null) {
                    BindLog.d(TAG, "onCreateView getDeclaredConstructor success = " + constructor.toString());
                    try {
                        return (View) constructor.newInstance(context, attrs);
                    } catch (Exception e1) {
                        BindLog.e(TAG, "onCreateView newInstance failed  = " + e1.toString());
                    }
                }
            }
            return null;
        }
    }

    private void parseAttributeDataContext(View view, String attrName, String attrValue) {
        attrValue = parseBindingSyntactic(view, attrValue);
        if (attrValue == null) return;
        Binding bd = new Binding();
        bd.setPath(attrValue);
        DependencyProperty dp = PropertyRegister.obtain(BINDING_DATA_CONTEXT, view.getClass());
        BindViewUtil.getDependencyObject(view).setBindings(dp, bd);
    }

    public void parseAttribute(View view, String attrName, String attrValue) {
        attrValue = parseBindingSyntactic(view, attrValue);
        if (attrValue == null)
            return;

        // "path=data.bindValue,property=pro,converter=stringToVisibleConverter,command=cmd,event=onClick,...
        // -->
        // path=data.bindValue | property=pro | converter=stringToVisibleConverter | ...
        String[] values = attrValue.split(",");
        if (values.length < 2) {
            if (view.isInEditMode())
                throw new IllegalArgumentException("binding expression illegal, at least specify the property and path");
            BindLog.e(TAG, "binding expression illegal, at least specify the property and path");
            return;
        }
        BindMeta bind = new BindMeta();
        for (String v : values) {
            parseBinding(bind, view, v);
        }
        doBind(view, bind);
    }

    private void doBind(View view, BindMeta bind) {
        if (bind.property != null && bind.path != null) {
            Binding bd = new Binding();
            bd.setPath(bind.path);
            if (bind.converter != null) {
                // parseConverter
                Class<? extends IValueConverter> converterType = ConverterRegister.getConverters().get(bind.converter);
                if (converterType != null) {
                    try {
                        bd.setValueConverter(converterType.newInstance());
                    } catch (Exception e) {
                        BindLog.e(TAG, "create converter failed. converterName = " + bind.converter
                                + " converterType = " + converterType + " e = " + e.toString());
                    }
                } else {
                    BindLog.e(TAG, "converter has not been registered, converter = " + bind.converter);
                }
            }
            DependencyProperty dp = PropertyRegister.obtain(bind.property, view.getClass());
            BindViewUtil.getDependencyObject(view).setBindings(dp, bd);
        }

        if (bind.event != null && bind.command != null) {
            ListenerToCommand ltc = null;
            // parse event
            Class<? extends ListenerToCommand> listenerImp = ListenerImpRegister.getListenerImps().get(bind.event);
            if (listenerImp != null) {
                try {
                    ltc = listenerImp.newInstance();
                } catch (Exception e) {
                    BindLog.e(TAG, "create listenerImp failed. eventName = " + bind.event
                            + " listenerImp = " + listenerImp + " e = " + e.toString());
                }
            } else {
                BindLog.e(TAG, "listenerImp has not been registered, listenerImp = " + bind.event);
            }

            if (ltc != null) {
                // parse command
                CommandBinding cmdbd = new CommandBinding();
                Class<? extends ICommand> command = CommandRegister.getCommands().get(bind.command);
                if (command != null) {
                    try {
                        ltc.setCommand(command.newInstance());
                    } catch (Exception e) {
                        BindLog.e(TAG, "create command failed. commandName = " + bind.command
                                + " commandType = " + command + " e = " + e.toString());
                    }
                } else {
                    BindLog.e(TAG, "command has not been registered,use custom command = " + bind.command);
                }
                cmdbd.setCommandName(bind.command);
                cmdbd.setCommandParamPath(bind.path);
                ltc.registerToView(view);
                BindViewUtil.getDependencyObject(view).setCmdBindings(ltc, cmdbd);
            }
        }
    }

    private void parseBinding(final BindMeta bind, final View view, final String value) {
        // path=data.bindValue  -->  path | data.bindValue
        String[] vs = value.split("=");
        if (vs.length != 2) {
            if (view.isInEditMode())
                throw new IllegalArgumentException("binding expression illegal, format like 'key=value'");
            BindLog.e(TAG, "binding expression illegal, format like 'k=v'");
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
        // "{bindValue}"  -->  "bindValue"
        if (!attrValue.startsWith("{") || !attrValue.endsWith("}")) {
            if (view.isInEditMode()) {
                throw new IllegalArgumentException("binding expression must start with '{' and end with '}'");
            } else {
                // ignore
                BindLog.e(TAG, "binding expression must start with '{' and end with '}'");
                return null;
            }
        }
        int length = attrValue.length();
        attrValue = attrValue.substring(1, length - 1);
        return attrValue;
    }

}
