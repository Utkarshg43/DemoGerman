package com.app_1apruefung.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    public static Fragment fragment;
    private static AppController mInstance;
    private static Context context;
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    public static String getPackageName;
    private static SharedPreferences login_reg_user_data;
    public static boolean plan_change=false;
    public static String qus="0";


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        getPackageName = getApplicationContext().getPackageName();

//        ACRA.init(this);

        try {
            mInstance = this;
            initSharedPreferences();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Context getContext() {
        return context;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    private void initSharedPreferences() {
        try {
//-----------------------------
            login_reg_user_data = getApplicationContext().getSharedPreferences("sharedPref_login_reg_user_data", Context.MODE_PRIVATE);
//-----------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static synchronized SharedPreferences getLoginLogout() {
        return login_reg_user_data;
    }
}
