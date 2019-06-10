package com.duonghd.test.kdb.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Create on 2019-06-03
 *
 * @author duonghd
 */
public class AppUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static DisplayMetrics dm;

    public static void init(Context context) {
        AppUtils.context = context;
    }

    public static int dpToPx(float dp) {
        if (context == null) throw new RuntimeException("Context is null, init(Context)");
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static int getWidth() {
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        return dm.widthPixels;
    }

    public static int getHeight() {
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        return dm.heightPixels;
    }

    public static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }
}
