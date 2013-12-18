/**
 * 
 */
package com.kk.binding.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import com.kk.binding.view.BindViewUtil;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 * */
public class BindListAdapter extends BaseAdapter {
	private IItemToBindDoConvert mConvert;
	private Context mContext;

	/**
	 * @param convert
	 *            the convert to set
	 */
	public void setConvert(IItemToBindDoConvert convert) {
		this.mConvert = convert;
	}

	/**
	 * 数据列表
	 */
	protected List<ListItemDataObject> mData;
	/**
	 * 绑定view的资源id
	 */
	protected int mResource;
	protected LayoutInflater mInflater;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            ：Context实例，可以使用Application Context
	 * @param resource
	 *            ：item layout id
	 */
	public BindListAdapter(Context context, int resource, List<ListItemDataObject> data) {
		this(context, resource);
		mData = data;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            ：Context实例，可以使用Application Context
	 * @param resource
	 *            ：item layout id
	 */
	public BindListAdapter(Context context, int resource) {
		mContext = context;
		mResource = resource;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mData.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			// 创建新的content view
			arg1 = BindViewUtil.inflateView(mContext, mResource, null, false);
			// arg1 = mInflater.inflate(mResource, null, false);
		}
		else {

		}
		if (arg0 < mData.size()) {
			ListItemDataObject ido = mData.get(arg0);
			Object result = ido;
			if (mConvert != null) {
				result = mConvert.convert(ido);
			}
			if (result != null && result instanceof BaseBindDataObject) {
				((BaseBindDataObject) result).setData(ido.getData());
			}
			BindViewUtil.setDataContext(arg1, result);
		}
		return arg1;
	}
}
