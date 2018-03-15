package com.agico.smk.carinspectionapp.SMKComponents;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;


/*
 * Created by AGICO on 1/31/2018.
 */

public class MaterialSpinner extends AppCompatAutoCompleteTextView implements View.OnTouchListener, AdapterView.OnItemSelectedListener {
    static final String TAG = MaterialSpinner.class.getName();
    static String hint = "", errorMessage = "Choose item from list";
    boolean shouldShowDropDown = true;
    TextInputLayout parent;
    private ArrayList<String> suggestions = new ArrayList<>();

    public MaterialSpinner(Context context) {
        super(context);
    }

    public MaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapterWithStringArray(String[] stringArray) {
        if (stringArray.length < 1)
            Log.e(TAG, "setAdapterWithStringArray: Strings Array can not be empty!");
        setAdapterWithStringArrayList(new ArrayList<>(Arrays.asList(stringArray)));
    }

    public void setAdapterWithStringArrayList(ArrayList<String> strings) {
        parent = ((TextInputLayout) getParent().getParent());
        if (parent.getHint() == null) {
            hint = "";
        } else {
            hint = parent.getHint().toString();
        }

        if (strings.size() < 1)
            Log.e(TAG, "setAdapterWithStringArrayList: Strings ArrayList can not be empty!");
        suggestions = new ArrayList<>(strings);
        setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, suggestions));
        setThreshold(0);
        setOnTouchListener(this);
        setOnItemSelectedListener(this);
        setValidator(new Validator() {
            @Override
            public boolean isValid(CharSequence charSequence) {
                String s = String.valueOf(charSequence);
                if (suggestions.contains(s)) {
                    if (isErrorShown()) showError(false);
                    return true;
                }
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence charSequence) {
                showError(true);
                return "";
            }
        });
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int motionEventAction = motionEvent.getAction();
        if (motionEventAction == MotionEvent.ACTION_SCROLL) shouldShowDropDown = false;
        if (motionEventAction == MotionEvent.ACTION_UP) {
            if (shouldShowDropDown) {
                showDropDown();
                requestFocus();
                InputMethodManager inputMethodManager =
                        (InputMethodManager) (getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            } else shouldShowDropDown = true;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setError(null);
        if (isErrorShown()) showError(false);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        showError(true);
    }

    boolean isErrorShown() {
        return String.valueOf(parent.getHint()).equals(errorMessage);
    }

    public void showError(boolean error) {
        if (error) {
            setError("Select from List Only");
            setText("");
            parent.setHint(errorMessage);
        } else {
            setError(null);
            parent.setHint(hint);
        }

    }

}
