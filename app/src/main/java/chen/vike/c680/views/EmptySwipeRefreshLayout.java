package chen.vike.c680.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by lht on 2017/3/10.
 * 解决ListView+setEmptyView()和SwipeRefreshLayout冲突
 * EmptyView无法显示的问题
 *
 *
 */

public class EmptySwipeRefreshLayout extends SwipeRefreshLayout {
    private View view;

    public EmptySwipeRefreshLayout(Context context) {
        super(context);
    }

    public EmptySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewGroup(View view) {
        this.view = view;
    }

    @Override
    public boolean canChildScrollUp() {
        if (view != null && view instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) view;
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() < absListView.getPaddingTop());
        }
        return super.canChildScrollUp();
    }
}
