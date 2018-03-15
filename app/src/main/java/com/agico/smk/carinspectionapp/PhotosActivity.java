package com.agico.smk.carinspectionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;

import com.agico.smk.carinspectionapp.Adapters.PhotosAdapter;
import com.agico.smk.carinspectionapp.SOAP.API_Tasks.API_TASK;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.LOADING_STATUS;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.STATUS;
import com.agico.smk.carinspectionapp.SOAP.Intimations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;

public class PhotosActivity extends AppCompatActivity {
    public PhotosAdapter photosAdapter;
    public API_TASK.GetImages getImages = null;
    public API_TASK.ImageUpload uploadImageTask;
    RecyclerView photosRecycler;
    FloatingActionButton add_image;
    private String inspection_id;
    private AlertDialog uploadActionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        add_image = findViewById(R.id.add_image);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int index = extras.getInt("index");
            STATUS status = (STATUS) extras.getSerializable("status");
            add_image.setVisibility((status == STATUS.Processed) ? View.GONE : View.VISIBLE);
            if (status != STATUS.Processed) {
                uploadActionDialog = new AlertDialog.Builder(this)
                        .setTitle("Pick Your Action")
                        .setItems(R.array.photo_upload_options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EZPhotoPickConfig config = new EZPhotoPickConfig();
                                switch (which) {
                                    case 0://Take picture
                                        config.photoSource = PhotoSource.CAMERA; // or PhotoSource.CAMERA
                                        config.isAllowMultipleSelect = true;// only for GALLERY pick and API >18
                                        config.exportingSize = 1024;
                                        EZPhotoPick.startPhotoPickActivity(PhotosActivity.this, config);
                                        break;
                                    case 1:
                                        config.photoSource = PhotoSource.GALLERY; // or PhotoSource.CAMERA
                                        config.exportingSize = 1024;
                                        EZPhotoPick.startPhotoPickActivity(PhotosActivity.this, config);
                                        break;
                                }
                            }
                        }).create();
            }
            inspection_id = Intimations.getInstance().getIntimationAt(index, status).INSPECTION_ID;
            getImages = new API_TASK.GetImages(this, inspection_id);
            getImages.execute();
        }

        photosRecycler = findViewById(R.id.photos_recycler);
        photosRecycler.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        photosAdapter = new PhotosAdapter(this);
        photosRecycler.setAdapter(photosAdapter);

    }

    public void onAddImage(final View view) {
        if (uploadActionDialog != null && !uploadActionDialog.isShowing()
                && uploadImageTask == null) {
            uploadActionDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void refreshImages() {
        if (getImages == null) {
            photosAdapter.updateLoadingStatus(LOADING_STATUS.LOADING);
            getImages = new API_TASK.GetImages(this, inspection_id);
            getImages.execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == EZPhotoPick.PHOTO_PICK_GALLERY_REQUEST_CODE || requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE) {
            try {
                String path = new EZPhotoPickStorage(this).getLatestStoredPhotoBitmapAbsolutePath();

                File file = new File(path);
                byte[] bytes = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                //noinspection ResultOfMethodCallIgnored
                fis.read(bytes);
                fis.close();
                String s = Base64.encodeToString(bytes, Base64.NO_WRAP);
//                Bitmap pickedPhoto = new EZPhotoPickStorage(this).loadLatestStoredPhotoBitmap();
                uploadImageTask = new API_TASK.ImageUpload(inspection_id, s, PhotosActivity.this);
                uploadImageTask.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //do something with the bitmap
        }
    }

    @Override
    public void onBackPressed() {
        if (getImages == null && uploadImageTask == null) {
            super.onBackPressed();
        } else {
            Snackbar.make(photosRecycler, "Please wait for task to finish.", Snackbar.LENGTH_SHORT).show();
        }
    }
}
