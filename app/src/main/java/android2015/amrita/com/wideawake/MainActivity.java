package android2015.amrita.com.wideawake;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.firebase.client.Firebase;


@JsonSerialize
public class MainActivity extends Activity {

    private String name;
    private String testMode;
    private long testDate;
    private EditText enteredName;
    private CheckBox box;
    Firebase ref;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://fiery-heat-6348.firebaseio.com");

        setContentView(R.layout.activity_main);
        testDate = System.currentTimeMillis();
        box = (CheckBox)this.findViewById(R.id.checkBox);
        testMode = "test";



        this.gotoCaptcha();
        this.gotoVoice();
        this.gotoBalance();
        // this.transfer();
        //this.read();

        checkResults();

    }

    private void checkResults() {
        Button results = (Button)this.findViewById(R.id.results);
        final Intent intent = new Intent(MainActivity.this, FinalActivity.class);
        results.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);

                    }
                }
        );
    }

    public void itemChecked(View v) {
        testMode = "test";
        CheckBox box = (CheckBox)v;
        if (box.isChecked()) {

            testMode = "baseline";
        } else {
            testMode = "test";
        }
        System.out.println("testmode is " + testMode);

        ref.child("message").setValue(testMode);



    }



    private void gotoVoice() {
        Button voicebutton = (Button)this.findViewById(R.id.mic_button);
        final Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
        voicebutton.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View v) {
                        intent.putExtra("testMode", testMode);

                        startActivity(intent);

                    }
                });
    }

    private void gotoBalance() {
        Button balancebutton = (Button)this.findViewById(R.id.balance_button);
        final Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
        balancebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putExtra("testMode", testMode);
                startActivity(intent);
            }
        });
    }


    private void gotoCaptcha() {
        Button captchaButton = (Button)this.findViewById(R.id.human_brain_icon);
        final Intent intent = new Intent(MainActivity.this, ReflexActivity.class);
        captchaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putExtra("testMode", testMode);
                startActivity(intent);
            }
        });

    }










}