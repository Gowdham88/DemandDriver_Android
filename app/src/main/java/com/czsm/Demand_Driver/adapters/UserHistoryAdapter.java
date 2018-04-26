package com.czsm.Demand_Driver.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.czsm.Demand_Driver.R;
import com.czsm.Demand_Driver.activities.UserBookingHistoryActivity;
import com.czsm.Demand_Driver.model.Data;
import com.czsm.Demand_Driver.model.Datauser;
import com.czsm.Demand_Driver.model.User_Details;
import com.czsm.Demand_Driver.model.User_completeDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czsm4 on 10/04/18.
 */

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.ViewHolder> {

    Context context;
    List<User_completeDetails> dataList = new ArrayList<>();


    public UserHistoryAdapter(Context context, List<User_completeDetails> dataList) {
        this.context  = context;
        this.dataList = dataList;
//        this.cellSize = Utils.getScreenWidth(context)/3;
    }
    public  void addData(List<User_completeDetails> stringArrayList){
        dataList.addAll(stringArrayList);
    }
    @Override
    public UserHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.
                        list_item_user_history, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
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
    public void onBindViewHolder(UserHistoryAdapter.ViewHolder holder, final int position) {

        holder.userphonenumber.setText(dataList.get(position).getUser_Phone_number());
        final String DateTime=dataList.get(position).getDate()+" "+dataList.get(position).getEnd_time();
        holder.datetime.setText(dataList.get(position).getEnd_time());
        holder.lat.setText(dataList.get(position).getUser_name());
        String address=dataList.get(position).getUser_Address();

        holder.ProviderLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, UserBookingHistoryActivity.class);
                intent.putExtra("phonenumber",dataList.get(position).getUser_Phone_number());
                intent.putExtra("name",dataList.get(position).getUser_name());
                intent.putExtra("datatime",dataList.get(position).getEnd_time());
                intent.putExtra("address",dataList.get(position).getUser_Address());
//                intent.putExtra("rating",dataList.get(position).getRating());
//                intent.putExtra("userlats",dataList.get(position).getCurrentlat());
//                intent.putExtra("userlongs",dataList.get(position).getCurrentlong());
                intent.putExtra("userdate",dataList.get(position).getDate());
//                intent.putExtra("usertime",dataList.get(position).getEnd_time());
                intent.putExtra("userreview",dataList.get(position).getUser_review());
                intent.putExtra("Booking_id",dataList.get(position).getUser_Booking_ID());
                context.startActivity(intent);
            }
        });


    }

    private void showRatingDialog() {

        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_review_booking, null);

        new AlertDialog.Builder(context).setIcon(android.R.drawable.btn_star_big_on).setTitle("Review")
                .setView(dialogView)
                .setMessage("Appreciate giving feedback about this Appointment")
                .setCancelable(false)
                .setPositiveButton("Rate",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.dialog_review_booking_ratingbar);
                                String review = ratingBar.getProgress() + "";
                                Toast.makeText(context, review, Toast.LENGTH_SHORT).show();
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
//                               context.finish();
                                dialog.cancel();
                            }
                        }).setCancelable(false).show();
    }




    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
