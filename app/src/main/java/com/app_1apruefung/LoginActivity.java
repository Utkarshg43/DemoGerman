package com.app_1apruefung;

import android.app.Dialog;
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

import com.app_1apruefung.utils.AppController;
import com.app_1apruefung.utils.AppUtils;
import com.app_1apruefung.utils.Listener_GetApiData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText et_password, et_email;
    Button bt_login;
    TextView tv_not_a_member, tv_forget_password;
    Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findById();

        initialize();

    }

    private void initialize() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().trim().length() == 0) {
                    et_email.setError("Please enter email");
                    et_email.requestFocus();
                } /*else if (!AppUtils.isValidEmail(et_email.getText().toString().trim())) {
                    et_email.setError("Please enter valid email");
                    et_email.requestFocus();
                }*/ else if (et_password.getText().toString().trim().length() == 0) {
                    et_password.setError("Please enter password");
                    et_password.requestFocus();
                } else
                    checkLogin();
            }
        });
        tv_not_a_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
                finish();
            }
        });
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
                /*startActivity(new Intent(context,RegisterActivity.class));
                finish();*/
            }
        });
    }

    private void findById() {
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);
        tv_not_a_member = findViewById(R.id.tv_not_a_member);
        tv_forget_password = findViewById(R.id.tv_forget_password);
    }

    public void checkLogin() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "" + et_email.getText().toString().trim());
        map.put("password", "" + et_password.getText().toString().trim());

        AppUtils.volleyStringRequst("" + getString(R.string.APP_URL_LOGIN), context, map, new Listener_GetApiData() {
            @Override
            public void getData(String result) {
                Log.e("Response--->", "Response-->" + result);

                if (result != null)
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String message = jsonObject1.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            String plan_id = jsonObject1.optString("plan_id");
                            int plan = Integer.parseInt(plan_id);
                            String user_id = jsonObject1.optString("user_id");
                            AppController.getLoginLogout().edit().putString("isLogin", "1").apply();
                            AppController.getLoginLogout().edit().putString("user_id", user_id).apply();
                            if (plan == 0) {
                                AppController.getLoginLogout().edit().putString("plan_id", "0").apply();
                                AppController.getLoginLogout().edit().putInt("qusNo", 10).apply();
                            } else if (plan == 1) {
                                AppController.getLoginLogout().edit().putString("plan_id", "1").apply();
                                AppController.getLoginLogout().edit().putInt("qusNo", 50).apply();
                            } else if (plan == 2) {
                                AppController.getLoginLogout().edit().putString("plan_id", "2").apply();
                                AppController.getLoginLogout().edit().putInt("qusNo", 100).apply();
                            } else if (plan == 3) {
                                AppController.getLoginLogout().edit().putString("plan_id", "3").apply();
                                AppController.getLoginLogout().edit().putInt("qusNo", 500).apply();
                            } else if (plan == 4) {
                                AppController.getLoginLogout().edit().putString("plan_id", "4").apply();
                                AppController.getLoginLogout().edit().putInt("qusNo", 1200).apply();
                            }

//                        AppController.getLoginLogout().edit().putInt("qusNo", 20).apply();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AppUtils.showDialogOkButton(context, message, "OK");
                        }
                    } catch (
                            JSONException e)

                    {
                        e.printStackTrace();
                    }
            }
        });
    }

    private void showForgotPasswordDialog() {
        final Dialog dialog = new Dialog(context, R.style.dialogTheme);
        dialog.setContentView(R.layout.inflate_otp);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_message = dialog.findViewById(R.id.tv_message);
        final EditText et_email = dialog.findViewById(R.id.et_email);

//        tv_message.setText(message);
//        et_otp.setText(otp);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_email.getText().toString().trim().length() <= 0) {
                    Toast.makeText(LoginActivity.this, "Email eingeben", Toast.LENGTH_SHORT).show();
                } else {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    checkEmailAPI(et_email.getText().toString().trim());
                }
//                startActivity(new Intent(getActivity(), ServiceDetailsActivity.class));
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void checkEmailAPI(String email) {
        Map<String, String> map = new HashMap<>();
        map.put("email", "" + email);
        Log.e("Login", "checkEmailAPI: "+email );
        AppUtils.volleyStringRequst("" + getString(R.string.APP_URL_ForgotPassword), context, map, new Listener_GetApiData() {
            @Override
            public void getData(String result) {
                Log.e("Response--->", "Response-->" + result);

                if (result != null)
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String message = jsonObject1.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            String password_token = jsonObject1.getString("password_token");

                            startActivity(new Intent(context, ChangePasswordActivity.class).putExtra("password_token", password_token));

                        } else {
                            AppUtils.showDialogOkButton(context, message, "OK");
                        }
                    } catch (
                            JSONException e)

                    {
                        e.printStackTrace();
                    }
            }
        });
    }
}
