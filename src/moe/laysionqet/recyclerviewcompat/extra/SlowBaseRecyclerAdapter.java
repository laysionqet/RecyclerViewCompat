
package moe.laysionqet.recyclerviewcompat.extra;

import android.support.v7.widget.RecyclerView;

/**
 * Created by shunli on 15-2-10.
 */
public abstract class SlowBaseRecyclerAdapter extends RecyclerView.Adapter {

    protected boolean mListBusy;

    public void setListBusy(boolean busy) {
        mListBusy = busy;
    }
}
