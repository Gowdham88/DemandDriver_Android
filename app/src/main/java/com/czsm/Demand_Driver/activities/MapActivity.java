package com.czsm.Demand_Driver.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toolbar;

import com.czsm.Demand_Driver.Firebasemodel.AppointmentList;
import com.czsm.Demand_Driver.Manifest;
import com.czsm.Demand_Driver.PreferencesHelper;
import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.Retrofit.DistanceApi;
import com.czsm.Demand_Driver.Utils;
import com.czsm.Demand_Driver.helper.Util;
import com.czsm.Demand_Driver.model.TimeModelClass;
import com.czsm.Demand_Driver.view.CustomDateTimePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.czsm.Demand_Driver.activities.BookServiceMapActivity.BaseUrl;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {
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
    String name,email,mobile_number, Current_Location, Service_type, namee, mob_num, map_loc;
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
    String UIAVALUE,PHNO;
    String distance,time;
    List<Address> addresses;
    String lattitude,longitude,address,city,state,country,postalCode,knownName;

    int i = 1, maxproviders = 0;
    long timems;
     FirebaseFirestore db;
    DocumentReference documentReference;
    String  Strlat,Strlong,latvalue,longitude1,lattitude1,lattitud,longtude;



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
         UIAVALUE= PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        PHNO= PreferencesHelper.getPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PHONENUMBER);
          db= FirebaseFirestore.getInstance();
        toolbar.setTitle("Current Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        setMarkerIcon(serviceId);
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
        String cartypeStr=carTypeSpinner.getText().toString().trim();
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

            }
        });
        bookNowTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (filterserviceproviders.size() > 0) {
                    if (!serviceId.equalsIgnoreCase("1") || !carTypeSpinner.getText().toString().isEmpty()) {

                        Log.e("cartype",carTypeSpinner.getText().toString());
                        Date now = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");

                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                        stf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                        bookDate = sdf.format(now);
                        bookTime = stf.format(now);
                        bookNow = "1";
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
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
                            bookDate = sdf.format(calendarSelected.getTime());
                            bookTime = stf.format(calendarSelected.getTime());
                            bookNow = "0";
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





    private void setMarkerIcon(String serviceId) {

        markerIcon = R.drawable.lmdriver;

    }
    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Mmap.clear();
//        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
//                SosMap.this, R.raw.style_json);
//        Mmap.setMapStyle(style);
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
        lattitud =String.valueOf(lat);
          longtude= String.valueOf(lng);
        Log.e("lattitude", lattitud);
        Log.e("longtude",longtude);


        LatLng locateme = new LatLng(lat, lng);
//        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
//        Mmap.setMapStyle(style);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
                      lattitude1= String.valueOf(lat);
                    longitude1= String.valueOf(lng);
////
//                    Log.e("locateme",lattitude1);
//
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
//                Toast.makeText(MapActivity.this, "haaiohb", Toast.LENGTH_SHORT).show();
                laln = cameraPosition.target;
                Mmap.clear();

                try {
                    Location mLocation = new Location("");
                    mLocation.setLatitude(laln.latitude);
                    mLocation.setLongitude(laln.longitude);





//                    DocumentReference washingtonRef = db.collection("Users").document(UIAVALUE);
//                    HashMap<String, Object> city1 = new HashMap<>();
//                    city1.put("lat", Strlat);
//                    city1.put("long",Strlong);
//
//// Set the "isCapital" field of the city 'DC'
//                    washingtonRef
//                            .update(city1)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
////                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
////                                    Log.w(TAG, "Error updating document", e);
//                                }
//                            });



                    Geocoder geo = new Geocoder(MapActivity.this.getApplicationContext(), Locale.getDefault());
                    addresses = geo.getFromLocation(laln.latitude, laln.longitude, 1);
                    if (addresses.isEmpty()) {


                    }
                    else {
                        if (addresses.size() > 0) {
                            String latti= String.valueOf(addresses.get(0).getLatitude());
                            latvalue=latti;

                             longitude= String.valueOf(addresses.get(0).getLongitude());
                             address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                             city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                             country = addresses.get(0).getCountryName();
                             postalCode = addresses.get(0).getPostalCode();
                             knownName = addresses.get(0).getFeatureName();
                            address1=(latvalue+","+longitude+","+address + "," + city + "," + state + "," + country + "," + postalCode);

//                            Log.e("Strlat", latvalue);
//                            Log.e("Strlong",longitude);
                             documentReference=db.collection("Users").document(UIAVALUE);
                            HashMap<String,Object> updates=new HashMap<>();
                            updates.put("lat", latvalue);
                            updates.put("long",longitude);
                            documentReference.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    Log.e("lat",latvalue);
//                                    Log.e("long",longitude);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


                                }
                            });
                            documentReference=db.collection("BookingRequest").document(UIAVALUE);
                            HashMap<String,Object> updates1=new HashMap<>();
                            updates1.put("lat", latvalue);
                            updates1.put("long",longitude);
                            documentReference.update(updates1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    Log.e("lat",latvalue);
//                                    Log.e("long",longitude);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


                                }
                            });


                            //                         Eaddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                        }
                    }
                    Toast.makeText(MapActivity.this, address1, Toast.LENGTH_SHORT).show();
                    changelocation.setText(address1);
