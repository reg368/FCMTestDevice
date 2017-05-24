package james.com.fcmtest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by A4037 on 2017/5/23.
 */
public class MyRecognizerListener implements RecognitionListener {

    private static final String TAG = "MyRecognizerListener";
    private TextView info;
    protected long mSpeechRecognizerStartListeningTime = 0;
    private Map<String, String> phoneMap;
    private Context context;

    public MyRecognizerListener(Context context, Map<String, String> phoneMap,TextView info) {
        this.phoneMap = phoneMap;
        this.context = context;
        this.info = info;
    }

    @Override
    public void onResults(Bundle results) {

        //會回傳辨識出所有可能的文字集合
        List<String> resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


        if (resList != null && resList.size() > 0 && phoneMap != null) {

            info.setText("");

            for(String task : resList){
                Log.d(TAG,"result : "+ task);
                info.append(task+"\n");

                //使用者說出"打電話給...."關鍵字
                if (task.startsWith("打電話給") && task.length() > 4) {
                    String name = task.substring(4);
                    Log.d(TAG, "recognizer name : " + name);
                    info.append("recognizer name : " + name+"\n");

                    //取得使用者電話 from map (key contact user name ; value phone number)
                    String number = this.phoneMap.get(name);

                    if(number != null){

                        Log.d(TAG, "Get user :"+name+" , number : "+number);
                        info.append("Get user :"+name+" , number : "+number+"\n");

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "CALL PERMISSION_GRANTED FAIL");
                            info.append("CALL PERMISSION_GRANTED FAIL"+"\n");
                            break;
                        }else{
                            Log.d(TAG, "Start make phone calling user : "+name+",  number : "+number);
                            info.append("Start make phone calling user : "+name+",  number : "+number+"\n");
                            this.context.startActivity(intent);
                            Log.d(TAG, "phone call user "+name+" done , ending process");
                            info.append("phone call user "+name+" done , ending process"+"\n");
                            break;
                        }

                    }else{
                        Log.d(TAG, "Contact user "+name+" does not exist");
                        info.append("Contact user "+name+" does not exist"+"\n");
                        continue;
                    }
                }

            }


        }

    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "Error Code: " + error);
        info.append("Error Code: " + error+"\n");

        long duration = System.currentTimeMillis() - mSpeechRecognizerStartListeningTime;
        if (duration < 500 && error == SpeechRecognizer.ERROR_NO_MATCH) {
            Log.d(TAG, "Doesn't seem like the system tried to listen at all. duration = " + duration + "ms. This might be a bug with onError and startListening methods of SpeechRecognizer");
            Log.d(TAG, "Going to ignore the error");
            return;
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}
