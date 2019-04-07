package com.app_1apruefung.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    //    public static String AUTH_KEY = "1543E25495A7981C40DG53344F1565C90F048F59027BD9C8C8900D5C78d";
    public static AlertDialog.Builder builder;
    public static AlertDialog alert;

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
    public static void hideKeyboard(Context context, View view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String ParseData2Send(JSONObject jsonObj, String method) {
        JSONObject jsonObjNew = new JSONObject();
        String data = "";
        /*try {
            jsonObjNew.put("data", jsonObj);
            jsonObjNew.put("method_name", "" + method);
            jsonObjNew.put("device_type", "Android");
//            jsonObjNew.put("language_id", "1");
            jsonObjNew.put("device_id", "");
            jsonObjNew.put("device_token", "");
            byte[] theByteArray = jsonObjNew.toString().getBytes();
            data = Base64Convert.encodeBytes(theByteArray);

            Log.e(method + "_request", "" + jsonObjNew.toString());
        } catch (Exception e) {
        }*/
//        data

//        return data;
        return jsonObj.toString();
    }

    public static String checkNullData(String data) {
        return data.equals("null") ? "" : data;
    }

    public static boolean isConnectingToInternet(Context context) {
        boolean isConnect = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return isConnect = true;
                    }
        }
        return isConnect;
    }

    /*public static String getJsnResult(Context context, String response) {
        String result = null;
        try {
            byte[] decodedBytes = Base64Convert.decode(response);
            result = new String(decodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            showDialogOkButton(context, response, "OK");
        }
        return result;
    }*/

    public static void showDialogOkButton(Context context, String message, String btn_text) {
        builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btn_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (alert != null && alert.isShowing())
                            alert.dismiss();
                    }
                });
        //Creating dialog box
        alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!!!");
        alert.show();

//        Toast.makeText(context, "Show Dialog : " + message, Toast.LENGTH_SHORT).show();

    }

    public static void volleyStringRequst(final String url,final Context context, final Map<String, String> map, final Listener_GetApiData getApiData) {
        new CallAllApis(url,context, map, getApiData);
    }


}
