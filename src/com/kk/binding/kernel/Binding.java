/**
 * 
 */
package com.kk.binding.kernel;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 * */
public class Binding {
	private String path;
	private Object dataContext;
	private IValueConverter valueConverter;

	public String getPath() {
		return path;
	}

	public void setPath(String propertyName) {
		this.path = propertyName;
	}

	public Object getDataContext() {
		return dataContext;
	}

	public void setDataContext(Object dataContext) {
		this.dataContext = dataContext;
	}

	/**
	 * @return the valueConverter
	 */
	public IValueConverter getValueConverter() {
		return valueConverter;
	}

	/**
	 * @param valueConverter
	 *            the valueConverter to set
	 */
	public void setValueConverter(IValueConverter valueConverter) {
		this.valueConverter = valueConverter;
	}
}
