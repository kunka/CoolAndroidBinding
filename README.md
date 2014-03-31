CoolAndroidBinding
==================

Json(Object)+DynamicXml+DesignPreview

## Features
 * Non-invasive, super lightweight, Compatible with android coding style, work as an optional plugin
 * Directly bind object to view
 * Converter support(scalable)
 * Command Binding support(Pre define UrlNavCommand, etc, scalable)
 * Custom Bind value setter(scalable)
 * Preview in design mode(specify dataContext through class name or raw/json)
 * Binding list data to ViewGroup + ViewPager support

## TODO(Not Support now)
 * Binding list data to ListView and pagination
 * Dynamic xml :)


###Simple usage
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

###Advanced Usage
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
            binding:binding2="{event=onClick,path=data[0].redirectUrl,command=urlNavCommand}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        
        <TextView
            binding:dataContext="{data[1]}"
            binding:binding="{property=text,path=title}"
            binding:binding2="{event=onClick,path=redirectUrl,command=urlNavCommand}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
            
     </LinearLayout>
     
     <com.kk.binding.view.BindableAdapterFrameLayout
            android:layout_width="wrap_content"
            binding:dataContext="{data}"
            binding:childLayout="@layout/list_item"
            android:layout_height="wrap_content">

            <!--Can be any kind of ViewGroup or ViewPager-->
            <LinearLayout
                android:gravity="center_vertical"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

        </com.kk.binding.view.BindableAdapterFrameLayout>
     
</com.kk.binding.view.BindableFrameLayout> 
```

list_item.xml

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:binding="http://schemas.android.com/apk/res-auto"
    android:gravity="center_vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
    
    <ImageView
        binding:binding="{property=url,path=picUrl}"
        binding:binding2="{event=onClick,path=redirectUrl,command=urlNavCommand}"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
            
    <TextView
        binding:binding="{property=text,path=title}"
        binding:binding2="{event=onClick,path=redirectUrl,command=urlNavCommand}"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</LinearLayout>
```

main.json

```json
 {
  "data": [
    {
      "title": "cat",
      "picUrl": "http://lorempixel.com/100/100",
      "redirectUrl":"http://github.com/"
    },
    {
      "title": "dog",
      "picUrl": "http://lorempixel.com/100/100",
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

Custom value setter

```java
BindEngine.setBindValueSetter(new BindValueSetter() {
            @Override
            public boolean setValue(final Object target, DependencyProperty dp, final Object value, String path) {
                if ("url".equals(dp.getPropertyName()) && ImageView.class.isAssignableFrom(dp.getOwnerType())) {
                    // set image with url. (Strongly Recommended to use the Universal-Image-Loader)
                    ...
                }
            }
     });
```         

By use our BindableFrameLayout as the root container, you can preview in Android Studio design mode.(Not necessary assuredly)

Use BindableAdapterFrameLayout as the container of ViewGroup,  you can bind list data and preview in Android Studio design mode.

