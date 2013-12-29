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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.kk.binding.util.BindDesignLog;

import java.util.List;

/**
 * Created by xj on 13-7-6.
 */
public abstract class SimpleListAdapter<T> extends BaseAdapter {
    private static final String TAG = "SimpleListAdapter";
    private Context mContext;
    private int mResId;
    private List<T> mData;

    public SimpleListAdapter(Context context, int resId, List<T> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (position < 0 || position > mData.size() - 1)
            return convertView;

        View v = convertView;
        if (convertView == null) {
            long start = 0;
            if (BindDesignLog.isLogOpen()) {
                start = System.nanoTime();
            }

            v = inflateView(mResId, viewGroup);

            if (BindDesignLog.isLogOpen()) {
                long end = System.nanoTime();
                BindDesignLog.d(TAG, "getView, inflate time(ms) = " + (end - start) / 1000000);
            }
        }

        long start = 0;
        if (BindDesignLog.isLogOpen()) {
            start = System.nanoTime();
        }

        if (BindDesignLog.isLogOpen()) {
            BindDesignLog.d(TAG, "onBindData, position = " + position + " totalCount = " + getCount());
        }

        onBindData(v, mData.get(position), position);

        if (BindDesignLog.isLogOpen()) {
            long end = System.nanoTime();
            long delta = (end - start) / 1000000;
            // Only log the unusual time
            if (delta > 2)
                BindDesignLog.w(TAG, "onBindData, time(ms) = " + delta);
        }
        return v;
    }

    protected abstract void onBindData(View view, T data, int position);

    /**
     * Override for custom inflate
     *
     * @param resourceId
     * @param parent
     * @return
     */
    protected View inflateView(int resourceId, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vp = (ViewGroup) layoutInflater.inflate(resourceId, parent);
        return vp.getChildAt(vp.getChildCount() - 1);
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetInvalidated();
    }
}
