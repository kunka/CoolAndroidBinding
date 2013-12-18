/**
 * 
 */
package binding.feature;

import java.util.ArrayList;

import binding.kernel.INotifyPropertyChanged;
import binding.kernel.IPropertyChanged;
import binding.kernel.PropertyChangedEventArgs;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 * */
public abstract class BaseBindDataObject implements INotifyPropertyChanged, IBindDataProvider {
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

	protected Object data;

	@Override
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		if (this.data != data) {
			this.data = data;
			raisePropertyChangedEvent("data");
		}
	}

	protected Object dataInDesign;

	public Object getDataInDesign() {
		return dataInDesign;
	}

	public void setDataInDesign(Object dataInDesign) {
		if (this.dataInDesign != data) {
			this.dataInDesign = data;
			raisePropertyChangedEvent("dataInDesign");
		}
	}

	public void clear() {
		listeners.clear();
	}
}
