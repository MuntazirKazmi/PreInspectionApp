package com.agico.smk.carinspectionapp.SOAP;

import com.agico.smk.carinspectionapp.SOAP.Data.Intimation;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.STATUS;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/8/2018.
 */

public class Intimations {
    private static final Intimations ourInstance = new Intimations();
    private ArrayList<Intimation> under_process, processed, pending;

    private Intimations() {
    }

    public static Intimations getInstance() {
        return ourInstance;
    }

    public void setIntimations(JSONArray array) {

        try {
            under_process = new ArrayList<>();
            pending = new ArrayList<>();
            processed = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                String object = array.getJSONObject(i).toString();
                Intimation intimation = new Gson().fromJson(object, Intimation.class);
                switch (intimation.STATUS1) {
                    case Intimation.STATUS_UNDER_PROCESS:
                        under_process.add(intimation);
                        break;
                    case Intimation.STATUS_PENDING:
                        pending.add(intimation);
                        break;
                    case Intimation.STATUS_PROCESSED:
                        processed.add(intimation);
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Intimation> getUnder_process() {
        return under_process;
    }

    public ArrayList<Intimation> getProcessed() {
        return processed;
    }

    public ArrayList<Intimation> getPending() {
        return pending;
    }

    public Intimation getIntimationAt(int index, STATUS status) {
        switch (status) {

            case Under_Process:
                return under_process.get(index);
            case Processed:
                return processed.get(index);
            case Pending:
                return pending.get(index);
            default:
                return null;
        }
    }
}
