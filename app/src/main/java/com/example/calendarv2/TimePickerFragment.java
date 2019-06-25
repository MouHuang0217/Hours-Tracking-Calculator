package com.example.calendarv2;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private EditText time;
    private EditText other_time;
    private EditText hoursTV;
    private EditText output;
    private EditText payPerHour;

    //create an object timepickerfragment to be able too retrieve specific TextViews
    //and update them accordinly
    public TimePickerFragment(EditText timeTV,EditText otherTV,EditText hoursTV, EditText outputTV) {
        this.time = timeTV;
        this.other_time = otherTV;
        this.hoursTV = hoursTV;
        this.output = outputTV;
        Log.i("Timepicker","createdFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String AM_PM ;
        if(hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        if (minute < 10){
            time.setText(hourOfDay + ":" + minute +"0 " + AM_PM);
        }
        else{
        time.setText(hourOfDay + ":" + minute + " " + AM_PM);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");

        Log.i("starttimes:" , String.valueOf(time.getText()));
        Log.i("endTime: ", String.valueOf(other_time.getText()));
        Date timeOne = null;
        Date timeTwo = null;
        try {
            // calculate the difference in time from Start time to end time
            timeOne = simpleDateFormat.parse(String.valueOf(time.getText()));
            timeTwo = simpleDateFormat.parse(String.valueOf(other_time.getText()));
            long difference = timeTwo.getTime() - timeOne.getTime();

            double days = (double) (difference / (1000*60*60*24));
            double hours = (double) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            double min = (double) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            hours = (hours < 0 ? -hours : hours);

            //set the total hours worked into the Textview of hours rounded to the 3rd decimal place
            hoursTV.setText(String.format("%.2f",hours) + " hours");
           // output.setText(String.valueOf(totalPay));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}

