package com.example.calendarv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Locale;
public class MainActivity extends FragmentActivity {
    private MaterialCalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendarView);

        //adds a Date changed listener to the calender, to get the selected date, and if a date is selected,
        //set buttons to be visible

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                TextView textView = findViewById(R.id.DateSelectedText);

                String month = Integer.toString(date.getMonth() + 1);
                String year = Integer.toString(date.getYear());
                String day = Integer.toString(date.getDay());

                String formattedDate = (String.format(Locale.getDefault(), "%s - %s - %s", month, day, year));
                textView.setText("DatePicked: " + formattedDate);
                //Toast.makeText(MainActivity.this, "" + formattedDate, Toast.LENGTH_SHORT).show(); //console check lien

                Button reminderBut = findViewById(R.id.reminderButton);
                Button shiftBut = findViewById(R.id.shiftButton);

                //set their visibility to true, now that a date is selected
                reminderBut.setVisibility(View.VISIBLE);
                shiftBut.setVisibility(View.VISIBLE);


            }
        });

    }

   //TODO add event of the selected time into the calendar
    // procedure when shiftButton is clicked
    public void shiftButtonProcedure(View view) {
        Log.i("shiftButton", "is clicked");
        openWorkHoursActivity();
    }
    //next Activity to get intent and data from user
    private void openWorkHoursActivity() {
        Intent intent = new Intent(this,WorkHoursActivity.class);
        startActivity(intent);
    }
}
