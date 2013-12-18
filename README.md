CoolAndroidBinding
==================

Json(Object)+DynamicXml+DesignPreview

## Features
 * Directly bind Object to View
 * Converter support
 * Command Binding support(Pre define UrlNavCommand, etc)
 * Preview in design mode(specify dataContext through Object Class name or raw/json)
 * Binding list data to ViewGroup + ViewPager support

## Not Support(TODO)
 * Binding list data to ListView and paging
 * Dynamic xml


Simple use

xml(View)

```xml
 <TextView
    xmlns:binding="http://schemas.android.com/apk/res-auto"
    binding:binding="{property=text,path=title}"
    binding:binding2="{event=OnClick,command=click}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

data(ViewModel)

```java
    public class ViewModel implements INotifyPropertyChanged {
        private IPropertyChanged listener;
    
        @Override
        public void setPropertyChangedListener(IPropertyChanged listener) {
            this.listener = listener;
        }
    
        private String title;
        private ICommand click;
    
        public ViewModel {
            title = "title";
            click = new ICommand() {
                @Override
                public void execute(View view, Object... args) {
                    // do sth
                }
            };
        }
    
        public void setTitle(String title) {
            if (!StringUtil.compare(this.title, title)) {
                this.title = title;
                if(listener!=null){
                    listener.propertyChanged(this, new PropertyChangedEventArgs("title"));
                }
            }
        }
        
        public String getTitle() {
            return title;
        }
    
        public ICommand getClick() {
            return click;
        }
    }
```

Simple bind

```java
    View view = ?;
    ViewModel viewModel = new ViewModel();
    BindViewUtil.setDataContext(view, viewModel);
    ...

    // change title
    viewModel.setTitle("newTitle");
```

************************************************************************
More use

