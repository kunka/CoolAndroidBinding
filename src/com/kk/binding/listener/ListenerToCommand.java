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
package com.kk.binding.listener;

import android.view.View;

import com.kk.binding.kernel.ICommand;
import com.kk.binding.util.BindDesignLog;

/**
 * @author xuanjue.hk
 * @date 2013-2-28
 */
public abstract class ListenerToCommand {
    private ICommand mCommand;
    private Object param;

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public ICommand getCommand() {
        return mCommand;
    }

    public void setCommand(ICommand command) {
        mCommand = command;
    }

    public void executeCommand(View view, Object... args) {
        if (mCommand != null)
            try {
                mCommand.execute(view, args);
            } catch (Exception e) {
                BindDesignLog.e("ListenerToCommand", "executeCommand failed " + e.toString());
            }
    }

    public abstract void registerToView(View v);
}
