package com.hfad.myplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Board2Activity extends AppCompatActivity {

    String U_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board2);


        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.title2);
        TextView text = (TextView) findViewById(R.id.text2);
        TextView ID = (TextView)findViewById(R.id.writer2);
        TextView date = (TextView)findViewById(R.id.date);
        final EditText editcm = (EditText)findViewById(R.id.edit_comment);
        Button writecm = (Button)findViewById(R.id.CommentButton);

        title.setText(intent.getStringExtra("B_TITLE"));
        text.setText(intent.getStringExtra("B_TEXT"));
        ID.setText(intent.getStringExtra("ID"));
        date.setText(intent.getStringExtra("B_DATE"));
        final int B_NUM = intent.getIntExtra("B_NUM",0);
        final int U_NUM = intent.getIntExtra("U_NUM",0);
        U_NAME = intent.getStringExtra("U_NAME");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    final CommentAdapter adapter = new CommentAdapter();
                    for (int i = 0; jsonResponse.length() > i; i++) {
                        JSONObject order = jsonResponse.getJSONObject(i);
                        int cm_num = order.getInt("CM_NUM");
                        String cm_text ="   "+ order.getString("CM_TEXT");
                        String ID = order.getString("ID");
                        String date = order.getString("CM_DATE");
                        adapter.addItem(cm_num,cm_text,ID,date);
                    }

                    ListView comment = (ListView) findViewById(R.id.comment);
                    comment.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                            if(adapter.getID(position).equals(U_NAME)){
                                final CharSequence[] item = {"삭제하기"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(Board2Activity.this);
                                builder.setItems(item,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                Toast.makeText(Board2Activity.this, "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                                recreate();
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    DeleteCommentRequest contestRequst1 = new DeleteCommentRequest(adapter.getCM_NUM(position), responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Board2Activity.this);
                                                    queue.add(contestRequst1);
                                            }
                                        });
                                builder.create();
                                builder.show();
                            }
                        }
                    };
                    comment.setOnItemClickListener(itemClickListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        CommentRequest commentRequest = new CommentRequest(B_NUM, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Board2Activity.this);
        queue.add(commentRequest);


        writecm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String CM_TEXT = editcm.getText().toString();

                SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String date1 = format1.format(date);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Board2Activity.this);
                                builder.setMessage("댓글을 작성하였습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                editcm.setText(null);
                                                recreate();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Board2Activity.this);
                                builder.setMessage("댓글 작성에 실패하였습니다.")
                                        .setNegativeButton("다시 시도",null)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                WriteCommentRequest registerRequest = new WriteCommentRequest(CM_TEXT,B_NUM,U_NUM,date1 ,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Board2Activity.this);
                queue.add(registerRequest);
            }
        });

    }
}
