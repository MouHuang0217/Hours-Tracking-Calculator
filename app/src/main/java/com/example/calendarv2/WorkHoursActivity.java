package com.example.calendarv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class WorkHoursActivity extends AppCompatActivity implements EditText.OnClickListener  {

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

        final EditText payInput = (EditText)findViewById(R.id.payPerHourInput);
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
        switch(view.getId()){
            case R.id.startTimeInput:
                InputTV = (EditText) findViewById(R.id.startTimeInput);
                OtherTV = (EditText) findViewById(R.id.endTimeInput);
                break;
            case R.id.endTimeInput:
                InputTV = (EditText) findViewById(R.id.endTimeInput);
                OtherTV = (EditText) findViewById(R.id.startTimeInput);
                break;
            default:
                throw new RuntimeException("Unknown EditText ID");
        }

        EditText TotalOutput = (EditText) findViewById(R.id.totalEarnedOutput);
        EditText payPerHourInput = (EditText) findViewById(R.id.payPerHourInput);
        final EditText totalTimeTV = (EditText) findViewById(R.id.totalHoursCalculated);
        DialogFragment timePicker = new TimePickerFragment(InputTV,OtherTV,totalTimeTV,TotalOutput);
        timePicker.show(getSupportFragmentManager(),"time picker");
    }

}
