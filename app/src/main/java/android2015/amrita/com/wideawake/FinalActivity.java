package android2015.amrita.com.wideawake;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class FinalActivity extends ActionBarActivity {

    Firebase ref;
    String balanceScore;
    String slurScore;
    String reflexScore;

    Float baselineBalance;
    Double baselineSlur;
    int baselineReflex;

    float bscore;
    Double sScore;
    int rScore;
  TextView verdict;
    TextView result;
    Button uber;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://fiery-heat-6348.firebaseio.com");
        uber = (Button)this.findViewById(R.id.Uber);
        uber.setEnabled(false);
        image = (ImageView)this.findViewById(R.id.imageView);

        verdict = (TextView)this.findViewById(R.id.textView4);;
        result = (TextView)this.findViewById(R.id.result);





        ref.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                balanceScore = dataSnapshot.getValue().toString();
                bscore = Float.parseFloat(balanceScore);
              //  t2.append(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ref.child("reflexScore").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reflexScore = dataSnapshot.getValue().toString();
                rScore = Integer.parseInt(reflexScore);
              // t3.append(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ref.child("slur").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                slurScore = dataSnapshot.getValue().toString();
                sScore = Double.parseDouble(slurScore);
              //  t4.append(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void seeBaseline(View v) {
        ref.child("message").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());



                if (snapshot.getValue().toString().equals("baseline")) {


                    try {
                        File myFile = new File("/sdcard/mysdfile.txt");
                        baselineBalance = Float.parseFloat(balanceScore);
                        baselineReflex = Integer.parseInt(reflexScore);
                        baselineSlur = Double.parseDouble(slurScore);
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.append(balanceScore + "\n" + slurScore + "\n" + reflexScore);
                        myOutWriter.close();
                        fOut.close();
                        Toast.makeText(getBaseContext(),
                                "Baseline saved",
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    verdict.setVisibility(View.VISIBLE);
                    result.setVisibility(View.VISIBLE);
                    uber.setVisibility(View.VISIBLE);

                    File sdcard = Environment.getExternalStorageDirectory();

                    File file = new File(sdcard, "mysdfile.txt");

                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                    } catch (IOException e) {
                        //You'll need to add proper error handling here
                    }


                  //  TextView tv = (TextView) findViewById(R.id.textView3);

                  //  tv.setText(text);
                    System.out.println(text);
                    String text2 = text.toString();
                    String [] strings = text2.split("\n");

                    int strike = 0;

                    if (bscore > Float.parseFloat(strings[0])) {

                        float diff = bscore - Float.parseFloat(strings[0]);
                        float temp = diff/bscore;
                        float percent = temp * 100;

                        int iPercent = (int)percent;
                        verdict.setText("Your balance is off by " + iPercent + "%");
                        strike++;

                    }
                    if (sScore < Double.parseDouble(strings[1])) {


                        double diff = Double.parseDouble(strings[1]) - sScore;
                        double temp = diff/sScore;
                        double percent = temp * 100;

                        int iPercent = (int)percent;
                        verdict.append("\nYou are slurring by " + iPercent + "%");


                        System.out.println("Youre slurring too much");
                        strike++;

                    }

                    if (rScore > Integer.parseInt(strings[2])) {
                        int diff = rScore - Integer.parseInt(strings[2]);
                       double temp = diff/rScore;
                        double percent = temp * 100.0;

                        int iPercent = (int)percent;

                        verdict.append("\nYour reflexes have worsened by " + iPercent + "%");

                        strike++;
                    }

                    if (strike >= 2) {
                        result.setText("You cannot drive");
                        image.setImageResource(R.drawable.stop_sign);

                        uber.setEnabled(true);

                    } else {
                        result.setText("You can drive!");
                        image.setImageResource(R.drawable.go_icon);

                        uber.setEnabled(false);
                    }



                }





                }



            @Override public void onCancelled(FirebaseError error) { }

        });

    }
    public void clicked(View v) {
        PackageManager pm = this.getApplicationContext().getPackageManager();
        try
        {
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            System.out.println("Uber is installed");
            // Do something awesome - the app is installed! Launch App.
        }
        catch (PackageManager.NameNotFoundException e)
        {
            System.out.println("Uber is not installed");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
