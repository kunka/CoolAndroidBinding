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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.binding.R;
import com.kk.binding.kernel.BindEngine;
import com.kk.binding.kernel.Binding;
import com.kk.binding.kernel.DependencyProperty;
import com.kk.binding.property.BindablePropertyDeclare;
import com.kk.binding.util.BindDesignLog;
import com.kk.binding.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hk on 13-12-4.
 */
public class BindableFrameLayout extends FrameLayout {
    private static final String TAG = "Binding-BindableFrameLayout";
    private int designDataResId = 0;
    private int realDataResId = 0;
    private int layoutId = 0;
    private String propertyDeclareClass = "";
    private String dataClass = "";
    private String dataContext = "";
    private boolean asRootContainer = true;

    public BindableFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public BindableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BindableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Context context = getContext();
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableFrameLayout);
            if (a != null) {
                designDataResId = a.getResourceId(R.styleable.BindableFrameLayout_designData, 0);
                layoutId = a.getResourceId(R.styleable.BindableFrameLayout_layout, 0);
                dataClass = a.getString(R.styleable.BindableFrameLayout_dataClass);
                dataContext = a.getString(R.styleable.BindableFrameLayout_designDataContext);
                propertyDeclareClass = a.getString(R.styleable.BindableFrameLayout_propertyDeclareClass);
                realDataResId = a.getResourceId(R.styleable.BindableFrameLayout_realData, 0);
            }
        }
        asRootContainer = !(layoutId > 0);
        BindDesignLog.setInDesignMode(isInEditMode());
        BindViewUtil.injectInflater(getContext());
        BindEngine.registerPropertyDeclareClass(propertyDeclareClass);

        BindDesignLog.d(TAG, "inflate BindableFrameLayout");
        if (isInEditMode()) {
            if (designDataResId > 0) {
                // as layout container
                if (!asRootContainer) {
                    BindViewUtil.inflateView(getContext(), layoutId, this, true);
                } else {
                    // as root element
//                    setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
//                        @Override
//                        public void onChildViewAdded(View parent, View child) {
//                            BindDesignLog.d(TAG, "BindableFrameLayout onChildViewAdded: " + (parent.isInEditMode() ? child.getClass().getName() : child.toString()));
//                        }
//
//                        @Override
//                        public void onChildViewRemoved(View parent, View child) {
//                        }
//                    });
                }
            }
        } else {
            // as root element
//            setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
//                @Override
//                public void onChildViewAdded(View parent, View child) {
//                    BindDesignLog.d(TAG, "BindableFrameLayout onChildViewAdded: " + (parent.isInEditMode() ? child.getClass().getName() : child.toString()));
//                }
//
//                @Override
//                public void onChildViewRemoved(View parent, View child) {
//                }
//            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        BindDesignLog.d(TAG, "onFinishInflate BindableFrameLayout");

        if (isInEditMode()) {
            if (designDataResId > 0) {
                bindDataInDesign(designDataResId);
            }
        } else {
            if (realDataResId > 0) {
                // delay execute
                post(new Runnable() {
                    @Override
                    public void run() {
                        bindDataInDesign(realDataResId);
                    }
                });
            }
        }
        // print log in design log
        // BindDesignLog.throwDesignLog();
    }

    private void bindDataInDesign(int resId) {
        InputStream is = null;
        byte[] data = null;
        try {
            is = getResources().openRawResource(resId);
            // will fail in design mode
            // is = getResources().getAssets().open(resName);
            data = new byte[is.available()];
            is.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Class<?> clazz = null;
        if (!StringUtil.isNullOrEmpty(dataClass)) {
            try {
                clazz = Class.forName(dataClass);
            } catch (ClassNotFoundException e) {
                if (isInEditMode())
                    throw new RuntimeException("parse dataClass failed " + e.toString());
                BindDesignLog.e(TAG, "parse dataClass failed " + e.toString());
            }
        }
        if (!StringUtil.isNullOrEmpty(dataContext)) {
            Binding bd = new Binding();
            String attrValue = ViewFactory.parseBindingSyntactic(this, dataContext);
            if (attrValue != null) {
                bd.setPath(ViewFactory.parseBindingSyntactic(this, dataContext));
                DependencyProperty dp = BindablePropertyDeclare.obtain(ViewFactory.BINDING_DATA_CONTEXT, Object.class);
                BindViewUtil.getDependencyObject(this).setBindings(dp, bd);
            }
        }

        BindViewUtil.setDataContext(this, data, clazz);
    }
}
