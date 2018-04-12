package com.czsm.Demand_Driver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.activities.UserBookingHistoryActivity;
import com.czsm.Demand_Driver.activities.UserOngoingBookingActivity;
import com.czsm.Demand_Driver.model.Datauser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czsm4 on 12/04/18.
 */

public class UserOngoingAdapter extends RecyclerView.Adapter<UserOngoingAdapter.ViewHolder> {

    Context context;
    List<Datauser> dataList = new ArrayList<>();


    public UserOngoingAdapter(Context context, List<Datauser> dataList) {
        this.context  = context;
        this.dataList = dataList;
//        this.cellSize = Utils.getScreenWidth(context)/3;
    }
    public  void addData(List<Datauser> stringArrayList){
        dataList.addAll(stringArrayList);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.
                        list_item_ongoing_bookings, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.userphonenumber.setText(dataList.get(position).getPhoneNumber());
        final String DateTime=dataList.get(position).getDate()+" "+dataList.get(position).getTime();
        holder.datetime.setText(DateTime);
        holder.lat.setText(dataList.get(position).getName());
        String address=dataList.get(position).getAddress();

        holder.ProviderLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, UserOngoingBookingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("phonenumber",dataList.get(position).getPhoneNumber());
                intent.putExtra("name",dataList.get(position).getName());
                intent.putExtra("datatime",DateTime);
                intent.putExtra("address",dataList.get(position).getAddress());
//                intent.putExtra("userlats",dataList.get(position).getCurrentlat());
//                intent.putExtra("userlongs",dataList.get(position).getCurrentlong());
                intent.putExtra("userdate",dataList.get(position).getDate());
                intent.putExtra("usertime",dataList.get(position).getTime());

                context.startActivity(intent);
            }
        });


    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userphonenumber,datetime,lat;
        LinearLayout ProviderLinLay;
        public ViewHolder(View itemView) {
            super(itemView);
            userphonenumber=(TextView)itemView.findViewById(R.id.list_item_booking_service_textview);
            datetime=(TextView)itemView.findViewById(R.id.list_item_booking_datetime_textview);
            lat=(TextView)itemView.findViewById(R.id.list_item_booking_user_name_textview);
            ProviderLinLay=(LinearLayout)itemView.findViewById(R.id.userhistorylinear_lay);

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

