package com.czsm.Demand_Driver.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.czsm.Demand_Driver.Firebasemodel.AppointmentList;
import com.czsm.Demand_Driver.PreferencesHelper;
import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.Retrofit.DistanceApi;
import com.czsm.Demand_Driver.Utils;
import com.czsm.Demand_Driver.helper.Util;
import com.czsm.Demand_Driver.model.Data;
import com.czsm.Demand_Driver.model.TimeModelClass;
import com.czsm.Demand_Driver.view.CustomDateTimePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static com.czsm.Demand_Driver.activities.BookServiceMapActivity.BaseUrl;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {
    Context context;
    private TrackGPS gps;
    double lat;
    double lng;
    double latitud, longitud,latitu,longitu;
    String address1;
    String vehicle;

    Location mLocation;
    SupportMapFragment mapFrag;
    GoogleMap Mmap;
    ImageButton book;

    LatLng laln;
    EditText changelocation;
    String newloc;

    GoogleMap mMap;
    LocationManager locationManager;
    ;
    private GoogleApiClient googleApiClient;
    android.support.v7.widget.Toolbar toolbar;
    TextView serviceTextView;
    ImageView serviceImageView;
    LinearLayout driverLayout;
    Spinner driverTypeSpinner;
    AutoCompleteTextView carTypeSpinner;
    LinearLayout tariffLayout;
    TextView bookNowTextview,LocTxt;
    TextView bookLaterTextview;
    Button bookButton;
    private static final int LOCATION_REQUEST_CODE = 101;
    GoogleMap googleMap;
    SupportMapFragment Map;
    CustomDateTimePicker bookLaterDateTimePicker;
    String serviceId = "1";
    String serviceName = "Driver";
    String bookDate, bookTime, bookNow;
    int markerIcon;
    String UIAVALUE,PHNO,Uid;
    String distance,time;
    List<Address> addresses;
    String lattitude,longitude,address,city,state,country,postalCode,knownName;
    private LatLng latlon1;
    int i = 1, maxproviders = 0;
    long timems;
     FirebaseFirestore db;
    DocumentReference documentReference;
    String  Strlat,Strlong,latvalue,longitude1,lattitude1,lattitud,longtude,refer,adds,StringDT;
    LatLng northbounds,southbounds;
    CharSequence text;
    Circle myCircle;
    String cartypeStr;
    Marker marker;
    private static final int PERMISSION_REQUEST_CODE = 200;
    View view;

//    double laat=12.9010;
//    double longg=80.2279;
    static double latarray[]={12.9010,12.9229,12.9760,13.0405};
    double longgarray[]={80.2279,80.1275,80.2212,80.2337};
    List<Data> datalist = new ArrayList<Data>();
    List<Data> datalist2 = new ArrayList<Data>();
    List<Data> datalist3 = new ArrayList<Data>();
    List<Data> datalist1 = new ArrayList<Data>();
    ArrayList<LatLng> list=new ArrayList();
    private Handler mHandler;
    String driverphonenumber;
    String Token, Rndmuid,addressval,TokenVal,Username;
    char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    Random rnd = new Random();

    int len= 8;
    String saltStr;

    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        private static final int RANDOM_STRING_LENGTH = 10;
    char ch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_service_details_toolbar);
        serviceTextView = (TextView) findViewById(R.id.service_details_service_textview);
        serviceImageView = (ImageView) findViewById(R.id.service_details_service_imageview);
        driverLayout = (LinearLayout) findViewById(R.id.service_details_call_driver_linearlayout);
        driverTypeSpinner = (Spinner) findViewById(R.id.service_details_driver_type_spinner);
        carTypeSpinner = (AutoCompleteTextView) findViewById(R.id.service_details_car_type_spinner);
        tariffLayout = (LinearLayout) findViewById(R.id.service_details_tariff_linearlayout);
        bookNowTextview = (TextView) findViewById(R.id.service_details_book_now_textview);
        bookLaterTextview = (TextView) findViewById(R.id.service_details_book_later_textview);
        bookButton = (Button) findViewById(R.id.service_details_book_button);
        LocTxt=(TextView)findViewById(R.id.location_txt);
        refer= FirebaseInstanceId.getInstance().getToken();
         UIAVALUE= PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
         Uid=PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        PHNO= PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PHONENUMBER);
        Username=PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USERNAME);
//        Token=PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_TOKEN);
          db= FirebaseFirestore.getInstance();
        toolbar.setTitle("Current Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
//               Intent in=new Intent(MapActivity.this,DashBoardActivity.class);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
//               startActivity(in);
            }
        });
        String uuid = UUID.randomUUID().toString();
        Rndmuid=uuid;
        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USERRANDMID,Rndmuid);
        TokenVal = PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_TOKEN);

