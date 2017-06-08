package james.com.fcmtest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by A4037 on 2017/5/24.
 */
public class LocationPostService extends Service {

    private GetUserLocation location;
    private static final String TAG = "LocationPostService";
    private Timer timer;
    private LocationPostService context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG,"LocationPostService Service running start");
                if(location != null && location.getMyLatLng() != null ) {

                    Log.d(TAG,"post lat : "+location.getMyLatLng().latitude+
                            " , long "+location.getMyLatLng().longitude+
                    "mac : "+GetDeviceMacAddress.getMacAddress(context));

                    try{
                        JSONObject json = new JSONObject();
                        json.put("device_id",GetDeviceMacAddress.getMacAddress(context));
                        json.put("lng",location.getMyLatLng().longitude);
                        json.put("lat",location.getMyLatLng().latitude);
                        OKHttp http = new OKHttp();
                        String result = http.post(getResources().getString(R.string.add_posUrl),json.toString());
                        Log.d(TAG," register result : "+result);
                    }catch(Exception e){
                        Log.d(TAG," post exception "+e.getMessage());
                        e.printStackTrace();
                    }
                }
                Log.d(TAG,"LocationPostService Service running end");
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,10000,10000);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        context = this;
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
