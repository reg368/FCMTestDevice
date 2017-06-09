package james.com.fcmtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    static final int check = 111;
    private SpeechRecognizer recognizer;
    private BluetoothHelper bluetoothHelper;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.info);

        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));
        //初始化lotcation
        GetUserLocation.getMylocation(this);

        //初始化藍芽物件
        bluetoothHelper = new BluetoothHelper(this);

        //藍芽掃描
        Button scanbluetoote = (Button)findViewById(R.id.btnScanBlueToooth);
        scanbluetoote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        recognizer = SpeechRecognizer.createSpeechRecognizer(this);

        recognizer.setRecognitionListener(new MyRecognizerListener(this,getPhoneBookInfo(),this.info));

        //voice
        Button voice = (Button)findViewById(R.id.btnVoice);

        voice.setOnTouchListener(new View.OnTouchListener(){


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                info.setText("");

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((Button) v).setPressed(true);
                    //TODO: Add the code of your onClick-event here
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                    recognizer.startListening(intent);
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        //啟動LocationPostService背景服務
        Intent intent = new Intent(MainActivity.this, LocationPostService.class);
        startService(intent);

    }

    //voice dialog result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == check && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(String result : results){
                Log.d(TAG,"voice : "+result);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


     BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            if(token != null)
            {
                Log.d(TAG,"device token : "+token);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(recognizer !=  null) {
            recognizer.stopListening();
            recognizer.destroy();
        }
    }

    //取得手機電話簿(sim 卡)通訊綠 Map<聯絡人名稱,聯絡人電話>
    private Map<String,String> getPhoneBookInfo(){

        Map<String,String> phoneMap = new HashMap<String,String>();

        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()){

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
                Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                while (pCur.moveToNext())
                {
                    String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneMap.put(name,contactNumber);
                    break;
                }
                pCur.close();
            }
        }
        return  phoneMap;
    }

}
