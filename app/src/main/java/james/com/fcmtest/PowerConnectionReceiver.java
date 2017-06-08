package james.com.fcmtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by A4037 on 2017/6/8.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = "PowerConnectionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG," Receive battery broadcast ");

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int percent = (level*100)/scale;

        Log.d(TAG,"percent : "+percent);

        try{

            Log.d(TAG," start post battery url ");

            JSONObject json = new JSONObject();
            json.put("device_id",GetDeviceMacAddress.getMacAddress(context));
            json.put("bettary",percent);
            OKHttp http = new OKHttp();
            String result = http.post(context.getResources().getString(R.string.add_batteryUrl),json.toString());
            Log.d(TAG," register result : "+result);

        }catch(Exception e){
            Log.d(TAG," PowerConnectionReceiver post service exception "+e.getMessage());
            e.printStackTrace();
        }

    }
}
