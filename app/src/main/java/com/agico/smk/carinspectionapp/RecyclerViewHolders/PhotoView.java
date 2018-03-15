package com.agico.smk.carinspectionapp.RecyclerViewHolders;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agico.smk.carinspectionapp.R;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 3/13/2018.
 */

public class PhotoView extends RecyclerView.ViewHolder {

    public AppCompatImageView imageView;
    private View root;

    public PhotoView(View itemView) {
        super(itemView);
        root = itemView;
        imageView = itemView.findViewById(R.id.image_view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        root.setOnClickListener(listener);
    }
}
