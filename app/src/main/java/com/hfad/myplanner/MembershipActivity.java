package com.hfad.myplanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.myplanner.model.UserModel;

import org.json.JSONObject;

public class MembershipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        final EditText idtext = (EditText)findViewById(R.id.id_Text);
        final EditText pwtext = (EditText)findViewById(R.id.pw_Text);
        final EditText nametext = (EditText)findViewById(R.id.name_Text);
        final EditText repwtext = (EditText)findViewById(R.id.re_pw_Text);

        Button registerbtton = (Button)findViewById(R.id.registerButton);

        registerbtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //아이디와 비밀번호를 editText에서 입력받음
                final String ID = idtext.getText().toString();
                final String PW = pwtext.getText().toString();
                final String U_NAME = nametext.getText().toString();
                final String RPW = repwtext.getText().toString();

                if (!ID.contains("@") && !ID.contains(".")) {
                    Toast.makeText(getApplicationContext(),"아이디를 주소형식으로 적어주세요.",Toast.LENGTH_LONG).show();
                    return;
                } else if (PW.length() < 5) {
                    Toast.makeText(getApplicationContext(),"비밀번호를 5글자 이상 적어주세요.",Toast.LENGTH_LONG).show();
                    return;
                } else if (!PW.equals(RPW)) {
                    Toast.makeText(getApplicationContext(),"비밀번호 입력과 재입력이 다름니다.",Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(idtext.getText().toString(), pwtext.getText().toString())
                        .addOnCompleteListener(MembershipActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String uid = task.getResult().getUser().getUid();
                                    UserModel userModel = new UserModel();
                                    userModel.userName = U_NAME;
                                    userModel.uid = uid;
                                    userModel.ID = ID;


                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {


                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                                                        builder.setMessage("회원가입에 성공하였습니다.")
                                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        finish();
                                                                    }
                                                                })
                                                                .create()
                                                                .show();
                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                                                        builder.setMessage("회원가입에 실패하였습니다.")
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

                                        RegisterRequest registerRequest = new RegisterRequest(ID, PW, U_NAME, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(MembershipActivity.this);
                                        queue.add(registerRequest);

                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                                    builder.setMessage("회원가입입에 실패하였습니다")
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
                            });

            }

    //취소 버튼을 누르면 이전 로그인 화면으로 돌아간다.
    public void onBack(View view){
        finish();
    }

}

