package binding.feature;

import binding.kernel.DependencyProperty;

/**
 * Created by hk on 13-12-12.
 */
public interface BindValueSetter {
    public boolean setValue(Object target, DependencyProperty dp, Object value, String path);
}

