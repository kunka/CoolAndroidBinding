CoolAndroidBinding
==================

Json(Object)+DynamicXml+DesignPreview

## Features
 * Directly bind Object to View
 * Converter support
 * Command Binding support(Pre define UrlNavCommand, etc)
 * Preview in design mode(specify dataContext through Object Class name or raw/json)
 * Binding list data to ViewGroup + ViewPager support

## TODO(Not Support)
 * Binding list data to ListView and paging
 * Dynamic xml


###Simple use
*************************************************************************************

layout1.xml(View)

```xml
 <TextView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:binding="http://schemas.android.com/apk/res-auto"
    binding:binding="{property=text,path=title}"
    binding:binding2="{event=OnClick,command=click}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

DataClass(ViewModel)

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
    View view = inflate(R.id.layout1);
    ViewModel viewModel = new ViewModel();
    BindViewUtil.setDataContext(view, viewModel);
    ...

    // change title
    viewModel.setTitle("newTitle");
```

###Advance use
*************************************************************************************
layout2.xml


```xml
<com.kk.binding.view.BindableFrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:design="http://schemas.android.com/apk/res-auto"
    xmlns:binding="http://schemas.android.com/apk/res-auto"
    design:propertyDeclareClass="com.kk.binding.PropertyDeclare"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    design:designData="@raw/main">
    
     <LinearLayout android:layout_width="wrap_content"
        android:orientation="vertical"
        android:layout_height="wrap_content" >
    
         <TextView
            binding:binding="{property=text,path=data[0].title}"
            binding:binding2="{event=onClick,path=redirectUrl,command=urlNavCommand}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        
        <TextView
            binding:dataContext="{data[1]}"
            binding:binding="{property=text,path=title}"
            binding:binding2="{event=onClick,path=redirectUrl,command=urlNavCommand}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
     </LinearLayout>
     
</com.kk.binding.view.BindableFrameLayout> 
```

main.json

```json
 {
  "data": [
    {
      "title": "cat",
      "redirectUrl":"http://github.com/"
    },
    {
      "title": "dog",
      "redirectUrl":"http://google.com/"
    }
    ]
}
```

Simple bind

```java
    View view = inflate(R.id.layout2);
    Object object = fromRawJson() or fromNetworkJson();
    BindViewUtil.setDataContext(view, object);
```

By use our BindableFrameLayout as the root container, you can preview in Android Studio design mode.(Not necessary assuredly)

