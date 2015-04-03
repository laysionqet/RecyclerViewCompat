
package moe.laysionqet.recyclerviewcompat.widget;

import android.support.v7.widget.RecyclerView;

/**
 * Created by shunli on 15-4-1.
 */
public abstract class WrapperRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {

    public abstract RecyclerView.Adapter<VH> getWrappedAdapter();

}
