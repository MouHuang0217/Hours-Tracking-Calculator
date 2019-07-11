package com.example.calendarv2.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.calendarv2.DatabaseHelper;
import com.example.calendarv2.Decorators.EventDecorator;
import com.example.calendarv2.Decorators.OneDayDec;
import com.example.calendarv2.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity {
    private ArrayList<EventDecorator> DEC_LIST = new ArrayList<>();
    private MaterialCalendarView calendar;

    CalendarDay SELECTED_DATE;

    Button deleteButton;
    Button shiftButton;
    Button calculateButton;
    Button calculateButtonDONE;
    DatabaseHelper MY_DATABASE ;
    private final OneDayDec oneDayDecorator = new OneDayDec();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        calendar = findViewById(R.id.calendarView);
        //add a one day decorator onto the calendar
        calendar.addDecorators(oneDayDecorator);
        calendar.invalidateDecorators();

        //create an instance of database
        MY_DATABASE = new DatabaseHelper(this);

        //updateCalendar using the data that is in SQliteDatabase
        updateCalendar();

        //adds a Date changed listener to the calender, to get the selected date, and if a date is selected,
        //set buttons to be visible
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                oneDayDecorator.setDate(date);
                widget.invalidateDecorators();
                SELECTED_DATE = date;
                TextView textView = findViewById(R.id.DateSelectedText);

                String month = Integer.toString(date.getMonth() + 1);
                String year = Integer.toString(date.getYear());
                String day = Integer.toString(date.getDay());

                String FORMAT_DATE = (String.format(Locale.getDefault(), "%s - %s - %s", month, day, year));

                textView.setText("DatePicked: " + FORMAT_DATE);


                deleteButton = findViewById(R.id.deleteButton);
                shiftButton = findViewById(R.id.shiftButton);

                //set their visibility to true, now that a date is selected
                shiftButton.setVisibility(View.VISIBLE);

                //make a toast that tells us how much money is made for the selected Date
                for(EventDecorator decs : DEC_LIST){
                    String dateOne = String.valueOf(decs.getDate());
                    String dateTwo = String.valueOf(date);
                    if(dateOne.equals(dateTwo)){
                        Toast.makeText(MainActivity.this, "$" +decs.getTotal_pay(), Toast.LENGTH_SHORT).show();
                        //if the button has an event, then set delete button to be visible, otherwise keep it hidden
                        deleteButton.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

    }
    //when calculate Button is clicked set the selection mode to a range and calculate the total
    //pay from those dates
    public void calculateButtonProcedure(View view){

        calculateButton = findViewById(R.id.Calculate_Button);
        calculateButtonDONE = findViewById(R.id.Calculate_Button_Done);

        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        calendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                calculateButton.setVisibility(View.INVISIBLE);
                calculateButtonDONE.setVisibility(View.VISIBLE);
                double paycheck_total = 0.0;
                for(CalendarDay i: dates){
                    for(EventDecorator j: DEC_LIST ) {
                        //Date syntax is different so we have to convert it to CalendarDay syntax
                        String year = String.valueOf(i.getYear());
                        String month = String.valueOf(i.getMonth());
                        String day = String.valueOf(i.getDay());

                        //compare the two Dates, and if they are the same then add the pay of that
                        //date to the paycheck total
                        String dateOne= "CalendarDay{" + year + "-" + month + "-" + day + "}";
                        String dateTwo = String.valueOf(j.getDate());

                        if(dateOne.equals(dateTwo)) {
                            double dateOne_pay = Double.parseDouble(j.getTotal_pay());
                            paycheck_total += dateOne_pay;
                        }
                    }
                }
                //display a toast of how much is made
                Toast.makeText(MainActivity.this, "Paycheck: $" +paycheck_total, Toast.LENGTH_SHORT).show();
            }
        });

    }
    //when button done is clicked, bascilly revert back to the default display and go back to single
    //selection mode
    public void calculateButtonDoneProcedure(View view){
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calculateButton.setVisibility(View.VISIBLE);
        calculateButtonDONE.setVisibility(View.INVISIBLE);

    }
    // procedure when shiftButton is clicked
    public void shiftButtonProcedure(View view) {
        Log.i("shiftButton", "is clicked");
        openWorkHoursActivity();
    }
    //next Activity to get intent and data from user
    private void openWorkHoursActivity() {
        Intent intent = new Intent(this,WorkHoursActivity.class);
        startActivityForResult(intent,1);
    }
    //procudure when deleteButton is clicked
    public void deleteButtonProcedure(View view){
        Log.i("delete button", "is clicked");
        deleteShift();
    }

    //delete the data when delete button is clicked
    public void deleteShift(){
        //convert the date to be from 1 - 12 because the SELECTED_DATE's month is in terms of 0-11
        String s_selected_date = convertDate(SELECTED_DATE);
        //return value to make Toast to check if its deleted or not
        boolean isDeleted = MY_DATABASE.deleteData(s_selected_date);
        for(int i = 0; i < DEC_LIST.size(); i++){
            //if the DECORATOR is the same date, then remove the decorator
            //set the visibility of delete button to invisible
            //and remove it from the decorator array
            if(DEC_LIST.get(i).getDate().equals(SELECTED_DATE)){
                calendar.removeDecorator(DEC_LIST.get(i));
                deleteButton.setVisibility(View.INVISIBLE);
                DEC_LIST.remove(i);
                break;
            }
        }
        //if successful, then print out these statements
        if (isDeleted) Toast.makeText(MainActivity.this,"Data deleted", Toast.LENGTH_LONG).show();
        else Toast.makeText(MainActivity.this,"Data not deleted", Toast.LENGTH_LONG).show();
    }

    //Convert date because month in Calendar is from 0 - 11 and we want it from 1 - 12
    private String convertDate(CalendarDay selected_date) {
        String date = selected_date.toString();
        date = date.replaceAll("\\{","");
        date = date.replaceAll("\\}","");
        date = date.replaceAll("CalendarDay","");

        String[] split = date.split("-");

        String year = split[0];
        int month_int = Integer.valueOf(split[1]) + 1;
        String month = String.valueOf(month_int);
        String day = split[2];

        String result = "CalendarDay{" + year + "-" + month + "-" + day +"}";
        Log.i("Date is now", result);
        return result;
    }

    //update the Calendar based on what is in the SQlitedatabase
    public void updateCalendar(){
        //get the information and plug it into a Cursor
        Cursor result = MY_DATABASE.getInformation();
        //while there is an result that is next
        while(result.moveToNext()){
            //get the Date in value of String and split and convert it into CalendarDay
            String s = result.getString(1);
            String[] split = s.split("CalendarDay");
            s = split[1].replaceAll("\\{","");
            s = s.replaceAll("\\}","");

            //get the total pay for the certain day
            String totalPayText = result.getString(2);

            CalendarDay date = null;
            try {
                date = CalendarDay.from(new SimpleDateFormat("yyyy-MM-dd").parse(s));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //create an event Decorator for each event
            final EventDecorator EVENTDECORATOR = new EventDecorator(Color.RED,date,totalPayText);
            DEC_LIST.add(EVENTDECORATOR);
            calendar.addDecorator(EVENTDECORATOR);
        }
    }
    //after WorkHoursActivity is done, get the data and apply decorators onto the selected date
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String totalPay_text = data.getStringExtra("totalPay");
                final EventDecorator EVENTDECORATOR = new EventDecorator(Color.RED,SELECTED_DATE,totalPay_text);
                DEC_LIST.add(EVENTDECORATOR);
                Log.i("totalPay_Text:", totalPay_text);
                calendar.addDecorator(EVENTDECORATOR);

                String DateString = convertDate(SELECTED_DATE);

                //the function returns a boolean to check if it was inserted or not
                boolean isInserted = MY_DATABASE.insertData(DateString,totalPay_text);
                if(isInserted = true) Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                else Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
            }
        }
    }

}
