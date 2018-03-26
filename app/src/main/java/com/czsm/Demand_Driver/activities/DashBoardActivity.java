package com.czsm.Demand_Driver.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.helper.RESTClient;
import com.czsm.Demand_Driver.receiver.NotificationBroadcastReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by macbook on 02/08/16.
 */
public class DashBoardActivity extends AppCompatActivity {


    ImageView Img_map,Img_support,Img_ongoing,Img_history;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SharedPreferences sharedPreferences;
    private NotificationBroadcastReceiver mReceiver;
    private static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        Bundle extras     = getIntent().getExtras();
        String fromPush   = null;
        String message    = null;

//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//
//                    displayFirebaseRegId();
//
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
////                    txtMessage.setText(message);
//                }
//            }
//        };
//
//        displayFirebaseRegId();
        
        if (extras != null) {

            fromPush = extras.getString("FROM_PUSH");
        }

        if (fromPush != null) {

            RESTClient.ID = sharedPreferences.getString("userId","");

        }


        message = sharedPreferences.getString("user_message", "");

        if (!message.equalsIgnoreCase("")) {

            showRejectDialog(message);
            sharedPreferences.edit().remove("user_message").apply();

        }

        mReceiver = new NotificationBroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                Bundle extra = intent.getExtras();
                if (extra != null && extra.getString("user_message") != null) {
                    showRejectDialog(extra.getString("user_message"));
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(NotificationBroadcastReceiver.NOTIFICATION_RECEIVED));


        Img_map     = (ImageView) findViewById(R.id.Img_book_driver);
        Img_support = (ImageView) findViewById(R.id.Img_support);
        Img_ongoing = (ImageView) findViewById(R.id.Img_current_book);
        Img_history = (ImageView) findViewById(R.id.Img_history);

        Img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent map = new Intent(getApplicationContext(),MapActivity.class);
                startActivity(map);


            }
        });

        Img_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent support = new Intent(getApplicationContext(),UserSupportActivity.class);
                startActivity(support);


            }
        });

        Img_ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ongoing = new Intent(getApplicationContext(),OngoingAppointmentActivity.class);
                startActivity(ongoing);
            }
        });

        Img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent history = new Intent(getApplicationContext(),UserHistoryActivity.class);
                startActivity(history);
            }
        });
        if (ContextCompat.checkSelfPermission(DashBoardActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashBoardActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DashBoardActivity.this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);

                // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }

//    private void displayFirebaseRegId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        Log.e("Str", "Firebase reg id: " + regId);
//
//        if (!TextUtils.isEmpty(regId)){
//
//        }
////            txtRegId.setText("Firebase Reg Id: " + regId);
//        else{
//
//        }
////            txtRegId.setText("Firebase Reg Id is not received yet!");
//    }

    private void showRejectDialog(String userMessage) {
        new android.support.v7.app.AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Booking")
                .setMessage(userMessage)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        finish();

    }


//    private void showPendingBookingDialog(String message,final DataSnapshot child) {
//        new android.support.v7.app.AlertDialog.Builder(getContext())
//                //set message, title, and icon
//                .setTitle("New Booking: ")
//                .setMessage(message)
//                .setIcon(R.drawable.ic_launcher)
//                .setCancelable(false)
//                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                        if(!appointment.getStatus().equals("Confirmed")) {
//
//                            Map appointmentData = new HashMap();
//                            appointmentData.put("status", "Confirmed");
//                            appointmentData.put("providerid", userid);
//                            child.getRef().updateChildren(appointmentData);
//
//                            /*****************Updating driver********************************/
//
//                            ValueEventListener maplistner = new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//
//                                        child.getRef().child("status").setValue("onduty");
//
//                                    }
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                    Log.e("loadPost:onCancelled", databaseError.toException().toString());
//                                }
//                            };
//
//                            db.child("ServiceproviderList").orderByKey().equalTo(userid).addListenerForSingleValueEvent(maplistner);
//
//                        } else {
//
//                            Toast.makeText(getActivity(),"Booking has already taken",Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                })
//                .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
//    }

}