//        POPup();
//        this.mHandler = new Handler();
//
//        this.mHandler.postDelayed(m_Runnable,5000);

//        addmap();
//        setMarkerIcon(serviceId);
        if (!serviceId.equalsIgnoreCase("1")) {
            driverLayout.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            driverLayout.setVisibility(View.INVISIBLE);
            carTypeSpinner.setVisibility(View.INVISIBLE);
            driverTypeSpinner.setVisibility(View.INVISIBLE);
        }


        serviceImageView.setImageResource(R.drawable.services_call_driver);
        serviceTextView.setText(serviceName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.car_type_array));
        carTypeSpinner.setThreshold(1);
        carTypeSpinner.setAdapter(adapter);
//        final String[] cartypeStr = {carTypeSpinner.getText().toString().trim()};
//        Toast.makeText(MapActivity.this, cartypeStr, Toast.LENGTH_SHORT).show();

        carTypeSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carTypeSpinner.showDropDown();
                Utils.hideKeyboard(MapActivity.this);
            }
        });
        tariffLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tariffIntent = new Intent(getApplicationContext(), TariffPlanActivity.class);
                tariffIntent.putExtra("serviceId", serviceId);
                startActivity(tariffIntent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });

//        com.google.firebase.firestore.Query driverfirst = db.collection("Driverone");
//
//        driverfirst.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                          @Override
//                                          public void onSuccess(QuerySnapshot documentSnapshots) {
//
//                                              if (documentSnapshots.getDocuments().size() < 1) {
//
//                                                  return;
//
//                                              }
//
//                                              for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
//
//                                                  Data data = document.toObject(Data.class);
//                                                  datalist.add(data);
//                                                   driverphonenumber= String.valueOf(datalist.get(0).getPhonenumber());
////                                                  String latt= String.valueOf(datalist.get(0).getLat());
////                                                  Toast.makeText(MapActivity.this, latt, Toast.LENGTH_SHORT).show();
//
////                                                  Log.e("datalist",datalist.get(0).getLat());
////                                hideProgressDialog();
//
//                                              }
////                            hideProgressDialog();
//
//
//                                          }
//                                      });


//

        bookNowTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (filterserviceproviders.size() > 0) {
                if (!serviceId.equalsIgnoreCase("1") || !carTypeSpinner.getText().toString().isEmpty()) {

                    Log.e("cartype",carTypeSpinner.getText().toString());
                    cartypeStr=carTypeSpinner.getText().toString().trim();
                     String drivertype=driverTypeSpinner.toString();
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                    SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss a");

                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                    stf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                    bookDate = sdf.format(now);
                    bookTime = stf.format(now);
                    bookNow = "1";
//                    Distance();
                    showConfirmDialog();

                } else
                    Toast.makeText(getApplicationContext(), "Must select car type and options", Toast.LENGTH_LONG).show();
//                }
//                else
//                    Toast.makeText(getApplicationContext(), "We are sorry, there is no service providers for this service at your region.", Toast.LENGTH_LONG).show();
            }
        });

//        String knownNamed= addresses.get(0).getFeatureName();
        bookLaterDateTimePicker = getCustomDateTimePicker(bookLaterTextview);
        bookLaterDateTimePicker.set24HourFormat(false);
        bookLaterDateTimePicker.setDate(Calendar.getInstance());
        bookLaterTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (providers.size() > 0)

                    bookLaterDateTimePicker.showDialog();
