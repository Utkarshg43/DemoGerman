package com.app_1apruefung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {
    ImageView iv_back;
    String answer, question;
    TextView tv_ans, tv_qus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        find();
        init();
    }

    private void init() {
        question = getIntent().getStringExtra("question");
        answer = getIntent().getStringExtra("answer");

        tv_ans.setText(answer);
        tv_qus.setText(question);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void find() {
        tv_qus = findViewById(R.id.tv_qus);
        tv_ans = findViewById(R.id.tv_ans);
        iv_back = findViewById(R.id.iv_back);
    }
}
