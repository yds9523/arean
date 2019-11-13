package com.hfad.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    String CATEGORY = "전체";
    String TEXT;
    int U_NUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        final String ID = intent.getStringExtra("ID");
        U_NUM = intent.getIntExtra("U_NUM",0);
        TEXT = intent.getStringExtra("TEXT");

        Button search = (Button)findViewById(R.id.search);
        final EditText c_name = (EditText)findViewById(R.id.searh_cname);
        final ListView list = (ListView)findViewById(R.id.search_list);
        final Spinner cateogry = (Spinner)findViewById(R.id.categorylist);


        cateogry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CATEGORY = (String)cateogry.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setVisibility(View.VISIBLE);
                final String C_NAME = c_name.getText().toString();

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
                            list.setAdapter(adapter);
                            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(SearchActivity.this, ContestActivity.class);
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
                            list.setOnItemClickListener(itemClickListener);


                        } catch (Exception e) {
                            Toast.makeText(SearchActivity.this, "해당 대회가 없습니다", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };
                SearchRequest searchRequst = new SearchRequest(C_NAME,CATEGORY,responseListener);
                RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
                queue.add(searchRequst);
            }
        });
        if(TEXT != null){
            c_name.setText(TEXT);
            search.performClick();
        }
    }
}
