/**
 * 
 */
package com.kk.binding.kernel;

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
