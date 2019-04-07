package com.app_1apruefung;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_1apruefung.utils.AppController;
import com.app_1apruefung.utils.AppUtils;
import com.app_1apruefung.utils.Listener_GetApiData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    ImageView iv_center, iv_help, iv_logout;
    TextView tv_center, tv_second, tv_first;
    Context context = MainActivity.this;
    ArrayList<IntroModel> listData = new ArrayList<>();
    IntroModel introModel;
    int pos = 0;
    Dialog dialog;
    boolean image_flag = true;

    private ViewPager viewPager;
    private ArrayList<Integer> layouts;
    MyViewPagerAdapter myViewPagerAdapter;

//    ArrayList<String> questionList = new ArrayList<>();
//    ArrayList<String> answerList = new ArrayList<>();

    ProgressDialog progressDialog = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        find();

//        readQuestions();
//        readAnswers();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == RESULT_OK) {
            checkQUS();
        }
    }

    private void datawithoutImage() {
        listData.remove(0);
    }


    private void init() {
//        dataWithImage();
//        showProgress();
        checkQUS();


        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                addBottomDots(position);
                Log.e("onPageSelected", "onPageSelected: " + position);
                if (image_flag) {
                    Log.e("image_flag", "onPageSelected: " + image_flag);
                    image_flag = false;
                    datawithoutImage();
                    set_view_pager();

                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        };
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
//        set_view_pager();


        tv_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Log.e("answer", "onClick: " + introModel.answer);
                Log.e("question", "onClick: " + introModel.question);
                startActivity(new Intent(context, QuestionActivity.class).putExtra("question", listData.get(pos).question)
                        .putExtra("answer", listData.get(pos).answer));*/

                String url = "https://www.1a-pruefung.de/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(context, QuestionwithOptionActivity.class), 001);
            }
        });
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_flag = true;
                introModel = new IntroModel();
                introModel.image = R.drawable.information;
                introModel.question = "";
                introModel.type = "image";
                introModel.answer = "";
                introModel.id = "0";
                listData.add(0, introModel);
                set_view_pager();
//                showHelpDialog();
//                startActivity(new Intent(context, HelpActivity.class));
            }
        });
    }

    private void find() {
        iv_center = findViewById(R.id.iv_center);
        iv_logout = findViewById(R.id.iv_logout);

        tv_center = findViewById(R.id.tv_center);
        tv_second = findViewById(R.id.tv_second);
        tv_first = findViewById(R.id.tv_first);
        iv_help = findViewById(R.id.iv_help);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void logoutDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppController.getLoginLogout().edit().clear().apply();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showHelpDialog() {
        dialog = new Dialog(context, R.style.dialogTheme);
        dialog.setContentView(R.layout.inflate_help);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void set_view_pager() {

        layouts = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            layouts.add(R.layout.welcome_slide);

        }
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            pos = position;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts.get(position), container, false);
            TextView tv_title = view.findViewById(R.id.sample_text);
            TextView tv_bottom = view.findViewById(R.id.tv_bottom);
            ImageView iv_close = view.findViewById(R.id.iv_close);
            ImageView iv_info = view.findViewById(R.id.iv_info);
            if (position == 0) {
                if (listData.get(position).type.equals("image")) {
                    iv_close.setVisibility(View.VISIBLE);
                    iv_info.setVisibility(View.VISIBLE);
                    tv_bottom.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                } else {
                    iv_close.setVisibility(View.GONE);
                    iv_info.setVisibility(View.GONE);
                    tv_bottom.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(listData.get(position).question);
                }
            } else {
                iv_close.setVisibility(View.GONE);
                iv_info.setVisibility(View.GONE);
                tv_bottom.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(listData.get(position).question);
            }
           /* if (position == 1) {
                if (listData.get(0).type.equals("image")) {
                    listData.remove(0);
                    Log.e("listData.size", "Removed : " + listData.size());
                    Log.e("layouts.size", "Removed : " + layouts.length);
                    myViewPagerAdapter.notifyDataSetChanged();
                }
            }*/
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(1);
                }
            });

            tv_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("List SIZE", "onClick: " + listData.size() + "Position : " + position);
                    startActivity(new Intent(context, QuestionActivity.class).putExtra("question", listData.get(position).question)
                            .putExtra("answer", listData.get(position).answer));
                }
            });
            container.addView(view);
            return view;
        }

        public void removeView(int index) {
            listData.remove(index);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return layouts.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void checkQUS() {
        Map<String, String> map = new HashMap<>();
        Log.e("plan_id", "plan_id : " + AppController.getLoginLogout().getString("plan_id", ""));
        Log.e("question", "question : " + AppController.getLoginLogout().getInt("qusNo", 0));
        map.put("user_id", "" + AppController.getLoginLogout().getString("user_id", ""));
        map.put("plan_id", "" + AppController.getLoginLogout().getString("plan_id", ""));
        map.put("plan_change", "" + AppController.plan_change);
        map.put("question", "" + AppController.getLoginLogout().getInt("qusNo", 0));

        AppUtils.volleyStringRequst("" + getString(R.string.APP_URL_PLAN), context, map, new Listener_GetApiData() {
            @Override
            public void getData(String result) {
                Log.e("Response--->", "Response-->" + result);

                if (result != null)
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String message = jsonObject1.getString("message");
                        if (status.equalsIgnoreCase("success")) {
                            listData.clear();
                            introModel = new IntroModel();
                            introModel.image = R.drawable.information;
                            introModel.question = "";
                            introModel.type = "image";
                            introModel.answer = "";
                            introModel.id = "0";
                            listData.add(introModel);

                            JSONArray questionsArr = jsonObject1.getJSONArray("questions");
                            if (questionsArr != null) {
                                for (int i = 0; i < questionsArr.length(); i++) {
                                    JSONObject jsonObject = questionsArr.getJSONObject(i);
                                    introModel = new IntroModel();
                                    introModel.question = jsonObject.optString("question");
                                    introModel.answer = jsonObject.optString("answer");
                                    introModel.id = jsonObject.optString("id");
                                    introModel.type = jsonObject.optString("question");
                                    listData.add(introModel);
                                }
                                Log.e("Question List", "getData: " + listData.size());
                                set_view_pager();
                            }
                       /*  Intent intent = new Intent(context, MainActivity.class);
                         startActivity(intent);
                         finish();*/
                        } else if (status.equalsIgnoreCase("error")) {
                            AppUtils.showDialogOkButton(context, message, "OK");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }
}

