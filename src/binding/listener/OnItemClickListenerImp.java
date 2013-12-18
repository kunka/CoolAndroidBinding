/**
 * 
 */
package binding.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author xuanjue.hk
 * @date 2013-2-28
 * */
public class OnItemClickListenerImp extends ListenerToCommand implements OnItemClickListener {
	@Override
	public void registerToView(View v) {
		if (!(v instanceof AdapterView<?>))
			return;
		((AdapterView<?>) v).setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		executeCommand(arg0, arg1, arg2, arg3);
	}
}
