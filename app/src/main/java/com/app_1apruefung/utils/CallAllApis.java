package com.app_1apruefung.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.app_1apruefung.utils.AppUtils.showDialogOkButton;

public class CallAllApis {
    ProgressDialog progressDialog;

    public CallAllApis(String url, Context context, Map<String, String> map, Listener_GetApiData getApiData) {
        volleyStringRequst(url, context, map, getApiData);
    }

    public void volleyStringRequst(final String url, final Context context, final Map<String, String> mapParams, final Listener_GetApiData getApiData) {
        String tag_json_obj = "json_obj_req";
        Log.e("volleyStringRequst", "volleyStringRequst: "+mapParams.toString() );
        try {
            //if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            // }

            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppUtils.isConnectingToInternet(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "" + url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        //  progressDialog = null;
                    }

                    if (response != null) {
                        String result = null;
                        try {
                            Log.e("encode Response : ", "encode Response : " + response.toString());
//                            Log.e("Response : ", "Response :" + AppUtils.getJsnResult(context, response));
                           /* byte[] decodedBytes = Base64.decode(response, 0);
                            result = new String(decodedBytes);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getApiData.getData(response);
                    } else {
                        showDialogOkButton(context, "Poor Intenet Connection", "OK");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Utils", "Error: " + error);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    showDialogOkButton(context, error.getMessage(), "OK");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                   /* Map<String, String> map = new HashMap<>();
                    map.put("username", "demo@gmail.com");
                    map.put("password", "demo");*/
                    Log.e("Req :--> ", "getParams: "+url+"?"+mapParams.toString() );
                    return mapParams;
                }

               /* @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }*/

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
//                    Log.e("authKey", "getHeaders: " + AppUtils.AUTH_KEY);
//                    headers.put("authKey", "123456");
                    return headers;
                }


            };
            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        } else {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            showDialogOkButton(context, "No Internet Connection", "OK");
        }
    }

}