package kwygonjin.com.moviecenter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kwygonjin.com.moviecenter.services.UpdateMoviesService;

/**
 * Created by KwygonJin on 05.01.2016.
 */
public class AutoStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, UpdateMoviesService.class));
    }
}
