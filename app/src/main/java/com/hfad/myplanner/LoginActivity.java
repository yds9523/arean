//로그인 자바 파일
package com.hfad.myplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private final String BROADCAST_MESSAGE = "com.example.limky.broadcastreceiver.gogo";

    private EditText idText;
    private EditText pwText;
    private CheckBox checkBox;
    private Button loginButton;
    String name;
    int i;

    static SharedPreferences auto;

    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idText = (EditText) findViewById(R.id.idText);
        pwText = (EditText) findViewById(R.id.pwText);
        loginButton = (Button) findViewById(R.id.loginButon);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        //자동 로그인을 위한 선언
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음엔 아무것도 없으므로 null값을 준다.
        auto.getString("id",null);
        auto.getInt("U_NUM",0);
        auto.getString("pw",null);
        auto.getString("name",null);

        //로그인 버튼을 누르면 로그인 진행
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //아이디와 비밀번호를 editText에서 입력받음
                final String ID = idText.getText().toString();
                final String PW = pwText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {

                                //닉네임을 불러오는 함수
                                //getName();

                                String ID = jsonResponse.getString("ID");
                                Toast.makeText(LoginActivity.this, "로그인되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //FLAG_ACTIVITY_CLEAR_TOP은 새로 생성하려는 액티비티와 동일한 액티비티가 스택에 있을 경우
                                //동일한 액티비티 위의 모든 액티비티를 종료시키고 기존 액티비티를 새로 생겅된 액티비티로
                                //교체하는 플래그
                                intent.putExtra("ID", ID);
                                intent.putExtra("PW", PW);
                                intent.putExtra("U_NAME", jsonResponse.getString("U_NAME"));
                                intent.putExtra("U_NUM",jsonResponse.getInt("U_NUM"));

                                //만일 자동 로그인이 체크 되었다면
                                if(checkBox.isChecked() == true){
                                    SharedPreferences.Editor editor = auto.edit();
                                    editor.putString("id",idText.getText().toString());
                                    editor.putString("pw",pwText.getText().toString());
                                    editor.putString("name", jsonResponse.getString("U_NAME"));
                                    editor.putInt("u_num",jsonResponse.getInt("U_NUM"));
                                    editor.commit();
                                }

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequst loginRequst = new LoginRequst(ID, PW, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequst);

                idText = (EditText) findViewById(R.id.idText);
                pwText = (EditText) findViewById(R.id.pwText);
                checkBox = (CheckBox) findViewById(R.id.checkBox);
                loginButton = (Button) findViewById(R.id.loginButon);

            }
        });


    }

    public void onMembership(View view) {
        Intent intent = new Intent(this, MembershipActivity.class);
        startActivity(intent);
    }

    public void Find_Id(View view){
        Intent intent = new Intent(this, FindIdActivity.class);
        startActivity(intent);
    }

    public void Find_Password(View view){
        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }

    void getName(){

        //디비에서 4번째 아이템에 존재. for문에서 찾기위한 변수
        i = 0;

        //uid를 받아온다.
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();

        database.getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String string = snapshot.getValue(String.class);
                    //4번째 값을 받아온다.
                    if (i == 3) {
                        name = string;
                    }
                    i++;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
