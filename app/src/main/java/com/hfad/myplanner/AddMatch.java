package com.hfad.myplanner;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddMatch extends AppCompatActivity {

    int Y ,M, D; //대회 시작 날짜 변수
    TextView t_date;
    String date;
    String C_NUM;
    int MEMBER;
    String name;
    EditText title;
    Button auto;
    int check;
    Spinner t1,t2;
    Intent intent;
    Button add;
    String T1,T2;
    int G_NUM;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        intent = getIntent();
        C_NUM = intent.getStringExtra("C_NUM");
        MEMBER = intent.getIntExtra("MEMBER",0);
         title = (EditText) findViewById(R.id.g_name);
        t_date = (TextView) findViewById(R.id.g_date);
        t1 = (Spinner) findViewById(R.id.t1);
        t2 = (Spinner) findViewById(R.id.t2);
        add = (Button) findViewById(R.id.addmatch);
        auto = (Button)findViewById(R.id.auto);
        check = intent.getIntExtra("check",0);
        T1 = intent.getStringExtra("T1");
        T2 = intent.getStringExtra("T2");
        G_NUM = intent.getIntExtra("G_NUM",0);


        Calendar calCSD = new GregorianCalendar();
        Y = calCSD.get(Calendar.YEAR);
        M = calCSD.get(Calendar.MONTH);
        D = calCSD.get(Calendar.DAY_OF_MONTH);

        if (check == 0) {
            CSDUpdate();
            date = t_date.getText().toString();
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    list = new ArrayList<String>() ;
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        String t = order.getString("T");
                        list.add(t);
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            AddMatch.this, android.R.layout.simple_list_item_1,list);
                    t1.setAdapter(adapter);
                    t2.setAdapter(adapter);

                    if(check ==1 ) {
                        int pos1 = list.indexOf(T1);
                        int pos2 = list.indexOf(T2);
                        t1.setSelection(pos1);
                        t2.setSelection(pos2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AddMatchRequest contestRequst = new AddMatchRequest(C_NUM,MEMBER,responseListener);
        RequestQueue queue = Volley.newRequestQueue(AddMatch.this);
        queue.add(contestRequst);

        if(check == 1){
            auto.setVisibility(View.GONE);
            add.setText("수정하기");
            title.setText(intent.getStringExtra("TITLE"));
            t_date.setText(intent.getStringExtra("DATE"));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = title.getText().toString();
                String text1 = t1.getSelectedItem().toString();
                String text2 = t2.getSelectedItem().toString();

                if (text1 == text2) {
                    Toast.makeText(AddMatch.this, "대전자가 같습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMatch.this);
                                    builder.setMessage("일정을 추가하였습니다")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMatch.this);
                                    builder.setMessage("다시 시도하세요")
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

                    if(check == 0){
                        SelectMatchRequest registerRequest = new SelectMatchRequest(C_NUM, t_date.getText().toString(), name, text1, text2, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AddMatch.this);
                        queue.add(registerRequest);
                    }
                    if(check == 1){
                        UpdateMatchRequest registerRequest = new UpdateMatchRequest(G_NUM,name, t_date.getText().toString() , text1, text2, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AddMatch.this);
                        queue.add(registerRequest);
                    }
                }
            }
        });
    }


    public void mOnClickD(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.g_date_button:
                //여기서 리스너도 등록함
                new DatePickerDialog(AddMatch.this,mDateSetListenerCSD, Y,M,D).show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener mDateSetListenerCSD =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤

                    Y = year;
                    M = month;
                    D = dayOfMonth;

                    CSDUpdate();//화면을 텍스트 뷰에 업데이트
                }
            };

    //텍스트 값을 업데이트 하는 메소드
    void CSDUpdate(){
        t_date.setText(String.format("%d-%02d-%02d" , Y, M +1, D));
    }

    public void AutoMatchButton(View view){
            name = title.getText().toString();
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String truecheck) {
                    try {
                        JSONObject jsonResponse = new JSONObject(truecheck);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMatch.this);
                            builder.setMessage("일정을 추가하였습니다")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMatch.this);
                            builder.setMessage("다시 시도하세요")
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

            RoundRobin rr = new RoundRobin(C_NUM, MEMBER,date, name, responseListener);
            RequestQueue queue = Volley.newRequestQueue(AddMatch.this);
            queue.add(rr);
    }

    public void tournamentButton(View view){
        name = title.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String truecheck) {
                try {
                    JSONObject jsonResponse = new JSONObject(truecheck);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMatch.this);
                        builder.setMessage("일정을 추가하였습니다")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMatch.this);
                        builder.setMessage("토너먼트는 8팀일때 생성할 수 있습니다")
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

        TournamentRequest rr = new TournamentRequest(C_NUM, MEMBER,date, name, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AddMatch.this);
        queue.add(rr);
    }
}
