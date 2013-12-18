package com.kk.binding.listener;

import android.view.View;

public class OnClickListenerImp extends ListenerToCommand implements View.OnClickListener {
    @Override
    public void registerToView(View v) {
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        executeCommand(v, getParam());
    }
}