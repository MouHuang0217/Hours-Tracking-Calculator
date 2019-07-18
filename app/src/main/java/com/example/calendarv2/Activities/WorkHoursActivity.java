package com.example.calendarv2.Activities;
/**
 * created on 7/5/2019
 * BY: Moulue Huang
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.calendarv2.Fragments.TimePickerFragment;
import com.example.calendarv2.R;

public class WorkHoursActivity extends AppCompatActivity implements EditText.OnClickListener  {
    private Thread THREAD;
    private float TOTALPAY = 0;
    private float HOURS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        THREAD = new Thread() {
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
        THREAD.start();

    }

    private void updateTotalPayTextView() {

        final EditText payInput = (EditText)findViewById(R.id.payPerHourInput);
        final EditText totalHours = (EditText)findViewById(R.id.totalHoursCalculated);

        float d_pay_per_hour = 0,d_total_hours = 0;

        //try/catch to see if the Edit Text's is empty or not
        try {
            d_pay_per_hour = Float.valueOf(payInput.getText().toString());

            //split the String to get the hours in decimal only
            String[] Split = totalHours.getText().toString().split(" ");
            d_total_hours = Float.valueOf(Split[0]);
            HOURS = d_total_hours;
        }
        catch (IllegalArgumentException x){
            Log.i("payInput/totalHours","empty String");
        }

        //calculate the totalPay
        TOTALPAY = d_pay_per_hour * d_total_hours;
        final EditText incomeOutputTV = (EditText)findViewById(R.id.totalEarnedOutput);

        //convert the string to two decimal places and set the Text
        String s_totalPay = String.format("%.2f",TOTALPAY);
        incomeOutputTV.setText("$" + s_totalPay);
    
    }

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
                THREAD.interrupt();
                Intent intent = new Intent();
                intent.putExtra("totalPay",String.format("%.2f",TOTALPAY));
                intent.putExtra("totalHours",String.format("%.2f",HOURS));
                setResult(RESULT_OK,intent);
                finish();
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
