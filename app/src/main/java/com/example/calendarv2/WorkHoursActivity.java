package com.example.calendarv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class WorkHoursActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle startEndTime = getIntent().getExtras();

        Intent passData_Intent = new Intent(this, WorkHoursActivity.class);

        EditText startTimeTV = (EditText) findViewById(R.id.startTimeInput);

        String startTime = "startTime:";
        passData_Intent.putExtra("startTime",startTime);

        String endTime = "endTime:";
        passData_Intent.putExtra("endTime",endTime);

        setResult(RESULT_OK,passData_Intent);

    }

    public void openTimePicker(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");
    }
}
