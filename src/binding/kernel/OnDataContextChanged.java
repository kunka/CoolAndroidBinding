package binding.kernel;

public interface OnDataContextChanged {
    public boolean onDataContextChanged(DependencyObject dpo, PropertyChangedEventArgs args);
}