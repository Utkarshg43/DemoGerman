package com.app_1apruefung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app_1apruefung.paypal.CheckPaymentActivity;
import com.app_1apruefung.paypal.PayPalConfig;
import com.app_1apruefung.utils.AppController;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class QuestionwithOptionActivity extends AppCompatActivity {
    ImageView iv_back;
    View view1, view2, view3, view4;
    private static PayPalConfiguration config;
    Context context = QuestionwithOptionActivity.this;
    String paymentAmount = "";
    public static final int PAYPAL_REQUEST_CODE = 123;
    int plan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwith_option);
        find();
        init();
    }

    private void getPayment() {
        //Getting the amount from editText
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "AUD", "Questions Charges", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent1 = new Intent(context, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent1, PAYPAL_REQUEST_CODE);

    }

    @Override
    public void onDestroy() {
        stopService(new Intent(context, PayPalService.class));
        super.onDestroy();
    }

    private void init() {
        config = new PayPalConfiguration()
                .environment(PayPalConfig.CONFIG_ENVIRONMENT)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan = 1;
                paymentAmount = "4.99";
                getPayment();
                /*startActivityForResult(new Intent(QuestionwithOptionActivity.this, CheckPaymentActivity.class)
                        .putExtra("amount", "4.99"), 001);*/
               /* AppController.getLoginLogout().edit().putString("plan_id", "1").apply();
                AppController.getLoginLogout().edit().putInt("qusNo", 50).apply();
                finish();*/

            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan = 2;
                paymentAmount = "8.99";
                getPayment();
              /*  startActivityForResult(new Intent(QuestionwithOptionActivity.this, CheckPaymentActivity.class)
                        .putExtra("amount", "8.99"), 002);*/
                /*AppController.getLoginLogout().edit().putString("plan_id", "2").apply();
                AppController.getLoginLogout().edit().putInt("qusNo", 100).apply();
                finish();*/
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan = 3;
                paymentAmount = "24.90";
                getPayment();
               /* AppController.getLoginLogout().edit().putString("plan_id", "3").apply();
                AppController.getLoginLogout().edit().putInt("qusNo", 500).apply();
                finish();*/
                /*startActivityForResult(new Intent(QuestionwithOptionActivity.this, CheckPaymentActivity.class)
                        .putExtra("amount", "24.90"), 003);*/
            }
        });
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan = 4;
                paymentAmount = "49.90";
                getPayment();
               /* AppController.getLoginLogout().edit().putString("plan_id", "4").apply();
                AppController.getLoginLogout().edit().putInt("qusNo", 1200).apply();
                finish();*/
                /*startActivityForResult(new Intent(QuestionwithOptionActivity.this, CheckPaymentActivity.class)
                        .putExtra("amount", "49.90"), 004);*/
            }
        });
    }

    private void find() {
        iv_back = findViewById(R.id.iv_back);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("QUESTION", "onActivityResult: " + resultCode);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentDetails", "paymentDetails : " + paymentDetails);
                        JSONObject jsonObject = new JSONObject(paymentDetails);


                        JSONObject jsonObject2 = jsonObject.getJSONObject("response");

                        String id = jsonObject2.getString("id");
                        String state = jsonObject2.getString("state");

                        if (state.equalsIgnoreCase("approved")) {
                            if (plan == 1) {
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
                            Intent intent = getIntent();
                            setResult(RESULT_OK, intent);
                            Toast.makeText(context, "Payment Done Successfully!!!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(context, "Payment Not done.", Toast.LENGTH_SHORT).show();
                        }

                        //   call_payment_api(paymentDetails);


                        //    getSavePaypalData(paymentDetails);
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
