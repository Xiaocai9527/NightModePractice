package com.xiaocai.nightmode.colorful.setter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 肖坤 on 2017/9/12.
 * 公司：依迅北斗
 * 邮箱：838494268@qq.com
 */

public class RecyclerViewSetter extends ViewGroupSetter
{

    public RecyclerViewSetter(ViewGroup targetView, int resId)
    {
        super(targetView, resId);
    }

    public RecyclerViewSetter(ViewGroup targetView)
    {
        super(targetView);
    }

    protected void clearRecyclerViewRecyclerBin(View rootView)
    {
        super.clearRecyclerViewRecyclerBin(rootView);
        ((RecyclerView) rootView).getRecycledViewPool().clear();
    }

}
