package com.app_1apruefung;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CardSwipeAdapter extends BaseAdapter {

    private ArrayList<IntroModel> data;
    private Context context;
    boolean flag_image;
    GestureDetector mGestureDetector;
    IntroModel introModel;


    public CardSwipeAdapter(ArrayList<IntroModel> data, Context context, IntroModel introModel, boolean flag_image) {
        this.data = data;
        this.context = context;
        this.introModel = introModel;
        this.flag_image = flag_image;
        Log.e("CardSwipeAdapter", "flag_image: " + flag_image);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public IntroModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // normally use a viewholder
            v = inflater.inflate(R.layout.test_card2, parent, false);
        }

        final ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
        final ImageView iv_close = (ImageView) v.findViewById(R.id.iv_close);
        final TextView textView = (TextView) v.findViewById(R.id.sample_text);
        final TextView tv_bottom = (TextView) v.findViewById(R.id.tv_bottom);
        CardView card_view = (CardView) v.findViewById(R.id.card_view);
        LinearLayout ll_main = v.findViewById(R.id.ll_main);
        RelativeLayout rl_main = v.findViewById(R.id.rl_main);
        Log.e("flag_image", "flag_image: " + flag_image);
        Log.e("getCount", "getCount: " + getCount());
        if (flag_image) {
            textView.setVisibility(View.GONE);
            tv_bottom.setVisibility(View.GONE);
//            Glide.with(context).load(getItem(position).image).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            iv_close.setVisibility(View.VISIBLE);
        } else {
            Log.e("else position", "getView: " + position);
            textView.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);
//            textView.setText(getItem(position).question);
            textView.setText(introModel.question);
            imageView.setVisibility(View.GONE);
            iv_close.setVisibility(View.GONE);
        }
       /* if (flag_image) {

            textView.setVisibility(View.GONE);
            tv_bottom.setVisibility(View.GONE);
//            Glide.with(context).load(getItem(position).image).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            iv_close.setVisibility(View.VISIBLE);

        } else {
            textView.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);
            textView.setText(getItem(position).question);
            imageView.setVisibility(View.GONE);
            iv_close.setVisibility(View.GONE);
        }*/
//        textView.setText(getItem(position).question);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_image = false;
                imageView.setVisibility(View.GONE);
                iv_close.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                tv_bottom.setVisibility(View.VISIBLE);
                textView.setText(introModel.question);
            }
        });

        tv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item_answer", "onClick: " + getItem(position).answer);
                Log.e("item_question", "onClick: " + getItem(position).question);
                context.startActivity(new Intent(context, QuestionActivity.class).putExtra("question", getItem(position).question)
                        .putExtra("answer", getItem(position).answer));
            }
        });


        return v;
    }


}