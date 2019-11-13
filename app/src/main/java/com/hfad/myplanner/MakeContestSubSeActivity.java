package com.hfad.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeContestSubSeActivity extends AppCompatActivity {

    String text;
    String C_NUM;
    String C_NAME;
    int alramCheck;
    int u;
    private int elemental;
    private int i;

    private FirebaseDatabase database;
    //uid정보를 담는 리스트
    private List<String> uidlists = new ArrayList<>();
    private int indexs;
    //token정보를 담는 리스트
    private List<String> tokenlists = new ArrayList<>();
    //단체 알람을 위해 JSONArray형태로 보내야 한다.
    private JSONArray jsonArray;

    //이곳으로 보내야 알람이 보내짐(안드로이드->클라우드->안드로이드 인데 클라우드에서 형식에 맞게 정보를 받은 뒤 알림을 보내줌, 그 클라우드 주소)
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    //내 클라우드 서버키
    private static final String SERVER_KEY = "AIzaSyDX8yKgmmn-yEP5sV_CyD064PdxnIPzUaU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contest_sub_se);

        Intent intent = getIntent();
        C_NUM  = intent.getStringExtra("C_NUM");
        if(!C_NUM.equals("null")){
            text = intent.getStringExtra("c_text");

            EditText c_text = (EditText)findViewById(R.id.c_text);
            if(!text.equals("")){
                c_text.setText(text);
            }
        }

    }

    public void onBack(View view){
        finish();
    }
    public void onCreateContest(View v) {


        Intent frontintent = getIntent();
        C_NUM = frontintent.getStringExtra("C_NUM");
        alramCheck = frontintent.getIntExtra("AC", 0);
        C_NAME = frontintent.getStringExtra("C_NAME");
        final int U_NUM = frontintent.getIntExtra("U_NUM", 0);
        final String CATEGORY = frontintent.getStringExtra("CATEGORY");
        final String CSDATE = frontintent.getStringExtra("CSDATE");
        final String JSDATE = frontintent.getStringExtra("JSDATE");
        final String JEDATE = frontintent.getStringExtra("JEDATE");
        final String ADDRESS = frontintent.getStringExtra("ADDRESS");
        final String FORM = frontintent.getStringExtra("FORM");
        final String MEMBER = frontintent.getStringExtra("MEMBER");
        final String S_MEMBER;
        if (MEMBER != "1") {
            S_MEMBER = MEMBER + "인 팀전";
        } else {
            S_MEMBER = "개인전";
        }
        final String NAME = frontintent.getStringExtra("NAME");
        final String ID = frontintent.getStringExtra("ID");
        u = frontintent.getIntExtra("u", 0);

        EditText c_text = (EditText) findViewById(R.id.c_text);

        final String C_TEXT = c_text.getText().toString();

        //요기서 C_TEXT가 비어있으면 토스 보내주고 아니면 대회를 생성시킴.
        if (TextUtils.isEmpty(C_TEXT)) {
            Toast.makeText(this, "대회 정보를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        } else {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {

                            Toast.makeText(MakeContestSubSeActivity.this, "대회정보가 입력되었습니다.", Toast.LENGTH_SHORT).show();
                            if (C_NUM.equals("null")) {

                                //RTDB에 정보 삽입
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("host", uid);
                                //map.put("uid",uid);
                                FirebaseDatabase.getInstance().getReference().child("Contest").child(String.valueOf(jsonResponse.getInt("C_NUM"))).setValue(map);


                                Intent intent = new Intent(MakeContestSubSeActivity.this, MainActivity.class);
                                //FLAG_ACTIVITY_CLEAR_TOP은 새로 생성하려는 액티비티와 동일한 액티비티가 스택에 있을 경우
                                //동일한 액티비티 위의 모든 액티비티를 종료시키고 기존 액티비티를 새로 생겅된 액티비티로
                                //교체하는 플래그
                                intent.putExtra("U_NUM", U_NUM);
                                intent.putExtra("ID",ID);
                                intent.putExtra("NAME",NAME);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                //알람 보내는 함수
                                int iMEMBER = Integer.valueOf(MEMBER);
                                sendAlarm();

                                Intent intent = new Intent(MakeContestSubSeActivity.this, ContestActivity.class);
                                intent.putExtra("name", C_NAME);
                                intent.putExtra("category", CATEGORY);
                                intent.putExtra("csdate", CSDATE);
                                intent.putExtra("jsdate", JSDATE);
                                intent.putExtra("jedate", JEDATE);
                                intent.putExtra("c_text", C_TEXT);
                                intent.putExtra("C_NUM", C_NUM);
                                intent.putExtra("U_NUM", U_NUM);
                                intent.putExtra("ID", ID);
                                intent.putExtra("S_MEMBER", S_MEMBER);
                                intent.putExtra("FORM", FORM);
                                intent.putExtra("MEMBER", iMEMBER);
                                intent.putExtra("LOCATION", ADDRESS);
                                intent.putExtra("u", u);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeContestSubSeActivity.this);
                            builder.setMessage("대회생성에 실패하였습니다")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            if (C_NUM.equals("null")) {
                ContestRequest contestRequest = new ContestRequest(U_NUM, C_NAME, CATEGORY, CSDATE, JSDATE, JEDATE, C_TEXT, FORM, MEMBER, ADDRESS, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MakeContestSubSeActivity.this);
                queue.add(contestRequest);
            } else {
                UpdateContestRequest contestRequest = new UpdateContestRequest(C_NUM, C_NAME, CATEGORY, CSDATE, JSDATE, JEDATE, C_TEXT, FORM, MEMBER, ADDRESS, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MakeContestSubSeActivity.this);
                queue.add(contestRequest);
            }
        }
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

        final String B_TITLE = "참가 대회 정보 갱신";
        final String B_TEXT = "'" + C_NAME + "'" + "의 정보가 갱신되었습니다.";
        jsonArray = new JSONArray();

        for (int a = 1; a < tokenlists.size(); a++) {
            jsonArray.put(tokenlists.get(a));
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    JSONObject data = new JSONObject();
                    notification.put("title", B_TITLE);
                    notification.put("body", B_TEXT);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", jsonArray);
                    data.put("P_NUM", C_NUM);

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