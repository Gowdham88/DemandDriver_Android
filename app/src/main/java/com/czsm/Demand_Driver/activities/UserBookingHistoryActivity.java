package com.czsm.Demand_Driver.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.czsm.Demand_Driver.PreferencesHelper;
import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.model.UserBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserBookingHistoryActivity extends AppCompatActivity {
    @BindView(R.id.user_booking_history_toolbar)
    Toolbar toolbar;

    @BindView(R.id.user_appointment_details_id_textview)
    TextView idTextView;

    @BindView(R.id.user_appointment_details_datetime_textview)
    TextView dateTimeTextView;

    @BindView(R.id.user_appointment_details_drname_textview)
    TextView drNameTextView;

    @BindView(R.id.user_appointment_details_address_textview)
    TextView addressTextView;

    @BindView(R.id.user_booking_history_name_textview)
    TextView providerTextView;

    @BindView(R.id.user_appointment_details_review_linearlayout)
    LinearLayout reviewLayout;

    @BindView(R.id.user_appointment_details_review_textview)
    TextView reviewTextView;

    private UserBooking booking;
    String UIAVALUE,drivername,appointmentid,date,driveraddress,review = "";

    @BindView(R.id.reviewlay)
    LinearLayout ReviewLinlay;
    FirebaseFirestore db;
    DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings_history);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(UserBookingHistoryActivity.this,UserHistoryActivity.class);
                startActivity(in);
            }
        });
        db= FirebaseFirestore.getInstance();
        UIAVALUE= PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        setTitle(R.string.app_details);

//        Rating=PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USERRATING);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            drivername    = extras.getString("name","");
            driveraddress = extras.getString("address","");
            date          = extras.getString("datatime","");
            appointmentid = extras.getString("id","");
            review        = extras.getString("userreview","");

        }

        try {

            idTextView.setText(appointmentid);
            dateTimeTextView.setText(date);
            drNameTextView.setText(drivername);
            addressTextView.setText(driveraddress);
            reviewTextView.setText(review);

//            int reviewstar = Integer.parseInt(review);
//                String reviewstr = "";
//                for (int i = 0; i < reviewstar; i++) {
//                    reviewstr += "*";
//                }


        } catch (NullPointerException e){

            e.printStackTrace();
        }
        ReviewLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });


    }

    public void showRatingDialog() {
        final View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_review_booking, null);

        new AlertDialog.Builder(this).setIcon(android.R.drawable.btn_star_big_on).setTitle("Review")
                .setView(dialogView)
                .setMessage("Appreciate giving feedback about this Appointment")
                .setCancelable(false)
                .setPositiveButton("Rate",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.dialog_review_booking_ratingbar);
                                String review = ratingBar.getProgress() + "";
                                Toast.makeText(UserBookingHistoryActivity.this, review, Toast.LENGTH_SHORT).show();
//                                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USERRATING,review);
                                reviewTextView.setText(review);
                                 documentReference = db.collection("User_details").document(UIAVALUE);
                                HashMap<String,Object> updatesvalues1=new HashMap<>();
                                updatesvalues1.put("Review",review);

                                documentReference.update(updatesvalues1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(UserBookingHistoryActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                    }
                                });

                                documentReference = db.collection("Current_booking").document(UIAVALUE);
                                HashMap<String,Object> updatesvalues=new HashMap<>();
                                updatesvalues.put("User_review",review);

                                documentReference.update(updatesvalues)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(UserBookingHistoryActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                    }
                                });

                                documentReference = db.collection("Completed_booking").document(UIAVALUE);
                                HashMap<String,Object> updatesvaluescomplete=new HashMap<>();
                                updatesvaluescomplete.put("User_review",review);

                                documentReference.update(updatesvaluescomplete)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(UserBookingHistoryActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                    }
                                });
//                                child.getRef().child("userreview").setValue(review);
                                dialog.dismiss();
//                                finish();
                            }
                        })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//                                child.getRef().child("userreview").setValue("0");
//                                finish();
                                dialog.cancel();
                            }
                        }).setCancelable(false).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;

        } else
            return super.onOptionsItemSelected(item);
    }
}