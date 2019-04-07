package com.app_1apruefung;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app_1apruefung.utils.AppUtils;
import com.app_1apruefung.utils.Listener_GetApiData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText et_fname, et_confirm_password, et_password, et_mobile, et_email, et_lname;
    Button bt_register;
    TextView tv_not_a_member;
    Context context = RegisterActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findById();

        initialize();
    }

    private void initialize() {
        clickListeners();
    }

    private void clickListeners() {
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    registrationprocess();
                }
            }
        });
        tv_not_a_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        });
    }

    private void registrationprocess() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "" + et_mobile.getText().toString().trim());
        map.put("password", "" + et_password.getText().toString().trim());
        map.put("first_name", "" + et_fname.getText().toString().trim());
        map.put("last_name", "" + et_lname.getText().toString().trim());
        map.put("email", "" + et_email.getText().toString().trim());
        map.put("plan_id", "0");

        AppUtils.volleyStringRequst("" + getString(R.string.APP_URL_REGISTER), context, map, new Listener_GetApiData() {
            @Override
            public void getData(String result) {
                Log.e("Response--->", "Response-->" + result);

                if (result != null)
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);

                        String status = jsonObject1.getString("status");
                        String message = jsonObject1.getString("message");
                        if (status.equalsIgnoreCase("success")) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (status.equalsIgnoreCase("fail")) {
                            AppUtils.showDialogOkButton(context, message, "OK");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    private void findById() {
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        tv_not_a_member = findViewById(R.id.tv_not_a_member);
        bt_register = findViewById(R.id.bt_register);
    }

    public boolean checkValidation() {
        if (et_fname.getText().toString().trim().length() <= 0) {
            et_fname.setError("Please enter first name.");
            et_fname.requestFocus();
        } else if (et_lname.getText().toString().trim().length() <= 0) {
            et_lname.setError("Please enter last name.");
            et_lname.requestFocus();
        } else if (et_email.getText().toString().trim().length() <= 0) {
            et_email.setError("Please enter email address.");
            et_email.requestFocus();
        } else if (!AppUtils.isValidEmail(et_email.getText().toString().trim())) {
            et_email.setError("Please enter valid email address.");
            et_email.requestFocus();
        } else if (et_mobile.getText().toString().trim().length() <= 0) {
            et_mobile.setError("Please enter mobile number.");
            et_mobile.requestFocus();
        } else if (et_password.getText().toString().trim().length() <= 0) {
            et_password.setError("Please enter password.");
            et_password.requestFocus();
        } else if (et_confirm_password.getText().toString().trim().length() <= 0) {
            et_confirm_password.setError("Please enter confirm password.");
            et_confirm_password.requestFocus();
        } else if (!et_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())) {
            et_confirm_password.setError("Password doesn't match.");
            et_confirm_password.requestFocus();
        } else {
            return true;
        }
        return false;
    }
}
