package com.duonghd.test.kdb.pager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create on 2019-06-03
 *
 * @author duonghd
 */

public class KDBPagerAdapter extends PagerAdapter {
    private List<View> views;
    private int fakePosition;
    private boolean isFirstRender;
    private boolean isIncrease;

    public KDBPagerAdapter(List<View> views) {
        this.fakePosition = Integer.MAX_VALUE / 2;
        this.views = new ArrayList<>();
        this.views.addAll(views);
        this.isFirstRender = true;
        this.isIncrease = true;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout;
        if (isFirstRender) {
            layout = views.get(views.size() / 2 + position - fakePosition);
        } else if (isIncrease) {
            layout = views.get(views.size() - 1);
        } else {
            layout = views.get(0);
        }
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void updateView(List<View> views, boolean isIncrease) {
        this.isFirstRender = false;
        this.isIncrease = isIncrease;
        this.views.clear();
        this.views.addAll(views);
    }
}
