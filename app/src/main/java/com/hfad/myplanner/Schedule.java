package com.hfad.myplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Schedule extends AppCompatActivity {

    String C_NUM;
    int U_NUM;
    int fc;
    int MEMBER;
    Button Delete;
    Button MySchedule;
    Boolean check = false;
    String checkname;

    ListView matchlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        matchlist = (ListView)findViewById(R.id.schedulelist1);

        Intent intent = getIntent();
        C_NUM = intent.getStringExtra("C_NUM");
        fc = intent.getIntExtra("fc",0);

    }

    //일정 입력하고 다시 포커스를 얻으면 그에대한 정보가 화면에 보여짐.
    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_schedule);

        matchlist = (ListView)findViewById(R.id.schedulelist);

        Intent intent = getIntent();
        C_NUM = intent.getStringExtra("C_NUM");
        fc = intent.getIntExtra("fc",0);
        MEMBER = intent.getIntExtra("MEMBER",0);
        U_NUM = intent.getIntExtra("U_NUM",0);
        Delete = (Button)findViewById(R.id.DeleteAll);
        MySchedule = (Button)findViewById(R.id.MySchedule);
        if(fc == 1){
            Delete.setVisibility(View.VISIBLE);
        }

        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        checkname = jsonResponse.getString("check");
                        MySchedule.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        P_NUMRequest contestRequst1 = new P_NUMRequest(U_NUM,responseListener1);
        RequestQueue queue1 = Volley.newRequestQueue(Schedule.this);
        queue1.add(contestRequst1);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    final MatchAdapter adapter = new MatchAdapter();
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        int g_num = order.getInt("G_NUM");
                        String g_name = order.getString("G_NAME");
                        String g_date = order.getString("G_DATE");
                        String t1 = order.getString("T1");
                        String t2 = order.getString("T2");
                        int r1 = order.getInt("R1");
                        int r2 = order.getInt("R2");
                        int to = order.getInt("to");
                        String result = order.getString("WIN");

                        adapter.addItem(g_name, g_date, t1, t2,g_num, Integer.toString(r1),Integer.toString(r2),result,to);
                    }
                    matchlist.setAdapter(adapter);

                    if(fc ==1){
                        matchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final CharSequence[] item = {"삭제하기","수정하기","결과입력"};

                                AlertDialog.Builder dialog= new AlertDialog.Builder(Schedule.this);
                                dialog.setTitle("일정 관리");
                                dialog.setItems(item,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which == 0){
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                Toast.makeText(Schedule.this, "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                                Intent intent = getIntent();
                                                                finish();
                                                                startActivity(intent);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    DeleteMatchRequest contestRequst1 = new DeleteMatchRequest(adapter.getG_NUM(position),responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Schedule.this);
                                                    queue.add(contestRequst1);
                                                }
                                                if(which ==1) {
                                                    Intent intent = new Intent(Schedule.this, AddMatch.class);
                                                    intent.putExtra("check", 1);
                                                    intent.putExtra("C_NUM",C_NUM);
                                                    intent.putExtra("MEMBER",MEMBER);
                                                    intent.putExtra("G_NUM",adapter.getG_NUM(position));
                                                    intent.putExtra("TITLE",adapter.getName(position));
                                                    intent.putExtra("DATE",adapter.getCategory(position));
                                                    intent.putExtra("T1",adapter.getT1(position));
                                                    intent.putExtra("T2",adapter.getT2(position));

                                                    startActivity(intent);
                                                }
                                                if(which ==2){
                                                    Intent intent1 = new Intent(Schedule.this, result.class);
                                                    intent1.putExtra("G_NUM",adapter.getG_NUM(position));
                                                    intent1.putExtra("T1",adapter.getT1(position));
                                                    intent1.putExtra("T2",adapter.getT2(position));
                                                    intent1.putExtra("to",adapter.getTo(position));
                                                    intent1.putExtra("C_NUM",C_NUM);
                                                    startActivity(intent1);
                                                }
                                            }
                                        });
                                dialog.create();
                                dialog.show();

                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        MatchRequest contestRequst = new MatchRequest(C_NUM,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Schedule.this);
        queue.add(contestRequst);
    }

    public void DeleteBtn(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(Schedule.this);
        builder.setMessage("모든 경기를 삭제하시겠습니까?")
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Toast.makeText(Schedule.this,"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                DeleteAllGame contestRequst = new DeleteAllGame(C_NUM,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Schedule.this);
                                queue.add(contestRequst);
                            }
                        })
                .setNegativeButton("아니오",null)
                .create()
                .show();

    }

    public void MyScheduleBtn(View v){
        Intent intent = new Intent(Schedule.this, Schedule2.class);
        intent.putExtra("check",checkname);
        intent.putExtra("C_NUM",C_NUM);
        startActivity(intent);
    }
}