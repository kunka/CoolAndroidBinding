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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.binding.R;

import com.kk.binding.adapter.BindViewGroupAdapter;
import com.kk.binding.adapter.BindViewPagerAdapter;
import com.kk.binding.adapter.ViewGroupAdapter;
import com.kk.binding.util.BindDesignLog;

/**
 * Created by hk on 13-12-4.
 */
public class BindableAdapterFrameLayout extends FrameLayout {
    public interface onAdapterCreatedListener {
        void onAdapterCreated();
    }

    private static final String TAG = "Binding-BindableAdapterFrameLayout";
    private int childLayoutId = 0;
    private BindViewGroupAdapter<ViewGroup> adapter;
    private BindViewPagerAdapter<ViewPager> pagerAdapter;
    private onAdapterCreatedListener onAdapterCreatedListener;

    public BindableAdapterFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public BindableAdapterFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BindableAdapterFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void setOnAdapterCreatedListener(BindableAdapterFrameLayout.onAdapterCreatedListener onAdapterCreatedListener) {
        this.onAdapterCreatedListener = onAdapterCreatedListener;
    }

    private void init(AttributeSet attrs) {
        Context context = getContext();
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableAdapterFrameLayout);
            if (a != null) {
                childLayoutId = a.getResourceId(R.styleable.BindableAdapterFrameLayout_childLayout, 0);
            }
        }

        BindDesignLog.d(TAG, "inflate BindableAdapterFrameLayout");
        if (childLayoutId > 0) {
            setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    BindDesignLog.d(TAG, "BindableAdapterFrameLayout onChildViewAdded: " + (parent.isInEditMode() ? child.getClass().getName() : child.toString()));
                    if (child instanceof ViewPager) {
                        pagerAdapter = new BindViewPagerAdapter<ViewPager>((ViewPager) child, childLayoutId);
                        if (onAdapterCreatedListener != null)
                            onAdapterCreatedListener.onAdapterCreated();
                    } else if (child instanceof ViewGroup) {
                        adapter = new BindViewGroupAdapter<ViewGroup>((ViewGroup) child, childLayoutId);
                        if (onAdapterCreatedListener != null)
                            onAdapterCreatedListener.onAdapterCreated();
                        adapter.setOnItemClickListener(new ViewGroupAdapter.OnItemClickListener<ViewGroup>() {
                            @Override
                            public void onItemClick(ViewGroup viewGroup, View child, int position) {
                                Log.d(TAG, "onItemClick index = " + position);
                                if (mOnItemClickListener != null) {
                                    mOnItemClickListener.onItemClick(viewGroup, child, position);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {

                }
            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        BindDesignLog.d(TAG, "onFinishInflate BindableAdapterFrameLayout");
    }

    public BindViewGroupAdapter<ViewGroup> getAdapter() {
        return adapter;
    }

    public BindViewPagerAdapter<ViewPager> getPagerAdapter() {
        return pagerAdapter;
    }

    private ViewGroupAdapter.OnItemClickListener<ViewGroup> mOnItemClickListener;

    public void setOnItemClickListener(ViewGroupAdapter.OnItemClickListener<ViewGroup> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
