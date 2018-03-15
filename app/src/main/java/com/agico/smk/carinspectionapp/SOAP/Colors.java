package com.agico.smk.carinspectionapp.SOAP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/7/2018.
 */

public class Colors {
    private static final Colors ourInstance = new Colors();
    private ArrayList<String> names = null, codes = null;

    private Colors() {
    }

    public static Colors getInstance() {
        return ourInstance;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    @SuppressWarnings("unused")
    public String getCodeFor(String color_name) {
        return (names.contains(color_name)) ? codes.get(names.indexOf(color_name)) : null;
    }

    public String getNameFor(String color_code) {
        return (codes.contains(color_code)) ? names.get(codes.indexOf(color_code)) : null;
    }

    public boolean isValidColorName(String color_name) {
        return color_name == null || names.contains(color_name);
    }

    public void setColors(JSONArray jsonArray) {
        names = new ArrayList<>();
        codes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String plr_desc = jsonObject.getString("PLR_DESC");
                String plr_code = jsonObject.getString("PLR_CODE");
                names.add(plr_desc);
                codes.add(plr_code);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
