# RecyclerGridView 1.1
#效果图
![image](https://github.com/xiangwencheng1994/RecyclerGridView/blob/master/screenshots/demo1.png)
##快捷引用
###1.在你的项目的build.gradle中添加maven { url "https://jitpack.io" } [![](https://jitpack.io/v/xiangwencheng1994/RecyclerGridView.svg)](https://jitpack.io/#xiangwencheng1994/RecyclerGridView)
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
compile 'com.github.xiangwencheng1994:RecyclerGridView:1.1'
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

        //新建简单试图适配器
        SimpleViewAdapter adapter = new SimpleViewAdapter(null);
        //新建适配规则
        Map<String, SimpleViewMatcher> matchers = new HashMap<>();
        //设置"text"，对应R.id.text，类型为TextView
        matchers.put("text", new SimpleViewMatcher(R.id.text, SimpleViewMatcher.TEXTVIEW));
        //设置"img"，对应R.id.image，类型为ImageView
        matchers.put("img", new SimpleViewMatcher(R.id.image, SimpleViewMatcher.IMAGEVIEW));
        //将适配器与适配规则绑定，R.layout.layout_card为视图布局，其中包含有R.id.text和R.id.image
        adapter.setLayout(R.layout.layout_grid_item, matchers);
        //获取适配器的数据源
        gridItems = adapter.getDatas();

        //添加数据
        for(int i = 0; i < 16; i++){
            Map<String, Object> item = new HashMap<>();
            //设置标题
            item.put("text", "Item-" + i);
            //设置图片
            //item.put("img", null);
            gridItems.add(item);
        }

        //监听项目点击事件
        //adapter.setOnItemClickListener(this);
        //监听项目交换
        //adapter.setOnItemMoveListener(this);
        //设置布局样式为GridStyle表格布局，4列
        //recyclerGridView.setLayoutStyle(RecyclerGridView.GridStyle, 4);   //默认
        //设置布局样式为StaggeredGridStyle瀑布流布局，2列
        //recyclerGridView.setLayoutStyle(RecyclerGridView.StaggeredGridStyle, 2);

        recyclerGridView.setAdapter(adapter);
        //设置长按拖拽排序
        //recyclerGridView.setLongTouchSortListener(adapter);

        adapter.notifyDataSetChanged();
```
