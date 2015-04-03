
package moe.laysionqet.recyclerviewcompat.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by shunli on 15-4-1.
 */
public class HFRecyclerView extends RecyclerView {

    private ArrayList<HeaderViewRecyclerAdapter.FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<HeaderViewRecyclerAdapter.FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    public HFRecyclerView(Context context) {
        super(context);
    }

    public HFRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HFRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view) {
        this.addHeaderView(view, null);
    }

    public void addHeaderView(View view, Object data) {
        final HeaderViewRecyclerAdapter.FixedViewInfo info = new HeaderViewRecyclerAdapter.FixedViewInfo();
        info.view = view;
        info.data = data;
        mHeaderViewInfos.add(info);

        if (getAdapter() != null) {
            if (!(getAdapter() instanceof HeaderViewRecyclerAdapter)) {
                setAdapterInParentClass(new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, getAdapter()));
            }

            getAdapter().notifyDataSetChanged();
        }
    }

    public void addFooterView(View view) {
        this.addFooterView(view, null);
    }

    public void addFooterView(View view, Object data) {
        final HeaderViewRecyclerAdapter.FixedViewInfo info = new HeaderViewRecyclerAdapter.FixedViewInfo();
        info.view = view;
        info.data = data;
        mFooterViewInfos.add(info);

        if (getAdapter() != null) {
            if (!(getAdapter() instanceof HeaderViewRecyclerAdapter)) {
                setAdapterInParentClass(new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, getAdapter()));
            }

            getAdapter().notifyDataSetChanged();
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderViewInfos.size();
    }

    public int getFooterViewsCount() {
        return mFooterViewInfos.size();
    }

    protected void setAdapterInParentClass(Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!mHeaderViewInfos.isEmpty() || !mFooterViewInfos.isEmpty()) {
            super.setAdapter(new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, adapter));
        } else {
            super.setAdapter(adapter);
        }
    }
}
