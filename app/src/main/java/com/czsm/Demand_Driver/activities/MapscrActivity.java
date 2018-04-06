package com.czsm.Demand_Driver.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.czsm.Demand_Driver.Utils;
import com.czsm.Demand_Driver.helper.Util;
import com.czsm.Demand_Driver.view.CustomDateTimePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MapscrActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {
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
    private LatLng latlon1;
    int i = 1, maxproviders = 0;
    long timems;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String  Strlat,Strlong,latvalue,longitude1,lattitude1,lattitud,longtude,refer;
    LatLng northbounds,southbounds;
    CharSequence text;
    Circle myCircle;
    String cartypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapscr);
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

//        Toast.makeText(MapActivity.this, cartypeStr, Toast.LENGTH_SHORT).show();

        carTypeSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carTypeSpinner.showDropDown();
                Utils.hideKeyboard(MapscrActivity.this);
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
                    cartypeStr=carTypeSpinner.getText().toString().trim();
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
        mapFrag.getMapAsync(MapscrActivity.this);

        gps = new TrackGPS(MapscrActivity.this);
        try {
            if(gps.canGetLocation()){
                Double lat =gps .getLatitude();
                Double lng =  gps.getLongitude();
                List<Address> addresses = null;
                try {
                    Geocoder geo = new Geocoder(MapscrActivity.this.getApplicationContext(), Locale.getDefault());
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

    private void setMarkerIcon(String serviceId) {

        markerIcon = R.drawable.lmdriver;

    }

    private CustomDateTimePicker getCustomDateTimePicker(final TextView textView) {
        CustomDateTimePicker custom = new CustomDateTimePicker(MapscrActivity.this, new CustomDateTimePicker.ICustomDateTimeListener() {

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
                            Toast.makeText(MapscrActivity.this, "Must select car type and options", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancel() {
            }
        });
        return custom;
    }



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
                    Geocoder geo = new Geocoder(MapscrActivity.this.getApplicationContext(), Locale.getDefault());
                    addresses = geo.getFromLocation(laln.latitude, laln.longitude, 1);
                    if (addresses.isEmpty()) {
                    }
                    else {
                        if (addresses.size() > 0) {
                            double dblat=addresses.get(0).getLatitude();
                            double dblon=addresses.get(0).getLongitude();
                             address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                             city = addresses.get(0).getLocality();
                             state = addresses.get(0).getAdminArea();
                             country = addresses.get(0).getCountryName();
                             postalCode = addresses.get(0).getPostalCode();
                             knownName = addresses.get(0).getFeatureName();
                            address1=(address + "," + city + "," + state + "," + country + "," + postalCode);
                            Circle circle = Mmap.addCircle(new CircleOptions()
                                    .center(new LatLng(dblat,dblon))
                                    .radius(10000)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(getResources().getColor(R.color.transporent_clr)).strokeWidth(2.0f));
                            Toast.makeText(MapscrActivity.this, address1, Toast.LENGTH_SHORT).show();

                            //                         Eaddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
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
    private void showConfirmDialog() {
        new android.support.v7.app.AlertDialog.Builder(MapscrActivity.this)
                //set message, title, and icon
                .setTitle("Booking")
                .setMessage("Do you want to book service at " + Util.getDateTime(bookDate, bookTime) + " ?")
                .setIcon(R.drawable.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /******************Adding appointments**********************/
                        documentReference=db.collection("UsersBookingRequest").document(UIAVALUE);
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
                        updateval1.put("token",refer);
//                        updateval1.put("radius",text);


                        documentReference.set(updateval1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MapscrActivity.this, "successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
//                        addappointment(0);

//                        Query myTopPostsQuery = db.child("AppointmentList").orderByChild("timems").equalTo(timems);
//                        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
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
////
////                                        if (i < maxproviders) {
////
////                                            Handler handler = new Handler();
////                                            handler.postDelayed(new Runnable() {
////                                                @Override
////                                                public void run() {
////
////                                                    if (appointmentlist.get(0).getStatus().equals("pending")) {
////
////                                                        Toast.makeText(getApplicationContext(), "" + i, Toast.LENGTH_SHORT).show();
////
////                                                        if (i == 1) {
////
////                                                            Map appointmentData = new HashMap();
////                                                            appointmentData.put("cancelid", filterserviceproviders.get(minIndex.get(0)).getProviderid());
////                                                            appointmentData.put("providerid", filterserviceproviders.get(minIndex.get(i)).getProviderid());
////                                                            appointmentData.put("drivername", filterserviceproviders.get(minIndex.get(i)).getName());
////                                                            appointmentData.put("driveraddress", filterserviceproviders.get(minIndex.get(i)).getAddress());
////                                                            child.getRef().updateChildren(appointmentData);
////
////                                                        } else {
////
////                                                            Map appointmentData = new HashMap();
////                                                            appointmentData.put("cancelid", appointmentlist.get(0).getCancelid() + "," + filterserviceproviders.get(minIndex.get(i)).getProviderid());
////                                                            appointmentData.put("providerid", filterserviceproviders.get(minIndex.get(i)).getProviderid());
////                                                            appointmentData.put("drivername", filterserviceproviders.get(minIndex.get(i)).getName());
////                                                            appointmentData.put("driveraddress", filterserviceproviders.get(minIndex.get(i)).getAddress());
////                                                            child.getRef().updateChildren(appointmentData);
////
////                                                        }
////
////                                                        i++;
////
////                                                    }
////
////                                                }
////                                            }, 10000);
////
////                                        } else {
////
////                                            if (appointmentlist.get(0).getStatus().equals("pending") && i == maxproviders) {
////
////                                                child.getRef().child("status").setValue("Cancelled");
////                                            }
////                                        }
//
//                                    }
//
//                                }
//
//
//                            }
//
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

}
