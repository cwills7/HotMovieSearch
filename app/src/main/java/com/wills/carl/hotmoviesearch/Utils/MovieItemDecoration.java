package com.wills.carl.hotmoviesearch.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MovieItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;

    public MovieItemDecoration (int spacing){
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect dimens, View view, RecyclerView parent, RecyclerView.State state){
        dimens.bottom = spacing;
        dimens.left = spacing;
        dimens.right = spacing;

        if (parent.getChildLayoutPosition(view) == 0){
            dimens.top = spacing;
        } else {
            dimens.top = 0;
        }
    }

}
