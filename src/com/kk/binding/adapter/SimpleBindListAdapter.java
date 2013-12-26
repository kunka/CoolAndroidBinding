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
import com.kk.binding.view.BindViewUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xj on 13-7-6.
 */
public class SimpleBindListAdapter extends BaseAdapter {
    private static final String TAG = "Binding-SimpleBindListAdapter";
    private Context mContext;
    private int mResId;
    private List<?> mData;

    public SimpleBindListAdapter(Context context, int resId, List<?> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    public SimpleBindListAdapter(Context context, int mResId, Object[] obj) {
        this(context, mResId, Arrays.asList(obj));
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (position >= mData.size()) return null;
        BindDesignLog.d(TAG, "SimpleBindListAdapter getView "
                + "\n position = " + position
                + "\n data = " + mData.get(position));

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(mResId, viewGroup);
        View v = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        BindViewUtil.setDataContext(v, mData.get(position));
        return v;
    }
}