//                    map_loc = "http://maps.google.com/maps?q=loc:" + laln.latitude + "," + laln.longitude + "1";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handlenewlocation(final LatLng laln)
    {
        Mmap.clear();
//        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(SosMap.this, R.raw.style_json);
//        Mmap.setMapStyle(style);
        //  Mmap.addMarker(new MarkerOptions().position(laln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
        // map.animateCamera(CameraUpdateFactory.zoomIn());
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
        Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(laln.latitude,laln.longitude), 13));
        latitu=laln.latitude;
        longitu=laln.longitude;

//        Log.e("newlat", String.valueOf(latitu));
//        Log.e("newlong", String.valueOf(longitu));

    }

    public void Distance() {

        final ProgressDialog dialog = ProgressDialog.show(this,"Fetching data","Please wait...",false,false);
        StringBuilder googlePlacesUrl = new StringBuilder("api/distancematrix/json?");
        googlePlacesUrl.append("origins=" + longitude1 + "," + lattitude1);
        googlePlacesUrl.append("&destinations=" +latvalue + "," +longitude);
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

                        distance = "Distance :" + response.body().getRows().get(0).getElements().get(0).getDistance().getText();
                        time = "Expected time :" + response.body().getRows().get(0).getElements().get(0).getDuration().getText();
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
                .setMessage("Do you want to book service at " + Util.getDateTime(bookDate, bookTime) + " ?"+ "\n" + time)
                .setIcon(R.drawable.logo01)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /******************Adding appointments**********************/

                        addappointment(0);
                        documentReference=db.collection("BookingRequest").document(UIAVALUE);
                        Map<String, Object> updateval1 = new HashMap<>();
                        updateval1.put("date",bookDate);
                        updateval1.put("time", bookTime);
                        updateval1.put("UID",UIAVALUE);
                        updateval1.put("phoneNumber",PHNO);
                        updateval1.put("Pickuplat",latvalue);
                        updateval1.put("Pickuplong",longitude);
                        updateval1.put("Currentlat",lattitud);
                        updateval1.put("Currentlong",longtude);
                        updateval1.put("address",address);
                        updateval1.put("city",city);
                        updateval1.put("state",state);
                        updateval1.put("country",country);
                        updateval1.put("Status",true);


                        documentReference.set(updateval1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MapActivity.this, "successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
                        documentReference=db.collection("Users").document(UIAVALUE).collection("CurrentBookings").document(UIAVALUE);
                        Map<String, Object> updateval = new HashMap<>();
                        updateval.put("date",bookDate);
                        updateval.put("time", bookTime);
                        updateval.put("UID",UIAVALUE);
                        updateval.put("phoneNumber",PHNO);
                        updateval.put("Pickuplat",latvalue);
                        updateval.put("Pickuplong",longitude);
                        updateval.put("Currentlat",lattitud);
                        updateval.put("Currentlong",longtude);
                        updateval.put("address",address);
                        updateval.put("city",city);
                        updateval.put("state",state);
                        updateval.put("country",country);

                        documentReference.set(updateval).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MapActivity.this, "success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });


//                        Query myTopPostsQuery = db.child("AppointmentList").orderByChild("timems").equalTo(timems);
//                        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {

//                                for (final DataSnapshot child : dataSnapshot.getChildren()) {
//
//                                    final AppointmentList appointmentList = child.getValue(AppointmentList.class);
//
//                                    appointmentlist.clear();
//
//                                    if (appointmentList.getUserid().equals(userid)) {
//
//                                        appointmentlist.add(appointmentList);
//
//                                        if (appointmentlist.get(0).getStatus().equals("pending") && i == 1) {
//
//                                            showsuccessDialog();
//
//                                        }
//
//                                        if (i < maxproviders) {
//
//                                            Handler handler = new Handler();
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    if (appointmentlist.get(0).getStatus().equals("pending")) {
//
//                                                        Toast.makeText(getApplicationContext(), "" + i, Toast.LENGTH_SHORT).show();
//
//                                                        if (i == 1) {
//
//                                                            java.util.Map appointmentData = new HashMap();
//                                                            appointmentData.put("cancelid", filterserviceproviders.get(minIndex.get(0)).getProviderid());
//                                                            appointmentData.put("providerid", filterserviceproviders.get(minIndex.get(i)).getProviderid());
//                                                            appointmentData.put("drivername", filterserviceproviders.get(minIndex.get(i)).getName());
//                                                            appointmentData.put("driveraddress", filterserviceproviders.get(minIndex.get(i)).getAddress());
//                                                            child.getRef().updateChildren(appointmentData);
//
//                                                        } else {
//
//                                                            Map appointmentData = new HashMap();
//                                                            appointmentData.put("cancelid", appointmentlist.get(0).getCancelid() + "," + filterserviceproviders.get(minIndex.get(i)).getProviderid());
//                                                            appointmentData.put("providerid", filterserviceproviders.get(minIndex.get(i)).getProviderid());
//                                                            appointmentData.put("drivername", filterserviceproviders.get(minIndex.get(i)).getName());
//                                                            appointmentData.put("driveraddress", filterserviceproviders.get(minIndex.get(i)).getAddress());
//                                                            child.getRef().updateChildren(appointmentData);
//
//                                                        }
//
//                                                        i++;
//
//                                                    }
//
//                                                }
//                                            }, 10000);
//
//                                        } else {
//
//                                            if (appointmentlist.get(0).getStatus().equals("pending") && i == maxproviders) {
//
//                                                child.getRef().child("status").setValue("Cancelled");
//                                            }
//                                        }
//
//                                    }
//
//                                }


//                            }

//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                                Log.e("Databaseerror", "" + databaseError.toException().toString());
//
//                            }
//                        });

                    }
                })

                /***************************************************************/

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).show();
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




}
