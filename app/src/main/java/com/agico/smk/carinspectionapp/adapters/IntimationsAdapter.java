package com.agico.smk.carinspectionapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.agico.smk.carinspectionapp.IntimationListActivity;
import com.agico.smk.carinspectionapp.PhotosActivity;
import com.agico.smk.carinspectionapp.R;
import com.agico.smk.carinspectionapp.RemarksActivity;
import com.agico.smk.carinspectionapp.ScrollingActivity;
import com.agico.smk.carinspectionapp.recyclerview_holders.IntimationView;
import com.agico.smk.carinspectionapp.soap.Intimations;
import com.agico.smk.carinspectionapp.soap.api_tasks.API_TASK;
import com.agico.smk.carinspectionapp.soap.enums.STATUS;

import java.lang.ref.WeakReference;

/*
 * Created by AGICO on 2/1/2018.
 */

/**
 * <h1>IntimationsAdapter</h1>
 * An Adapter for RecyclerView which shows Intimations depending on {@link STATUS}
 * provided to adapter.
 *
 * @author Syed Muntazir Mehdi
 */
public class IntimationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final WeakReference<IntimationListActivity> activityWeakReference;
    private final int VIEW_NORMAL = 1;
    //    private final
    private STATUS status;

    /**
     * Default Constructor which sets default {@link STATUS} to {@link STATUS#Under_Process}
     */
    public IntimationsAdapter(IntimationListActivity intimationListActivity) {
        status = STATUS.Under_Process;
        activityWeakReference = new WeakReference<>(intimationListActivity);
    }

    /**
     * Sets {@link STATUS} of {@link IntimationsAdapter} so that it can update the RecyclerView
     * to show Intimations of provided {@link STATUS}
     *
     * @param status Status of intimations to be shown in RecyclerView
     */
    public void setStatus(STATUS status) {
        if (this.status != status) {
            this.status = status;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_EMPTY = 0;
        if (this.getIntimationsListSize() == 0) return VIEW_EMPTY;
        else
            return VIEW_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_NORMAL) {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_intimation, parent, false);
            return new IntimationView(inflatedView);
        } else {
            AppCompatTextView emptymessage = new AppCompatTextView(parent.getContext());
            emptymessage.setText(R.string.no_intimations);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            emptymessage.setLayoutParams(params);
            emptymessage.setGravity(Gravity.CENTER);
            emptymessage.setTextAppearance(parent.getContext(), R.style.TextAppearance_AppCompat_Headline);
            return new EmptyView(emptymessage);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (getIntimationsListSize() != 0) {
            final IntimationView holder = (IntimationView) viewHolder;
            holder.setData(Intimations.getInstance().getIntimationAt(position, status));
            if (status == STATUS.Under_Process) {
                holder.setColor(R.color.under_process);

            } else if (status == STATUS.Processed) {
                holder.setColor(R.color.processed);

            } else if (status == STATUS.Pending) {
                holder.setColor(R.color.pending);

            }
            View.OnClickListener editIntimation = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = ((ViewGroup) view.getParent()).getContext();
                    Intent intent = new Intent(context, ScrollingActivity.class);
                    intent.putExtra("index", holder.getAdapterPosition())
                            .putExtra("status", status);
                    context.startActivity(intent);
                }
            };
            View.OnClickListener remarksIntimation = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = ((ViewGroup) v.getParent()).getContext();
                    Intent intent = new Intent(context, RemarksActivity.class);
                    intent.putExtra("index", holder.getAdapterPosition())
                            .putExtra("status", status);
                    context.startActivity(intent);
                }
            };
            View.OnClickListener photosIntimation = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = ((ViewGroup) view.getParent()).getContext();
                    Intent intent = new Intent(context, PhotosActivity.class);
                    intent.putExtra("index", holder.getAdapterPosition())
                            .putExtra("status", status);
                    context.startActivity(intent);
                }
            };

            holder.edit.setOnClickListener(editIntimation);
            holder.remarks.setOnClickListener(remarksIntimation);
            holder.photos.setOnClickListener(photosIntimation);
            if (status == STATUS.Processed) {
                holder.submit.setVisibility(View.GONE);
            } else {
                View.OnClickListener submitIntimation = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntimationListActivity intimationListActivity = activityWeakReference.get();
                        if (intimationListActivity.finalSubmit == null) {
                            String inspection_id = Intimations.getInstance().getIntimationAt(holder.getAdapterPosition(), status).INSPECTION_ID;
                            intimationListActivity.finalSubmit = new API_TASK.FinalSubmit(intimationListActivity, inspection_id);
                            intimationListActivity.finalSubmit.execute();
                        } else {
                            Snackbar.make(view, "Please wait for previous task to end.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                };
                holder.submit.setVisibility(View.VISIBLE);
                holder.submit.setOnClickListener(submitIntimation);
            }

        }
    }

    @Override
    public int getItemCount() {
        return Math.max(getIntimationsListSize(), 1);
    }

    /**
     * Returns number of Intimations having {@link STATUS} described in {@link IntimationsAdapter}.
     */
    private int getIntimationsListSize() {
        switch (status) {
            case Under_Process:
                return Intimations.getInstance().getUnderProcess().size();
            case Processed:
                return Intimations.getInstance().getProcessed().size();
            case Pending:
                return Intimations.getInstance().getPending().size();
            default:
                return 0;
        }
    }

    private class EmptyView extends RecyclerView.ViewHolder {
        EmptyView(View itemView) {
            super(itemView);
        }
    }
}
