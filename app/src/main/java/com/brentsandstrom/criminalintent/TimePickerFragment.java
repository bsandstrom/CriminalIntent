package com.brentsandstrom.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_HOUR = "com.bignerdranch.android.criminalintent.hour";
    public static final String EXTRA_MINUTE = "com.bignerdranch.android.criminalintent.minute";

    private static final String ARG_DATE = "date";

    private TimePicker mDatePicker;

    //Creates a new DatePickerFragment and sticks a date object into a bundle and sticks that to the
    // fragment, then returns the fragment
    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Run when the date dialog is first created. Loads the date from the arguments then sets the
    // datePickers date to that value
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mDatePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        mDatePicker.setHour(hour);
        mDatePicker.setMinute(min);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        //define what happens when you press ok
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Get the date from the datepicker

                                int hour = mDatePicker.getHour();
                                int min = mDatePicker.getMinute();

                                //Send the date with the sendResult
                                sendResult(Activity.RESULT_OK, hour, min);
                            }
                        })
                .create();
    }

    //Sends the time to the target fragment. Probably crimefragment although others could be used.
    private void sendResult(int resultCode, int hour, int min){
        //Must have targetFragment
        if(getTargetFragment() == null) {
            return;
        }

        //Put the date in an intent
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hour);
        intent.putExtra(EXTRA_MINUTE, min);

        //Explicitly call onActivityResult and pass data over.
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
