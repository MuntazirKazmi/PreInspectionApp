package com.agico.smk.carinspectionapp.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agico.smk.carinspectionapp.ImageActivity;
import com.agico.smk.carinspectionapp.PhotosActivity;
import com.agico.smk.carinspectionapp.R;
import com.agico.smk.carinspectionapp.recyclerview_holders.PhotoView;
import com.agico.smk.carinspectionapp.recyclerview_holders.PlaceHolderView;
import com.agico.smk.carinspectionapp.soap.enums.LOADING_STATUS;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 3/13/2018.
 */

public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<String> image_urls;
    private LOADING_STATUS status;
    private WeakReference<PhotosActivity> reference;

    public PhotosAdapter(PhotosActivity photosActivity) {
        reference = new WeakReference<>(photosActivity);
        status = LOADING_STATUS.LOADING;
        image_urls = new ArrayList<>();
        image_urls.add("https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg");
        image_urls.add("https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg");
        image_urls.add("https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg");
    }

    @Override
    public int getItemViewType(int position) {
        return status.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType != LOADING_STATUS.LOADED_OK.ordinal()) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_placeholder, parent, false);
            return new PlaceHolderView(inflate);
        }
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_photo, parent, false);
        return new PhotoView(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (status == LOADING_STATUS.LOADED_OK) {
            Glide.with(reference.get())
                    .load(Uri.parse(image_urls.get(holder.getAdapterPosition())))
                    .apply(new RequestOptions().placeholder(R.color.mdtp_light_gray).error(R.drawable.ic_error))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            ((PhotoView) holder).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    notifyItemChanged(holder.getAdapterPosition());
                                }
                            });
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ((PhotoView) holder).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reference.get().startActivity(new Intent(reference.get(), ImageActivity.class).putExtra("url", image_urls.get(holder.getAdapterPosition())));
                                }
                            });
                            return false;
                        }
                    })
                    .into(((PhotoView) holder).imageView);
        } else {
            ((PlaceHolderView) holder).setLoadingStatus(status);
        }
    }

    @Override
    public int getItemCount() {
        if (status == LOADING_STATUS.LOADED_OK) {
            return image_urls.size();
        } else
            return 1;
    }

    public void updateLoadingStatus(LOADING_STATUS status) {
        this.status = status;
        notifyDataSetChanged();
    }
}
