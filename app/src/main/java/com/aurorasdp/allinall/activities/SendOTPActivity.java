package com.aurorasdp.allinall.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aurorasdp.allinall.R;
import com.aurorasdp.allinall.controller.AllinAllController;
import com.aurorasdp.allinall.helper.RESTClient;
import com.aurorasdp.allinall.helper.Util;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SendOTPActivity extends AppCompatActivity implements Validator.ValidationListener, RESTClient.ServiceResponseInterface {

    @InjectView(R.id.send_otp_mobile_edittext)
    @NotEmpty
    @Length(min = 10)
    EditText phoneEditText;

    @InjectView(R.id.send_otp_send_button)
    Button sendButton;

    @InjectView(R.id.send_otp_country_code_spinner)
    Spinner countryCodeSpinner;

    private Validator otpValidator;
    private AllinAllController allinAllController;
    String code, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        ButterKnife.inject(this);

        allinAllController = new AllinAllController(this, this);
        otpValidator = new Validator(this);
        otpValidator.setValidationListener(this);

        // country code
        String codes[] = {"0091"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, codes);
        countryCodeSpinner.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpValidator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        mobile = phoneEditText.getText().toString();
        code = countryCodeSpinner.getSelectedItem().toString();
        allinAllController.sendSms(code + mobile);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Util.onValidationFailed(this, errors);
    }

    @Override
    public void sendServiceResult(String serviceResult) {
        if (serviceResult.equalsIgnoreCase(getString(R.string.otp_sendsms_success))) {
            Intent verifyOTPIntent = new Intent(this, VerifyOTPActivity.class);
            Bundle verifyOTPBundle = new Bundle();
            verifyOTPBundle.putString("code", code);
            verifyOTPBundle.putString("mobile", mobile);
            verifyOTPIntent.putExtras(verifyOTPBundle);
            startActivity(verifyOTPIntent);
            finish();
        } else {
            Toast.makeText(this, serviceResult, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void requestFailed() {
        Util.requestFailed(this);
    }
}
