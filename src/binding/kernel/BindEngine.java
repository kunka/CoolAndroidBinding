package binding.kernel;

import android.content.Context;

import binding.feature.BindValueSetter;

/**
 * Created by hk on 13-12-10.
 */
public class BindEngine {
    private static BindEngine bindEngine;
    private static BindValueSetter bindValueSetter;
    private Context mContext;

    public static BindEngine instance() {
        if (bindEngine == null) {
            bindEngine = new BindEngine();
        }
        return bindEngine;
    }

    private BindEngine() {
    }

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public static void setBindValueSetter(BindValueSetter bindValueSetter) {
        BindEngine.bindValueSetter = bindValueSetter;
    }

    public static BindValueSetter getBindValueSetter() {
        return bindValueSetter;
    }
}
