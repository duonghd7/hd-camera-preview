package com.duonghd.test.kdb.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.duonghd.test.kdb.R;
import com.duonghd.test.kdb.utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create on 2019-06-03
 *
 * @author duonghd
 */

public class KDBCalendar extends FrameLayout {
    private final String TAG = KDBCalendar.class.getSimpleName();
    private Context context;

    private boolean kdbSwipeable;
    private int screenWidth, paddingLeft, paddingRight, kdbMonth, kdbYear;
    private Calendar calendar;
    private Calendar calendarCurrentDay;

    public KDBCalendar(Context context) {
        super(context);
        initLocalData(context, null);
    }

    public KDBCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLocalData(context, attrs);
    }

    public KDBCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLocalData(context, attrs);
    }

    private void initLocalData(Context context, @Nullable AttributeSet attrs) {
        this.context = context;

        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendarCurrentDay = Calendar.getInstance();
        calendarCurrentDay.setTime(new Date());

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.attrKDBCalendar, 0, 0);
        kdbSwipeable = typedArray.getBoolean(R.styleable.attrKDBCalendar_kdbSwipeable, true);
        kdbMonth = typedArray.getInteger(R.styleable.attrKDBCalendar_kdbMonth, 0);
        kdbYear = typedArray.getInteger(R.styleable.attrKDBCalendar_kdbYear, 0);

        paddingLeft = (int) typedArray.getDimension(R.styleable.attrKDBCalendar_kdbPaddingLeft, 0);
        paddingRight = (int) typedArray.getDimension(R.styleable.attrKDBCalendar_kdbPaddingRight, 0);

        if (kdbMonth > 0 && kdbMonth < 13 && kdbYear > 0) calendar.set(kdbYear, kdbMonth - 1, 1);

        firstPosition = Integer.MAX_VALUE / 2;
        lastPosition = firstPosition;
    }

    private boolean canRender = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (canRender) {
            canRender = false;
            renderView();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* *
     * RENDER----------------------------------------------------------------------------------------
     * */
    private KDBPagerAdapter kdbPagerAdapter;
    private int firstPosition;
    private int lastPosition;

    private void renderView() {
        this.removeAllViews();
        ViewPager viewPager = new ViewPager(context);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(kdbPagerAdapter = new KDBPagerAdapter(initData(calendar)));
        viewPager.setCurrentItem(firstPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                calendar.add(Calendar.MONTH, position - lastPosition);
                Log.e("-----", "-----");
                Log.e("selectMonth", String.valueOf(calendar.get(Calendar.MONTH) + 1));
                Log.e("selectYear", String.valueOf(calendar.get(Calendar.YEAR)));
                kdbPagerAdapter.updateView(initData(calendar), lastPosition < position);
                kdbPagerAdapter.notifyDataSetChanged();
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        this.addView(viewPager);
    }

    /* *
     * DATA----------------------------------------------------------------------------------------
     * */
    private List<View> initData(Calendar calendar) {
        List<View> views = new ArrayList<>();
        Calendar calendarCopy = Calendar.getInstance();
        calendarCopy.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 3, calendar.get(Calendar.DATE));
        for (int i = 0; i < 5; i++) {
            calendarCopy.add(Calendar.MONTH, 1);
            views.add(renderEachMonth(calendarCopy));
        }
        return views;
    }

    /* *
     * 일 - sun
     * 월 - mon
     * 화 - tue
     * 수 - wes
     * 목 - thu
     * 금 - fri
     * 토 - sat
     * */
    private String[] weekdays = new String[]{
            "",     // - error
            "일",    // - sun
            "월",    // - mon
            "화",    // - tue
            "수",    // - wes
            "목",    // - thu
            "금",    // - fri
            "토"     // - sat
    };
    private int lineColor = Color.parseColor("#949597");
    private int dayColor = Color.parseColor("#E6F0F6");
    private int textColor = Color.parseColor("#797A79");
    private int textColorWhite = Color.parseColor("#ffffff");
    private int transparentColor = Color.parseColor("#00000000");
    private int lineSize = AppUtils.dpToPx(0.66f);
    private int size30 = AppUtils.dpToPx(30f);
    private int size3 = AppUtils.dpToPx(3f);

    private View renderEachMonth(Calendar calendarInput) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarInput.get(Calendar.YEAR), calendarInput.get(Calendar.MONTH), calendarInput.get(Calendar.DATE));
        Log.e("RENDER", String.format("%s - %s", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        int realWidth = screenWidth - paddingLeft - paddingRight;
        int heightTitle = (int) (2.0 / 3 * realWidth / 7);
        int heightItem = (int) (4.0 / 3 * realWidth / 7);
        int sizeTextCalendar = ((realWidth - 8) / 7 - 2 * size3) / 2;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        //region --start---->
        View line1 = new View(context);
        line1.setLayoutParams(new LinearLayout.LayoutParams(realWidth, lineSize));
        line1.setBackgroundColor(lineColor);
        //endregion

        //region --start---->
        LinearLayout llTitleContainer = new LinearLayout(context);
        llTitleContainer.setLayoutParams(new LinearLayout.LayoutParams(realWidth, heightTitle));
        llTitleContainer.setOrientation(LinearLayout.HORIZONTAL);
        llTitleContainer.setBackgroundColor(dayColor);

        View line10 = new View(context);
        line10.setLayoutParams(new LinearLayout.LayoutParams(lineSize, LayoutParams.MATCH_PARENT));
        line10.setBackgroundColor(lineColor);

        llTitleContainer.addView(line10);

        for (int i = 1; i < 8; i++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
            textView.setGravity(Gravity.CENTER);
            textView.setText(weekdays[i]);
            textView.setTextColor(textColor);

            View line11 = new View(context);
            line11.setLayoutParams(new LinearLayout.LayoutParams(lineSize, LayoutParams.MATCH_PARENT));
            line11.setBackgroundColor(lineColor);

            llTitleContainer.addView(textView);
            llTitleContainer.addView(line11);
        }
        //endregion

        //region --start---->
        View line2 = new View(context);
        line2.setLayoutParams(new LinearLayout.LayoutParams(realWidth, lineSize));
        line2.setBackgroundColor(lineColor);
        //endregion

        linearLayout.addView(line1);
        linearLayout.addView(llTitleContainer);
        linearLayout.addView(line2);

        int maxWeekOfMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < maxWeekOfMonth; i++) {
            //region --start---->
            LinearLayout llItemContainer = new LinearLayout(context);
            llItemContainer.setLayoutParams(new LinearLayout.LayoutParams(realWidth, heightItem));
            llItemContainer.setOrientation(LinearLayout.HORIZONTAL);

            View line20 = new View(context);
            line20.setLayoutParams(new LinearLayout.LayoutParams(lineSize, LayoutParams.MATCH_PARENT));
            line20.setBackgroundColor(lineColor);

            llItemContainer.addView(line20);

            for (int j = 1; j < 8; j++) {
                FrameLayout frameLayout = new FrameLayout(context);
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));

                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                if ((i == 0 && j >= startDay) || (i != 0 && day > 1 && day <= maxDayOfMonth)) {
                    TextView textView = new TextView(context);
                    LinearLayout.LayoutParams textLP = new LinearLayout.LayoutParams(sizeTextCalendar, sizeTextCalendar);
                    textLP.setMargins(sizeTextCalendar + size3, size3, size3, size3);
                    textView.setLayoutParams(textLP);

                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(12);
                    textView.setText(String.valueOf(day));
                    //todo

                    if (day != calendarCurrentDay.get(Calendar.DATE) || month != calendarCurrentDay.get(Calendar.MONTH) || year != calendarCurrentDay.get(Calendar.YEAR)) {
                        textView.setTextColor(textColor);
                        textView.setBackgroundColor(transparentColor);
                    } else {
                        textView.setTextColor(textColorWhite);
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_calendar_cirle_black));
                    }

                    frameLayout.addView(textView);
                    calendar.add(Calendar.DATE, 1);
                }

                View line21 = new View(context);
                line21.setLayoutParams(new LinearLayout.LayoutParams(lineSize, LayoutParams.MATCH_PARENT));
                line21.setBackgroundColor(lineColor);

                llItemContainer.addView(frameLayout);
                llItemContainer.addView(line21);
            }
            //endregion

            //region --start---->
            View line3 = new View(context);
            line3.setLayoutParams(new LinearLayout.LayoutParams(realWidth, lineSize));
            line3.setBackgroundColor(lineColor);
            //endregion

            linearLayout.addView(llItemContainer);
            linearLayout.addView(line3);
        }
        return linearLayout;
    }

    /*
     * ADAPTER--------------------------------------------------------------------------------------
     * */
    private class KDBPagerAdapter extends PagerAdapter {
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
}
