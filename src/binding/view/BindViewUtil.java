/**
 *
 */
package binding.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.binding.R;

import java.lang.reflect.Type;

import binding.converter.Converter;
import binding.converter.IConverter;
import binding.kernel.Binding;
import binding.kernel.DependencyObject;
import binding.kernel.DependencyProperty;
import binding.util.BindDesignLog;

/**
 * @author xuanjue.hk
 * @date 2013-2-26
 */
public class BindViewUtil {
    private static final String TAG = "Binding";
    private static Converter converter;

    private static Converter defaultConverter = new Converter() {
        @Override
        public Object from(String string, Type type) {
            return JSON.parseObject(string);//JSON.parseObject(string, type);
        }

        @Override
        public Object from(byte[] data, Type type) {
            return JSON.parseObject(new String(data));//JSON.parseObject(data, type);
        }
    };

    public static void setConverter(Converter converter) {
        BindViewUtil.converter = converter;
    }

    // for design use
    public static View inflateView(Context context, int layoutId, ViewGroup parent, boolean attachToRoot) {
        injectInflater(context);
        return LayoutInflater.from(context).inflate(layoutId, parent, attachToRoot);
    }

    /**
     * inject inflater with our binding view factory
     *
     * @param context
     */
    public static void injectInflater(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (inflater.getFactory() == null) {
            ViewFactory factory = new ViewFactory(inflater);
            inflater.setFactory(factory);
        }
    }

    public static DependencyObject getDependencyObject(View view) {
        DependencyObject dpo = (DependencyObject) view.getTag(R.id.tag_for_attach_property);
        if (dpo == null) {
            dpo = new DependencyObject();
            dpo.setOriginTarget(view);
            view.setTag(R.id.tag_for_attach_property, dpo);
        }
        return dpo;
    }

    public static void setDataContext(View view, String string, Type claszz) {
        IConverter c = converter == null ? defaultConverter : converter;
        Object obj = null;
        try {
            obj = c.from(string, claszz);
        } catch (Exception e) {

        }
        setDataContext(view, obj);
    }

    public static void setDataContext(View view, byte[] data, Type claszz) {
        IConverter c = converter == null ? defaultConverter : converter;
        Object obj = null;
        try {
            obj = c.from(data, claszz);
        } catch (Exception e) {

        }
        setDataContext(view, obj);
    }

    private static void setDataContext(final View view, final Object dataContext, int level) {
        if (view == null || getBindDataObject(view) == dataContext)
            return;
        // view.toString() will throw exception when you set a view's id in xml
        BindDesignLog.d(TAG, "setDataContext : view(" + level + ") = \n"
                + (view.isInEditMode() ? view.getClass().getName() : view.toString())
                + "\n dataContext= " + (dataContext != null ? dataContext.toString() : null));
        view.setTag(R.id.tag_for_binding_data_object, dataContext);

        DependencyObject dpo = getDependencyObject(view);
        boolean handled = dpo.setDataContext(dataContext);
        if (handled) return;

        if (view instanceof ViewGroup) {
            Object targetObject = dpo.getResolvedTargetObject();
            ++level;
            ViewGroup vg = (ViewGroup) view;
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = vg.getChildAt(i);
                BindDesignLog.d(TAG, "setDataContext : child(" + i + "), total(" + count + ")");
                setDataContext(v, targetObject, level);
            }
        }
    }

    public static void setDataContext(final View view, final Object dataContext) {
        setDataContext(view, dataContext, 0);
    }

    public static Object getBindDataObject(View view) {
        return view.getTag(R.id.tag_for_binding_data_object);
    }

    public static String getDataInDesignResName(View view) {
        DependencyObject dpo = BindViewUtil.getDependencyObject(view);
        for (java.util.Map.Entry<DependencyProperty, Binding> e : dpo.getBindings().entrySet()) {
            if ("dataInDesignRes".equalsIgnoreCase(e.getKey().getPropertyName())) {
                return e.getValue().getPath();
            }
        }
        return null;
    }

    public static String getDataInDesignClassName(View view) {
        DependencyObject dpo = BindViewUtil.getDependencyObject(view);
        for (java.util.Map.Entry<DependencyProperty, Binding> e : dpo.getBindings().entrySet()) {
            if ("dataInDesignClass".equalsIgnoreCase(e.getKey().getPropertyName())) {
                return e.getValue().getPath();
            }
        }
        return null;
    }

    public static String getDataContextName(View view) {
        DependencyObject dpo = BindViewUtil.getDependencyObject(view);
        for (java.util.Map.Entry<DependencyProperty, Binding> e : dpo.getBindings().entrySet()) {
            if ("dataContext".equalsIgnoreCase(e.getKey().getPropertyName())) {
                return e.getValue().getPath();
            }
        }
        return null;
    }
}
