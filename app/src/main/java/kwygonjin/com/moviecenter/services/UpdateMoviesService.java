package kwygonjin.com.moviecenter.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import kwygonjin.com.moviecenter.MainActivity;
import kwygonjin.com.moviecenter.R;
import kwygonjin.com.moviecenter.adapters.MyViewAdapter;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;

public class UpdateMoviesService extends Service {
    NotificationManager nm;
    private static final int NOTIFY_ID = 101;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doUpdate();
        sendNotif();

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean doUpdate() {
        if (MovieHTTPRequest.isInternetConnection(getApplicationContext()) && !MainActivity.showOnlyFavorite){
            MovieHTTPRequest.doRequest(getApplicationContext(), 1, MyViewAdapter.getInstance(getApplicationContext()));
            //Toast.makeText(getApplicationContext(), "Ohh my godness!", Toast.LENGTH_LONG).show();
            return true;
            }
        else return false;
    }

    private void sendNotif() {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.notification_msg))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_msg));

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
