package com.zhengxinpeng.nct;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhengxinpeng.nct.model.Question;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class StudyActivity extends AppCompatActivity {

    private TextView tv_title;
    private RadioGroup rg_select;
    private RadioButton rb_a;
    private RadioButton rb_b;
    private RadioButton rb_c;
    private RadioButton rb_d;
    private TextView tv_analysis;
    private Button btn_sure;
    private Button btn_next;

    private List<Question> list_question = new ArrayList<>();
    private String mHsfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        tv_title = findViewById(R.id.tv_title);
        rg_select = findViewById(R.id.rg_select);
        rb_a = findViewById(R.id.rb_a);
        rb_b = findViewById(R.id.rb_b);
        rb_c = findViewById(R.id.rb_c);
        rb_d = findViewById(R.id.rb_d);
        tv_analysis = findViewById(R.id.tv_analysis);
        btn_sure = findViewById(R.id.btn_sure);
        btn_next = findViewById(R.id.btn_next);

        String id = getIntent().getStringExtra("id");

        loadQuestions(id);
        for (int i = 0; i < rg_select.getChildCount(); i++) {
            rg_select.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        //选择a
                        case R.id.rb_a:
                            setHcfs(v, "a");
                            break;
                        //  选择b
                        case R.id.rb_b:
                            setHcfs(v, "b");
                            break;
                        //      选择c
                        case R.id.rb_c:
                            setHcfs(v, "c");
                            break;
                        //    选择d
                        case R.id.rb_d:
                            setHcfs(v, "d");
                            break;
                    }
                }
            });
        }
    }
    private void setHcfs(View view, String hcfs) {
        if (hcfs.equals(mHsfs)) {
            rg_select.clearCheck();
            mHsfs = "";
        } else {
            mHsfs = hcfs;
            rg_select.check(view.getId());
        }

    }
    private void loadQuestions(String id) {
        BmobQuery<Question> query = new BmobQuery<>();
        query.addWhereEqualTo("bank", id);
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null && list != null) {
                    list_question.addAll(list);
                    if (!list_question.isEmpty()) {
                        showQuestion(0);
                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showQuestion(final int index) {
        Question q = list_question.get(index);

        tv_title.setText(index + 1 + "." + q.getTitle());
        rb_a.setText(q.getSelectA());
        rb_b.setText(q.getSelectB());
        rb_c.setText(q.getSelectC());
        rb_d.setText(q.getSelectD());

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String select = "";

                if (rg_select.getCheckedRadioButtonId() == R.id.rb_a) {
                    select = "A";
                } else if (rg_select.getCheckedRadioButtonId() == R.id.rb_b) {
                    select = "B";
                }
                if (rg_select.getCheckedRadioButtonId() == R.id.rb_c) {
                    select = "C";
                }
                if (rg_select.getCheckedRadioButtonId() == R.id.rb_d) {
                    select = "D";
                }

                if (select.equals("")) {
                    Toast.makeText(StudyActivity.this, "请选择一项选项", Toast.LENGTH_SHORT).show();
                } else {
                    if (select.equals(q.getAnswer())) {
                        Toast.makeText(StudyActivity.this, "答对了", Toast.LENGTH_SHORT).show();
                        rg_select.clearCheck();
                        if (index < list_question.size() - 1) {
                            // 加载下一题
                            showQuestion(index + 1);
                            tv_analysis.setText("");
                        }
                        else{
                            Toast.makeText(StudyActivity.this, "最后一题了", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StudyActivity.this, "答错了", Toast.LENGTH_SHORT).show();
                        tv_analysis.setText(q.getAnalysis());
                    }
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String select = "";

                if (rg_select.getCheckedRadioButtonId() == R.id.rb_a) {
                    select = "A";
                } else if (rg_select.getCheckedRadioButtonId() == R.id.rb_b) {
                    select = "B";
                }
                if (rg_select.getCheckedRadioButtonId() == R.id.rb_c) {
                    select = "C";
                }
                if (rg_select.getCheckedRadioButtonId() == R.id.rb_d) {
                    select = "D";
                }

                if (select.equals("")) {
                    Toast.makeText(StudyActivity.this, "请选择一项选项", Toast.LENGTH_SHORT).show();
                }
                else if (index < list_question.size() - 1)
                {
                    showQuestion(index + 1);
                    rg_select.clearCheck();
                }
                else{
                    Toast.makeText(StudyActivity.this, "最后一题了", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}