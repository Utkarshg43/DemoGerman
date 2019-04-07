package com.app_1apruefung;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app_1apruefung.utils.AppUtils;
import com.app_1apruefung.utils.Listener_GetApiData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    String password_token = "";
    Context context = ChangePasswordActivity.this;
    private Button bt_save;
    EditText et_confirmPassword, et_newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        findViewByIds();
        initialize();
        getInatent();

    }

    private void initialize() {
        getInatent();
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation())
                    changePasswordAPI();
            }
        });
    }

    private boolean checkValidation() {
        if (et_newPassword.getText().toString().trim().length() <= 0) {
            AppUtils.showToast(context, "Please enter new password.");
            et_newPassword.requestFocus();
        } else if (et_confirmPassword.getText().toString().trim().length() <= 0) {
            AppUtils.showToast(context, "Please enter confirm password.");
            et_confirmPassword.requestFocus();
        } else if (!et_newPassword.getText().toString().trim().equals(et_confirmPassword.getText().toString().trim())) {
            AppUtils.showToast(context, "Password doesn't match.");
            et_confirmPassword.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private void getInatent() {
        try {
            password_token = getIntent().getStringExtra("password_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViewByIds() {
        et_newPassword = findViewById(R.id.et_newPassword);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        bt_save = findViewById(R.id.bt_save);
    }

    public void changePasswordAPI() {
        Map<String, String> map = new HashMap<>();
        map.put("password_token", "" + password_token);
        map.put("password", "" + et_newPassword.getText().toString().trim());
        Log.e("password_token", "password_token: "+password_token );
        Log.e("password", "password_: "+et_newPassword.getText().toString().trim() );

        AppUtils.volleyStringRequst("" + getString(R.string.APP_URL_reset_password), context, map, new Listener_GetApiData() {
            @Override
            public void getData(String result) {
                Log.e("Response--->", "Response-->" + result);

                if (result != null)
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String message = jsonObject1.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            Toast.makeText(context, "Password Reset Successful!!!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(context, LoginActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        } else if (status.equalsIgnoreCase("error")) {
                            AppUtils.showDialogOkButton(context, message, "OK");
                        }
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }
}
