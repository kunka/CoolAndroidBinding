/**
 * 
 */
package com.kk.binding.kernel;

import android.view.View;

/**
 * @author xuanjue.hk
 * @date 2013-2-27
 * */
public interface ICommand {
	public void execute(View view, Object... args);
}
