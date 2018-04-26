package com.czsm.Demand_Driver.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czsm.Demand_Driver.PreferencesHelper;
import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.Service.CapPhoto;
import com.czsm.Demand_Driver.controller.AllinAllController;
import com.czsm.Demand_Driver.helper.RESTClient;
import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;

/**
 * Created by macbook on 02/08/16.
 */
public class UserSupportActivity extends AppCompatActivity  implements RESTClient.ServiceResponseInterface {

    private Button signoutButton;
    private SharedPreferences allinallSharedPref;
    private AllinAllController allinAllController;
    private FirebaseAuth mAuth;
    ImageView phoneimage,Emailimg,Webimg;
    TextView PhoneTxt,EmailTxt,WebTxt;
    LinearLayout PhoneLinLay,EmailLinLay,WebLinLay;
    String contact="+919941123110";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_support);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Support");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent in=new Intent(UserSupportActivity.this,DashBoardActivity.class);
//                startActivity(in);
            }
        });
        phoneimage=(ImageView)findViewById(R.id.img_call);
        PhoneTxt=(TextView)findViewById(R.id.fragment_support_phone_textview);
        PhoneLinLay=(LinearLayout)findViewById(R.id.phonelay);

        Emailimg=(ImageView)findViewById(R.id.img_mail);
        EmailTxt=(TextView)findViewById(R.id.fragment_support_mail_textview);
        EmailLinLay=(LinearLayout)findViewById(R.id.emaillay);

        Webimg=(ImageView)findViewById(R.id.Img_web);
        WebTxt=(TextView)findViewById(R.id.fragment_support_website_textview);
        WebLinLay=(LinearLayout)findViewById(R.id.weblay);

        phoneimage.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                call.setData(Uri.parse("tel:"+ contact));
                startActivity(call);
            }
        });
        PhoneTxt.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                call.setData(Uri.parse("tel:"+ contact));
                startActivity(call);
            }
        });
        PhoneLinLay.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                call.setData(Uri.parse("tel:"+ contact));
                startActivity(call);
            }
        });

        Emailimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gowdhaman@czsm.co.in", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
        EmailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gowdhaman@czsm.co.in", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
        EmailLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gowdhaman@czsm.co.in", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });


        Webimg.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                Uri uri = Uri.parse("http://www.czsm.co.in/");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        WebTxt.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                Uri uri = Uri.parse("http://www.czsm.co.in/");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        WebLinLay.setOnClickListener(new View.OnClickListener() {
            Intent call = new Intent(Intent.ACTION_DIAL);
            @Override
            public void onClick(View v){
                Uri uri = Uri.parse("http://www.czsm.co.in/");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


//        allinAllController = new AllinAllController(UserSupportActivity.this, this);
//        allinallSharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        signoutButton      = (Button) findViewById(R.id.fragment_user_support_signout_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });


    }



    private void showConfirmDialog() {
        new android.support.v7.app.AlertDialog.Builder(UserSupportActivity.this)
                //set message, title, and icon
                .setTitle("Sign out")
                .setMessage("Do you want to sign out?")
                .setIcon(R.drawable.logo01)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        allinAllController.userSignOut(RESTClient.ID);
//                        SharedPreferences.Editor editor = PreferencesHelper.edit();
//                        editor.putBoolean("firstLaunch", true);
//                        editor.remove("userId");
//                        editor.apply();
//                        Intent service = new Intent(getApplicationContext(), CapPhoto.class);
//                        stopService(service);
//                        FirebaseAuth.getInstance().signOut();
                        PreferencesHelper.signOut(UserSupportActivity.this);
                        mAuth.signOut();
//                        RESTClient.ID = null;
                        Intent loginIntent = new Intent(getApplicationContext(), LoginScreenActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void sendServiceResult(String serviceResult) {

    }

    @Override
    public void requestFailed() {

    }
}
