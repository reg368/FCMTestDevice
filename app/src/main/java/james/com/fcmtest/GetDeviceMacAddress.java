package james.com.fcmtest;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by A4037 on 2017/6/8.
 */
public class GetDeviceMacAddress {

    public static String getMacAddress(Context context){
        //取得MAC資訊
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

}
