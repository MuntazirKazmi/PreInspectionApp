package com.agico.smk.carinspectionapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.agico.smk.carinspectionapp.soap.Colors;
import com.agico.smk.carinspectionapp.soap.Intimations;
import com.agico.smk.carinspectionapp.soap.SOAPClient;
import com.agico.smk.carinspectionapp.soap.Vehicles;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

/*
 * A login screen that offers login via email/password.
 */
public class InspectorLoginActivity extends AppCompatActivity {


    private static final String TAG = "InspectorLoginActivity";
    /*
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_login);


        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, InspectorLoginActivity.this);
            mAuthTask.execute();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final WeakReference<InspectorLoginActivity> activityWeakReference;

        UserLoginTask(String email, String password, InspectorLoginActivity context) {
            mEmail = email;
            mPassword = password;
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Log.d(TAG, "doInBackground() sent: " + mEmail + ":" + mPassword);
                String s = SOAPClient.authenticate(mEmail, mPassword);
                Log.d(TAG, "doInBackground() returned: " + s);
                JSONArray jsonArray = new JSONArray(s);
                Intimations.getInstance().setIntimations(jsonArray);

                s = SOAPClient.getRequest(SOAPClient.GET_COLORS);
                jsonArray = new JSONArray(s);
                Colors.getInstance().setColors(jsonArray);

                s = SOAPClient.getRequest(SOAPClient.GET_VEHICLE_LIST);
                jsonArray = new JSONArray(s);
                Vehicles.getInstance().setVehicles(jsonArray);

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            InspectorLoginActivity loginActivity = activityWeakReference.get();
            loginActivity.mAuthTask = null;
            loginActivity.showProgress(false);

            if (success) {
                loginActivity.getSharedPreferences("myPrefs", MODE_PRIVATE).edit()
                        .putString("username", mEmail).putString("password", mPassword).apply();
                loginActivity.startActivity(new Intent(loginActivity, IntimationListActivity.class));
            } else {
                loginActivity.mPasswordView.setError(loginActivity.getString(R.string.error_login_failed));
                loginActivity.mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            InspectorLoginActivity loginActivity = activityWeakReference.get();
            loginActivity.mAuthTask = null;
            loginActivity.showProgress(false);
        }
    }
}

