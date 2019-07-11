package com.example.calendarv2.Decorators;

import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private final String total_pay;
    private int color;
    private CalendarDay date;

    public EventDecorator(int color, CalendarDay date,String total_pay) {
        Log.i("EVENTDEC","created");
        this.color = color;
        this.date = date;
        this.total_pay = total_pay;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public CalendarDay getDate() {
        return date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
    }
}
