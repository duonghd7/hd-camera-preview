package com.hdd.camera_preview;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.SurfaceHolder;

/**
 * Create on 2019-06-10
 *
 * @author duonghd
 */

public abstract class CameraImpl {
    protected SurfaceHolder surfaceHolder;

    public CameraImpl(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    protected abstract void init();

    protected abstract void takePicture(TakenPictureCallback callback);

    protected abstract void resetCamera();

    protected Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }

    /*
     * Taken picture callback
     * */
    public interface TakenPictureCallback {
        void onTaken(Bitmap bitmap);

        void onError(String message);
    }
}
