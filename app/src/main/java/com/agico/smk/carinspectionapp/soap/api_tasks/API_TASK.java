package com.agico.smk.carinspectionapp.soap.api_tasks;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.agico.smk.carinspectionapp.IntimationListActivity;
import com.agico.smk.carinspectionapp.PhotosActivity;
import com.agico.smk.carinspectionapp.RemarksActivity;
import com.agico.smk.carinspectionapp.ScrollingActivity;
import com.agico.smk.carinspectionapp.soap.Intimations;
import com.agico.smk.carinspectionapp.soap.SOAPClient;
import com.agico.smk.carinspectionapp.soap.data.Intimation;
import com.agico.smk.carinspectionapp.soap.data.Remark;
import com.agico.smk.carinspectionapp.soap.enums.LOADING_STATUS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Syed Muntazir Mehdi
 * For AGICO
 * on 2/8/2018.
 */

public class API_TASK {

    public static class AddRemarks extends AsyncTask<Void, Void, String> {
        private String inspection_id;
        private String remarks;
        private WeakReference<RemarksActivity> remarksActivityWeakReference;

        public AddRemarks(String inspection_id, String remarks, RemarksActivity remarksActivity) {
            this.inspection_id = inspection_id;
            this.remarks = remarks;
            this.remarksActivityWeakReference = new WeakReference<>(remarksActivity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return SOAPClient.addRemarks(inspection_id, remarks);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RemarksActivity remarksActivity = remarksActivityWeakReference.get();
            if ("Remarks Saved!".equals(s)) {
                new AlertDialog.Builder(remarksActivity).setTitle("SUCCESS").setMessage("Remarks have been added!").setPositiveButton("OK", null).create().show();
                remarksActivity.refreshRemarks(null);
            } else {
                new AlertDialog.Builder(remarksActivity).setTitle("OOPS!!").setMessage("Looks like remarks are not added").setPositiveButton("OK", null).create().show();
            }
            remarksActivity.addRemarks = null;
        }
    }

    public static class ViewRemarks extends AsyncTask<Void, Void, String> {
        private WeakReference<RemarksActivity> remarksActivityWeakReference;
        private String inspection_id;

        public ViewRemarks(RemarksActivity remarksActivity, String inspection_id) {
            this.remarksActivityWeakReference = new WeakReference<>(remarksActivity);
            this.inspection_id = inspection_id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return SOAPClient.getRemarksFor(inspection_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("", "onPostExecute() returned: " + s);
            RemarksActivity remarksActivity = remarksActivityWeakReference.get();
            if ("No Record Found!".equals(s)) {
                remarksActivity.remarksAdapter.remarks.clear();
                remarksActivity.remarksAdapter.updateLoadingStatus(LOADING_STATUS.LOADED_EMPTY);
                remarksActivity.viewRemarks = null;
                return;
            }
            try {
                JSONArray array = new JSONArray(s);
                remarksActivity.remarksAdapter.remarks.clear();
                for (int i = 0; i < array.length(); i++) {
                    remarksActivity.remarksAdapter.remarks.add(new Gson().fromJson(array.getJSONObject(i).toString(), Remark.class));
                }
                remarksActivity.remarksAdapter
                        .updateLoadingStatus((remarksActivity.remarksAdapter.remarks.size() != 0) ? LOADING_STATUS.LOADED_OK : LOADING_STATUS.LOADED_EMPTY);

            } catch (JSONException e) {
                e.printStackTrace();
                remarksActivity.remarksAdapter.updateLoadingStatus(LOADING_STATUS.LOADED_ERROR);
            }
            remarksActivity.viewRemarks = null;
        }
    }

    public static class UpdateIntimation extends AsyncTask<Void, Void, String> {
        private String intimationJSON;
        private WeakReference<ScrollingActivity> scrollingActivityWeakReference;

        public UpdateIntimation(Intimation intimation, ScrollingActivity scrollingActivity) {
            intimationJSON = new GsonBuilder().serializeNulls().create().toJson(intimation);
            this.scrollingActivityWeakReference = new WeakReference<>(scrollingActivity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return SOAPClient.updateInspection(intimationJSON);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final ScrollingActivity scrollingActivity = scrollingActivityWeakReference.get();
            Log.d("999", "onPostExecute() returned: " + s);
            if ("True".equals(s)) {
                new AlertDialog.Builder(scrollingActivity).setTitle("SUCCESS").setMessage("Intimation has been updated.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scrollingActivity.finish();
                            }
                        })
                        .create().show();
            } else {
                new AlertDialog.Builder(scrollingActivity).setTitle("OOPS!!").setMessage("Looks like record is not updated.\n" + s).setPositiveButton("OK", null).create().show();
            }
            scrollingActivity.updateIntimation = null;
        }
    }

