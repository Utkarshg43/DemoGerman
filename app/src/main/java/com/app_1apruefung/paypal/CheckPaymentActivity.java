package com.app_1apruefung.paypal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app_1apruefung.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class CheckPaymentActivity extends AppCompatActivity {
    private static PayPalConfiguration config;
    Context context=CheckPaymentActivity.this;
    public static final int PAYPAL_REQUEST_CODE = 123;
    String paymentAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        config = new PayPalConfiguration()
                .environment(PayPalConfig.CONFIG_ENVIRONMENT)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        try {
            paymentAmount = getIntent().getStringExtra("amount");
//            paymentAmount = "100";
        }catch (Exception e){
            e.printStackTrace();
        }

        getPayment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("CheckPayment", "onActivityResult: CheckPayment");
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
                            Intent i=getIntent();
                            setResult(RESULT_OK,i);
                            finish();
                            Toast.makeText(context,"Payment Done Successfully!!!",Toast.LENGTH_LONG).show();
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
    }
}
