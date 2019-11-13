package com.hfad.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Schedule2 extends AppCompatActivity {

    String C_NUM;
    int U_NUM;
    int MEMBER;
    String check;

    ListView matchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule2);

        Intent intent = getIntent();
        C_NUM = intent.getStringExtra("C_NUM");
        check = intent.getStringExtra("check");
        matchlist = (ListView)findViewById(R.id.schedulelist1);

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

                        if(t1.equals(check) || t2.equals(check)) {
                            adapter.addItem(g_name, g_date, t1, t2,g_num, Integer.toString(r1),Integer.toString(r2),result,to);
                        }
                    }
                    matchlist.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        MatchRequest contestRequst = new MatchRequest(C_NUM,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Schedule2.this);
        queue.add(contestRequst);
    }
}
