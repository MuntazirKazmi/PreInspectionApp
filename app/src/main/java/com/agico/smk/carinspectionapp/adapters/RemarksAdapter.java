package com.agico.smk.carinspectionapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agico.smk.carinspectionapp.R;
import com.agico.smk.carinspectionapp.recyclerview_holders.PlaceHolderView;
import com.agico.smk.carinspectionapp.recyclerview_holders.RemarkView;
import com.agico.smk.carinspectionapp.soap.data.Remark;
import com.agico.smk.carinspectionapp.soap.enums.LOADING_STATUS;

import java.util.ArrayList;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/23/2018.
 */

public class RemarksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Remark> remarks;
    private LOADING_STATUS status;

    public RemarksAdapter() {
        status = LOADING_STATUS.LOADING;
        remarks = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        return status.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if (viewType != LOADING_STATUS.LOADED_OK.ordinal()) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_placeholder, parent, false);
            return new PlaceHolderView(inflate);
        }
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_remark, parent, false);
        return new RemarkView(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (status == LOADING_STATUS.LOADED_OK) {
            ((RemarkView) holder).setRemark(remarks.get(position).REMARKS_ID, remarks.get(position).REMARKS, remarks.get(position).INSPECTION_DATE);
        } else {
            ((PlaceHolderView) holder).setLoadingStatus(status);
        }
    }

    @Override
    public int getItemCount() {
        if (status == LOADING_STATUS.LOADED_OK) {
            return remarks.size();
        } else
            return 1;
    }

    public void updateLoadingStatus(LOADING_STATUS status) {
        this.status = status;
        notifyDataSetChanged();
    }
}
