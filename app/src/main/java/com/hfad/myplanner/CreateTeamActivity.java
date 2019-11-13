package com.hfad.myplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class CreateTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        Intent intent = getIntent();
        final int U_NUM = intent.getIntExtra("U_NUM",0);
        final String C_NUM = intent.getStringExtra("C_NUM");
        final int MEMBER = intent.getIntExtra("MEMBER",0);
        final int u = intent.getIntExtra("u",0);
        final String ID = intent.getStringExtra("ID");
        final List<String> tlist = intent.getStringArrayListExtra("tlist");

        final EditText teamname = (EditText)findViewById(R.id.teamname);
        Button createteam = (Button)findViewById(R.id.createteambutton);

        createteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String T_NAME = teamname.getText().toString();
                int check = 0;

                if (tlist.size() == 0) {
                    check = 1;
                } else {
                    for (int a = 0; a < tlist.size(); a++) {
                        if (tlist.get(a).equals(T_NAME)) {
                            check = 0;
                            break;
                        } else {
                            check = 1;
                        }
                    }
                }


                if (check == 1) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTeamActivity.this);
                                    builder.setMessage("팀 생성이 완료되었습니다")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(CreateTeamActivity.this, JoinActivity.class);
                                                    intent.putExtra("C_NUM", C_NUM);
                                                    intent.putExtra("U_NUM", U_NUM);
                                                    intent.putExtra("MEMBER", MEMBER);
                                                    intent.putExtra("ID", ID);
                                                    intent.putExtra("u", u);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTeamActivity.this);
                                    builder.setMessage("팀 생성에 실패하였습니다.")
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

                    CreateTeamRequest teamRequest = new CreateTeamRequest(C_NUM, T_NAME, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CreateTeamActivity.this);
                    queue.add(teamRequest);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTeamActivity.this);
                    builder.setMessage("동일한 팀 이름이 있습니다.")
                            .setNegativeButton("다시 시도", null)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
                }


            }
        });
    }
}
