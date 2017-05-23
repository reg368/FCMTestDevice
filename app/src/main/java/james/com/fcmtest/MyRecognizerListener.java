package james.com.fcmtest;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.List;

/**
 * Created by A4037 on 2017/5/23.
 */
public class MyRecognizerListener implements RecognitionListener {

    @Override
    public void onResults(Bundle results) {
        List<String> resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuffer sb = new StringBuffer();
        for(String res : resList) {
            sb.append(res + "\n");
        }

        Log.d("RECOGNIZER", "onResults: " + sb.toString());
    }

    @Override
    public void onError(int error) {
        Log.d("RECOGNIZER", "Error Code: " + error);
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
