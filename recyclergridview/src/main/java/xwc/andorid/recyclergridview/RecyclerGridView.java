package xwc.andorid.recyclergridview;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

/**
 * 支持拖动的表格视图
 *
 * @author 向文成
 */
public class RecyclerGridView extends RecyclerView {

    private ItemTouchHelper mItemTouchHelper;
    private int spanCount; // 表格列数

    private GridLayoutManager layoutManager;

    public RecyclerGridView(Context context) {
        this(context, null);
    }

    public RecyclerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //提取android:numColumns，默认4列
        if (attrs != null)
            spanCount = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "numColumns", 4);
        else spanCount = 4;
        layoutManager = new GridLayoutManager(context, spanCount);
        this.setHasFixedSize(true);
        this.setLayoutManager(layoutManager);
        this.setItemAnimator(new DefaultItemAnimator());
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        if (layoutManager != null) layoutManager.setSpanCount(spanCount);
    }

    public void setLongTouchSortListener(SimpleViewAdapter adapter) {
        adapter.setOnItemLongClickListener(new SimpleViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                mItemTouchHelper.startDrag((ViewHolder) view.getTag());
            }
        });
        ItemTouchHelper.Callback callback = new SortTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(this);
    }

}
