package xwc.andorid.recyclergridview;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class SortTouchHelperCallback extends ItemTouchHelper.Callback {

	public static final float ALPHA_FULL = 1.0f;

	private final ItemTouchHelperAdapter adapter;

	public interface OnChangeListener{
		boolean onItemMove(int fromPosition, int toPosition);
	}

	public SortTouchHelperCallback(ItemTouchHelperAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public int getMovementFlags(RecyclerView arg0, ViewHolder arg1) {
		// 滑动的时候支持的方向
		int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
				| ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
		// 拖拽的时候支持的方向
		int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

		// 必须调用该方法告诉ItemTouchHelper支持的flags
		return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder,
			ViewHolder target) {
		if (target == null){
			Log.i("Adapter", "Moved + target is null");
		}
		if (viewHolder == null){
			Log.i("Adapter", "Moved + viewHolder is null");
		}
		if (recyclerView == null){
			Log.i("Adapter", "Moved + recyclerView is null");
		}
		if (viewHolder.getItemViewType() != target.getItemViewType()) {
			return false;
		}
		adapter.onItemMove(viewHolder.getAdapterPosition(),
				target.getAdapterPosition());
		return true;
	}

	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView,
			ViewHolder viewHolder, float dX, float dY, int actionState,
			boolean isCurrentlyActive) {
		if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
			// 左右滑动时改变Item的透明度
			final float alpha = ALPHA_FULL - Math.abs(dX)
					/ (float) viewHolder.itemView.getWidth();
			viewHolder.itemView.setAlpha(alpha);
			viewHolder.itemView.setTranslationX(dX);
		} else {
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
					isCurrentlyActive);
		}
	}

	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
			int actionState) {
		if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
			Log.d("ACTION_STATE_IDLE", "ACTION_STATE_IDLE");
			if (viewHolder instanceof ItemTouchHelperViewHolder) {
				Log.d("instanceof", "instanceof");
				ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
				itemViewHolder.onItemSelected();
			}
		}
		super.onSelectedChanged(viewHolder, actionState);
	}

	@Override
	public void clearView(RecyclerView recyclerView,
			RecyclerView.ViewHolder viewHolder) {
		super.clearView(recyclerView, viewHolder);

		Log.d("clearView", "clearView");
		viewHolder.itemView.setAlpha(ALPHA_FULL);

		if (viewHolder instanceof ItemTouchHelperViewHolder) {
			ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
			itemViewHolder.onItemClear();
		}
	}

	/**
	 * 滑动删除
	 */
	@Override
	public void onSwiped(ViewHolder viewHolder, int arg1) {
		adapter.onItemDismiss(viewHolder.getAdapterPosition());
	}

	/**
	 * 支持长按开始拖拽
	 * 
	 * @return
	 */
	@Override
	public boolean isLongPressDragEnabled() {
		return false;
	}

	/**
	 * 支持左右滑动
	 * 
	 * @return
	 */
	@Override
	public boolean isItemViewSwipeEnabled() {
		return false;
	}
}
