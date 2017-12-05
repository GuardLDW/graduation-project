package com.bjut.cyl.kfyrip.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 15/8/27.
 */
public class CalendarUtil {
    private Context context;
    private static String calanderURL = "";
    private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";
    static{
        if(Integer.parseInt(Build.VERSION.SDK) >= 8){
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";

        }else{
            calanderURL = "content://calendar/calendars";
            calanderEventURL = "content://calendar/events";
            calanderRemiderURL = "content://calendar/reminders";
        }
    }

    public void writeToCalender(Context context ,String title){
        this.context = context;
        String calId = "";
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null,
                null, null, null);
        if(userCursor.getCount() > 0){
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));

        }
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("eventTimezone", Time.getCurrentTimezone());
        event.put("description", "通知");
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
//        mCalendar.set(Calendar.HOUR_OF_DAY ,17);
//        mCalendar.set(Calendar.MINUTE , 00);
        long start = mCalendar.getTime().getTime();
//                                mCalendar.set();
        long end = mCalendar.getTime().getTime();

        event.put("dtstart", start);
        event.put("dtend", end);
        event.put("hasAlarm",1);

        Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);
        long id = Long.parseLong( newEvent.getLastPathSegment() );
        ContentValues values = new ContentValues();
        values.put( "event_id", id );
        values.put( "minutes", 10 );
        values.put( "method", 1 );
        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        Toast.makeText(context, "设置提醒成功", Toast.LENGTH_LONG).show();
    }
}
