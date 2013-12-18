package binding.adapter;

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
