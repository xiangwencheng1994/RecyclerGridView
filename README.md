# RecyclerGridView
##快捷引用<br>
###1.在你的项目的build.gradle中添加maven { url "https://jitpack.io" }
```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
###2.在你要引入的模块中添加dependencies
```gradle
compile 'com.github.xiangwencheng1994:RecyclerGridView:aeeda56b1a'
```
##简单使用
###Step1.布局文件中添加View
```xml
<xwc.andorid.recyclergridview.RecyclerGridView
        android:id="@+id/recyclerGridView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:numColumns="4"
        android:layout_below="@+id/roll_view_pager" >
</xwc.andorid.recyclergridview.RecyclerGridView>
```
###Step2.初始化控件
```java
        //找到View
        RecyclerGridView recyclerGridView = (RecyclerGridView) findViewById(R.id.recyclerGridView);

        SimpleViewAdapter adapter = new SimpleViewAdapter(null);
        Map<String, SimpleViewMatcher> matchers = new HashMap<>();
        matchers.put("text", new SimpleViewMatcher(R.id.text, SimpleViewMatcher.TEXTVIEW));
        matchers.put("img", new SimpleViewMatcher(R.id.image, SimpleViewMatcher.IMAGEVIEW));
        adapter.setLayout(R.layout.layout_grid_item, matchers); //设置适配器，布局和适配规则
        gridItems = adapter.getDatas();

        //TODO:添加数据
        for(int i = 0; i < 16; i++){
            Map<String, Object> item = new HashMap<>();
            //设置标题
            item.put("text", "Item-" + i);
            //设置图片
            //item.put("img", null);
            gridItems.add(item);
        }

        //添加点击事件
        //adapter.setOnItemClickListener(this);
        recyclerGridView.setAdapter(adapter);
        recyclerGridView.setLongTouchSortListener(adapter);
        adapter.notifyDataSetChanged();
```