package binding.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.binding.R;

import java.io.IOException;
import java.io.InputStream;

import binding.kernel.Binding;
import binding.kernel.DependencyProperty;
import binding.property.BindablePropertyDeclare;
import binding.util.BindDesignLog;
import binding.util.StringUtil;

/**
 * Created by hk on 13-12-4.
 */
public class BindableFrameLayout extends FrameLayout {
    private static final String TAG = "Binding-BindableFrameLayout";
    private int designDataResId = 0;
    private int realDataResId = 0;
    private int layoutId = 0;
    private String dataClass = "";
    private String propertyDeclareClass = "";
    private String dataContext = "";
    private boolean asRootContainer = true;

    public BindableFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public BindableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BindableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Context context = getContext();
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableFrameLayout);
            if (a != null) {
                designDataResId = a.getResourceId(R.styleable.BindableFrameLayout_designData, 0);
                layoutId = a.getResourceId(R.styleable.BindableFrameLayout_layout, 0);
                dataClass = a.getString(R.styleable.BindableFrameLayout_dataClass);
                dataContext = a.getString(R.styleable.BindableFrameLayout_designDataContext);
                propertyDeclareClass = a.getString(R.styleable.BindableFrameLayout_propertyDeclareClass);
                realDataResId = a.getResourceId(R.styleable.BindableFrameLayout_realData, 0);
            }
        }
        asRootContainer = !(layoutId > 0);
        BindDesignLog.setInDesignMode(isInEditMode());
        BindViewUtil.injectInflater(getContext());
        BindDesignLog.d(TAG, "propertyDeclareClass =  " + propertyDeclareClass);
        if (!StringUtil.isNullOrEmpty(propertyDeclareClass)) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(propertyDeclareClass);
                BindDesignLog.d(TAG, "parse propertyDeclareClass success " + clazz.toString());
            } catch (ClassNotFoundException e) {
                if (isInEditMode())
                    throw new RuntimeException("parse propertyDeclareClass failed " + e.toString());
                BindDesignLog.d(TAG, "parse propertyDeclareClass failed " + e.toString());
            }
            if (clazz != null) {
                try {
                    Object instance = clazz.newInstance();
                    if (instance != null)
                        BindDesignLog.d(TAG, "create propertyDeclareClass instance success " + instance.toString());
                } catch (Exception e) {
                    if (isInEditMode())
                        throw new RuntimeException("create propertyDeclareClass instance failed " + e.toString());
                    BindDesignLog.d(TAG, "create propertyDeclareClass instance failed " + e.toString());
                }
            }
        }

        BindDesignLog.d(TAG, "inflate BindableFrameLayout");
        if (isInEditMode()) {
            if (designDataResId > 0) {
                // as layout container
                if (!asRootContainer) {
                    BindViewUtil.inflateView(getContext(), layoutId, this, true);
                } else {
                    // as root element
//                    setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
//                        @Override
//                        public void onChildViewAdded(View parent, View child) {
//                            BindDesignLog.d(TAG, "BindableFrameLayout onChildViewAdded: " + (parent.isInEditMode() ? child.getClass().getName() : child.toString()));
//                        }
//
//                        @Override
//                        public void onChildViewRemoved(View parent, View child) {
//                        }
//                    });
                }
            }
        } else {
            // as root element
//            setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
//                @Override
//                public void onChildViewAdded(View parent, View child) {
//                    BindDesignLog.d(TAG, "BindableFrameLayout onChildViewAdded: " + (parent.isInEditMode() ? child.getClass().getName() : child.toString()));
//                }
//
//                @Override
//                public void onChildViewRemoved(View parent, View child) {
//                }
//            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        BindDesignLog.d(TAG, "onFinishInflate BindableFrameLayout");

        if (isInEditMode()) {
            if (designDataResId > 0) {
                bindDataInDesign();
            }
        } else {
            if (realDataResId > 0) {
                // delay execute
                post(new Runnable() {
                    @Override
                    public void run() {
                        bindDataInDesign();
                    }
                });
            }
        }
        // print log in design log
        // BindDesignLog.throwDesignLog();
    }

    private void bindDataInDesign() {
        InputStream is = null;
        byte[] data = null;
        try {
            is = getResources().openRawResource(designDataResId);
            // will fail in design mode
            // is = getResources().getAssets().open(resName);
            data = new byte[is.available()];
            is.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Class<?> clazz = null;
        if (!StringUtil.isNullOrEmpty(dataClass)) {
            try {
                clazz = Class.forName(dataClass);
            } catch (ClassNotFoundException e) {
                if (isInEditMode())
                    throw new RuntimeException("parse dataClass failed " + e.toString());
                BindDesignLog.d(TAG, "parse dataClass failed " + e.toString());
            }
        }
        if (!StringUtil.isNullOrEmpty(dataContext)) {
            Binding bd = new Binding();
            String attrValue = ViewFactory.parseBindingSyntactic(this, dataContext);
            if (attrValue != null) {
                bd.setPath(ViewFactory.parseBindingSyntactic(this, dataContext));
                DependencyProperty dp = BindablePropertyDeclare.obtain(ViewFactory.BINDING_DATA_CONTEXT, Object.class);
                BindViewUtil.getDependencyObject(this).setBindings(dp, bd);
            }
        }

        BindViewUtil.setDataContext(this, data, clazz);
    }
}
