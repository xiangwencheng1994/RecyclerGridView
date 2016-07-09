package xwc.andorid.recyclergridview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 列表适配器,适用于带一个ImageView和一个TextView的布局,put("text")设置文本，put("img")设置图标;
 *
 * @author 向文成
 */
public class SimpleViewAdapter extends
        RecyclerView.Adapter<SimpleViewAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {

    private int layout;
    private int maskColor = Color.argb(100, 100, 100, 100);
    private Map<String, SimpleViewMatcher> remoteView;
    private List<Map<String, Object>> datas;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemMoveListener mOnItemMoveListener;

    public SimpleViewAdapter(List<Map<String, Object>> datas) {
        if (datas == null) datas = new ArrayList<>();
        this.datas = datas;
    }

    public void setLayout(@LayoutRes int layout, Map<String, SimpleViewMatcher> remoteViews) {
        this.layout = layout;
        this.remoteView = remoteViews;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.fillView(datas.get(position));
        holder.itemView.setTag(holder);
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView
                    .setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemLongClickListener.onItemLongClick(
                                    holder.itemView, pos);
                            return true;
                        }
                    });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(this.layout, parent,
                false));
        return holder;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnItemMoveListener {
        boolean onItemMove(int fromPosition, int toPosition);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        this.mOnItemMoveListener = onItemMoveListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private Map<String, Object> data;

        AnimatorSet upSet, downSet;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View itemView) {
            super(itemView);
            // 创建动画
            ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(itemView,
                    "scaleX", 0.95f);
            ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(itemView,
                    "scaleY", 0.95f);
            ObjectAnimator upAnim = ObjectAnimator.ofFloat(itemView,
                    "translationZ", 10);
            upSet = new AnimatorSet();
            upSet.playSequentially(scaleXAnim, scaleYAnim, upAnim);
            upSet.setDuration(100);
            upSet.setInterpolator(new DecelerateInterpolator());
            ObjectAnimator downAnim = ObjectAnimator.ofFloat(itemView,
                    "translationZ", 0);
            ObjectAnimator scaleXDownAnim = ObjectAnimator.ofFloat(itemView,
                    "scaleX", 1.0f);
            ObjectAnimator scaleYDownAnim = ObjectAnimator.ofFloat(itemView,
                    "scaleY", 1.0f);
            downSet = new AnimatorSet();
            downSet.playSequentially(scaleXDownAnim, scaleYDownAnim, downAnim);
            downSet.setDuration(100);
            downSet.setInterpolator(new DecelerateInterpolator());
        }

        public Map<String, Object> getData() {
            return this.data;
        }

        public void fillView(Map<String, Object> data) {
            this.data = data;

            Set<String> keys = data.keySet();
            for (String key : keys) {
                SimpleViewMatcher matcher = remoteView.get(key);
                if (matcher == null) continue;
                View v = itemView.findViewById(matcher.getId());
                if (v == null) continue;
                switch (matcher.getType()) {
                    case SimpleViewMatcher.TEXTVIEW:
                        ((TextView) v).setText((String) data.get(key));
                        break;
                    case SimpleViewMatcher.IMAGEVIEW:
                        Object img = data.get(key);
                        if (img == null) {
                            ((ImageView) v).setImageResource(R.drawable.ic_launcher);
                        }
                        if (img instanceof Integer) {
                            ((ImageView) v).setImageResource((Integer) img);
                        } else if (img instanceof Bitmap) {
                            ((ImageView) v).setImageBitmap((Bitmap) img);
                        } else if (img instanceof Uri) {
                            ((ImageView) v).setImageURI((Uri) img);
                        } else {
                            Log.e("SimpleViewAdapter", "unknown src for ImageView with " + img);
                        }
                        break;
                    default:
                        Log.e("SimpleViewAdapter", "unsupported SimpleViewMatcher.Type :  "
                                + matcher.getType());
                        break;
                }
            }

        }

        @Override
        public void onItemSelected() {
            itemView.clearAnimation();
            upSet.start();
        }

        @Override
        public void onItemClear() {
            itemView.clearAnimation();
            downSet.start();
        }

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (mOnItemMoveListener != null) {
            if (!mOnItemMoveListener.onItemMove(fromPosition, toPosition))
                return false;
        }
        Collections.swap(datas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        datas.remove(position);

        notifyItemRemoved(position);
    }

}
