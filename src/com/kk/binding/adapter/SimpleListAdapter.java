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
import com.kk.binding.util.BindLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
        boolean isLogOpen = BindLog.isLogOpen();
        long start = 0;

        if (convertView == null) {
            if (isLogOpen) {
                start = System.nanoTime();
            }

            v = inflateView(mResId, viewGroup);

            if (isLogOpen) {
                long end = System.nanoTime();
                BindLog.d(TAG, "getView, inflate time(ms) = " + TimeUnit.NANOSECONDS.toMillis(end - start));
            }
        }

        if (isLogOpen) {
            start = System.nanoTime();
            BindLog.d(TAG, "onBindData, position = " + position + " totalCount = " + getCount());
        }

        onBindData(v, mData.get(position), position);

        if (isLogOpen) {
            long end = System.nanoTime();
            long delta = TimeUnit.NANOSECONDS.toMillis(end - start);
            // Only log the unusual time
            if (delta > 2)
                BindLog.w(TAG, "onBindData, time(ms) = " + delta);
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
        ViewGroup vg = (ViewGroup) layoutInflater.inflate(resourceId, parent);
        return vg.getChildAt(vg.getChildCount() - 1);
    }

    public void setData(List<T> data) {
        mData = data;
    }
}
