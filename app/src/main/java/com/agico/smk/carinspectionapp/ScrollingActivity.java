package com.agico.smk.carinspectionapp;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.agico.smk.carinspectionapp.SMKComponents.MaterialSpinner;
import com.agico.smk.carinspectionapp.SOAP.API_Tasks.API_TASK;
import com.agico.smk.carinspectionapp.SOAP.Colors;
import com.agico.smk.carinspectionapp.SOAP.Data.Intimation;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.STATUS;
import com.agico.smk.carinspectionapp.SOAP.Intimations;
import com.agico.smk.carinspectionapp.SOAP.Utils.SOAPUtils;
import com.agico.smk.carinspectionapp.SOAP.Vehicles;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DateRangeLimiter;

import java.util.Calendar;
import java.util.Locale;


public class ScrollingActivity extends AppCompatActivity {

    final String TAG = ScrollingActivity.class.getName();
    public API_TASK.UpdateIntimation updateIntimation = null;
    //Intent Extras
    Bundle extras;
    Intimation intimation;
    FloatingActionButton fab_save;
    //    Intimation Details Head
    AppCompatTextView id, name, contact, address;

    //SUMMARY
    private TextInputEditText cnic, mkt_agent, model, reg_no, reg_date, chassis_no, engine_no;
    private RadioGroup RG_coverage_type, RG_permit_type, RG_color_condition;
    private MaterialSpinner vehicle_make, color;
    private String coverage_type = "", permit_type = "", color_condition = "";


    //ACCESSORIES
    private TextInputEditText other_acc;
    private AppCompatCheckBox odometer, jackrod, cassete_radio, cd_player, spare_wheel, air_conditioner;

    //MARKET VALUE
    private TextInputEditText v_vcl_mkt_val, v_acc_mkt_val;

    //GENERAL POINTS
    private AppCompatCheckBox headlights, rearLights, parkingLights, indicatorLights, bumper;
    private TextInputEditText dents, gen_cond;

    //MISCELLANEOUS
    private SwitchCompat isInsuredOwner, isUnderPurchase, isReconditioned;
    private TextInputEditText owner_name, owner_address, conditioned_detail, last_insured, inspection_loc;
    private RadioGroup RG_parking_condition;
    private String parking_condition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_intimation_details);

