package com.duonghd.test.kdb.camera;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.duonghd.test.kdb.R;
import com.hdd.camera_preview.CameraImpl;
import com.hdd.camera_preview.CameraPreview;

public class CameraActivity extends AppCompatActivity {
    private final String TAG = CameraActivity.class.getSimpleName();

    private ImageView ivResult;
    private CameraPreview cameraPreview;
    private boolean canTakePhoto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivResult = findViewById(R.id.activity_camera_iv_result);
        cameraPreview = findViewById(R.id.activity_camera_cp_campreview);

        findViewById(R.id.activity_camera_v_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureClick();
            }
        });

        findViewById(R.id.activity_camera_tv_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryClick();
            }
        });
    }

    private void captureClick() {
        if (canTakePhoto) {
            canTakePhoto = false;
            cameraPreview.takePicture(new CameraImpl.TakenPictureCallback() {
                @Override
                public void onTaken(Bitmap bitmap) {
                    Log.e("takePicture.onTaken", "ok");

                    ivResult.setImageBitmap(bitmap);
                    ivResult.setVisibility(View.VISIBLE);

                    canTakePhoto = true;
                }

                @Override
                public void onError(String message) {
                    Log.e("takePicture.onError", message);

                    canTakePhoto = true;
                }
            });
        }
    }

    private void retryClick() {
        ivResult.setImageBitmap(null);
        ivResult.setVisibility(View.GONE);

        cameraPreview.resetCamera();
    }

    @Override
    protected void onResume() {
       // if (cameraPreview != null) cameraPreview.invalidate();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
