package com.czsm.Demand_Driver.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.czsm.Demand_Driver.controller.AllinAllController;
import com.czsm.Demand_Driver.helper.RESTClient;
import com.czsm.Demand_Driver.helper.Util;
import com.czsm.Demand_Driver.model.Data;
import com.czsm.Demand_Driver.model.Datauser;
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
public class UserHistoryActivity extends AppCompatActivity{

    private SwipeRefreshLayout swipeContainer;
    private AllinAllController allinAllController;

    private ListView listView;
    RecyclerView recyclerview;
    FirebaseFirestore db;
    List<Datauser> datalist = new ArrayList<Datauser>();
    SharedPreferences preferences;
    ArrayList<AppointmentList> Bookinglist = new ArrayList<AppointmentList>();
    String id;
    UserHistoryAdapter userHistoryAdapter ;
    String contact="1234567890";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_history);
//        allinAllController = new AllinAllController(UserHistoryActivity.this, this);

        recyclerview       = (RecyclerView)findViewById(R.id.fragment_user_history_listview);

        db = FirebaseFirestore.getInstance();

//        preferences       = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        id                = preferences.getString("userId","");
        TextView caltx=(TextView)findViewById(R.id.calltxt);
        caltx.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                call.setData(Uri.parse("tel:"+ contact));
                startActivity(call);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(UserHistoryActivity.this,DashBoardActivity.class);
                startActivity(in);
            }
        });


        swipeContainer  = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        listView        = (ListView) findViewById(R.id.fragment_user_history_listview);
        dataload();
        userHistoryAdapter =new UserHistoryAdapter(UserHistoryActivity.this,datalist);
        recyclerview.setAdapter(userHistoryAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(UserHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent bookingIntent = new Intent(getApplicationContext(), UserBookingHistoryActivity.class);
//                Bundle bookingBundle = new Bundle();
//                bookingBundle.putString("drivername",    Bookinglist.get(position).getDrivername());
//                bookingBundle.putString("id",            primaryidlist.get(position));
//                bookingBundle.putString("driveraddress", Bookinglist.get(position).getDriveraddress());
//                bookingBundle.putString("date",          Bookinglist.get(position).getDate()+" "+Bookinglist.get(position).getTime());
//                bookingBundle.putString("review",        Bookinglist.get(position).getUserreview());
//                bookingIntent.putExtras(bookingBundle);
//                startActivity(bookingIntent);
//
//            }
//        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Appointment();
//                userHistoryAdapter =new UserHistoryAdapter(UserHistoryActivity.this,datalist);
//                recyclerview.setAdapter(userHistoryAdapter);
//                recyclerview.setLayoutManager(new LinearLayoutManager(UserHistoryActivity.this, LinearLayoutManager.VERTICAL, false));

            }
        });



//        TextView emptyView = Util.getEmptyView(R.string.no_history, getApplicationContext());
//        ((ViewGroup) listView.getParent().getParent()).addView(emptyView);
//        listView.setEmptyView(emptyView);
//
//            Appointment();
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

                        for(DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            Datauser data = document.toObject(Datauser.class);
                            datalist.add(data);

                        }
                        userHistoryAdapter.notifyDataSetChanged();
                    }



                });


//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adapter != null)
//            adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void sendServiceResult(String serviceResult) {
//
//
//    }

//    public void Appointment(){
//
//        ValueEventListener appointmentlistner = new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//
//                    AppointmentList appointment  = child.getValue(AppointmentList.class);
//
//                    if(appointment.getStatus().equals("Completed")) {
//
//                        Bookinglist.clear();
//                        primaryidlist.clear();
//                        Bookinglist.add(appointment);
//                        primaryidlist.add(child.getKey());
//
//                        if (adapter == null) {
//
//                            adapter = new UserHistoryListAdapter(getApplicationContext(), R.layout.list_item_user_history);
//                            listView.setAdapter(adapter);
//
//                        } else {
//
//                            adapter.notifyDataSetChanged();
//                            swipeContainer.setRefreshing(false);
//                        }
//
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                Log.e("loadPost:onCancelled", databaseError.toException().toString());
//            }
//        };
//
//        db.child("AppointmentList").orderByChild("userid").equalTo(id).addValueEventListener(appointmentlistner);
//
//
//
//    }




//    @Override
//    public void requestFailed() {
//        Util.requestFailed(getApplicationContext());
//    }
//
//    private class UserHistoryListAdapter extends ArrayAdapter<AppointmentList> {
//        public View mView;
//        public TextView userNameTextview;
//        public TextView dateTimeTextview;
//        public TextView serviceTextview;
//        public ImageView providerImageview;
//        public Context context;
//        public int resource;
//
//
//        public UserHistoryListAdapter(Context context, int resource) {
//            super(context, resource, Bookinglist);
//            this.context = context;
//            this.resource = resource;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(
//                        Context.LAYOUT_INFLATER_SERVICE);
//                convertView = viewInflater.inflate(resource, null);
//            }
//            mView = convertView;
//            AppointmentList booking = Bookinglist.get(position);
//            userNameTextview  = (TextView) convertView.findViewById(R.id.list_item_booking_user_name_textview);
//            dateTimeTextview  = (TextView) convertView.findViewById(R.id.list_item_booking_datetime_textview);
//            serviceTextview   = (TextView) convertView.findViewById(R.id.list_item_booking_service_textview);
//            providerImageview = (ImageView) convertView.findViewById(R.id.list_item_booking_user_imagview);
//
//            userNameTextview.setText(booking.drivername);
//            dateTimeTextview.setText(booking.getDate()+" "+booking.getTime());
//            serviceTextview.setText(booking.getDriveraddress());
//
//            if(booking.getDriverimage() != null)
//                Picasso.with(context).load(booking.getDriverimage()).into(providerImageview);
//
//            return convertView;
//        }
    }

}
