package com.hfad.myplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MakeContestActivity extends AppCompatActivity {
    int mYearCSD, mMonthCSD, mDayCSD; //대회 시작 날짜 변수
    int mYearJSD, mMonthJSD, mDayJSD; //참가 시작 날짜 변수
    int mYearJED, mMonthJED, mDayJED; //참가 마감 날짜 변수

    //날짜 달력을 만들기 위해 전역변수 선언
    TextView mTxtDateCSD;
    TextView mTxtDateJSD;
    TextView mTxtDateJED;

    //대회 이름 빈공간 방지를 위해 전연변수 선언
    EditText contest_name;

    String category;

    Spinner spinner;
    Spinner spinner2;

    //대회 수정 알람 체크
    int alramCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contest);

        alramCheck= 0;

        // 에디터 텍스트 선언
        contest_name = (EditText)findViewById(R.id.contestname);

        //텍스트 뷰 연결 위에서부터 대회 시작 날짜, 신청 시작 날짜, 신청 마감 날짜.
        mTxtDateCSD = (TextView)findViewById(R.id.txtdateCSD);
        mTxtDateJSD = (TextView)findViewById(R.id.txtdateJSD);
        mTxtDateJED = (TextView)findViewById(R.id.txtdateJED);

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언 위에서부터 대회 시작날짜, 신청 시작 날짜, 신청 마감 날짜.
        Calendar calCSD = new GregorianCalendar();
        mYearCSD = calCSD.get(Calendar.YEAR);
        mMonthCSD = calCSD.get(Calendar.MONTH);
        mDayCSD = calCSD.get(Calendar.DAY_OF_MONTH);

        Calendar calJSD = new GregorianCalendar();
        mYearJSD = calJSD.get(Calendar.YEAR);
        mMonthJSD = calJSD.get(Calendar.MONTH);
        mDayJSD = calJSD.get(Calendar.DAY_OF_MONTH);

        Calendar calJED = new GregorianCalendar();
        mYearJED = calJED.get(Calendar.YEAR);
        mMonthJED = calJED.get(Calendar.MONTH);
        mDayJED = calJED.get(Calendar.DAY_OF_MONTH);

        Intent intent  = getIntent();
        contest_name.setText(intent.getStringExtra("C_NAME"));
        mTxtDateCSD.setText(intent.getStringExtra("csdate"));
        mTxtDateJSD.setText(intent.getStringExtra("jsdate"));
        mTxtDateJED.setText(intent.getStringExtra("jedate"));
        category = intent.getStringExtra("category");

        if(contest_name.equals("null")){
            alramCheck = 1;
        }


        if(category.equals("null")){
            CSDUpdate();//화면을 텍스트 뷰에 업데이트 해줌 - 대회시작
            JSDUpdate();//화면을 텍스트 뷰에 업데이트 해줌 - 참가시작
            JEDUpdate();//화면을 텍스트 뷰에 업데이트 해줌 - 참가마감
        }

        spinner = (Spinner)findViewById(R.id.gamelist);
        spinner2 = (Spinner)findViewById(R.id.form_list);

        switch(category){
            case "리그 오브 레전드" : spinner.setSelection(0);
                break;
            case "배틀 그라운드" : spinner.setSelection(1);
                break;
            case "카트 라이더" : spinner.setSelection(2);
                break;
            case "스타크래프트" : spinner.setSelection(3);
                break;
            case "오버워치" : spinner.setSelection(4);
                break;
            case "null": spinner.setSelection(0); break;
        }



        String FOMR = intent.getStringExtra("FORM");
        switch(FOMR){
            case "리그": spinner2.setSelection(1); break;
            case "기타": spinner2.setSelection(2); break;
            default: spinner2.setSelection(0);
        }
    }




    //대회 시작 날짜 온클릭 메소드
    public void mOnClickCSD(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.btnchangedateCSD:
                //여기서 리스너도 등록함
                new DatePickerDialog(MakeContestActivity.this, mDateSetListenerCSD, mYearCSD, mMonthCSD, mDayCSD).show();
                break;
        }
    }

    //참가 시작 날짜 온클릭 메소드
    public void mOnClickJSD(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.btnchangedateJSD:
                //여기서 리스너도 등록함
                new DatePickerDialog(MakeContestActivity.this, mDateSetListenerJSD, mYearJSD, mMonthJSD, mDayJSD).show();
                break;
        }
    }

    //참가 마감 날짜 온클릭 메소드
    public void mOnClickJED(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.btnchangedateJED:
                //여기서 리스너도 등록함
                new DatePickerDialog(MakeContestActivity.this, mDateSetListenerJED, mYearJED, mMonthJED, mDayJED).show();
                break;
        }
    }

    //날짜 대화상자 리스너 부분 - 대회 시작 날짜
    DatePickerDialog.OnDateSetListener mDateSetListenerCSD =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤

                    mYearCSD = year;
                    mMonthCSD = month;
                    mDayCSD = dayOfMonth;

                    CSDUpdate();//화면을 텍스트 뷰에 업데이트
                }
            };

    //텍스트 값을 업데이트 하는 메소드
    void CSDUpdate(){
        mTxtDateCSD.setText(String.format("%d-%02d-%02d" , mYearCSD, mMonthCSD +1, mDayCSD));
    }

    //날짜 대화상자 리스너 부분 - 참가 시작 날짜
    DatePickerDialog.OnDateSetListener mDateSetListenerJSD =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤

                    mYearJSD = year;
                    mMonthJSD = month;
                    mDayJSD = dayOfMonth;

                    JSDUpdate();//화면을 텍스트 뷰에 업데이트
                }
            };

    //텍스트 값을 업데이트 하는 메소드
    void JSDUpdate(){
        mTxtDateJSD.setText(String.format("%d-%02d-%02d" , mYearJSD, mMonthJSD +1, mDayJSD));
    }

    //날짜 대화상자 리스너 부분 - 참가 마감 날짜
    DatePickerDialog.OnDateSetListener mDateSetListenerJED =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤

                    mYearJED = year;
                    mMonthJED = month;
                    mDayJED = dayOfMonth;

                    JEDUpdate();//화면을 텍스트 뷰에 업데이트
                }
            };

    //텍스트 값을 업데이트 하는 메소드
    void JEDUpdate(){
        mTxtDateJED.setText(String.format("%d-%02d-%02d" , mYearJED, mMonthJED +1, mDayJED));
    }

    //현재 페이지의 모든 정보를 string으로 변환시켜 MakeContestSubActivity로 넘겨줌니다.
    public void cOnClick(View view){

        if(TextUtils.isEmpty(contest_name.getText())){
            Toast.makeText(this, "대회 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else {

            Spinner game_list = (Spinner) findViewById(R.id.gamelist);

            String name = contest_name.getText().toString();
            String game = game_list.getSelectedItem().toString();
            String CSD = mTxtDateCSD.getText().toString();
            String JSD = mTxtDateJSD.getText().toString();
            String JED = mTxtDateJED.getText().toString();
            String FORM = spinner2.getSelectedItem().toString();

            Intent frontintent = getIntent();
            int U_NUM = frontintent.getIntExtra("U_NUM",0);
            String C_NUM = frontintent.getStringExtra("C_NUM");
            String ID = frontintent.getStringExtra("ID");
            String NAME = frontintent.getStringExtra("NAME");

            Intent intent = new Intent(this, MakeContestSubActivity.class);

            intent.putExtra("C_NUM",C_NUM);
            intent.putExtra("U_NUM",U_NUM);
            intent.putExtra("C_NAME", name);
            intent.putExtra("CATEGORY", game);
            intent.putExtra("CSDATE", CSD);
            intent.putExtra("JSDATE", JSD);
            intent.putExtra("JEDATE", JED);
            intent.putExtra("FORM", FORM);
            intent.putExtra("ID", ID);
            intent.putExtra("NAME", NAME);
            intent.putExtra("AC", alramCheck);
            if(category.equals("null")){
                intent.putExtra("S_MEMBER","null");
            } else{
                intent.putExtra("S_MEMBER",frontintent.getStringExtra("S_MEMBER"));
                intent.putExtra("MEMBER",frontintent.getIntExtra("MEMBER",0));
                intent.putExtra("c_text",frontintent.getStringExtra("c_text"));
                intent.putExtra("ADDRESS",frontintent.getStringExtra("ADDRESS"));
                intent.putExtra("ID",frontintent.getStringExtra("ID"));
                intent.putExtra("u",frontintent.getIntExtra("u",0));
            }



            startActivity(intent);
        }
    }

    //취소버튼을 누르면 엑티비티가 끝난다.
    public void onCancel(View view){
        finish();
    }


}





