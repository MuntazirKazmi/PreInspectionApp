package com.agico.smk.carinspectionapp.soap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/8/2018.
 */

public class Vehicles {
    private static final Vehicles ourInstance = new Vehicles();
    private ArrayList<String> names = null, codes = null;

    private Vehicles() {
    }

    public static Vehicles getInstance() {
        return ourInstance;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    @SuppressWarnings("unused")
    public String getCodeFor(String vehicle_name) {
        return (names.contains(vehicle_name)) ? codes.get(names.indexOf(vehicle_name)) : null;
    }

    public String getNameFor(String vehicle_code) {
        return (codes.contains(vehicle_code)) ? names.get(codes.indexOf(vehicle_code)) : null;
    }

    public boolean isValidVehicleName(String vehicle_name) {
        return vehicle_name == null || names.contains(vehicle_name);
    }

    public void setVehicles(JSONArray jsonArray) {
        names = new ArrayList<>();
        codes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pmk_desc = jsonObject.getString("PMK_DESC");
                String pmk_make_code = jsonObject.getString("PMK_MAKE_CODE");
                names.add(pmk_desc);
                codes.add(pmk_make_code);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
