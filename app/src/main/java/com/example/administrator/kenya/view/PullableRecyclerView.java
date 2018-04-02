package com.example.administrator.kenya.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.administrator.kenya.interfaces.Pullable;


/**
 * Created by Administrator on 2018/3/14.
 */

public class PullableRecyclerView extends RecyclerView implements Pullable {

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {
        if (getChildCount() == 0) {
            return true;
        } else if (getChildAt(0).getTop() >= 0) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int firstVisibleItem = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {
                    return true;
                }
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                int firstVisibleItem = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        return false;
    }
}