//        Get Intent Values
        fab_save = findViewById(R.id.save);
        extras = getIntent().getExtras();
        if (extras != null) {
            int index = extras.getInt("index");
            STATUS status = (STATUS) extras.getSerializable("status");
            fab_save.setVisibility((status == STATUS.Processed) ? View.GONE : View.VISIBLE);
            intimation = Intimations.getInstance().getIntimationAt(index, status);
        } else {
            Log.e(TAG, "46 onCreate: NO EXTRAS PASSED!");
        }
        initViews();
        initData();


    }

    void initViews() {

        //INTIMATION DETAILS
        id = findViewById(R.id.v_id);
        name = findViewById(R.id.v_name);
        contact = findViewById(R.id.v_contact);
        address = findViewById(R.id.v_address);

        //SUMMARY
        cnic = findViewById(R.id.v_cnic);
        mkt_agent = findViewById(R.id.v_mkt_agent);
        model = findViewById(R.id.v_model);
        reg_no = findViewById(R.id.v_reg_no);
        reg_date = findViewById(R.id.v_reg_date);
        chassis_no = findViewById(R.id.v_chassis_no);
        engine_no = findViewById(R.id.v_engine_no);
        vehicle_make = findViewById(R.id.v_vehicle_make);
        color = findViewById(R.id.v_color);
        RG_coverage_type = findViewById(R.id.RG_coverage_type);
        RG_permit_type = findViewById(R.id.RG_coverage_comm_type);
        RG_color_condition = findViewById(R.id.RG_color_condition);


        //Accessories
        odometer = findViewById(R.id.cb_odometer);
        jackrod = findViewById(R.id.cb_jackRod);
        cassete_radio = findViewById(R.id.cb_cassette_radio);
        cd_player = findViewById(R.id.cb_cd_player);
        spare_wheel = findViewById(R.id.cb_spare_wheel);
        air_conditioner = findViewById(R.id.cb_air_conditioner);
        other_acc = findViewById(R.id.v_other_acc);

        //MARKET VALUE
        v_vcl_mkt_val = findViewById(R.id.v_vcl_mkt_val);
        v_acc_mkt_val = findViewById(R.id.v_acc_mkt_val);

        //GENERAL POINTS
        headlights = findViewById(R.id.cb_headLights);
        rearLights = findViewById(R.id.cb_rearLights);
        parkingLights = findViewById(R.id.cb_parkingLights);
        indicatorLights = findViewById(R.id.cb_indicatorLights);
        bumper = findViewById(R.id.cb_bumper);
        dents = findViewById(R.id.v_dents);
        gen_cond = findViewById(R.id.v_gen_cond);

        //MISCELLANEOUS
        owner_name = findViewById(R.id.v_owner_name);
        owner_address = findViewById(R.id.v_owner_address);
        isInsuredOwner = findViewById(R.id.sw_isInsuredOwner);
        isUnderPurchase = findViewById(R.id.sw_isUnderPurchase);
        isReconditioned = findViewById(R.id.sw_isReconditioned);
        conditioned_detail = findViewById(R.id.v_conditioned_detail);
        last_insured = findViewById(R.id.v_last_insured);
        inspection_loc = findViewById(R.id.v_inspectionLoc);
        RG_parking_condition = findViewById(R.id.RG_parking_condition);
    }

    void initData() {
        //INSPECTION DETAILS
        id.setText(intimation.INSPECTION_ID);
        name.setText(intimation.INSURED);
        contact.setText(intimation.INSURED_CONTACT);
        address.setText(intimation.INSURED_ADDRESS);

        //SUMMARY
        cnic.setText((intimation.INSURED_CNIC != null) ? intimation.INSURED_CNIC : "");
        mkt_agent.setText((intimation.MARKETING_AGENT != null) ? intimation.MARKETING_AGENT : "");
        vehicle_make.setText((intimation.VEHICLE_MAKE_CODE != null) ? Vehicles.getInstance().getNameFor(intimation.VEHICLE_MAKE_CODE) : "");
        vehicle_make.setAdapterWithStringArrayList(Vehicles.getInstance().getNames());
        model.setText((intimation.VEHICLE_MODEL != null) ? intimation.VEHICLE_MODEL : "");
        if (intimation.COVERAG_TYPE != null) {
            coverage_type = intimation.COVERAG_TYPE;
            switch (intimation.COVERAG_TYPE) {
                case Intimation.COVERAGE_PRIVATE:
                    RG_coverage_type.check(R.id.RB_coverage_private);
                    break;
                case Intimation.COVERAGE_COMMERCIAL:
                    RG_coverage_type.check(R.id.RB_coverage_commercial);
                    break;
            }
        }
        if (intimation.PERMIT_TYPE != null) {
            permit_type = intimation.PERMIT_TYPE;
            switch (intimation.PERMIT_TYPE) {
                case Intimation.PERMIT_PRIVATE:
                    RG_permit_type.check(R.id.RB_coverage_comm_private);
                    break;
                case Intimation.PERMIT_PUBLIC:
                    RG_permit_type.check(R.id.RB_coverage_comm_public);
                    break;
            }
        }
        reg_no.setText((intimation.REGISTRATION_NO != null) ? intimation.REGISTRATION_NO : "");
        reg_date.setText((intimation.REGISTRATION_DATE != null) ? SOAPUtils.getDateFromSoap(intimation.REGISTRATION_DATE) : "");
        chassis_no.setText((intimation.CHASSIS != null) ? intimation.CHASSIS : "");
        engine_no.setText((intimation.ENGINE != null) ? intimation.ENGINE : "");
        if (intimation.COLOR_CONDITION != null) {
            color_condition = intimation.COLOR_CONDITION;
            switch (intimation.COLOR_CONDITION) {
                case Intimation.COLOR_FADED:
                    RG_color_condition.check(R.id.RB_color_faded);
                    break;
                case Intimation.COLOR_FAIR:
                    RG_color_condition.check(R.id.RB_color_fair);
                    break;
                case Intimation.COLOR_GOOD:
                    RG_color_condition.check(R.id.RB_color_good);
                    break;
            }
        }
        color.setText((intimation.COLOR != null) ? Colors.getInstance().getNameFor(intimation.COLOR) : "");
        color.setAdapterWithStringArrayList(Colors.getInstance().getNames());

        //ACCESSORIES
        if (intimation.ODOMETER.equals("Y")) odometer.setChecked(true);
        if (intimation.JACK.equals("Y")) jackrod.setChecked(true);
        if (intimation.CASSATE.equals("Y")) cassete_radio.setChecked(true);
        if (intimation.CD.equals("Y")) cd_player.setChecked(true);
        if (intimation.SPAREWHEEL.equals("Y")) spare_wheel.setChecked(true);
        if (intimation.AC.equals("Y")) air_conditioner.setChecked(true);
        other_acc.setText((intimation.OTHERACCOSSORIES != null) ? intimation.OTHERACCOSSORIES : "");

        //MARKET VALUE
        v_vcl_mkt_val.setText((intimation.SUM_INSURED != null) ? intimation.SUM_INSURED : "");
        v_acc_mkt_val.setText((intimation.MARKET_VALUE != null) ? intimation.MARKET_VALUE : "");

        //GENERAL POINTS
        if (intimation.HEADLIGHT.equals("Y")) headlights.setChecked(true);
        if (intimation.REARLIGHT.equals("Y")) rearLights.setChecked(true);
        if (intimation.PARKINGLIGHT.equals("Y")) parkingLights.setChecked(true);
        if (intimation.INDICATORLIGHT.equals("Y")) indicatorLights.setChecked(true);
        if (intimation.BUMPER.equals("Y")) bumper.setChecked(true);
        dents.setText((intimation.DENTS != null) ? intimation.DENTS : "");
        gen_cond.setText((intimation.GENERAL_CONDITION != null) ? intimation.GENERAL_CONDITION : "");

        //MISCELLANEOUS
        owner_name.setText((intimation.OWNER_NAME != null) ? intimation.OWNER_NAME : "");
        owner_address.setText((intimation.OWNER_ADDRESS != null) ? intimation.OWNER_ADDRESS : "");
        if (intimation.OWNER_VEHICLE.equals("Y")) isInsuredOwner.setChecked(true);
        if (intimation.PARKING_CONDITION != null) {
            parking_condition = intimation.PARKING_CONDITION;
            switch (intimation.PARKING_CONDITION) {
                case Intimation.PARKING_GARAGE:
                    RG_parking_condition.check(R.id.RB_parking_garage);
                    break;
                case Intimation.PARKING_OPEN:
                    RG_parking_condition.check(R.id.RB_parking_open);
                    break;
                case Intimation.PARKING_UNCOVERED:
                    RG_parking_condition.check(R.id.RB_parking_uncovered);
                    break;
            }
        }
        if (intimation.VEHICLE_HIRE.equals("Y")) isUnderPurchase.setChecked(true);
        if (intimation.RECONDITIONED.equals("Y")) isReconditioned.setChecked(true);
        conditioned_detail.setText((intimation.VEHICLE_CONDITION_DETAIL != null) ? intimation.VEHICLE_CONDITION_DETAIL : "");
        last_insured.setText((intimation.LAST_INSURED_WITH != null) ? intimation.LAST_INSURED_WITH : "");
        inspection_loc.setText((intimation.INSPECTION_PLACE != null) ? intimation.INSPECTION_PLACE : "");
    }

    public void onRadioButtonClicked(View v) {
        int id = v.getId();

        switch (id) {
            //Summary
            case R.id.RB_coverage_private:
                coverage_type = Intimation.COVERAGE_PRIVATE;
                break;
            case R.id.RB_coverage_commercial:
                coverage_type = Intimation.COVERAGE_COMMERCIAL;
                break;

            case R.id.RB_coverage_comm_private:
                permit_type = Intimation.PERMIT_PRIVATE;
                break;
            case R.id.RB_coverage_comm_public:
                permit_type = Intimation.PERMIT_PUBLIC;
                break;

            case R.id.RB_color_good:
                color_condition = Intimation.COLOR_GOOD;
                break;
            case R.id.RB_color_fair:
                color_condition = Intimation.COLOR_FAIR;
                break;
            case R.id.RB_color_faded:
                color_condition = Intimation.COLOR_FADED;
                break;

            case R.id.RB_parking_garage:
                parking_condition = Intimation.PARKING_GARAGE;
                break;
            case R.id.RB_parking_open:
                parking_condition = Intimation.PARKING_OPEN;
                break;
            case R.id.RB_parking_uncovered:
                parking_condition = Intimation.PARKING_UNCOVERED;
                break;
        }
    }

    public void showDatePicker(final View v) {
        final Calendar now = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String date =
                        String.format(Locale.US, "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
                ((TextInputEditText) v).setText(date);
                ((ScrollingActivity) v.getContext()).chassis_no.requestFocus();
            }
        };
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.show(getFragmentManager(), "DatePickerDialog");
        dpd.setDateRangeLimiter(new DateRangeLimiter() {
            @Override
            public int getMinYear() {
                return 1800;
            }

            @Override
            public int getMaxYear() {
                return now.get(Calendar.YEAR);
            }

            @NonNull
            @Override
            public Calendar getStartDate() {
                Calendar calendar = Calendar.getInstance();
                calendar.set(1800, 0, 1);
                return calendar;
            }

            @NonNull
            @Override
            public Calendar getEndDate() {
                return Calendar.getInstance();
            }

            @Override
            public boolean isOutOfRange(int year, int month, int day) {
                Calendar now = Calendar.getInstance();
                if (year < now.get(Calendar.YEAR)) return false;
                else if (year == now.get(Calendar.YEAR))
                    if (month < now.get(Calendar.MONTH)) return false;
                    else if (month == now.get(Calendar.MONTH))
                        if (day <= now.get(Calendar.DAY_OF_MONTH)) return false;
                return true;
            }

            @NonNull
            @Override
            public Calendar setToNearestDate(@NonNull Calendar day) {
                return day;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        });
    }

    public void OnSaveChanges(View v) {
        if (updateIntimation == null) {
            if (!Colors.getInstance().isValidColorName(color.getText().toString())) {
                color.showError(true);
                color.requestFocus();
                return;
            } else if (!Vehicles.getInstance().isValidVehicleName(vehicle_make.getText().toString())) {
                vehicle_make.showError(true);
                vehicle_make.requestFocus();
                return;
            }

            //SUMMARY
            intimation.INSURED_CNIC = (!(cnic.getText().toString().trim().isEmpty()) ? cnic.getText().toString().trim() : null);
            intimation.MARKETING_AGENT = (!(mkt_agent.getText().toString().trim().isEmpty()) ? mkt_agent.getText().toString().trim() : null);
            intimation.VEHICLE_MAKE_CODE = Vehicles.getInstance().getCodeFor(vehicle_make.getText().toString());
            intimation.VEHICLE_MODEL = (!(model.getText().toString().trim().isEmpty()) ? model.getText().toString().trim() : null);
            intimation.REGISTRATION_NO = (!(reg_no.getText().toString().trim().isEmpty()) ? reg_no.getText().toString().trim() : null);
            intimation.REGISTRATION_DATE = (!(reg_date.getText().toString().isEmpty()) ? SOAPUtils.getSoapFromDate(reg_date.getText().toString()) : null);
            intimation.CHASSIS = (!(chassis_no.getText().toString().trim().isEmpty()) ? chassis_no.getText().toString().trim() : null);
            intimation.ENGINE = (!(engine_no.getText().toString().trim().isEmpty()) ? engine_no.getText().toString().trim() : null);
            intimation.COLOR = Colors.getInstance().getCodeFor(color.getText().toString());
            intimation.COLOR_CONDITION = (!color_condition.isEmpty()) ? color_condition : null;
            intimation.COVERAG_TYPE = (!coverage_type.isEmpty()) ? coverage_type : null;
            intimation.PERMIT_TYPE = (!permit_type.isEmpty()) ? permit_type : null;

            //ACCESSORIES
            intimation.ODOMETER = (odometer.isChecked() ? "Y" : "N");
            intimation.JACK = (jackrod.isChecked() ? "Y" : "N");
            intimation.CASSATE = (cassete_radio.isChecked() ? "Y" : "N");
            intimation.CD = (cd_player.isChecked() ? "Y" : "N");
            intimation.SPAREWHEEL = (spare_wheel.isChecked() ? "Y" : "N");
            intimation.AC = (air_conditioner.isChecked() ? "Y" : "N");
            intimation.OTHERACCOSSORIES = (!(other_acc.getText().toString().trim().isEmpty()) ? other_acc.getText().toString().trim() : null);

            //MARKET VALUE
            intimation.SUM_INSURED = (!(v_vcl_mkt_val.getText().toString().trim().isEmpty()) ? v_vcl_mkt_val.getText().toString().trim() : null);
            intimation.MARKET_VALUE = (!(v_acc_mkt_val.getText().toString().trim().isEmpty()) ? v_acc_mkt_val.getText().toString().trim() : null);

            //GENERAL POINTS
            intimation.HEADLIGHT = (headlights.isChecked() ? "Y" : "N");
            intimation.REARLIGHT = (rearLights.isChecked() ? "Y" : "N");
            intimation.PARKINGLIGHT = (parkingLights.isChecked() ? "Y" : "N");
            intimation.INDICATORLIGHT = (indicatorLights.isChecked() ? "Y" : "N");
            intimation.BUMPER = (bumper.isChecked() ? "Y" : "N");
            intimation.DENTS = (!(dents.getText().toString().trim().isEmpty()) ? dents.getText().toString().trim() : null);
            intimation.GENERAL_CONDITION = (!(gen_cond.getText().toString().trim().isEmpty()) ? gen_cond.getText().toString().trim() : null);

            //MISCELLANEOUS
            intimation.OWNER_VEHICLE = (isInsuredOwner.isChecked() ? "Y" : "N");
            intimation.RECONDITIONED = (isReconditioned.isChecked() ? "Y" : "N");
            intimation.VEHICLE_HIRE = (isUnderPurchase.isChecked() ? "Y" : "N");
            intimation.OWNER_NAME = (!(owner_name.getText().toString().trim().isEmpty()) ? owner_name.getText().toString().trim() : null);
            intimation.OWNER_ADDRESS = (!(owner_address.getText().toString().trim().isEmpty()) ? owner_address.getText().toString().trim() : null);
            intimation.VEHICLE_CONDITION_DETAIL = (!(conditioned_detail.getText().toString().trim().isEmpty()) ? conditioned_detail.getText().toString().trim() : null);
            intimation.LAST_INSURED_WITH = (!(last_insured.getText().toString().trim().isEmpty()) ? last_insured.getText().toString().trim() : null);
            intimation.INSPECTION_PLACE = (!(inspection_loc.getText().toString().trim().isEmpty()) ? inspection_loc.getText().toString().trim() : null);
            intimation.PARKING_CONDITION = (!parking_condition.isEmpty()) ? parking_condition : null;

            String json = new GsonBuilder().serializeNulls().create().toJson(intimation);
            Log.d(TAG, "Saved: " + json);

            updateIntimation = new API_TASK.UpdateIntimation(intimation, ScrollingActivity.this);
            updateIntimation.execute();
            Snackbar.make(headlights, "Please wait while record updates", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onBackPressed() {
        if (updateIntimation == null) {
            super.onBackPressed();
        }
    }
}
