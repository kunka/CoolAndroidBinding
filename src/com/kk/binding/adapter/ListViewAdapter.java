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

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by hk on 13-12-28.
 */
public class ListViewAdapter<T extends ListView> {

    public interface OnItemClickListener<T> {
        void onItemClick(T viewGroup, View child, int position);
    }

    private T listView;
    protected BaseAdapter mAdapter;
    private OnItemClickListener<T> mOnItemClickListener;

    public ListViewAdapter(T listView) {
        this.listView = this.listView;
    }

    public T getListView() {
        return listView;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public BaseAdapter getAdapter() {
        return this.mAdapter;
    }
}
