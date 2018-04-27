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
import com.czsm.Demand_Driver.model.User_Details;
import com.czsm.Demand_Driver.model.User_completeDetails;
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
    List<User_completeDetails> datalist = new ArrayList<User_completeDetails>();
//    List<User_completeDetails> datalistreview = new ArrayList<User_completeDetails>();
    SharedPreferences preferences;
    ArrayList<AppointmentList> Bookinglist = new ArrayList<AppointmentList>();
    String id;
    UserHistoryAdapter userHistoryAdapter ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_history);
//        allinAllController = new AllinAllController(UserHistoryActivity.this, this);

        recyclerview       = (RecyclerView)findViewById(R.id.fragment_user_history_listview);

        db = FirebaseFirestore.getInstance();

//        preferences       = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        id                = preferences.getString("userId","");
//        TextView caltx=(TextView)findViewById(R.id.calltxt);
//        caltx.setOnClickListener(new View.OnClickListener() {
//            Intent call = new Intent(Intent.ACTION_DIAL);
//            @Override
//            public void onClick(View v){
//                call.setData(Uri.parse("tel:"+ contact));
//                startActivity(call);
//            }
//        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
//                Intent in=new Intent(UserHistoryActivity.this,DashBoardActivity.class);
//                startActivity(in);
            }
        });


        swipeContainer  = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(
                R.color.colorPrimary);
//        listView        = (ListView) findViewById(R.id.fragment_user_history_listview);
        dataload();
        userHistoryAdapter =new UserHistoryAdapter(UserHistoryActivity.this,datalist);
        recyclerview.setAdapter(userHistoryAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(UserHistoryActivity.this, LinearLayoutManager.VERTICAL, false));


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Appointment();
//                userHistoryAdapter =new UserHistoryAdapter(UserHistoryActivity.this,datalist);
//                recyclerview.setAdapter(userHistoryAdapter);
//                recyclerview.setLayoutManager(new LinearLayoutManager(UserHistoryActivity.this, LinearLayoutManager.VERTICAL, false));

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

        Query first = db.collection("Completed_booking").orderBy("End_time", Query.Direction.DESCENDING);

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.getDocuments().size() < 1) {

                            return;

                        }

                        for(DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            User_completeDetails data = document.toObject(User_completeDetails.class);
                            datalist.add(data);

                        }
                        userHistoryAdapter.notifyDataSetChanged();
                    }



                });


//        Query first1 = db.collection("Completed_booking").orderBy("End_time", Query.Direction.DESCENDING);
//
//        first1.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//
//                        if (documentSnapshots.getDocuments().size() < 1) {
//
//                            return;
//
//                        }
//
//                        for(DocumentSnapshot document : documentSnapshots.getDocuments()) {
//
//                            User_completeDetails data = document.toObject(User_completeDetails.class);
//                            datalistreview.add(data);
//
//                        }
//                        userHistoryAdapter.notifyDataSetChanged();
//                    }
//
//
//
//                });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
    }

}
