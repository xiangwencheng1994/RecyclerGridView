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

public class MainActivity extends Activity implements SimpleViewAdapter.OnItemClickListener {

    private RecyclerGridView recyclerGridView;
    private SimpleViewAdapter adapter;
    private List<Map<String, Object>> gridItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerGridView = (RecyclerGridView) findViewById(R.id.RecyclerGridView);

        initGridView();

    }

    private void initGridView() {
        adapter = new SimpleViewAdapter(null);
        Map<String, SimpleViewMatcher> matchers = new HashMap<>();
        matchers.put("text", new SimpleViewMatcher(R.id.text, SimpleViewMatcher.TEXTVIEW));
        matchers.put("img", new SimpleViewMatcher(R.id.image, SimpleViewMatcher.IMAGEVIEW));
        adapter.setLayout(R.layout.layout_grid_item, matchers);
        gridItems = adapter.getDatas();
        gridItems.clear();

        //add Data
        View.OnClickListener unDone = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("该功能尚未开放")
                        .setPositiveButton("确定", null)
                        .show();
            }
        };
        for(int i = 0; i < 16; i++){
            addGridItem("Item-" + i, null, unDone);
        }

        //set to recyclerGridView
        adapter.setOnItemClickListener(this);
        recyclerGridView.setAdapter(adapter);
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
}
