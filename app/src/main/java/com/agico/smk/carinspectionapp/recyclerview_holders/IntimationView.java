package com.agico.smk.carinspectionapp.recyclerview_holders;

import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agico.smk.carinspectionapp.R;
import com.agico.smk.carinspectionapp.soap.data.Intimation;

/*
 * Created by AGICO on 1/30/2018.
 */

public class IntimationView extends RecyclerView.ViewHolder {


    public View edit, remarks, photos, submit;
    private View rootView, intimationView;
    private AppCompatTextView v_id, v_name, v_contact, v_address;

    public IntimationView(View itemView) {
        super(itemView);
        rootView = itemView;
        intimationView = itemView.findViewById(R.id.intimation_view);
        v_id = itemView.findViewById(R.id.v_id);
        v_name = itemView.findViewById(R.id.v_name);
        v_contact = itemView.findViewById(R.id.v_contact);
        v_address = itemView.findViewById(R.id.v_address);

        edit = itemView.findViewById(R.id.edit);
        remarks = itemView.findViewById(R.id.remarks);
        photos = itemView.findViewById(R.id.photos);
        submit = itemView.findViewById(R.id.submit);
    }

    @SuppressWarnings("unused")
    public View getRootView() {
        return rootView;
    }

    public void setData(Intimation intimation) {
        v_id.setText(String.valueOf(intimation.INSPECTION_ID));
        v_name.setText(intimation.INSURED);
        v_contact.setText(intimation.INSURED_CONTACT);
        v_address.setText(intimation.INSURED_ADDRESS);
    }

    public void setColor(@ColorRes int resId) {
        intimationView.setBackgroundResource(resId);
    }

}
