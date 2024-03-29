package com.example.dailyworkscheduler;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity {
    DatabaseHelper mydb1;
    EditText timefrom,timeto;
    Button startbutton;
    Button notifybutton;



    public static final String TAG ="activity2tag";
    public String mNotificationChannelId = null;



    protected void onCreate(Bundle savedInstanceState) {

        mydb1 = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        timefrom = (EditText) findViewById(R.id.timefrom);
        timeto = (EditText) findViewById(R.id.timeto);
        startbutton = (Button) findViewById(R.id.login);
        notifybutton = (Button) findViewById(R.id.notify);


        addData();
        notifybutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    //here i tried to store the query value in a string str
                    public void onClick(View v) {
                        Cursor res=mydb1.retrievework();
                        if(res.getCount()==0) {
                            Log.i(TAG,"no data");
                            return;
                        }
                        StringBuffer str=new StringBuffer();
                        while(res.moveToNext()) {
                            str.append(res.getString(0));
                            Log.i(TAG,"String value retrieved from database");

                        }
                        res.close();
                        Cursor res1=mydb1.retrievetimefrom();
                        if(res1.getCount()==0) {
                            Log.i(TAG,"no data");
                            return;
                        }
                        StringBuffer str1=new StringBuffer();
                        while(res1.moveToNext()) {
                            str1.append(res1.getString(0));
                            Log.i(TAG,"String value retrieved from database");

                        }
                        res1.close();
                       /* Cursor timefrom=mydb.retrievework();
                        if(timefrom.getCount()==0) {
                            Log.i("time from","no data");
                            return;
                        }
                        StringBuffer time=new StringBuffer();
                        while(timefrom.moveToNext()) {
                            str.append(timefrom.getString(1));

                        }*/
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                            // Configure the notification channel.
                            notificationChannel.setDescription("Channel description");
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.WHITE);
                            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                            notificationChannel.enableVibration(true);
                            notificationManager.createNotificationChannel(notificationChannel);
                        }


                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Activity2.this, "NOTIFICATION_CHANNEL_ID");

                        notificationBuilder.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setLights(Color.GREEN, 3000, 3000)
                                .setSmallIcon(R.drawable.ic_menu_add)
                                .setTicker("Hearty365")
                                .setPriority(NotificationManager.IMPORTANCE_MAX)
                                .setContentTitle("work is scheduled")
                                .setContentText("Hi dhivya,do the work: "+str+" from "+str1)
                                .setChannelId(NOTIFICATION_CHANNEL_ID)
                                .setOngoing(true)
                                .setAutoCancel(true)
                                .setContentInfo("Info");

                        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
                        Log.i("notification",     "notification is called" );
                    }
                }
        );


    }


    public void addData(){
        startbutton.setOnClickListener(

                new View.OnClickListener(){
                    public void onClick(View v){

                        boolean isInserted=mydb1.create_time_hours(timefrom.getText().toString(),
                                timeto.getText().toString());
                        if(isInserted==true)
                            Toast.makeText(Activity2.this,"data inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Activity2.this,"data not inserted",Toast.LENGTH_LONG).show();



                        //dataInserted();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



