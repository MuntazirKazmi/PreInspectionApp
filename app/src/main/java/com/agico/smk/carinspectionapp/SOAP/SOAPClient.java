package com.agico.smk.carinspectionapp.SOAP;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/7/2018.
 */

public class SOAPClient {

    public static final String GET_COLORS = "Get_Colors";
    public static final String GET_VEHICLE_LIST = "Vehicle_list";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ADDRESS = "https://askapi.agico.com.pk/inspection.asmx?WSDL";
    private static final String AUTHENTICATE = "Authenticate";
    private static final String UPDATE_INSPECTION = "updateinspection";
    private static final String VIEW_REMARKS = "View_Remarks";
    private static final String ADD_REMARKS = "Add_Remarks";
    private static final String UPLOAD_IMAGE = "Upload_Image";
    private static final String GET_IMAGES = "Get_Images";
    private static final String FINAL_SUBMIT = "Final_Submit";

    private static String doAction(SoapObject request, String action) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response;
        try {
            httpTransport.call(NAMESPACE + action, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        return response.toString();
    }

    public static String get_request(String getAction) {
        if (getAction.equals(GET_COLORS)
                || getAction.equals(GET_VEHICLE_LIST)) {
            SoapObject request = new SoapObject(NAMESPACE, getAction);
            return doAction(request, getAction);
        }
        return null;
    }

    public static String authenticate(String username, String password) {
        SoapObject request = new SoapObject(NAMESPACE, AUTHENTICATE);
        request = setParams(request, new String[]{"SurveyorID", "PASSWORD"}, new String[]{username, password});
        return doAction(request, AUTHENTICATE);
    }

    public static String updateInspection(String intimationJSON) {
        SoapObject request = new SoapObject(NAMESPACE, UPDATE_INSPECTION);
        request = setParams(request, new String[]{"jsonvalue"}, new String[]{intimationJSON});
        return doAction(request, UPDATE_INSPECTION);
    }

    public static String addRemarks(String inspection_id, String remarks) {
        SoapObject request = new SoapObject(NAMESPACE, ADD_REMARKS);
        request = setParams(request, new String[]{"InspectionID", "Remarks"}, new String[]{inspection_id, remarks});
        return doAction(request, ADD_REMARKS);
    }

    public static String getRemarksFor(String inspection_id) {
        SoapObject request = new SoapObject(NAMESPACE, VIEW_REMARKS);
        request = setParams(request, new String[]{"InspectionID"}, new String[]{inspection_id});
        return doAction(request, VIEW_REMARKS);
    }

    public static String getImagesFor(String inspection_id) {
        SoapObject request = new SoapObject(NAMESPACE, GET_IMAGES);
        request = setParams(request, new String[]{"inspection_id"}, new String[]{inspection_id});
        return doAction(request, GET_IMAGES);
    }

    public static String finalSubmit(String inspection_id) {
        SoapObject request = new SoapObject(NAMESPACE, FINAL_SUBMIT);
        request = setParams(request, new String[]{"inspection_id"}, new String[]{inspection_id});
        return doAction(request, FINAL_SUBMIT);
    }

    public static String uploadImage(String inspection_id, String base64Image) {
        SoapObject request = new SoapObject(NAMESPACE, UPLOAD_IMAGE);
        request = setParams(request, new String[]{"InspectionId", "inspec_img"}, new String[]{inspection_id, base64Image});
        return doAction(request, UPLOAD_IMAGE);
    }

    private static SoapObject setParams(SoapObject request, String[] keys, String[] vals) {
        for (int i = 0; i < keys.length; i++) {
            PropertyInfo pi = new PropertyInfo();
            pi.setName(keys[i]);
            pi.setValue(vals[i]);
            pi.setType(Integer.class);
            request.addProperty(pi);
        }
        return request;
    }


}
