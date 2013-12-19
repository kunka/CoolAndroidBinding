/**
 *
 */
package com.kk.binding.converter;

import android.view.View;

import com.kk.binding.kernel.IValueConverter;

/**
 * @author xuanjue.hk
 * @date 2013-5-28
 */
public class TrueToVisibleConverter implements IValueConverter {

    /*
     * (non-Javadoc)
     *
     * @see binding.kernel.IConverter#converter(java.lang.Object)
     */
    @Override
    public Object converter(Object source) {
        return Boolean.parseBoolean(String.valueOf(source)) ? View.VISIBLE : View.GONE;
    }
}
