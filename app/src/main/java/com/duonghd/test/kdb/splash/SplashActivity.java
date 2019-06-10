package com.duonghd.test.kdb.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.duonghd.test.kdb.R;
import com.duonghd.test.kdb.calendar.CalendarActivity;
import com.duonghd.test.kdb.camera.CameraActivity;
import com.duonghd.test.kdb.glide.GlideActivity;
import com.duonghd.test.kdb.pager.PagerActivity;
import com.duonghd.test.kdb.progress.ProgressActivity;
import com.gun0912.tedpermission.TedPermissionResult;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import io.reactivex.functions.Consumer;

/**
 * Create on 2019-05-28
 *
 * @author duonghd
 */

@SuppressLint("CheckResult")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.activity_splash_tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraHardware()) {
                    TedRx2Permission.with(SplashActivity.this)
                            .setRationaleTitle("Request camera permission title")
                            .setRationaleMessage("Request camera permission CONTENT")
                            .setPermissions(Manifest.permission.CAMERA)
                            .request()
                            .subscribe(new Consumer<TedPermissionResult>() {
                                @Override
                                public void accept(TedPermissionResult tedPermissionResult) throws Exception {
                                    if (tedPermissionResult.isGranted()) {
                                        startActivity(new Intent(SplashActivity.this, CameraActivity.class));
                                    } else {
                                        Toast.makeText(SplashActivity.this,
                                                "PERMISSION CANCEL\n" + tedPermissionResult.getDeniedPermissions().toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SplashActivity.this, "Device don't have camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.activity_splash_tv_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, ProgressActivity.class));
            }
        });

        findViewById(R.id.activity_splash_tv_glide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, GlideActivity.class));
            }
        });

        findViewById(R.id.activity_splash_tv_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, CalendarActivity.class));
            }
        });

        findViewById(R.id.activity_splash_tv_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, PagerActivity.class));
            }
        });
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
