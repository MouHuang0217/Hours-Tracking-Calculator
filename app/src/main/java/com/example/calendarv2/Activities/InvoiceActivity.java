package com.example.calendarv2.Activities;

/**
 * created on 7/16/2019
 * BY: Moulue Huang
 */

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calendarv2.R;

public class InvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        //set the title of the actionbar to Invoice
        getSupportActionBar().setTitle("Invoice");

        TextView dataTV = findViewById(R.id.dataTextView);

        //get all the data from the intent
        String all_data = getIntent().getStringExtra("all data");

        //Split the data so we can format it
        String[] separated_data = new String[0];
        try {
            separated_data = all_data.split("\n");
        }
        catch (NullPointerException e){ e.printStackTrace();}
        for (String i : separated_data) {
            if (!i.equals("")) {
                //split it even further
                String[] data = i.split("\t\t");

                //calculate the number of hours
                float total = Float.valueOf(data[2]);
                float hrs = Float.valueOf(data[1]);
                Log.i("total:", String.valueOf(total));
                Log.i("hrs:", String.valueOf(hrs));
                float pay_hours_num = Float.valueOf(data[2]) / Float.valueOf(data[1]);
                String formatted_pay_hour = String.format(" hrs\t\t\t\t$%.2f/hr", pay_hours_num);
                dataTV.append(data[0] + "\t\t" + data[1] + formatted_pay_hour
                        + "\t\t\t$" + data[2] + "\n");
            }

        }
        String total = getIntent().getStringExtra("total");
        //use the total and add it into the invoice
        dataTV.append(String.format("\n\nTotal without taxes: \t\t$%.2f", Float.valueOf(total)));
    }

    //close the procedure if the done button is pressed
    public void doneButtonProcedure(View view) {
        finish();
    }
}
