package com.agico.smk.carinspectionapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.agico.smk.carinspectionapp.Adapters.RemarksAdapter;
import com.agico.smk.carinspectionapp.SOAP.API_Tasks.API_TASK;
import com.agico.smk.carinspectionapp.SOAP.Data.Intimation;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.LOADING_STATUS;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.STATUS;
import com.agico.smk.carinspectionapp.SOAP.Intimations;

public class RemarksActivity extends AppCompatActivity {
    final String TAG = RemarksActivity.class.getName();
    public AlertDialog remarkDialog;
    public RemarksAdapter remarksAdapter;
    public API_TASK.ViewRemarks viewRemarks = null;
    public API_TASK.AddRemarks addRemarks = null;
    Bundle extras;
    RecyclerView remarksRecycler;
    //    public AlertDialog.Builder remarkDialogBuilder = null;
    private String inspection_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);
        extras = getIntent().getExtras();
        if (extras != null) {
            int index = extras.getInt("index");
            STATUS status = (STATUS) extras.getSerializable("status");
            Intimation intimation = Intimations.getInstance().getIntimationAt(index, status);
            inspection_id = intimation.INSPECTION_ID;

            AppCompatTextView id = findViewById(R.id.v_id);
            AppCompatTextView name = findViewById(R.id.v_name);
            AppCompatTextView contact = findViewById(R.id.v_contact);
            AppCompatTextView address = findViewById(R.id.v_address);

            id.setText(intimation.INSPECTION_ID);
            name.setText(intimation.INSURED);
            contact.setText(intimation.INSURED_CONTACT);
            address.setText(intimation.INSURED_ADDRESS);
            if (viewRemarks == null) {
                viewRemarks = new API_TASK.ViewRemarks(this, inspection_id);
                viewRemarks.execute();
            }
        } else {
            Log.e(TAG, " onCreate: NO EXTRAS PASSED!");
        }

        remarksRecycler = findViewById(R.id.remarks_recycler);
        remarksAdapter = new RemarksAdapter();
        remarksRecycler.setLayoutManager(new LinearLayoutManager(this));
        remarksRecycler.setAdapter(remarksAdapter);
    }

    @SuppressLint("InflateParams")
    public void onAddRemarks(View view) {
        if (addRemarks == null) {


            View bodyView = LayoutInflater.from(RemarksActivity.this).inflate(R.layout.dialog_new_remarks, null);
            AlertDialog.Builder remarkDialogBuilder = new AlertDialog.Builder(RemarksActivity.this);
            remarkDialogBuilder.setTitle("Add New Remarks");
            remarkDialogBuilder.setView(bodyView);
            final TextInputEditText input = bodyView.findViewById(R.id.et_remarks);
            remarkDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String remarks = input.getText().toString().trim();
                    if (!remarks.isEmpty()) {
                        if (addRemarks == null) {
                            addRemarks = new API_TASK.AddRemarks(inspection_id, remarks, RemarksActivity.this);
                            addRemarks.execute();
                        }

                    } else
                        new android.app.AlertDialog.Builder(RemarksActivity.this).setTitle("Error").setMessage("Remarks cannot be Empty!").setPositiveButton("OK", null).create().show();

                }
            });
            remarkDialog = remarkDialogBuilder.create();
            remarkDialog.show();
        }
    }

    public void refreshRemarks(View view) {
        if (viewRemarks == null) {
            remarksAdapter.updateLoadingStatus(LOADING_STATUS.LOADING);
            viewRemarks = new API_TASK.ViewRemarks(this, inspection_id);
            viewRemarks.execute();
        }

    }

    @Override
    public void onBackPressed() {
        if (addRemarks == null) {
            super.onBackPressed();
        }
    }
}
