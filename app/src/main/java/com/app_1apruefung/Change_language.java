package com.app_1apruefung;

import android.app.Activity;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by dhiraj.kumar on 9/15/2016.
 */
public class Change_language {
    boolean is_language_called = false;

//    public boolean reload(Activity activity, String language_code, String language) {
//        Locale locale = new Locale(language_code.toLowerCase());
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
//        Intent intent = activity.getIntent();
//        activity.overridePendingTransition(0, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        activity.overridePendingTransition(0, 0);
//        AppController_SmartSeha.getLogin_reg_user_data().edit().putString("language", language).putString("language_code", language_code).apply();
//        activity.startActivity(intent);
//        activity.finish();
//        return true;
//    }

    public static boolean reload_splash(Activity activity, String language_code) {
        Locale locale = new Locale(language_code.toLowerCase());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
//        Intent intent = new Intent(activity, activity_second.getClass());
//        activity.overridePendingTransition(0, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        activity.overridePendingTransition(0, 0);
//        if (!is_language_called) {
//            AppController_SmartSeha.getLogin_reg_user_data().edit().putString("language", language).putString("language_code", language_code).apply();
//            activity.startActivity(intent);
//            activity.finish();
//            is_language_called = true;
//        }
        return true;
    }
}