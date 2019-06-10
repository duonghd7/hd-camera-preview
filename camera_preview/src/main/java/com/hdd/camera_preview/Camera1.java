package com.hdd.camera_preview;

import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Create on 2019-06-10
 *
 * @author duonghd
 */

public class Camera1 extends CameraImpl {
    private Camera camera;

    public Camera1(SurfaceHolder surfaceHolder) {
        super(surfaceHolder);
    }

    @Override
    public void init() {
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e("test", "surfaceCreated-0");
                if (camera == null) camera = getCameraInstance();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.setDisplayOrientation(90);
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPreviewSize(1280, 960);
                    parameters.setPictureSize(1280, 960);

                    camera.startPreview();
                } catch (Exception e) {
                    Log.e("Ex", "surfaceCreated-0: " + e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e("test", "surfaceChanged-2 -> w:" + width + " - h: " + height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e("test", "surfaceDestroyed-3");
                stopPreviewAndFreeCamera();
            }
        });
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.e("Ex", "getCameraInstance-0: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    private void stopPreviewAndFreeCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    @Override
    public void takePicture(final TakenPictureCallback callback) {
        try {
            if (camera != null) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inMutable = true;
                        callback.onTaken(rotate(BitmapFactory.decodeByteArray(data, 0, data.length, options), 90));
                    }
                });
            }
        } catch (Exception e) {
            if (callback != null) callback.onError("" + e.getMessage());
        }
    }

    @Override
    public void resetCamera() {
        camera.startPreview();
    }
}
