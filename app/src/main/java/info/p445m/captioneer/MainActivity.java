package info.p445m.captioneer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.speech.*;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecognitionListener {
    public final int SPEECH_REQUEST_CODE=0;

    private class getV extends AsyncTask<String , Void,String> {
        @Override
        protected String doInBackground(String... params) {
            return "";

        }
        @Override
        protected void onPostExecute(String s) {
            TextView t = (TextView) findViewById (R.id.answer);
            t.setText(s);
            Log.w("captioneer", "on post execute");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        //setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                runSpeech();

            }
        });
        FloatingActionButton hitSend = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        hitSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "No network support", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                EditText answer = (EditText) findViewById(R.id.answer);
                sendToServer(answer.getText().toString());
            }
        });
        hitSend.setVisibility(View.INVISIBLE);
    }
    public void sendToServer(String message) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nick = sPrefs.getString("nick","");
        String serverPort = sPrefs.getString("server","");
        String []ray =serverPort.split(":");
        int port = Integer.parseInt(ray[1]);
        try {
            Socket s = new Socket(ray[0],port);
        } catch (IOException e) {

        }


    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runSpeech() {
        SpeechRecognizer speech = SpeechRecognizer.createSpeechRecognizer(this);
        if (SpeechRecognizer.isRecognitionAvailable(this) == false) {
            EditText answer = (EditText) findViewById(R.id.answer);
            answer.setText("no recognition available\n");

        } else {
            //speech.setRecognitionListener(this);
            Intent tent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            tent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(tent, 0);
            //speech.startListening(tent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EditText answer = (EditText) findViewById(R.id.answer);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> myList = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spoken = myList.get(0);
            answer.setText(spoken);

        } else {
            answer.setText(String.format("result: %d", resultCode));
        }
    }





    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }
}
