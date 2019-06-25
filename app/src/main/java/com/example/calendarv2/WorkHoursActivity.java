package com.example.calendarv2;
/**
 * created on 6/17/2019
 * BY: Moulue Huang
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class WorkHoursActivity extends AppCompatActivity implements EditText.OnClickListener  {
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //initialize bundle to pass data back into main frame
        Bundle startEndTime = getIntent().getExtras();
        Intent passData_Intent = new Intent(this, WorkHoursActivity.class);

        // get the startime textview and apply onclick and work on it to change data
        final EditText startTimeTV = (EditText) findViewById(R.id.startTimeInput);
        startTimeTV.setOnClickListener(this);
        final EditText endTimeTV = (EditText) findViewById(R.id.endTimeInput);
        endTimeTV.setOnClickListener(this);

        //add a listener to the finishButton to terminate the Activity
        //and return data to the main activity
        final Button finishButton = (Button)findViewById(R.id.finishButton);
        finishButton.setOnClickListener(this);

        //add a new thread to update the totalPay periodically
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //call the updateTotalPayTextView periodically
                                updateTotalPayTextView();
                            }
                        });
                    }
                }catch (InterruptedException e){
                }
            }


        };
        //officially start the Thread
        t.start();

    }

    private void updateTotalPayTextView() {

        final EditText payInput = (EditText)findViewById(R.id.payPerHourInput);
        final EditText totalHours = (EditText)findViewById(R.id.totalHoursCalculated);

        float d_pay_per_hour = 0,d_total_hours = 0;

        //try/catch to see if the Edit Text's are empty or not
        try {
            d_pay_per_hour = Float.valueOf(payInput.getText().toString());

            //split the String to get the hours in decimal only
            String[] Split = totalHours.getText().toString().split(" ");
            d_total_hours = Float.valueOf(Split[0]);
        }
        catch (IllegalArgumentException x){
            Log.i("payInput/totalHours","empty String");
        }

        //calculate the totalPay
        float totalPay = d_pay_per_hour * d_total_hours;
        final EditText incomeOutputTV = (EditText)findViewById(R.id.totalEarnedOutput);

        //convert the string to two decimal places and set the Text
        String s_totalPay = String.format("%.2f",totalPay);
        incomeOutputTV.setText("$" + s_totalPay);
    
    }
//TODO add Calculate button, and finish button - > calculate
/*
        String startTime = "startTime:";
        passData_Intent.putExtra("startTime",startTime);

        String endTime = "endTime:";
        passData_Intent.putExtra("endTime",endTime);

        setResult(RESULT_OK,passData_Intent);
*/



    @Override
    public void onClick(View view) {
        EditText InputTV = null;
        EditText OtherTV = null;
        //depending on what is clicked, do the specified procedure
        switch(view.getId()){
            case R.id.startTimeInput:
                InputTV = (EditText) findViewById(R.id.startTimeInput);
                OtherTV = (EditText) findViewById(R.id.endTimeInput);
                break;
            case R.id.endTimeInput:
                InputTV = (EditText) findViewById(R.id.endTimeInput);
                OtherTV = (EditText) findViewById(R.id.startTimeInput);
                break;
            case R.id.finishButton:
                finish();
                t.interrupt();
                Log.i("WorkHoursActivity","finished");
                break;
            default:
                throw new RuntimeException("Unknown EditText ID");
        }
        //if the button is not clicked then open up the dialogFraments
        //to take in the user's time
        if(view.getId() == R.id.startTimeInput || view.getId() == R.id.endTimeInput) {
            EditText TotalOutput = (EditText) findViewById(R.id.totalEarnedOutput);
            EditText payPerHourInput = (EditText) findViewById(R.id.payPerHourInput);
            final EditText totalTimeTV = (EditText) findViewById(R.id.totalHoursCalculated);
            DialogFragment timePicker = new TimePickerFragment(InputTV, OtherTV, totalTimeTV, TotalOutput);
            timePicker.show(getSupportFragmentManager(), "time picker");
        }
    }

}
