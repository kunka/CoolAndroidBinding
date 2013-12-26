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

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * Created by hk on 13-12-4.
 */
public class ViewGroupAdapter<T extends ViewGroup> {
    private static final String TAG = "Binding-ViewGroupAdapter";

    public interface OnItemClickListener<T> {
        void onItemClick(T viewGroup, View child, int position);
    }

    private T viewGroup;
    protected ListAdapter mAdapter;
    protected DataSetObserver mDataSetObserver;
    private OnItemClickListener<T> mOnItemClickListener;

    public ViewGroupAdapter(T viewGroup) {
        this.viewGroup = viewGroup;
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                reloadView();
            }
        };
    }

    public T getViewGroup() {
        return viewGroup;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            if (viewGroup.isInEditMode()) {
                // cannot called async in design mode
                mDataSetObserver.onChanged();
            } else {
                // async notify
                viewGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataSetObserver.onChanged();
                    }
                });
            }
        }
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void reloadView() {
        viewGroup.removeAllViews();
        if (mAdapter == null) return;
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            final View view = mAdapter.getView(i, null, viewGroup);
            if (view != null) {
                final int index = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onItemClick index = " + index);
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(viewGroup, view, index);
                        }
                    }
                });
            }
        }
    }
}
