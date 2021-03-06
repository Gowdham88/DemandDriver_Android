package com.czsm.Demand_Driver;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by czsm4 on 19/03/18.
 */

public class PreferencesHelper {
    // region Constants
    private static final String USER_PREFERENCES = "userPreferences";
    public static final String PREFERENCE_EMAIL = USER_PREFERENCES + ".email";
    public static final String PREFERENCE_ID = USER_PREFERENCES + ".id";
    public static final String PREFERENCE_PROFILE_PIC = ".profilePic";
    public static final String PREFERENCE_STATUS = ".status";
    public static final String PREFERENCE_IS_FIRST ="IsFirst";
    public static final String PREFERENCE_NAME ="name";
    public static final String PREFERENCE_CITY ="city";
    public static final String PREFERENCE_DOB ="dob";
    public static final String PREFERENCE_GENDER ="gender";
    public static final String PREFERENCE_FIREBASE_TOKEN ="firebase_token";
    public static final String PREFERENCE_FIREBASE_UUID ="firebase_uuid";
    public static final String PREFERENCE_PHONENUMBER ="phonenumber";
    public static final String PREFERENCE_TOKEN ="tokenvalue";
    public static final String PREFERENCE_USER_DESCRIPTION ="user_desc";
    public static final String PREFERENCE_TAGS ="tags";
    public static final String PREFERENCE_TAG_IDS ="tag_ids";
    public static final String PREFERENCE_FIRST_TIME ="first_time";
    public static final String PREFERENCE_LOGGED_IN ="logged in";
    public static final String PREFERENCE_DASHBOARD ="dashboard";
    public static final String PREFERENCE_USERRATING ="rating";
    public static final String PREFERENCE_USERRANDMID ="rating";
    public static final String PREFERENCE_USERNAME ="username";
    public static final String PREFERENCE_RATING ="review";



    // endregion

    // region Constructors
    private PreferencesHelper() {
        //no instance
    }
    // endregion


    public static void signOut(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(USER_PREFERENCES);
        editor.remove(PREFERENCE_USERNAME);
        editor.remove(PREFERENCE_EMAIL);
        editor.remove(PREFERENCE_FIREBASE_UUID);
        editor.remove(PREFERENCE_PROFILE_PIC);
        editor.remove(PREFERENCE_LOGGED_IN);
        editor.apply();
    }
    public static void setPreference(Context context, String preference_name, String details) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(preference_name, details);
        editor.apply();
    }

    public static void setPreferenceBoolean(Context context, String preference_name, boolean details) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(preference_name, details);
        editor.apply();
    }

    public static String getPreference(Context context, String name) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(name, "");
    }

    public static boolean getPreferenceBoolean(Context context, String name) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(name, false);
    }

    public static void setPreferenceInt(Context context, String preference_name,int details) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(preference_name, details);
        editor.apply();
    }

    public static int getPreferenceInt(Context context, String name) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(name, 0);
    }

    // region Helper Methods
    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }


    private SharedPreferences getSharedPreferences(String preference_name, int i) {

        SharedPreferences settings = getSharedPreferences("your_preference_name", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("LoggedIn", true);
        editor.commit();
        return settings;
    }


    // endregion
}

