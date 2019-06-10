package com.duonghd.test.kdb.pager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.duonghd.test.kdb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create on 2019-06-03
 *
 * @author duonghd
 */

public class PagerActivity extends AppCompatActivity {
    private KDBPagerAdapter kdbPagerAdapter;
    private int firstPosition = Integer.MAX_VALUE / 2;
    private int firstMonth = 5;
    private int lastPosition = firstPosition;
    private int selectMonth = firstMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final ViewPager viewPager = findViewById(R.id.activity_pager_view_pager);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(kdbPagerAdapter = new KDBPagerAdapter(initData(firstMonth)));
        viewPager.setCurrentItem(firstPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                Log.e("select Month", String.valueOf(selectMonth = firstMonth + (position - firstPosition)));
                Log.e("lastPosition", String.valueOf(lastPosition));
                kdbPagerAdapter.updateView(initData(selectMonth), lastPosition < position);
                kdbPagerAdapter.notifyDataSetChanged();
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<View> initData(int currentMonth) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setBackgroundColor(Color.parseColor("#F0F0F0"));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            TextView textView = new TextView(this);
            textView.setText(String.valueOf(currentMonth - 2 + i));
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            views.add(linearLayout);
        }
        return views;
    }
}
