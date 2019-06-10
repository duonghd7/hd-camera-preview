package com.hdd.camera_preview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Create on 2019-05-25
 *
 * @author duonghd
 */

public class CameraPreview extends SurfaceView {
    private CameraImpl cameraImpl;

    /* *
     * ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
     * ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
     * */

    public void takePicture(final CameraImpl.TakenPictureCallback callback) {
        if (cameraImpl != null) cameraImpl.takePicture(callback);
    }

    /* *
     * ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
     * ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
     * */

    public CameraPreview(Context context) {
        super(context);
        initView(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        cameraImpl = new Camera1(getHolder());
        cameraImpl.init();
    }

    public void resetCamera() {
        if (cameraImpl != null) cameraImpl.resetCamera();
    }
}