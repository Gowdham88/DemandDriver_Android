package com.aurorasdp.allinall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.aurorasdp.allinall.R;
import com.aurorasdp.allinall.helper.RESTClient;
import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;

import java.util.HashMap;

public class PushNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "customHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action=" + action);
        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
            //Check for Pushbots Instance
            Pushbots pushInstance = Pushbots.sharedInstance();
            if (!pushInstance.isInitialized()) {
                Log.i("Initializing Pushbots.", "");
                Pushbots.sharedInstance().init(context.getApplicationContext());
            }

            //Clear Notification array
            if (PBNotificationIntent.notificationsArray != null) {
                PBNotificationIntent.notificationsArray = null;
            }

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_OPEN);
            Log.i(TAG, "User clicked notification with Message: " + PushdataOpen.get("message"));

            //Report Opened Push Notification to Pushbots
            if (Pushbots.sharedInstance().isAnalyticsEnabled()) {
                Pushbots.sharedInstance().reportPushOpened((String) PushdataOpen.get("PUSHANALYTICS"));
            }

            //Start lanuch Activity [original by Pushbots]
//            String packageName = context.getPackageName();
//            Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            resultIntent.putExtras(intent.getBundleExtra("pushData"));
//            Pushbots.sharedInstance().startActivity(resultIntent);
/*
            //Start MainActivity
            Intent resultIntent = new Intent(context, com.aurorasdp.physionest.activities.LoginActivity.class);
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            resultIntent.putExtra("FROM_PUSH", "1");
            Pushbots.sharedInstance().startActivity(resultIntent);
*/
            if (PushdataOpen.get("mode") != null) {
                if (PushdataOpen.get("mode").toString().equals("CANCEL_APPOINTMENT") || PushdataOpen.get("mode").toString().equals("CONFIRM_APPOINTMENT")) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
                    RESTClient.ID = sharedPreferences.getString("userId", "");
                    if (!RESTClient.ID.equalsIgnoreCase("")) {
                        Intent resultIntent = new Intent(context, com.aurorasdp.allinall.activities.UserActivity.class);
                        resultIntent.putExtra("FROM_PUSH", "1");
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Pushbots.sharedInstance().startActivity(resultIntent);
                    } else {
                        Intent resultIntent = new Intent(context, com.aurorasdp.allinall.activities.LoginActivity.class);
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Pushbots.sharedInstance().startActivity(resultIntent);
                    }

                } else if (PushdataOpen.get("mode").toString().equals("PENDING_APPOINTMENT")) {
                    Intent resultIntent = new Intent(context, com.aurorasdp.allinall.activities.LoginActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("FROM_PUSH", "1");
//                    bundle.putString("message", PushdataOpen.get("message").toString());
//                    bundle.putString("appointment_id", PushdataOpen.get("appointment_id").toString());
//                    resultIntent.putExtras(bundle);
//                    Log.e("AllinAll", "resultIntent.getExtras() " + resultIntent.getExtras());
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Pushbots.sharedInstance().startActivity(resultIntent);
                }
//                Toast.makeText(context, "Notification" + RESTClient.ID, Toast.LENGTH_LONG).show();
            }

        } // Handle Push Message when received

        else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
//            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
//            String messages = sharedPreferences.getString("messages", "");
//            String ids = sharedPreferences.getString("ids", "");
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            if (messages.equalsIgnoreCase("")) {
//                editor.putString("messages", PushdataOpen.get("message").toString());
//                editor.putString("ids", PushdataOpen.get("appointment_id").toString());
//            } else {
//                editor.putString("messages", messages + "," + PushdataOpen.get("message").toString());
//                editor.putString("ids", ids + "," + PushdataOpen.get("appointment_id").toString());
//            }
//            editor.apply();

//            Toast.makeText(context, "Mode: " + PushdataOpen.get("mode"), Toast.LENGTH_SHORT).show();
            /*if (PushdataOpen.get("mode") != null) {
                if (PushdataOpen.get("mode").toString().equals("MESSAGE_REPLY"))
                    context.sendBroadcast(new Intent(UserMessageReceiver.ACTION_USER_MESSAGE_RECEIVED));
                else if (PushdataOpen.get("mode").toString().equals("CONFIRM_APPOINTMENT_DOCTOR"))
                    context.sendBroadcast(new Intent(DoctorAppointmentConfirmedReceiver.ACTION_DOCTOR_APPOINTMENT_CONFIRMED_RECEIVED));
            }*/
            //Insert new event in the database
          /*   DBController dbController = new DBController(context);
            Event event = new Event();

            if(PushdataOpen.get("mode") != null && PushdataOpen.get("mode").toString().equals("ADD"))
            {
                event.setId(PushdataOpen.get("id").toString());
                event.setTitle(PushdataOpen.get("title").toString());
                event.setDate(PushdataOpen.get("date").toString());
                event.setCategory(PushdataOpen.get("category").toString());

                if (PushdataOpen.get("description") != null)
                    event.setDescription(PushdataOpen.get("description").toString());
                else
                    event.setDescription("");

                if (PushdataOpen.get("venue") != null)
                    event.setVenue(PushdataOpen.get("venue").toString());
                else
                    event.setVenue("");

                if (PushdataOpen.get("fromTime") != null)
                    event.setFromTime(PushdataOpen.get("fromTime").toString());
                else
                    event.setFromTime("");

                if (PushdataOpen.get("toTime") != null)
                    event.setToTime(PushdataOpen.get("toTime").toString());
                else
                    event.setToTime("");

                event.setStatus("S");
                event.setLastUpdateTime((String) PushdataOpen.get("lastUpdateTime"));
                event.setLastUpdateTime((String) PushdataOpen.get("contactNumber"));
                event.setLastUpdateTime((String) PushdataOpen.get("websiteLink"));
                event.setLastUpdateTime((String) PushdataOpen.get("broadcastArea"));

                dbController.insertEvent(event);
                Log.e("MYAPP", "User Received notification with Message: ADDDDDDDDD" + PushdataOpen.get("message"));
            }
            else if(PushdataOpen.get("mode") != null && PushdataOpen.get("mode").toString().equals("DELETE"))
            {
                event.setId(PushdataOpen.get("id").toString());
                dbController.deleteEvent(event);
                Log.e("MYAPP", "User Received notification with Message: DELETEEEEEEEE" + PushdataOpen.get("message"));
            }
*/
            Log.i("MYAPP", "User Received notification with Message: " + PushdataOpen.get("message"));

        }
    }


}
