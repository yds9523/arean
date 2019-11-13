package com.hfad.myplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateBoardActivity extends AppCompatActivity {
    //알람을 위해 필요한 것들
    //데이터베이스에서 정보를 빼오기 위해
    private FirebaseDatabase database;
    //uid정보를 담는 리스트
    private List<String> uidlists = new ArrayList<>();
    //token정보를 담는 리스트
    private List<String> tokenlists = new ArrayList<>();
    private int indexs;
    private int elemental;
    private int i;
    //단체 알람을 위해 JSONArray형태로 보내야 한다.
    private JSONArray jsonArray;
    //개최자가 플레이어에게인지 플레이어가 개최자에게 인지 구분하기 위해
    private int pp;


    String NAME;
    String B_TEXT;
    EditText b_text;
    String B_TITLE;
    CheckBox ppCheck;
    String C_NUM;
    int u;
    int B_NUM;


    //이곳으로 보내야 알람이 보내짐(안드로이드->클라우드->안드로이드 인데 클라우드에서 형식에 맞게 정보를 받은 뒤 알림을 보내줌, 그 클라우드 주소)
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    //내 클라우드 서버키
    private static final String SERVER_KEY = "AIzaSyDX8yKgmmn-yEP5sV_CyD064PdxnIPzUaU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        final EditText b_title = (EditText) findViewById(R.id.b_title);
        b_text = (EditText) findViewById(R.id.b_text);
        Button writeboard = (Button) findViewById(R.id.CreateBoardButton);
        ppCheck = (CheckBox) findViewById(R.id.ppcheck);
        final Intent intent = getIntent();
        u = intent.getIntExtra("u", 0);
        final int U_NUM = intent.getIntExtra("U_NUM", 0);
        C_NUM = intent.getStringExtra("C_NUM");
        NAME = intent.getStringExtra("NAME");
        B_NUM = intent.getIntExtra("B_NUM",0);
        b_title.setText(intent.getStringExtra("TITLE"));
        b_text.setText(intent.getStringExtra("TEXT"));


        if(u != U_NUM){
            ppCheck.setText("질문");
        }


        writeboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B_TITLE = b_title.getText().toString();
                B_TEXT = b_text.getText().toString();

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String date1 = format1.format(date);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                //알림으로 C_NUM을 같이 날려주기 위해 여기다 변수 삽입
                                //MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
                                //myFirebaseMessagingService.P_NUM = C_NUM;
                                if (u == U_NUM && ppCheck.isChecked()) {
                                    B_TITLE = "공지 : " + B_TITLE;
                                    pp = 1;
                                    sendAlarm();
                                }
                                if (u != U_NUM && ppCheck.isChecked()) {
                                    B_TITLE = "질문 : " + B_TITLE;
                                    pp = 0;
                                    sendAlarm();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateBoardActivity.this);
                                builder.setMessage("게시글을 작성하였습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateBoardActivity.this);
                                builder.setMessage("게시글 작성에 실패하였습니다.")
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

                if(B_NUM == 0){
                    WriteBoardRequest registerRequest = new WriteBoardRequest(B_TITLE, B_TEXT, U_NUM, C_NUM, date1, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CreateBoardActivity.this);
                    queue.add(registerRequest);
                }
                else{
                    UpdateBoard registerRequest = new UpdateBoard(B_NUM,B_TITLE,B_TEXT, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CreateBoardActivity.this);
                    queue.add(registerRequest);
                }


            }
        });
    }

    void sendAlarm() {

        database = FirebaseDatabase.getInstance();
        database.getReference().child("Contest").child(C_NUM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidlists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String string = snapshot.getValue(String.class);
                    uidlists.add(string);
                }
                indexs = 0;
                gettoken();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void gettoken() {
        if (indexs == 0) {
            tokenlists.clear();
            elemental = uidlists.size() - 1;
            i = 0;
        }
        database = FirebaseDatabase.getInstance();
        database.getReference().child("users").child(uidlists.get(indexs)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String string = snapshot.getValue(String.class);
                    if (i == 1) {
                        tokenlists.add(string);
                    }
                    i++;

                }

                if (i == 4) {
                    if (indexs == elemental) {
                        sendPushAlarm();
                    } else {
                        indexs++;
                        i = 0;
                        gettoken();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


        void sendPushAlarm () {
            jsonArray = new JSONArray();

            if (pp == 1) {
                for (int a = 1; a < tokenlists.size(); a++) {
                    jsonArray.put(tokenlists.get(a));
                }
            }else{
                jsonArray.put(tokenlists.get(pp));
            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // FMC 메시지 생성 start
                        JSONObject root = new JSONObject();
                        JSONObject notification = new JSONObject();
                        JSONObject data = new JSONObject();
                        notification.put("body", B_TEXT);
                        notification.put("title", B_TITLE);
                        root.put("notification", notification);
                        root.put("data", data);
                        root.put("registration_ids", jsonArray);
                        data.put("P_NUM", C_NUM);

                        //root.put("id_token", idToken);
                        //root.put("to", "ec9RqnwNNxY:APA91bGpVu5L8eN_rX215MLsdClxDdEFLYkLdF7s8xsdUlvvc2kOiQ_WOQV1_CnxmHAaRatr6-otmdzwicId_o4K-wlVg9JKknkn2f_jtZ7vIPQCRyBtDsf1twnSHs2lnyShrCu94-Ky");
                        // FMC 메시지 생성 end

                        URL Url = new URL(FCM_MESSAGE_URL);
                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestProperty("Content-type", "application/json");
                        OutputStream os = conn.getOutputStream();
                        os.write(root.toString().getBytes("utf-8"));
                        os.flush();
                        conn.getResponseCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
}


