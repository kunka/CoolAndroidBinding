/**
 * 
 */
package binding.converter;

import android.view.View;
import binding.kernel.IValueConverter;

/**
 * @author xuanjue.hk
 * @date 2013-5-28
 * */
public class StringToVisibleConverter implements IValueConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see binding.kernel.IConverter#converter(java.lang.Object)
	 */
	@Override
	public Object converter(Object source) {
		if (source != null && source.getClass() == String.class) {
			String value = (String) source;
			return (!value.isEmpty()) ? View.VISIBLE : View.GONE;
		}
		return View.GONE;
	}
}
