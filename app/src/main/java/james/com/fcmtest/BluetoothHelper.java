package james.com.fcmtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.util.List;


/**
 * Created by A4037 on 2017/6/9.
 */
public class BluetoothHelper {

    private FragmentActivity activity;
    private BluetoothLeScanner mLeScanner;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private int REQUEST_ENABLE_BT = 1;

    private boolean mScanning;
    private Handler mHandler;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;


    public BluetoothHelper(FragmentActivity activity){
        this.activity = activity;
    }

    public boolean isBLEavailable(){
        if (!this.activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this.activity, "裝置不支援BLE", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            bluetoothManager =
                    (BluetoothManager) this.activity.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return false;
            }else{
                //有藍芽可以用 初始化 blue scan call back
                this.mLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                //有藍芽可以用 建立處理scan 藍芽的handler
                mHandler=new Handler();
                Toast.makeText(this.activity, "開始掃描BLE", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    }

    public void scanLeDevice() {
        if (isBLEavailable()) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mLeScanner.stopScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mLeScanner.startScan(mLeScanCallback);
        } else {
            mScanning = false;
            mLeScanner.stopScan(mLeScanCallback);
        }
    }

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

}
