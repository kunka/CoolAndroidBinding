package com.kk.demo;

import android.app.Activity;
import android.os.Bundle;
import com.kk.binding.kernel.BindEngine;
import com.kk.binding.view.BindViewUtil;
import com.kk.example.BindingExample.R;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set context on activity created
        BindEngine.instance().init(this);
        // inject inflater
        BindViewUtil.injectInflater(this);
        setContentView(R.layout.activity_main_waterfall_vertical);
    }
}
