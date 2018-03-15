package com.agico.smk.carinspectionapp.RecyclerViewHolders;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;

import com.agico.smk.carinspectionapp.R;
import com.agico.smk.carinspectionapp.SOAP.Utils.SOAPUtils;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/23/2018.
 */

public class RemarkView extends RecyclerView.ViewHolder {
    private AppCompatTextView tv_remarks;

    public RemarkView(View itemView) {
        super(itemView);
        tv_remarks = itemView.findViewById(R.id.tv_remarks);
    }

    public void setRemark(String id, String remark, String date) {
        TextAppearanceSpan subhead = new TextAppearanceSpan(tv_remarks.getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Headline);
        TextAppearanceSpan medium = new TextAppearanceSpan(tv_remarks.getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Menu);
        TextAppearanceSpan small = new TextAppearanceSpan(tv_remarks.getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Small);
        if (date == null) date = "NIL";
        else date = SOAPUtils.getDateFromSoap(date);
        SpannableString spannableString = new SpannableString(
                "Remark ID:" + id + "\n" +
                        remark + "\n\n" +
                        "- " + date
        );

        int start = 0, end = id.length() + 11;
        spannableString.setSpan(subhead, start, end, 0);
        start = end;
        end += remark.length() + 2;
        spannableString.setSpan(medium, start, end, 0);
        start = end;
        end += date.length() + 2;
        spannableString.setSpan(small, start, end, 0);

        tv_remarks.setText(spannableString);
    }
}
