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
package com.kk.binding.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.binding.util.BindLog;
import com.kk.binding.view.BindViewUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xj on 13-7-6.
 */
public class SimpleBindPagerAdapter extends PagerAdapter {
    private static final String TAG = "Binding-SimpleBindPagerAdapter";
    private Context mContext;
    private int mResId;
    private List<?> mData;

    public SimpleBindPagerAdapter(Context context, int resId, List<?> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    public SimpleBindPagerAdapter(Context context, int mResId, Object[] obj) {
        this(context, mResId, Arrays.asList(obj));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        viewPager = container;
        if (position >= mData.size()) return null;
        BindLog.d(TAG, "instantiateItem "
                + "\n position = " + position
                + "\n data = " + mData.get(position));

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(mResId, null);
        BindViewUtil.setDataContext(v, mData.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

    ViewGroup viewPager = null;

//    @Override
//    public float getPageWidth(int position) {
//        BindLog.d(TAG, "getPageWidth position = " + position);
//        if (viewPager != null && viewPager.getChildCount() > position) {
//            View child = viewPager.getChildAt(position);
//            int width = 0;
//            if (child != null) {
//                width = child.getMeasuredWidth();
//            }
//            int pagerWidth = viewPager.getMeasuredWidth();
//            BindLog.d(TAG, "getPageWidth "
//                    + "\n width = " + width
//                    + "\n pagerWidth = " + pagerWidth
//                    + "\n position = " + position);
//            if (width > 0 && pagerWidth > 0) {
//                return ((float) width) / pagerWidth;
//            }
//        }
//        return super.getPageWidth(position);
//        return 0.8f;
//    }
}
