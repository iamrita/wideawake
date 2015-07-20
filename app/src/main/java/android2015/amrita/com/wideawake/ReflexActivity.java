package android2015.amrita.com.wideawake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;


public class ReflexActivity extends Activity {

    Animation animation;
    CountDownTimer countDownTimer;
    TextView report;
    private static String TAG = "Animation";
    private ArrayList<Integer> scores = new ArrayList<Integer>();
    private int nTimes = 0;
    private String name;
    private long testDate;
    private String testMode;
    private Button b;
    Firebase ref;
    private int score;


    private float finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex);
        ref = new Firebase("https://fiery-heat-6348.firebaseio.com");

        animation = (Animation) findViewById(R.id.testReflex);
        report = (TextView) findViewById(R.id.meter);
        b = (Button)findViewById(R.id.startBut);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReflexActivity.this);
        alertDialogBuilder.setNeutralButton("Press stop once the blue circle gets close to the red line.",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("cool");
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        alertDialog.getWindow().setLayout(1000, 350);





        countDownTimer = new CountDownTimer(10000, 25) {
            public void onTick(long toFinish) {
                long timeLeft = toFinish/1000;
                animation.update();
                animation.invalidate();
                //Log.v(TAG, "timer left = " + timeLeft);




            }
            public void onFinish() {
                Log.v(TAG, "Timer Done");



            }
        };
        //countDownTimer.start();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startAction(View v) {
        countDownTimer.start();
    }
    public void stopAction(View v) {
        countDownTimer.cancel();
        scores.add(animation.getScore());
        nTimes++;
        report.setText(" Score = " + animation.getScore());
        score = animation.getScore();



    }

    public void clicked(View v) {
        ref.child("reflexScore").setValue(score +"");

    }
    public void clearAction(View v) {
        nTimes=0;
        scores.clear();
        animation.reset();
        animation.invalidate();
        report.setText("Score : ");

    }



}
