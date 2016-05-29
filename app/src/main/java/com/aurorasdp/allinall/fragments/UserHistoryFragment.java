package com.aurorasdp.allinall.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aurorasdp.allinall.R;
import com.aurorasdp.allinall.activities.UserBookingActivity;
import com.aurorasdp.allinall.controller.AllinAllController;
import com.aurorasdp.allinall.helper.RESTClient;
import com.aurorasdp.allinall.helper.Util;
import com.aurorasdp.allinall.model.UserBooking;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHistoryFragment extends Fragment implements RESTClient.ServiceResponseInterface {

    private SwipeRefreshLayout swipeContainer;
    private AllinAllController allinAllController;
    private UserHistoryListAdapter adapter;


    public UserHistoryFragment() {
        // Required empty public constructor
        allinAllController = new AllinAllController(getContext(), this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_history, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        ListView listView = (ListView) view.findViewById(R.id.fragment_user_history_listview);
        adapter = new UserHistoryListAdapter(getContext(), R.layout.list_item_user_booking);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserBooking userBooking = adapter.getItem(position);
                Intent bookingIntent = new Intent(getContext(), UserBookingActivity.class);
                Bundle bookingBundle = new Bundle();
                bookingBundle.putInt("bookingIndex", position);
                bookingIntent.putExtras(bookingBundle);
                startActivity(bookingIntent);
            }
        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allinAllController.listUserHistory(RESTClient.ID, null);
            }
        });

        TextView emptyView = Util.getEmptyView(R.string.no_history, getContext());
        ((ViewGroup) listView.getParent().getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void sendServiceResult(String serviceResult) {
        adapter.clear();
        adapter.addAll(RESTClient.USER_BOOKINGS);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void requestFailed() {
        Util.requestFailed(getContext());
    }

    private class UserHistoryListAdapter extends ArrayAdapter<UserBooking> {
        public View mView;
        public TextView userNameTextview;
        public TextView dateTimeTextview;
        public TextView serviceTextview;
        public ImageView providerImageview;
        public Context context;
        public int resource;
        ArrayList<UserBooking> userBookings;


        public UserHistoryListAdapter(Context context, int resource) {
            super(context, resource, RESTClient.USER_BOOKINGS);
            this.context = context;
            this.resource = resource;
            this.userBookings = RESTClient.USER_BOOKINGS;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = viewInflater.inflate(resource, null);
            }
            mView = convertView;
            userNameTextview = (TextView) convertView.findViewById(R.id.list_item_booking_user_name_textview);
            dateTimeTextview = (TextView) convertView.findViewById(R.id.list_item_booking_datetime_textview);
            serviceTextview = (TextView) convertView.findViewById(R.id.list_item_booking_service_textview);
            providerImageview = (ImageView) convertView.findViewById(R.id.list_item_booking_user_imagview);

            userNameTextview.setText(userBookings.get(position).getProviderName());
            dateTimeTextview.setText(userBookings.get(position).getDateTime());
            serviceTextview.setText(userBookings.get(position).getService());
            if (userBookings.get(position).getDecodedPic() == null || userBookings.get(position).getDecodedPic().length == 0) {
                providerImageview.setImageResource(R.drawable.profile_circle);
            } else {
                byte[] decodedImage = userBookings.get(position).getDecodedPic();
                providerImageview.setImageBitmap(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length));
            }
            return convertView;
        }
    }

}