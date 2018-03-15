package com.agico.smk.carinspectionapp.SOAP.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/13/2018.
 */

public class SOAPUtils {
    public static String getDateFromSoap(String soapDate) {
        soapDate = soapDate.replaceAll("\\D+", "");
        java.sql.Date date = new java.sql.Date(Long.parseLong(soapDate));
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date);
    }

    public static String getSoapFromDate(String date) {
        if (date.isEmpty()) return null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            long millis = format.parse(date).getTime();
            return String.format(Locale.US, "/Date(%s)/", millis);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
