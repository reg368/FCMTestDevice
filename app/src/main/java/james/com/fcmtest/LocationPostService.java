package james.com.fcmtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by A4037 on 2017/5/24.
 */
public class LocationPostService extends Service {

    private GetUserLocation location;
    private static final String TAG = "LocationPostService";
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG,"LocationPostService Service running start");
                if(location != null && location.getMyLatLng() != null ) {
                        Log.d(TAG," Latitude 緯度 : "+location.getMyLatLng().latitude+" : " +
                                "  經度 longitude :  "+location.getMyLatLng().longitude);
                }
                Log.d(TAG,"LocationPostService Service running end");
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,3000,5000);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        location = new GetUserLocation(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null)
            timer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
