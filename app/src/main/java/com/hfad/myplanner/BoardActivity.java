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

public class BoardActivity extends AppCompatActivity {

    String U_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        final int U_NUM = intent.getIntExtra("U_NUM", 0);
        final String C_NUM = intent.getStringExtra("C_NUM");
        final int u = intent.getIntExtra("u", 0);
        final String NAME = intent.getStringExtra("NAME");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        final int U_NUM = intent.getIntExtra("U_NUM",0);
        final String C_NUM = intent.getStringExtra("C_NUM");
        final int u = intent.getIntExtra("u",0);
        final String NAME = intent.getStringExtra("NAME");
        U_NAME = intent.getStringExtra("U_NAME");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    final BoardAdapter adapter = new BoardAdapter();
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        int b_num = order.getInt("B_NUM");
                        String b_title = order.getString("B_TITLE");
                        String b_text = order.getString("B_TEXT");
                        String writer = order.getString("ID");
                        String b_date = order.getString("B_DATE");
                        adapter.addItem(b_num, b_title, b_text, writer, b_date);
                    }
                    ListView board_list = (ListView) findViewById(R.id.board_list);
                    board_list.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener =
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(BoardActivity.this, Board2Activity.class);
                                    intent.putExtra("B_NUM", adapter.getB_NUM(position));
                                    intent.putExtra("B_TITLE", adapter.getB_TITLE(position));
                                    intent.putExtra("B_TEXT", adapter.getB_TEXT(position));
                                    intent.putExtra("B_DATE", adapter.getB_DATE(position));
                                    intent.putExtra("ID", adapter.getID(position));
                                    intent.putExtra("U_NUM", U_NUM);
                                    intent.putExtra("U_NAME",U_NAME);
                                    startActivity(intent);
                                }
                            };
                    board_list.setOnItemClickListener(itemClickListener);
                    AdapterView.OnItemLongClickListener itemLongClickListener =
                            new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    if (adapter.getID(position).equals(U_NAME)){
                                        final CharSequence[] item = {"삭제하기", "수정하기"};
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                                        builder.setTitle("게시글 관리");
                                         builder       .setItems(item,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (which == 0) {
                                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            try {
                                                                                Toast.makeText(BoardActivity.this, "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                                                recreate();
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    };
                                                                    DeleteBoardRequest contestRequst1 = new DeleteBoardRequest(adapter.getB_NUM(position), responseListener);
                                                                    RequestQueue queue = Volley.newRequestQueue(BoardActivity.this);
                                                                    queue.add(contestRequst1);
                                                                }
                                                                if (which == 1) {
                                                                    Intent intent = new Intent(BoardActivity.this,CreateBoardActivity.class );
                                                                    intent.putExtra("B_NUM",adapter.getB_NUM(position));
                                                                    intent.putExtra("TITLE",adapter.getB_TITLE(position));
                                                                    intent.putExtra("TEXT",adapter.getB_TEXT(position));
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                         builder.create();
                                         builder.show();

                                    }
                                    return true;


                                }
                            };
                    board_list.setOnItemLongClickListener(itemLongClickListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        BoardRequest BoardRequest = new BoardRequest(C_NUM, responseListener);
        RequestQueue queue = Volley.newRequestQueue(BoardActivity.this);
        queue.add(BoardRequest);

        Button writebutton  = (Button)findViewById(R.id.WriteButton);
        writebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(U_NUM ==0){
                    Toast.makeText(BoardActivity.this, "로그인해주세요", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent1 = new Intent(BoardActivity.this, CreateBoardActivity.class);
                    intent1.putExtra("C_NUM",C_NUM);
                    intent1.putExtra("U_NUM",U_NUM);
                    intent1.putExtra("u",u);
                    intent1.putExtra("NAME",NAME);
                    intent1.putExtra("position", getIntent().getIntExtra("position", 0));
                    startActivity(intent1);
                }
            }
        });
    }

}
