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

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 * */
public class PropertyChangedEventArgs {
	private String propertyName;
	private Object oldValue;
	private Object newValue;

	public PropertyChangedEventArgs(String propertyName) {
		this.propertyName = propertyName;
	}

	public PropertyChangedEventArgs(String propertyName, Object oldValue, Object newValue) {
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return the propertyName
	 */
	public String getName() {
		return propertyName;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}
}
