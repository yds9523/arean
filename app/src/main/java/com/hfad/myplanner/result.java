package com.hfad.myplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class result extends AppCompatActivity {

    String result,C_NUM;

    String t1, t2;
    String winner;

    int to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        final int g_num = intent.getIntExtra("G_NUM",0);
        t1 = intent.getStringExtra("T1");
        t2 = intent.getStringExtra("T2");
        to = intent.getIntExtra("to",0);
        C_NUM = intent.getStringExtra("C_NUM");

        final TextView T1 = (TextView)findViewById(R.id.t1);
        final TextView T2 = (TextView)findViewById(R.id.t2);
        final EditText R1 = (EditText)findViewById(R.id.t1_result);
        final EditText R2 = (EditText)findViewById(R.id.t2_result);

        T1.setText(t1);
        T2.setText(t2);

        Button resultbutton = (Button)findViewById(R.id.result);
        resultbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(R1.getText())) {
                    Toast.makeText(result.this, T1.getText().toString() + "의 점수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(R2.getText())) {
                    Toast.makeText(result.this, T2.getText().toString() + "의 점수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    int r1 = Integer.parseInt(R1.getText().toString());
                    int r2 = Integer.parseInt(R2.getText().toString());

                    if (r1 == r2) {
                        result = "무승부";
                    }
                    if (r1 > r2) {
                        result = T1.getText().toString() + " 승";
                        winner = T1.getText().toString();
                    }
                    if (r2 > r1) {
                        result = T2.getText().toString() + " 승";
                        winner = T2.getText().toString();
                    }

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(result.this);
                                    builder.setMessage("결과를 입력하였습니다")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(result.this);
                                    builder.setMessage("회원가입입에 실패하였습니다")
                                            .setNegativeButton("다시 시도", null)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .create()
                                            .show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ResultRequest contestRequst = new ResultRequest(g_num, r1, r2, result,to,C_NUM,winner, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(result.this);
                    queue.add(contestRequst);

                }
            }
        });
    }
}
