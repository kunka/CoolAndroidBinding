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
import android.widget.BaseAdapter;

/**
 * Created by hk on 13-12-4.
 */
public class ViewGroupAdapter<T extends ViewGroup> {
    private static final String TAG = "Binding-ViewGroupAdapter";

    public interface OnItemClickListener<T> {
        void onItemClick(T viewGroup, View child, int position);
    }

    private T viewGroup;
    protected BaseAdapter mAdapter;
    protected DataSetObserver mDataSetObserver;
    private OnItemClickListener<T> mOnItemClickListener;

    public ViewGroupAdapter(T viewGroup) {
        this.viewGroup = viewGroup;
        mDataSetObserver = new DataSetObserver() {
            @Override public void onChanged() {
                reloadView();
            }

            @Override public void onInvalidated() {
                rebindView();
            }
        };
    }

    public T getViewGroup() {
        return viewGroup;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != adapter) {
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(mDataSetObserver);
            }
            mAdapter = adapter;
            if (mAdapter != null) {
                mAdapter.registerDataSetObserver(mDataSetObserver);
            }
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

    public BaseAdapter getAdapter() {
        return this.mAdapter;
    }

    public void reloadView() {
        Log.d(TAG, "reloadView");
        if (mAdapter == null) {
            viewGroup.removeAllViews();
            return;
        }
        int count = mAdapter.getCount();
        if (viewGroup.getChildCount() > count) {
            viewGroup.removeViews(count, viewGroup.getChildCount() - count);
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            // reuse view instead of recreate to optimize performance
            View child = i < childCount ? viewGroup.getChildAt(i) : null;
            final View view = mAdapter.getView(i, child, viewGroup);
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

    private void rebindView() {
        Log.d(TAG, "rebindView");
        if (mAdapter == null) {
            viewGroup.removeAllViews();
            return;
        }
        int count = mAdapter.getCount();
        if (viewGroup.getChildCount() > count) {
            viewGroup.removeViews(count, viewGroup.getChildCount() - count);
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            // reuse view instead of recreate to optimize performance
            View child = i < childCount ? viewGroup.getChildAt(i) : null;
            final View view = mAdapter.getView(i, child, viewGroup);
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
