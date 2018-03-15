package com.agico.smk.carinspectionapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import com.agico.smk.carinspectionapp.SOAP.API_Tasks.API_TASK;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.STATUS;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;

public class CameraActivity extends AppCompatActivity {

    public API_TASK.UploadImage uploadImageTask;
    Bundle extras;
    int index;
    STATUS status;
    String inspection_id;
    AppCompatImageView iv;
    LinearLayout ll_uploading;
    private CameraView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        extras = getIntent().getExtras();
        if (extras != null) {
            inspection_id = extras.getString("inspectionId");
        }
        iv = findViewById(R.id.imageView);
        ll_uploading = findViewById(R.id.uploading);
        camera = findViewById(R.id.camera);
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                // Create a bitmap or a file...
                // CameraUtils will read EXIF orientation for you, in a worker thread.
                String base64 = Base64.encodeToString(picture, Base64.NO_WRAP);
                uploadImageTask = new API_TASK.UploadImage(inspection_id, base64, CameraActivity.this);
                uploadImageTask.execute();
                CameraUtils.decodeBitmap(picture, new CameraUtils.BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        ll_uploading.setVisibility(View.VISIBLE);
                        camera.setVisibility(View.INVISIBLE);
                        iv.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

    public void capture(View view) {
        if (uploadImageTask == null) {
            camera.capturePicture();
        }

    }
}
