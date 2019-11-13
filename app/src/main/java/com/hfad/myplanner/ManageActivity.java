package com.hfad.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        final ListView joinlist = (ListView)findViewById(R.id.joinlist);
        final ListView createlist = (ListView)findViewById(R.id.createlist);

        Intent intent  = getIntent();
        final String ID = intent.getStringExtra("ID");
        final int U_NUM = intent.getIntExtra("U_NUM",0);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    final ListViewAdapter adapter = new ListViewAdapter();
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        String S_MEMBER = "";
                        String name = order.getString("C_NAME");
                        String category = order.getString("CATEGORY");
                        String csdate = order.getString("CSDATE");
                        String jsdate = order.getString("JSDATE");
                        String jedate = order.getString("JEDATE");
                        String c_text = order.getString("C_TEXT");
                        String C_NUM = order.getString("C_NUM");
                        String FORM =  order.getString("FORM");
                        String location = order.getString("LOCATION");
                        int MEMBER = order.getInt("MEMBER");
                        if(MEMBER != 1){
                            S_MEMBER = MEMBER + "인 팀전" ;
                        }else {
                            S_MEMBER = "개인전";
                        }
                        int u = order.getInt("U_NUM");

                        adapter.addItem(name, category, csdate, jsdate, jedate, c_text, C_NUM, S_MEMBER, MEMBER, FORM,location, u );
                    }
                    createlist.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ManageActivity.this, ContestActivity.class);
                            intent.putExtra("name", adapter.getName(position));
                            intent.putExtra("category", adapter.getCategory(position));
                            intent.putExtra("csdate", adapter.getCSDATE(position));
                            intent.putExtra("jsdate", adapter.getJSDATE(position));
                            intent.putExtra("jedate", adapter.getJEDATE(position));
                            intent.putExtra("c_text", adapter.getC_TEXT(position));
                            intent.putExtra("C_NUM", adapter.getC_NUM(position));
                            intent.putExtra("U_NUM",U_NUM);
                            intent.putExtra("S_MEMBER", adapter.getS_MEMBER(position));
                            intent.putExtra("FORM", adapter.getFORM(position));
                            intent.putExtra("MEMBER",adapter.getMEMBER(position));
                            intent.putExtra("LOCATION",adapter.getLocation(position));
                            intent.putExtra("u",adapter.getU_NUM(position));
                            startActivity(intent);
                        }
                    };
                    createlist.setOnItemClickListener(itemClickListener);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ManageRequest contestRequst = new ManageRequest(U_NUM, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ManageActivity.this);
        queue.add(contestRequst);

        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    final ListViewAdapter adapter = new ListViewAdapter();
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        String S_MEMBER = "";
                        String name = order.getString("C_NAME");
                        String category = order.getString("CATEGORY");
                        String csdate = order.getString("CSDATE");
                        String jsdate = order.getString("JSDATE");
                        String jedate = order.getString("JEDATE");
                        String c_text = order.getString("C_TEXT");
                        String C_NUM = order.getString("C_NUM");
                        String FORM =  order.getString("FORM");
                        String location = order.getString("LOCATION");
                        int MEMBER = order.getInt("MEMBER");
                        if(MEMBER != 1){
                            S_MEMBER = MEMBER + "인 팀전" ;
                        }else {
                            S_MEMBER = "개인전";
                        }
                        int u = order.getInt("U_NUM");

                        adapter.addItem(name, category, csdate, jsdate, jedate, c_text, C_NUM, S_MEMBER, MEMBER, FORM,location, u );
                    }
                    joinlist.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ManageActivity.this, ContestActivity.class);
                            intent.putExtra("name", adapter.getName(position));
                            intent.putExtra("category", adapter.getCategory(position));
                            intent.putExtra("csdate", adapter.getCSDATE(position));
                            intent.putExtra("jsdate", adapter.getJSDATE(position));
                            intent.putExtra("jedate", adapter.getJEDATE(position));
                            intent.putExtra("c_text", adapter.getC_TEXT(position));
                            intent.putExtra("C_NUM", adapter.getC_NUM(position));
                            intent.putExtra("U_NUM",U_NUM);
                            intent.putExtra("ID", ID);
                            intent.putExtra("S_MEMBER", adapter.getS_MEMBER(position));
                            intent.putExtra("FORM", adapter.getFORM(position));
                            intent.putExtra("MEMBER",adapter.getMEMBER(position));
                            intent.putExtra("LOCATION",adapter.getLocation(position));
                            intent.putExtra("u",adapter.getU_NUM(position));
                            startActivity(intent);

                        }
                    };
                    joinlist.setOnItemClickListener(itemClickListener);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ManageRequest2 contestRequst2 = new ManageRequest2(U_NUM, responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(ManageActivity.this);
        queue.add(contestRequst2);
    }
}
