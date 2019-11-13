package com.hfad.myplanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPageActivity extends AppCompatActivity {

    String ID;
    int U_NUM;
    String NAME;

    private FirebaseDatabase database;

    //탈퇴 회원이 개최한 대회 번호를 찾는다.
    private List<String> deletUserCNUM = new ArrayList<>();
    //전체 C_NUM
    private List<String> DCnum = new ArrayList<>();
    int i = 0;
    String uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();

    static SharedPreferences auto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        U_NUM = intent.getIntExtra("U_NUM",0);
        NAME = intent.getStringExtra("U_NAME");

        TextView MyID = (TextView) findViewById(R.id.MyID);

        MyID.setText(ID);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto.getString("pw",null);
        auto.getString("name",null);




    }

    public void name_change(View view){
        //빌더 선언
        AlertDialog.Builder c_name = new AlertDialog.Builder(MyPageActivity.this);
        c_name.setTitle("닉네임을 다시 설정해 주세요.");

        //빌더 속에 들어갈 에디트텍스트 선언
        final EditText co_name = new EditText(MyPageActivity.this);
        //빌더 속에 에디트텍스트 삽입
        c_name.setView(co_name);

        //변경 버튼 생성
        c_name.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //co_name이 공백일 경우
                if(co_name.getText().toString().length() == 0){
                    //공백일 땐 아무일도 일어나지 않음
                    Toast.makeText(MyPageActivity.this, "아무 것도 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    //공백이 아니라 속에 무언가가 있을 경우 디비에 저장해줘.
                    //내 디비에 넣기위해 uid얻어옴
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //헤쉬형태로 만들어 삽입
                    Map<String,Object> map = new HashMap<>();
                    map.put("userName", co_name.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
                    //메인액티비티로 넘겨주고, 자동로그인에 넣기위해 선언
                    NAME = co_name.getText().toString();
                    //자동로그인에 넣음
                    SharedPreferences.Editor editor = auto.edit();
                    editor.putString("name", NAME);
                    editor.commit();

                    dialog.dismiss();
                }
            }
        });
        //취소를 누르면 아무일 없음
        c_name.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        c_name.show();
    }

    public void pw_change(View view){
        //빌더 선언
        AlertDialog.Builder c_name = new AlertDialog.Builder(MyPageActivity.this);
        c_name.setTitle("비밀번호를 다시 설정해 주세요.\n각각 비밀번호와 재입력이니 같게 입력해주세요.");

        //빌더 속에 들어갈 에디트텍스트 선언
        final EditText co_pw = new EditText(MyPageActivity.this);
        //빌더 속에 에디트텍스트 삽입
        c_name.setView(co_pw);

        //변경 버튼 생성
        c_name.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //co_pw이 공백일 경우
                if(co_pw.getText().toString().length() == 0){
                    //공백일 땐 아무일도 일어나지 않음
                    Toast.makeText(MyPageActivity.this, "아무 것도 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    //비밀번호 디비에 저장해줘.

                    //자동로그인 오토에 넣기위한 선언
                    String pw = co_pw.getText().toString();
                    //자동로그인에 삽입
                    SharedPreferences.Editor editor = auto.edit();
                    editor.putString("pw", pw);
                    editor.commit();


                    dialog.dismiss();

                }
            }
        });
        //취소를 누르면 아무일 없음
        c_name.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        c_name.show();
    }

    public void good_bye(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPageActivity.this);
        builder.setMessage("회원탈퇴 하시겠습니까?" +"\n"+
                "삭제하면 대회정보/게시글은 모두 삭제됩니다.")
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                //deletCnum();
                                                deleteAuth();
                                                logout();
                                                Toast.makeText(MyPageActivity.this, "회원탈퇴 되었습니다..", Toast.LENGTH_SHORT).show();


                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MyPageActivity.this);
                                                builder.setMessage("회원탈퇴를 할 수 없습니다.")
                                                        .setNegativeButton("다시 시도", null)
                                                        .create()
                                                        .show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                DeleteUserRequest loginRequst = new DeleteUserRequest(U_NUM, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MyPageActivity.this);
                                queue.add(loginRequst);
                            }
                        })
                .setNegativeButton("아니오",null)
                .create()
                .show();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
        intent.putExtra("U_NUM", U_NUM);
        intent.putExtra("ID", ID);
        intent.putExtra("U_NAME", NAME);
        startActivity(intent);
    }

    void deleteAuth(){
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });
        database.getReference().child("users").child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                }
            }
        });
    }

    /*void deletCnum(){
        DCnum.clear();
        deletUserCNUM.clear();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Contest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String string = snapshot.getValue(String.class);
                    DCnum.add(string);
                    searchCnum();

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void searchCnum(){

        database = FirebaseDatabase.getInstance();
        database.getReference().child("Contest").child(DCnum.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String host = snapshot.getValue(String.class);
                    if(host == uid){
                        database.getReference().child("Contest").child(DCnum.get(i)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                }
                            }
                        });
                    }
                    i++;
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    void logout(){
        U_NUM = 0;
        ID = null;
        NAME = null;
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = auto.edit();
        editor.clear();
        editor.commit();
        onBackPressed();
    }
}
