package com.agico.smk.carinspectionapp.soap.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/13/2018.
 */

public class SOAPUtils {
    public static String getDateFromSoap(String soapDate) {
        String millis = soapDate.replaceAll("\\D+", "");
        java.sql.Date date = new java.sql.Date(Long.parseLong(millis));
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date);
    }

    public static String getSoapFromDate(String date) {
        if (date.isEmpty()) return null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            long millis = format.parse(date).getTime();
            Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            utc.setTimeInMillis(millis);
            return String.format(Locale.US, "/Date(%s)/", millis);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
