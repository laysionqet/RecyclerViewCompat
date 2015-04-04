
package moe.laysionqet.recyclerviewcompat.extra;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by shunli on 15-2-10.
 */
public abstract class SlowRecyclerAdapterOnScrollListener extends RecyclerView.OnScrollListener {

    private SlowBaseRecyclerAdapter mAdapter;

    private boolean mStrictMode = false;

    private int mScrollState = RecyclerView.SCROLL_STATE_IDLE;

    public SlowRecyclerAdapterOnScrollListener(SlowBaseRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    public SlowRecyclerAdapterOnScrollListener(SlowBaseRecyclerAdapter adapter, boolean strictMode) {
        this(adapter);
        mStrictMode = strictMode;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        mScrollState = newState;
        switch (mScrollState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                recyclerView.postDelayed(new BindRunnable(recyclerView, mAdapter, this), 500);
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                mAdapter.setListBusy(true);
            case RecyclerView.SCROLL_STATE_DRAGGING:
                break;

            default:
                if (mStrictMode) {
                    mAdapter.setListBusy(true);
                }
        }
    }

    private static class BindRunnable implements Runnable {
        private final WeakReference<RecyclerView> mRecyclerViewRef;

        private final WeakReference<SlowBaseRecyclerAdapter> mSlowAdapterRef;

        private final WeakReference<SlowRecyclerAdapterOnScrollListener> mScrollListenerRef;

        // use weak reference to avoid memory-leak
        public BindRunnable(RecyclerView view, SlowBaseRecyclerAdapter slowAdapter,
                SlowRecyclerAdapterOnScrollListener onScrollListener) {
            mRecyclerViewRef = new WeakReference<>(view);
            mSlowAdapterRef = new WeakReference<>(slowAdapter);
            mScrollListenerRef = new WeakReference<>(onScrollListener);
        }

        @Override
        public void run() {
            RecyclerView recyclerView = mRecyclerViewRef.get();
            SlowBaseRecyclerAdapter slowAdapter = mSlowAdapterRef.get();
            SlowRecyclerAdapterOnScrollListener onScrollListener = mScrollListenerRef.get();

            if (null == recyclerView || null == slowAdapter || null == onScrollListener) {
                return;
            }

            if (RecyclerView.SCROLL_STATE_IDLE != onScrollListener.mScrollState) {
                return;
            }

            slowAdapter.setListBusy(false); // make adapter can perform
            // time-consuming operation
            final RecyclerView.Adapter wrapperAdapter = recyclerView.getAdapter();
            final int count = recyclerView.getChildCount();
            for (int i = 0; i < count; i++) {
                // get exact postion and holder as data set from recycler view
                final View childView = recyclerView.getChildAt(i);
                final int position = recyclerView.getChildLayoutPosition(childView);
                final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(childView);
                // Rebind view holder to let adapter perform time-consuming operation.
                // Be aware that the adapter actually set in the RecyclerView might be HeaderViewRecyclerAdapter,
                // in which the adapter we are dealing wrapped.
                wrapperAdapter.onBindViewHolder(holder, position);
            }
        }
    }

}
