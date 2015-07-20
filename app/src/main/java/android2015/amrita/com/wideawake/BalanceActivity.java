package android2015.amrita.com.wideawake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.NumberFormat;
import java.util.ArrayList;


public class BalanceActivity extends Activity {
    Firebase ref;

    private DrawCompass myCompass;
    private DrawShaking myshakes;
    private static SensorManager sensorService;
    private Sensor sensor;
    private TextView tv;
    private TextView baselinetv;
    private TextView strike1;
    private ArrayList<Float> calibrations;
    private ArrayList<Float> diffs;
    private ArrayList<Float> diffsRoll;
    private ArrayList<Float> diffsPitch;
    private ArrayList<Float> baselineY;
    private ArrayList<Float> baselineZ;
    private ArrayList<Float> averageCalc;

    private ArrayList<Float> diff0;
    private ArrayList<Float> diff1;
    private ArrayList<Float> diff2;

    private float baseline;
    private float baselineR;
    private float baselineP;
    private Button check;
    private Button readData;
    private Button saveData;
    private EditText userinfo;
    private float reading;
    private float roll;
    private float pitch;

    private float avgReading;

    private TextView averageDisplay;
    private String name;
    private long testDate;
    private String testMode;
    private boolean walk=false;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Firebase.setAndroidContext(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BalanceActivity.this);
        alertDialogBuilder.setNeutralButton("Walk in a straight line, holding the phone steady.",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println("cool");
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        alertDialog.getWindow().setLayout(1000, 350);
        saveData = (Button)this.findViewById(R.id.save);
        ref = new Firebase("https://fiery-heat-6348.firebaseio.com");



        Intent previousIntent = this.getIntent();
        testMode = previousIntent.getStringExtra("testMode");
        System.out.println("balance test mode is " + testMode);

        myCompass = (DrawCompass)findViewById(R.id.compass);
        myshakes = (DrawShaking)findViewById(R.id.shaking);
        check = (Button)this.findViewById(R.id.testButton);


        tv = (TextView)findViewById(R.id.textView);
        calibrations = new ArrayList<Float>();
        diffs = new ArrayList<Float>();
        averageCalc = new ArrayList<Float>();
        baselineY = new ArrayList<Float>();
        baselineZ = new ArrayList<Float>();
        diffsRoll = new ArrayList<Float>();
        diffsPitch = new ArrayList<Float>();
        diff0 = new ArrayList<Float>();
        diff1 = new ArrayList<Float>();
        diff2 = new ArrayList<Float>();
        baseline = 0;
        //baselinetv = (TextView)findViewById(R.id.baselineText);
        //strike1 = (TextView)findViewById(R.id.strike1);
        // myCompass.invalidate();

        if (myCompass == null) {
            Log.v("mycompass object ", "is null");
        }

        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        startAction();
        stopAction();

        checkAction();
        saveAction();



    }

    public float getReading() {
        return reading;
    }

    public void setReading(float f, float r, float p) {
        reading = f;
        roll = r;
        pitch = p;

    }



    public void startAction() {
        Button start = (Button)this.findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sensor != null) {
                    sensorService.registerListener(mySensorEventListener, sensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    calibrations.clear();
                    baselineY.clear();
                    baselineZ.clear();
                    Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");
                } else {
                    Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
                    finish();
                }
            }
        });

    }



    public void stopAction() {
        Button stop = (Button)this.findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sensorService.unregisterListener(mySensorEventListener);
                float sum = 0;
                for (int i = 0; i < calibrations.size(); i++) {
                    sum = sum + calibrations.get(i);
                }
                baseline = calibrations.get(calibrations.size()-1);
                baselineR = baselineY.get(baselineY.size()-1);
                baselineP = baselineZ.get(baselineZ.size()-1);
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMinimumFractionDigits(2);
                formatter.setMaximumFractionDigits(2);
            }
        });

    }

    public void saveAction() {

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("saving");



                ref.child("balance").setValue(avgReading);
                //AlertData data = dbhelper.readData(name, testMode);
                //Log.v("BalanceActivity", data.toString());


            }
        });
    }





    public void checkAction() {


        check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sensorService.registerListener(mySensorEventListener, sensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
                walk = true;
                diffs.clear();
                diffsRoll.clear();
                diffsPitch.clear();
                averageCalc.clear();
                diff0.clear();
                diff1.clear();
                diff2.clear();

                new CountDownTimer(10000, 1000) {


                    public void onTick(long millisUntilFinished) {

                        Log.v("in the loop", "Yup");

                        diffs.add(reading-baseline);
                        diffsRoll.add(Math.abs(roll-baselineR));
                        diffsPitch.add(Math.abs(pitch-baselineP));
                        averageCalc.add(Math.abs(reading-baseline));
                        Log.v("here are: ", diffs.toString());


                    }

                    public void onFinish() {
                        sensorService.unregisterListener(mySensorEventListener);
                        walk=false;

                        myshakes.setPath(diffs);
                        float sum = 0;
                        for (int i = 0; i < averageCalc.size(); i++) {
                            sum = sum + averageCalc.get(i);
                        }
                        float sum2 = 0;
                        for (int i = 0; i < diffsRoll.size(); i++) {
                            sum2 = sum2 + diffsRoll.get(i);
                        }
                        float averageRoll = sum2/(diffsRoll.size());
                        float sum3 = 0;
                        for (int i = 0; i < diffsPitch.size(); i++) {
                            sum3 = sum3 + diffsPitch.get(i);
                        }
                        float averagePitch = sum3/(diffsPitch.size());
                        System.out.println("your average diff is " + sum/averageCalc.size() );
                        avgReading = sum/averageCalc.size();
                        //averageDisplay.setText("azimuth :   " + avgReading + " pitch :  " + averageRoll + " roll :  " + averagePitch);

                        float avgAz = 0;
                        for(int i=0; i<diff0.size(); i++) {
                            avgAz += diff0.get(i);
                        }
                        avgAz = avgAz/diff0.size();

                        float avgRoll = 0;
                        for(int i=0; i<diff1.size(); i++) {
                            avgRoll += diff1.get(i);
                        }
                        avgRoll = avgRoll/diff1.size();

                        float avgPitch = 0;
                        for(int i=0; i<diff2.size(); i++) {
                            avgPitch += diff2.get(i);
                        }
                        avgPitch = avgPitch/diff2.size();


                        NumberFormat formatter = NumberFormat.getNumberInstance();
                        formatter.setMinimumFractionDigits(2);
                        formatter.setMaximumFractionDigits(2);



                    }
                }.start();

            }
        });
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=West
            Log.v("getdirection", "onSensorChanged");
            float azimuth = event.values[0];
            float roll = event.values[1];
            float pitch = event.values[2];
            myCompass.updateData(azimuth);

            if(!walk) {
                calibrations.add(azimuth);
                baselineY.add(roll);
                baselineZ.add(pitch);
            } else {
                float d0 = Math.abs(baseline - azimuth);
                float d1 = Math.abs(baselineR - roll);
                float d2 = Math.abs(baselineP - pitch);
                diff0.add(d0);
                diff1.add(d1);
                diff2.add(d2);
            }
            setReading(azimuth, roll, pitch);
            tv.setText("Delta: " + azimuth);
            Log.v("azimuth value is " , azimuth +"");

        }
    };




}
