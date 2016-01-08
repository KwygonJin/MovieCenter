package kwygonjin.com.moviecenter.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by KwygonJin on 05.01.2016.
 */
public class ScheduleReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent autoStartService = new Intent(context, AutoStartReceiver.class);
        PendingIntent recurringAutoStartService = PendingIntent.getBroadcast(context,
                0, autoStartService, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + (1000 * 60 * 1), 1000 * 60 * 1,recurringAutoStartService);
    }
}