    public static class ImageUpload extends AsyncTask<Void, Void, String> {
        private String inspection_id;
        private String base64;
        private WeakReference<PhotosActivity> weakReference;

        public ImageUpload(String inspection_id, String base64, PhotosActivity cameraActivity) {
            this.inspection_id = inspection_id;
            this.base64 = base64;
            this.weakReference = new WeakReference<>(cameraActivity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return SOAPClient.uploadImage(inspection_id, base64);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final PhotosActivity cameraActivity = weakReference.get();
            Log.d("333", "onPostExecute() returned: " + s);
            if ("Saved".equals(s)) {
                new AlertDialog.Builder(cameraActivity).setTitle("SUCCESS").setMessage("Photo has been uploaded!").setPositiveButton("OK", null).create().show();
            } else {
                new AlertDialog.Builder(cameraActivity).setTitle("OOPS!!").setMessage("Looks like photo is not uploaded").setPositiveButton("OK", null).create().show();
            }
            cameraActivity.uploadImageTask = null;
            cameraActivity.refreshImages();
        }
    }

    public static class GetImages extends AsyncTask<Void, Void, String> {
        private WeakReference<PhotosActivity> photosActivityWeakReference;
        private String inspection_id;

        public GetImages(PhotosActivity photosActivity, String inspection_id) {
            this.photosActivityWeakReference = new WeakReference<>(photosActivity);
            this.inspection_id = inspection_id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return SOAPClient.getImagesFor(inspection_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("", "onPostExecute() returned: " + s);
            PhotosActivity photosActivity = photosActivityWeakReference.get();
            try {
                JSONArray array = new JSONArray(s);
                photosActivity.photosAdapter.image_urls.clear();
                for (int i = 0; i < array.length(); i++) {
                    String imagepath = array.getJSONObject(i).getString("imagepath");
                    photosActivity.photosAdapter.image_urls.add(imagepath);
                }
                photosActivity.photosAdapter
                        .updateLoadingStatus((photosActivity.photosAdapter.image_urls.size() != 0) ? LOADING_STATUS.LOADED_OK : LOADING_STATUS.LOADED_EMPTY);

            } catch (JSONException e) {
                e.printStackTrace();
                photosActivity.photosAdapter.updateLoadingStatus(LOADING_STATUS.LOADED_ERROR);
            }
            photosActivity.getImages = null;
        }
    }

    public static class FinalSubmit extends AsyncTask<Void, Void, String> {
        private WeakReference<IntimationListActivity> activityWeakReference;
        private String inspection_id;

        public FinalSubmit(IntimationListActivity intimationListActivity, String inspection_id) {
            this.activityWeakReference = new WeakReference<>(intimationListActivity);
            this.inspection_id = inspection_id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return SOAPClient.finalSubmit(inspection_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("3232", "onPostExecute() returned: " + s);
            IntimationListActivity intimationListActivity = activityWeakReference.get();
            if ("TRUE".equals(s)) {
                intimationListActivity.refreshAll();
                new AlertDialog.Builder(intimationListActivity).setTitle("Success").setMessage("Final Submission Successful").create().show();
            } else
                new AlertDialog.Builder(intimationListActivity).setTitle("OOPS").setMessage("Some Error Occurred.\nPlease Try Again.").create().show();
            intimationListActivity.finalSubmit = null;

        }
    }

    public static class RefreshIntimations extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final WeakReference<IntimationListActivity> activityWeakReference;

        public RefreshIntimations(IntimationListActivity context) {
            SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
            mEmail = myPrefs.getString("username", "");
            mPassword = myPrefs.getString("password", "");
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String s = SOAPClient.authenticate(mEmail, mPassword);
                JSONArray jsonArray = new JSONArray(s);
                Intimations.getInstance().setIntimations(jsonArray);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            IntimationListActivity intimationListActivity = activityWeakReference.get();
            intimationListActivity.refreshTask = null;
            if (success) {
                intimationListActivity.recycler_adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onCancelled() {
            IntimationListActivity intimationListActivity = activityWeakReference.get();
            intimationListActivity.refreshTask = null;
        }
    }
}
