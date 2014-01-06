/*
 * Copyright (C) 2014 kk-team.com.
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

package com.kk.binding.register;

import com.kk.binding.listener.ListenerToCommand;
import com.kk.binding.listener.OnClickListenerImp;
import com.kk.binding.listener.OnFocusChangeListenerImp;
import com.kk.binding.listener.OnItemClickListenerImp;
import com.kk.binding.util.StringUtil;

import java.util.HashMap;

/**
 * Created by xj on 14-1-6.
 */
public class ListenerImpRegister {
    private static final String TAG = "Binding-ListenerImpRegister";
    private static HashMap<String, Class<? extends ListenerToCommand>> listenerImps;

    public static HashMap<String, Class<? extends ListenerToCommand>> getListenerImps() {
        if (listenerImps == null) {
            listenerImps = new HashMap<String, Class<? extends ListenerToCommand>>(32);
            registerInner();
        }
        return listenerImps;
    }

    private static void registerInner() {
        register("OnClick", OnClickListenerImp.class);
        register("OnItemClick", OnItemClickListenerImp.class);
        register("OnFocusChange", OnFocusChangeListenerImp.class);
    }

    public static void register(String listenerName, Class<? extends ListenerToCommand> listenerImpType) {
        if (!StringUtil.isNullOrEmpty(listenerName) && listenerImpType != null)
            getListenerImps().put(listenerName, listenerImpType);
    }
}