//                else
//                    Toast.makeText(getApplicationContext(), "We are sorry, there is no service providers for this service at your region.", Toast.LENGTH_LONG).show();
//
            }
        });

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(MapActivity.this);

        gps = new TrackGPS(MapActivity.this);
        try {
            if(gps.canGetLocation()){
//                for (int i = 0; i < datalist.size(); i++) {
//
//                    marker = Mmap.addMarker(new MarkerOptions().position(new LatLng(datalist.get(i).getLat(), datalist.get(i).getLongitude()))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bitmapfordriver))
//                            .flat(true));
//
//                }
                Double lat =gps .getLatitude();
                Double lng =  gps.getLongitude();
                 Strlat= String.valueOf(laln.latitude);
                 Strlong= String.valueOf(laln.longitude);
                List<Address> addresses = null;

                try {
                    Geocoder geo = new Geocoder(MapActivity.this, Locale.getDefault());
                    addresses = geo.getFromLocation(lat, lng, 1);
                    if (addresses.isEmpty()) {
                    }
                    else {
                        if (addresses.size() > 0) {
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            address1=(address + "," + city + "," + state + "," + country + "," + postalCode);
                            Toast.makeText(MapActivity.this, address1, Toast.LENGTH_SHORT).show();
//                            for (int i = 0; i < datalist.size(); i++) {
//
//                                marker = Mmap.addMarker(new MarkerOptions().position(new LatLng(datalist.get(i).getLat(), datalist.get(i).getLongitude()))
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bitmapfordriver))
//                                        .flat(true));
//
//                            }

                            //                         Eaddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //    private final Runnable m_Runnable = new Runnable()
//    {
//        public void run()
//
//        {
////            Toast.makeText(MapActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
//
//            MapActivity.this.mHandler.postDelayed(m_Runnable, 5000);
//        }
//
//    };
    private CustomDateTimePicker getCustomDateTimePicker(final TextView textView) {
        CustomDateTimePicker custom = new CustomDateTimePicker(MapActivity.this, new CustomDateTimePicker.ICustomDateTimeListener() {

            @Override
            public void onSet(Dialog dialog, final Calendar calendarSelected,
                              Date dateSelected, int year, String monthFullName,
                              String monthShortName, int monthNumber, int date,
                              String weekDayFullName, String weekDayShortName,
                              int hour24, int hour12, int min, int sec,
                              String AM_PM) {
                textView.setText(calendarSelected
                        .get(Calendar.DAY_OF_MONTH)
                        + "/" + (monthNumber + 1) + "/" + year
                        + ", " + hour12 + ":" + min
                        + " " + AM_PM);
                bookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!serviceId.equalsIgnoreCase("1") || !carTypeSpinner.getText().toString().isEmpty()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                            SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");
                            bookDate = sdf.format(calendarSelected.getTime());
                            bookTime = stf.format(calendarSelected.getTime());
                            bookNow = "0";
                            StringDT=bookDate+bookTime;
//                            Distance();
                            showConfirmDialog();
                        } else
                            Toast.makeText(MapActivity.this, "Must select car type and options", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancel() {
            }
        });
        return custom;
    }





//    private void setMarkerIcon(String serviceId) {
//
//        markerIcon = R.drawable.lmdriver;
//
//    }
@Override
public void onLocationChanged(Location location) {
    mLocation = location;
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    Mmap.clear();

    //    Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
    Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
    Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
    Mmap.setMaxZoomPreference(14.5f);
    Mmap.setMinZoomPreference(6.5f);
}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mmap=googleMap;
        Mmap.clear();
        Double lat = gps.getLatitude();
        Double lng = gps.getLongitude();
        LatLng locateme = new LatLng(lat, lng);
        Mmap.getUiSettings().isZoomControlsEnabled();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Mmap.setMyLocationEnabled(true);
        //   Mmap.addMarker(new MarkerOptions().position(locateme).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(locateme,6.5f));
        // map.animateCamera(CameraUpdateFactory.zoomIn());
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
        //      Mmap.addMarker(new MarkerOptions().position(new LatLng(12.978424,80.219333)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        //    Mmap.addMarker(new MarkerOptions().position(new LatLng(13.031522,80.201531)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        Mmap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if(gps.canGetLocation()) {
                    Double lat = gps.getLatitude();
                    Double lng = gps.getLongitude();
                    LatLng locateme = new LatLng(lat, lng);
                    handlenewlocation(locateme);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"SORRY WE COULDN`T TRACK YOUR LOCATION",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        Mmap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                laln = cameraPosition.target;
                Mmap.clear();

                try {
                    Location mLocation = new Location("");
                    mLocation.setLatitude(laln.latitude);
                    mLocation.setLongitude(laln.longitude);
                    List<Address> addresses = null;
                    Geocoder geo = new Geocoder(MapActivity.this.getApplicationContext(), Locale.getDefault());
                    addresses = geo.getFromLocation(laln.latitude, laln.longitude, 1);
                    if (addresses.isEmpty()) {
                    }
                    else {
                        if (addresses.size() > 0) {
                            String latti= String.valueOf(addresses.get(0).getLatitude());
                            latvalue=latti;

                            longitude= String.valueOf(addresses.get(0).getLongitude());
                            double lat= Double.parseDouble(String.valueOf(addresses.get(0).getLatitude()));
                            double logs=Double.parseDouble(String.valueOf(addresses.get(0).getLongitude()));
                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            country = addresses.get(0).getCountryName();
                            postalCode = addresses.get(0).getPostalCode();
                            knownName = addresses.get(0).getFeatureName();
                            address1=(address + "," + city + "," + state + "," + country + "," + postalCode);
//                            Circle circle = Mmap.addCircle(new CircleOptions()
//                                    .center(new LatLng(dblat,dblon))
//                                    .radius(10000)
//                                    .strokeColor(Color.BLUE)
//                                    .fillColor(getResources().getColor(R.color.transporent_clr)).strokeWidth(2.0f));
                            Toast.makeText(MapActivity.this, address1, Toast.LENGTH_SHORT).show();

                            //                         Eaddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
//                            Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                        }
                    }

//                    changelocation.setText(address1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handlenewlocation(final LatLng laln)
    {
        Mmap.clear();

        //  Mmap.addMarker(new MarkerOptions().position(laln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
        // map.animateCamera(CameraUpdateFactory.zoomIn());
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
        latitu=laln.latitude;
        longitu=laln.longitude;



    }


    public void Distance() {


//        Collections.reverse(datalist);
        final ProgressDialog dialog = ProgressDialog.show(this,"Fetching data","Please wait...",false,false);
        StringBuilder googlePlacesUrl = new StringBuilder("api/distancematrix/json?");
        googlePlacesUrl.append("origins=" +datalist.get(0).getLat() + "," +datalist.get(0).getLongitude());
        googlePlacesUrl.append("&destinations=" +latvalue  + "," +longitude);
        googlePlacesUrl.append("&key=" + "AIzaSyDv2rBW15Rnox8k13gIrgr5ksSerLqf2T0");


//        Log.e("dsdjsh",filterserviceproviders.get(minIndex.get(0)).getLatitude()+filterserviceproviders.get(minIndex.get(0)).getLongitude());

        BaseUrl = googlePlacesUrl.toString();

//        RestAdapter adapter = new RestAdapter.Builder()
//                .setEndpoint("https://maps.googleapis.com/maps")
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        DistanceApi distanceApi =  retrofit.create(DistanceApi.class);

        Call<TimeModelClass> call = distanceApi.getUsers(BaseUrl);
        call.enqueue(new Callback<TimeModelClass>() {
            @Override
            public void onResponse(Call<TimeModelClass> call, Response<TimeModelClass> response) {

                if(response.body().getStatus().equals("OK")){

                    try {

                        Log.e("Dsdsds", response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                        Log.e("latl", String.valueOf(datalist.get(i).getLat()));
                        Log.e("latl", String.valueOf(datalist.get(i).getLongitude()));

                        distance = "Distance :" + response.body().getRows().get(0).getElements().get(0).getDistance().getText();
                        time = "Expected time :" + response.body().getRows().get(0).getElements().get(0).getDuration().getText();
                        Log.e("distance",distance);
                        dialog.dismiss();
                        showConfirmDialog();

                    } catch (NullPointerException e){

                        e.printStackTrace();
                    }


                } else {

                    dialog.dismiss();
                    showConfirmDialog();
                }

            }

            @Override
            public void onFailure(Call<TimeModelClass> call, Throwable t) {

                dialog.dismiss();
                showConfirmDialog();

            }
        });

    }
    private void showConfirmDialog() {
        new AlertDialog.Builder(MapActivity.this)
                //set message, title, and icon
                .setTitle("Booking")
                .setMessage("Do you want to book service at " + Util.getDateTime(bookDate, bookTime)+ "?")
                .setIcon(R.drawable.logo01)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
            popup();
            String userBDT=bookDate+" "+bookTime;

                        documentReference=db.collection("User_details").document(UIAVALUE);
                        HashMap<String,Object> updatesval=new HashMap<>();

                        updatesval.put("Start_Lat",latvalue);
                        updatesval.put("Start_Long",longitude);
                        updatesval.put("User_Address",address1);
                        documentReference.update(updatesval)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(MapActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });

                        documentReference=db.collection("Current_booking").document(Rndmuid);
                        HashMap<String,Object> updatesvalues=new HashMap<>();

                        updatesvalues.put("User_name",Username);
                        updatesvalues.put("User_ID",UIAVALUE);
                        updatesvalues.put("User_Phone_number",PHNO);
                        updatesvalues.put("Car_type",cartypeStr);
                        updatesvalues.put("Start_Lat",latvalue);
                        updatesvalues.put("Start_Long",longitude);
                        updatesvalues.put("User_Address",address1);
                        updatesvalues.put("City",city);
                        updatesvalues.put("User_Booking_ID",Rndmuid);
                        updatesvalues.put("Status","notCompleted");
                        updatesvalues.put("Date",bookDate);
                        updatesvalues.put("User_Booking_Time",bookTime);
                        updatesvalues.put("User_Book_Date_Time",userBDT);
                        updatesvalues.put("User_Token",TokenVal);
                        updatesvalues.put("Request","NotAssigned");

                        documentReference.set(updatesvalues)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(MapActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
//                        documentReference=db.collection("Completed_booking").document(Rndmuid);
//                        HashMap<String,Object> updatesvaluescomplete=new HashMap<>();
////                        updatesvalues.put("Driver_name",);
////                        updatesvalues.put("Driver_ID",);
//                        updatesvaluescomplete.put("User_name","Poojitha");
//                        updatesvaluescomplete.put("User_ID",UIAVALUE);
////                        updatesvalues.put("Driver_Phone_number",);
//                        updatesvaluescomplete.put("User_Phone_number",PHNO);
//                        updatesvaluescomplete.put("Car_type",cartypeStr);
//                        updatesvaluescomplete.put("Start_Lat",latvalue);
//                        updatesvaluescomplete.put("Start_Long",longitude);
//                        updatesvaluescomplete.put("User_Address",address1);
//                        updatesvaluescomplete.put("City",city);
//                        updatesvaluescomplete.put("User_Booking_ID",Rndmuid);
//                        updatesvaluescomplete.put("Status","notCompleted");
//                        updatesvaluescomplete.put("Date",bookDate);
////                        updatesvaluescomplete.put("User_Token",TokenVal);
//
//                        documentReference.set(updatesvaluescomplete)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(MapActivity.this, "successfull", Toast.LENGTH_SHORT).show();
////
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//
//                            }
//                        });




                        documentReference=db.collection("User_current_booking").document(UIAVALUE);
                        HashMap<String,Object> usercurrentbookingid=new HashMap<>();
//
                        usercurrentbookingid.put("Booking_ID",Rndmuid);
//
                        documentReference.set(usercurrentbookingid)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(MapActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
//
                    }
                })


                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).show();
    }
    private void popup() {


        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.userreqalert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView Acce = (TextView)deleteDialogView.findViewById(R.id.accept_button);
//        TextView DriverNumber = (TextView)deleteDialogView.findViewById(R.id.divernumber);
//        DriverNumber.setText(driverphonenumber);
        final AlertDialog alertDialog1 = alertDialog.create();
        Acce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog1.show();
//        alertDialog1.getWindow().setLayout((int) Utils.convertDpToPixel(228,getActivity()),(int)Utils.convertDpToPixel(220,getActivity()));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
//        lp.height=200dp;
//        lp.width=228;
        lp.gravity = Gravity.CENTER;
//        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog1.getWindow().setAttributes(lp);
    }

    public void addappointment(int position){

        Calendar currentCal = Calendar.getInstance();
        timems = currentCal.getTimeInMillis();

        AppointmentList apointmentlist = new AppointmentList();
//        apointmentlist.setUserid(userid);
//        apointmentlist.setUsername(username);
//        apointmentlist.setUsermobilenumber(usermobileno);
//        apointmentlist.setUseraddress(useraddress);
//        apointmentlist.setUserimage(userimage);
        apointmentlist.setUserreview("0");
//        apointmentlist.setProviderid(filterserviceproviders.get(minIndex.get(position)).getProviderid());
//        apointmentlist.setDrivername(filterserviceproviders.get(minIndex.get(position)).getName());
//        apointmentlist.setDriveraddress(filterserviceproviders.get(minIndex.get(position)).getAddress());
//        apointmentlist.setDriverimage(filterserviceproviders.get(minIndex.get(position)).getDriverimage());
        apointmentlist.setCartype(carTypeSpinner.getText().toString());
        apointmentlist.setHourly_or_workstation(driverTypeSpinner.getSelectedItem().toString());
        apointmentlist.setDate(bookDate);
        apointmentlist.setTime(bookTime);
        apointmentlist.setStatus("pending");
        apointmentlist.setUser_latitude(String.valueOf(addresses.get(0).getLatitude()));
        apointmentlist.setUser_longitude(String.valueOf(addresses.get(0).getLongitude()));
        apointmentlist.setBook_now(bookNow);
        apointmentlist.setCancelid("");
        apointmentlist.setTimems(timems);


//        db.child("AppointmentList").push().setValue(apointmentlist);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        for (int i = 0; i < datalist.size(); i++) {
//
//            marker = Mmap.addMarker(new MarkerOptions().position(new LatLng(datalist.get(i).getLat(), datalist.get(i).getLongitude()))
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bitmapfordriver))
//                    .flat(true));
//
//        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
    }


}
