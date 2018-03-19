package com.agico.smk.carinspectionapp.recyclerview_holders;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agico.smk.carinspectionapp.R;
import com.agico.smk.carinspectionapp.soap.enums.LOADING_STATUS;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/28/2018.
 */

public class PlaceHolderView extends RecyclerView.ViewHolder {
    private LinearLayoutCompat loading;
    private AppCompatTextView error, empty;

    public PlaceHolderView(View itemView) {
        super(itemView);
        loading = itemView.findViewById(R.id.loading);
        error = itemView.findViewById(R.id.error);
        empty = itemView.findViewById(R.id.empty);
    }

    /**
     * Changes View of Placeholder to Error,loading or Empty upon given state;
     *
     * @param status true if state is 'error' and false if state is 'loading'
     */
    public void setLoadingStatus(LOADING_STATUS status) {
        error.setVisibility((status == LOADING_STATUS.LOADED_ERROR) ? View.VISIBLE : View.GONE);
        loading.setVisibility((status == LOADING_STATUS.LOADING) ? View.VISIBLE : View.GONE);
        empty.setVisibility((status == LOADING_STATUS.LOADED_EMPTY) ? View.VISIBLE : View.GONE);
    }


}
