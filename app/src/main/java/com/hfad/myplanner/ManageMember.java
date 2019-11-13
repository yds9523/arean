package com.hfad.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageMember extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_member);

        Intent intent = getIntent();
        final String NUM = intent.getStringExtra("C_NUM");
        final int c = intent.getIntExtra("MEMBER", 0);

        final ListView listmember = (ListView) findViewById(R.id.list_player);
        final ListView listteam = (ListView) findViewById(R.id.list_team);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        Button showteam = (Button) findViewById(R.id.showteam);
        Button showplayer = (Button) findViewById(R.id.showplayer);

        if (c == 1) {
            showteam.setVisibility(View.GONE);
            showplayer.setVisibility(View.GONE);
            listteam.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.VISIBLE);

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        ArrayList<String> list = new ArrayList<String>();
                        ArrayAdapter adapter = new ArrayAdapter(ManageMember.this,android.R.layout.simple_list_item_1,list);
                        for (int i = 0; jsonResponse.length() > i; i++) {
                            JSONObject order = jsonResponse.getJSONObject(i);
                            if (c != 1) {
                                String P_NAME = order.getString("P_NAME") + " / " + order.getString("T_NAME");
                                list.add(P_NAME);
                            } else {
                                String P_NAME = order.getString("P_NAME");
                                list.add(P_NAME);
                            }
                        }

                        listmember.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            MemberRequest commentRequest = new MemberRequest(NUM, c, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ManageMember.this);
            queue.add(commentRequest);

        } else {
            showteam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.setVisibility(View.INVISIBLE);
                    listteam.setVisibility(View.VISIBLE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonResponse = new JSONArray(response);
                                ArrayList<String> list = new ArrayList<String>();
                                ArrayAdapter adapter = new ArrayAdapter(ManageMember.this,android.R.layout.simple_list_item_1,list);
                                for (int i = 0; jsonResponse.length() > i; i++) {
                                    JSONObject order = jsonResponse.getJSONObject(i);
                                    String T_NAME = order.getString("T_NAME");
                                    list.add(T_NAME);
                                }

                                listteam.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    TeamRequest commentRequest = new TeamRequest(NUM, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ManageMember.this);
                    queue.add(commentRequest);
                }
            });

            showplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listteam.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonResponse = new JSONArray(response);
                                ArrayList<String> list = new ArrayList<String>();
                                ArrayAdapter adapter = new ArrayAdapter(ManageMember.this,android.R.layout.simple_list_item_1,list);
                                for (int i = 0; jsonResponse.length() > i; i++) {
                                    JSONObject order = jsonResponse.getJSONObject(i);
                                    if (c != 1) {
                                        String P_NAME = order.getString("P_NAME") + " / " + order.getString("T_NAME");
                                        list.add(P_NAME);
                                    } else {
                                        String P_NAME = order.getString("P_NAME");
                                        list.add(P_NAME);
                                    }
                                }

                                listmember.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    MemberRequest commentRequest = new MemberRequest(NUM, c, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ManageMember.this);
                    queue.add(commentRequest);
                }
            });
        }
    }
}
