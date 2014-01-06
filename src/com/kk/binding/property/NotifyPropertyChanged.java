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
package com.kk.binding.property;

import java.util.ArrayList;

import com.kk.binding.property.INotifyPropertyChanged;
import com.kk.binding.property.IPropertyChanged;
import com.kk.binding.property.PropertyChangedEventArgs;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 * */
public abstract class NotifyPropertyChanged implements INotifyPropertyChanged {
	protected ArrayList<IPropertyChanged> listeners = new ArrayList<IPropertyChanged>();

	public void removeListener(IPropertyChanged listener) {
		listeners.remove(listener);
	}

	@Override
	public void setPropertyChangedListener(IPropertyChanged listener) {
		listeners.add(listener);
	}

	protected void raisePropertyChangedEvent(String PropertyName) {
		for (IPropertyChanged listener : listeners) {
			listener.propertyChanged(this, new PropertyChangedEventArgs(PropertyName));
		}
	}

	public void clear() {
		listeners.clear();
	}
}
