package com.agico.smk.carinspectionapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jsibbold.zoomage.ZoomageView;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String url = getIntent().getStringExtra("url");
        ZoomageView imageView = findViewById(R.id.zoomage);
        Glide.with(this).load(Uri.parse(url)).apply(new RequestOptions().error(R.drawable.ic_error).placeholder(R.color.mdtp_light_gray).fitCenter())
                .into(imageView);
    }
}
