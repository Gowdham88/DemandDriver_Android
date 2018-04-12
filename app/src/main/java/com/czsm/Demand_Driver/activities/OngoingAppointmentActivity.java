package com.czsm.Demand_Driver.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.czsm.Demand_Driver.Firebasemodel.AppointmentList;
import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.adapters.UserHistoryAdapter;
import com.czsm.Demand_Driver.adapters.UserOngoingAdapter;
import com.czsm.Demand_Driver.helper.RESTClient;
import com.czsm.Demand_Driver.helper.Util;
import com.czsm.Demand_Driver.model.Datauser;
import com.czsm.Demand_Driver.receiver.NotificationBroadcastReceiver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 02/08/16.
 */
public class OngoingAppointmentActivity extends AppCompatActivity {


    private SwipeRefreshLayout swipeContainer;

    private UserOngoingAdapter adapter;
    private ListView listView;
    private NotificationBroadcastReceiver mReceiver;

    AppointmentList appointment;
    ArrayList<AppointmentList> Bookinglist = new ArrayList<AppointmentList>();
    ArrayList<String> primaryidlist = new ArrayList<String>();


    DatabaseReference db;
    SharedPreferences sharedPreferences;
    String userid;
    RecyclerView recyclerView;
    List<Datauser> datalist = new ArrayList<Datauser>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ongoing_appointments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Appointment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(OngoingAppointmentActivity.this,DashBoardActivity.class);
                startActivity(in);
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_ongoing_appointments_listview);

        dataload();
        adapter = new UserOngoingAdapter(OngoingAppointmentActivity.this, datalist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(OngoingAppointmentActivity.this, LinearLayoutManager.VERTICAL, false));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Appointment();

            }
        });

    }

    private void Appointment() {
        dataload();
        swipeContainer.setRefreshing(false);
    }

    private void dataload() {
        datalist.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query first = db.collection("UsersCurrentBooking");

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.getDocuments().size() < 1) {

                            return;

                        }

                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            Datauser data = document.toObject(Datauser.class);
                            datalist.add(data);

                        }
                        adapter.notifyDataSetChanged();
                    }


                });


    }
}


