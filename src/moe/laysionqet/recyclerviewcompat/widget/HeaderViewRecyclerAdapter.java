
package moe.laysionqet.recyclerviewcompat.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by shunli on 15-4-1.
 */
public class HeaderViewRecyclerAdapter extends WrapperRecyclerAdapter implements Filterable {

    private final RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    ArrayList<FixedViewInfo> mHeaderViewInfos;
    ArrayList<FixedViewInfo> mFooterViewInfos;

    static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>(0);

    private final boolean mIsFilterable;

    public HeaderViewRecyclerAdapter(ArrayList<FixedViewInfo> headerViewInfos,
                                     ArrayList<FixedViewInfo> footerViewInfos,
                                     RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
        mIsFilterable = mAdapter instanceof Filterable;

        if (headerViewInfos == null) {
            mHeaderViewInfos = EMPTY_INFO_LIST;
        } else {
            mHeaderViewInfos = headerViewInfos;
        }

        if (footerViewInfos == null) {
            mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            mFooterViewInfos = footerViewInfos;
        }
    }

    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    public boolean isEmpty() {
        return mAdapter == null || mAdapter.getItemCount() == 0;
    }

    public boolean removeHeader(View v) {
        for (int i = 0; i < mHeaderViewInfos.size(); i++) {
            FixedViewInfo info = mHeaderViewInfos.get(i);
            if (info.view == v) {
                mHeaderViewInfos.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.view == v) {
                mFooterViewInfos.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean isHeaderView(int position) {
        if (position < 0) throw new IllegalArgumentException(String.format("Invalid position %d", position));
        return position < mHeaderViewInfos.size();
    }

    public boolean isFooterView(int position) {
        if (position >= getItemCount()) throw new IllegalArgumentException(String.format("Position out of bound %d", position));
        return position >= getHeadersCount() + mAdapter.getItemCount();
    }

    private boolean isFixedViewType(int viewType) {
        return viewType <= 0;
    }

    private FixedViewHolder newFixedViewHolderByViewType(int viewType) {
        final int position = -viewType;
        if (isHeaderView(position)) {
            return new FixedViewHolder(mHeaderViewInfos.get(position).view);
        }
        if (isFooterView(position)) {
            return new FixedViewHolder(mFooterViewInfos.get(position - getHeadersCount()).view);
        }
        throw new IllegalArgumentException("Invalid fixed view type");
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return -position;
        } else if (isFooterView(position)) {
            return -(position - mAdapter.getItemCount());
        }

        final int wrappedType = mAdapter.getItemViewType(position - getHeadersCount());
        if (wrappedType <= 0)
            throw new IllegalArgumentException(String.format(
                    "item view type must >= 1\nheader count %d  footer count %d  item count %d\ngiven position\ngiven position %d",
                    getHeadersCount(),
                    getFootersCount(),
                    mAdapter.getItemCount(),
                    position));
        return wrappedType;
    }

    @Override
    public RecyclerView.Adapter getWrappedAdapter() {
        return mAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isFixedViewType(viewType)) {
            return newFixedViewHolderByViewType(viewType);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position) || isFooterView(position)) {
            return;
        }

        if (!(holder instanceof FixedViewHolder)) {
            mAdapter.onBindViewHolder(holder, position - getHeadersCount());
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public Filter getFilter() {
        if (mIsFilterable) {
            return ((Filterable) mAdapter).getFilter();
        }
        return null;
    }

    static final class FixedViewInfo {
        public View view;
        public Object data;
    }

    private final class FixedViewHolder extends RecyclerView.ViewHolder {

        public FixedViewHolder(View itemView) {
            super(itemView);
        }
    }

}
