package com.brentsandstrom.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Brent on 2/3/2019.
 */

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Note: savedInstanceState is a bundle that you're given when the fragment is resumed
    // after it's already been created only. To get the bundle placed into the fragment when it's
    // created, use getArguments()
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get the view to be returned. It's the fragment_crime.xml
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        //The text field to edit the crimes title
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        //We need to save the text to our model every time it
        // changes because there is no "save" button.
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }
            //The textWatcher method we care about
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        //The button that pops up the date picker dialog
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            //create a new datePickerFragment with the date of this crime.
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //The button that pops up the time picker dialog
        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            //create a new datePickerFragment with the date of this crime.
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        //Now after both date and time buttons are created
        updateDate();


        //Just a checkbox to set if the crime is solved or not.
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        //Listen for change and save to model
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        //For activities/fragments returning a date like DatePickerFragment
        if (requestCode == REQUEST_DATE) {

            //Get the time from current date in mCrime
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCrime.getDate());
            int hour = calendar.get(calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);

            //Get the date from intent
            Date pickedDate = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(pickedDate);
            int year = calendar2.get(calendar.YEAR);
            int month = calendar2.get(Calendar.MONTH);
            int day = calendar2.get(Calendar.DAY_OF_MONTH);

            mCrime.setDate(new GregorianCalendar(year, month, day, hour, minute).getTime());
            updateDate();
        }else if (requestCode == REQUEST_TIME) {
            //Get the date from mCrime
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCrime.getDate());
            int year = calendar.get(calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //get the time from the intent
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0);

            mCrime.setDate(new GregorianCalendar(year, month, day, hour, minute).getTime());
            updateDate();
        }
    }

    private void updateDate() {
        DateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        mDateButton.setText(dayFormatter.format(mCrime.getDate()));
        DateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        mTimeButton.setText(timeFormatter.format(mCrime.getDate()));
    }
}
