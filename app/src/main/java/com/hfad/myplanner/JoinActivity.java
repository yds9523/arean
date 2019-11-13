package com.hfad.myplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {

    String T_NUM;
    private List<String> tlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent  = getIntent();
        final int U_NUM = intent.getIntExtra("U_NUM",0);
        final String C_NUM = intent.getStringExtra("C_NUM");
        final int MEMBER = intent.getIntExtra("MEMBER",0);
        final int u = intent.getIntExtra("u",0);
        final String ID = intent.getStringExtra("ID");


        final EditText gamename = (EditText)findViewById(R.id.game_name);
        TextView teamtext = (TextView)findViewById(R.id.teamtext);
        final Spinner Teamlist = (Spinner)findViewById(R.id.teamlist);
        Button createteam = (Button)findViewById(R.id.createteam);
        Button joincontest = (Button)findViewById(R.id.join_contest2);

        if(MEMBER == 1){
            T_NUM = "";
            Teamlist.setVisibility(View.INVISIBLE);
            createteam.setVisibility((View.INVISIBLE));
            teamtext.setVisibility((View.INVISIBLE));
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    final TeamAdapter teamlist = new TeamAdapter();
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        String t_num = order.getString("T_NUM");
                        String teamname = order.getString("T_NAME");
                        String c_num = order.getString("C_NUM");
                        teamlist.addItem(t_num,teamname,c_num);
                        tlist.add(teamname);
                    }
                    Teamlist.setAdapter(teamlist);

                    Teamlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            T_NUM = teamlist.getT_NUM(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            T_NUM = "";
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        TeamRequest teamRequst = new TeamRequest(C_NUM,responseListener);
        RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
        queue.add(teamRequst);

        joincontest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String P_NAME = gamename.getText().toString();
                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                if(U_NUM != u) {
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("player" + ID.substring(0, ID.indexOf('@')), uid);
                                    //map.put("uid",uid);
                                    FirebaseDatabase.getInstance().getReference().child("Contest").child(C_NUM).updateChildren(map);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                builder.setMessage("참가신청이 완료되었습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                builder.setMessage("이미 참가신청한 대회입니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
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

                JoinRequest joinRequst = new JoinRequest(P_NAME, U_NUM,C_NUM,T_NUM, responseListener1);
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                queue.add(joinRequst);
            }
        });

        createteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(JoinActivity.this,CreateTeamActivity.class);
                intent1.putExtra("C_NUM",C_NUM);
                intent1.putExtra("U_NUM",U_NUM);
                intent1.putExtra("MEMBER",MEMBER);
                intent1.putExtra("ID",ID);
                intent1.putExtra("u",u);
                intent1.putStringArrayListExtra("tlist", (ArrayList<String>) tlist);
                startActivity(intent1);
            }
        });
    }
}
