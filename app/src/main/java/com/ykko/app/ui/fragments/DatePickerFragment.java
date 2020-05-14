package com.ykko.app.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

public static String date = "";
    private OnDateListener callback;
    public interface OnDateListener {
        public void OnDateSubmit(String Selected_Date);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnDateListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
        //return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
       Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, view.getYear());
        c.set(Calendar.MONTH, view.getMonth());
        c.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
        date =dateFormat.format(c.getTime());
        callback.OnDateSubmit(date);
// Pass data to other Fragment

    }

}

