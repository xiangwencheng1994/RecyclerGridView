package xwc.andorid.recyclergridviewdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xwc.andorid.recyclergridview.RecyclerGridView;
import xwc.andorid.recyclergridview.SimpleViewAdapter;
import xwc.andorid.recyclergridview.SimpleViewMatcher;

public class MainActivity extends Activity implements SimpleViewAdapter.OnItemClickListener, SimpleViewAdapter.OnItemMoveListener {

    private RecyclerGridView recyclerGridView;
    private SimpleViewAdapter adapter;
    private List<Map<String, Object>> gridItems;

    private View.OnClickListener unDone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("该功能尚未开放")
                    .setPositiveButton("确定", null)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerGridView = (RecyclerGridView) findViewById(R.id.RecyclerGridView);

        initGridView();
    }

    private void initGridView() {
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

        //向适配器的数据源中添加数据
        for(int i = 0; i < 16; i++){
            addGridItem("Item-" + i, null, unDone);
        }

        //监听项目点击事件
        adapter.setOnItemClickListener(this);
        //监听项目交换
        adapter.setOnItemMoveListener(this);
        //设置布局样式为GridStyle表格布局，4列
        recyclerGridView.setLayoutStyle(RecyclerGridView.GridStyle, 4);
        //设置布局样式为StaggeredGridStyle瀑布流布局，2列
        //recyclerGridView.setLayoutStyle(RecyclerGridView.StaggeredGridStyle, 2);

        recyclerGridView.setAdapter(adapter);
        //设置长按拖拽排序
        recyclerGridView.setLongTouchSortListener(adapter);

        adapter.notifyDataSetChanged();
    }

    private void addGridItem(String text, Integer drawable_id, View.OnClickListener v) {
        Map<String, Object> item = new HashMap<>();
        item.put("text", text);
        item.put("img", drawable_id);
        item.put("onClickListener", v);
        gridItems.add(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        SimpleViewAdapter.ViewHolder holder = (SimpleViewAdapter.ViewHolder) view.getTag();
        View.OnClickListener onClickCallback = (View.OnClickListener) holder.getData().get("onClickListener");
        if (onClickCallback != null) onClickCallback.onClick(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //允许移动，返回true;不允许移动，返回false
        Toast.makeText(this, "Item at " +fromPosition + " has move to " + toPosition, Toast.LENGTH_LONG).show();
        return true;
    }
}
