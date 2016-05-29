package com.aurorasdp.allinall.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.aurorasdp.allinall.R;
import com.aurorasdp.allinall.helper.RESTClient;
import com.aurorasdp.allinall.helper.Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by AAshour on 5/15/2016.
 */
public class AllinAllController {

    Context context;
    RESTClient.ServiceResponseInterface restClientInterface;
    final String DEV_URL = "http://54.200.102.214/All_In_All_Backend/index.php";
    final String DEPLOY_URL = "";
    String mainServiceURL;

    public AllinAllController(Context context, RESTClient.ServiceResponseInterface restClientInterface) {
        this.context = context;
        this.restClientInterface = restClientInterface;
        mainServiceURL = DEV_URL;
    }

    public void sendSms(String mobile) {
        Log.e(context.getString(R.string.tag), "Controller: Send SMS " + mobile);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/OTP/sendsms",
                new ArrayList<String>(Arrays.asList("phone")),
                new ArrayList<String>(Arrays.asList(mobile)), "Sending SMS .....");
    }

    public void userSignUp(String name, String age, String countryCode, String mobile, String email, String password, String profileImage, String profileImageExt) {
        Log.e(context.getString(R.string.tag), "Controller: User Sign Up " + name + " " + age + " " + mobile + " " + email + " " + profileImage + " " + profileImageExt);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/user/signup",
                new ArrayList<String>(Arrays.asList("name", "age", "country_code", "phone", "email", "password", "profile_pic", "image_extension")),
                new ArrayList<String>(Arrays.asList(name, age, countryCode, mobile, email, Util.md5(password), profileImage, profileImageExt)), "Signing up .....");
    }

    public void userLogin(String phone, String password) {
        Log.e(context.getString(R.string.tag), "Controller: User Login " + phone + " " + password);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/user/login",
                new ArrayList<String>(Arrays.asList("phone", "password")),
                new ArrayList<String>(Arrays.asList(phone, Util.md5(password))), "Loging in .....");
    }


    public void providerSignUp(String name, String age, String mobile, String serviceOffered, String profileImage, String profileImageExt) {
        Log.e(context.getString(R.string.tag), "Controller: Service Provider Sign Up " + name + " " + age + " " + mobile + " " + serviceOffered + " " + profileImage + " " + profileImageExt);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/service_provider/signup",
                new ArrayList<String>(Arrays.asList("name", "phone", "age", "profile_pic", "image_extension", "service_id")),
                new ArrayList<String>(Arrays.asList(name, mobile, age, profileImage, profileImageExt, serviceOffered)), "Signing up .....");
    }

    public void providerLogin(String phone, String password) {
        Log.e(context.getString(R.string.tag), "Controller: Service Provider Login " + phone + " " + password);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/service_provider/login",
                new ArrayList<String>(Arrays.asList("phone", "password")),
                new ArrayList<String>(Arrays.asList(phone, password)), "Loging in .....");
    }

    public void listProviderBookings(String providerId, String message) {
        Log.e(context.getString(R.string.tag), "Controller: List Provider Bookings " + providerId);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/appointment/getServiceProviderBookings",
                new ArrayList<String>(Arrays.asList("service_provider_id")),
                new ArrayList<String>(Arrays.asList(providerId)), message);
    }

    public void getWalletData(String providerId) {
        Log.e(context.getString(R.string.tag), "Controller: Get Provider Wallet data " + providerId);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/service_provider/getWalletData",
                new ArrayList<String>(Arrays.asList("service_provider_id")),
                new ArrayList<String>(Arrays.asList(providerId)), "Loading Wallet Data ..... ");
    }

    public void listUserHistory(String userId, String message) {
        Log.e(context.getString(R.string.tag), "Controller: List User History " + userId);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, DEV_URL + "/appointment/getHistoryForUser",
                new ArrayList<String>(Arrays.asList("user_id")),
                new ArrayList<String>(Arrays.asList(userId)), message);
    }

    public void resetPassword(String userPhone, String encryptedPass, String decryptedPass) {
        Log.e(context.getString(R.string.tag), "Controller: Send Mail By new Password " + userPhone + " - " + encryptedPass + " - " + decryptedPass);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, mainServiceURL + "/user/sendMailByNewPassword",
                new ArrayList<String>(Arrays.asList("phone", "encrypted_password", "unencrypted_password")),
                new ArrayList<String>(Arrays.asList(userPhone, encryptedPass, decryptedPass)), "Sending Email .....");
    }

    public void getUserEmail(String userPhone) {
        Log.e(context.getString(R.string.tag), "Controller: Get User Email " + userPhone);
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.POST, mainServiceURL + "/user/getUserEmail",
                new ArrayList<String>(Arrays.asList("phone")),
                new ArrayList<String>(Arrays.asList(userPhone)), "Wait .....");
    }

    public void getServices() {
        Log.e(context.getString(R.string.tag), "Controller: Get services ");
        RESTClient restClient = new RESTClient(context);
        restClient.setServiceResponseInterface(restClientInterface);
        restClient.callRESTService(Request.Method.GET, DEV_URL + "/service/getServices",
                new ArrayList<String>(),
                new ArrayList<String>(), null);
    }



}