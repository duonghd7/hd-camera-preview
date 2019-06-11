package com.hdd.camera_preview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.AudioManager;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Create on 2019-06-10
 *
 * @author duonghd
 */

public class Camera1 extends CameraImpl {
    private static final String TAG = Camera1.class.getSimpleName();
    private Camera camera;

    public Camera1(SurfaceHolder surfaceHolder, Context context) {
        super(surfaceHolder, context);
    }

    @Override
    public void init() {
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (camera == null) camera = getCameraInstance();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.setDisplayOrientation(90);
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPreviewSize(1280, 960);
                    parameters.setPictureSize(1280, 960);

                    camera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
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
            Log.e(TAG, "" + e.getMessage());
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
                camera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        if (mgr != null) {
                            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
                        }
                    }
                }, null, new Camera.PictureCallback() {
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
