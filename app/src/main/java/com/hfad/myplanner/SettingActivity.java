package com.hfad.myplanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
/*
    String ID;

    String C_NAME; // 앞 '대회 이름 : '을 제외한 원래 정보가 들어있음
    TextView name;

    String CATEGORY; // "를 제외한 원래 정보가 들어있음
    TextView category;
    int ca_item; // category에서 선택된 배열을 저장할 변수

    int mYearCSD, mMonthCSD, mDayCSD; //대회 시작 날짜 변수
    int csfirst = 0; // 처음 시작할 때 당일 날짜가 나오지 않도록 해주는 변수
    String CSDATE;
    TextView csdate;

    int mYearJSD, mMonthJSD, mDayJSD; //참가 시작 날짜 변수
    int jsfirst = 0;
    String JSDATE;
    TextView jsdate;

    int mYearJED, mMonthJED, mDayJED; //참가 마감 날짜 변수
    int jefirst = 0;
    String JEDATE;
    TextView jedate;

    String C_TEXT;
    TextView text;

    String FORM;
    int fo_item;
    TextView form;

    int check_member;
    int num_member;
    RadioGroup member;
    EditText text_member;
    String MEMBER;

*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
/*
        name = (TextView) findViewById(R.id.setting_name);
        category = (TextView) findViewById(R.id.setting_category);
        csdate = (TextView) findViewById(R.id.setting_csdate);
        jsdate = (TextView) findViewById(R.id.setting_jsdate);
        jedate = (TextView) findViewById(R.id.setting_jedate);
        text = (TextView) findViewById(R.id.setting_text);
        form = (TextView) findViewById(R.id.setting_form);
        member = (RadioGroup)  findViewById(R.id.member);
        Button board = (Button) findViewById(R.id.BoardButton);
        text_member = (EditText) findViewById(R.id.text_member);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        category.setText(intent.getStringExtra("category"));
        csdate.setText(intent.getStringExtra("csdate"));
        jsdate.setText(intent.getStringExtra("jsdate"));
        jedate.setText(intent.getStringExtra("jedate"));
        text.setText(intent.getStringExtra("c_text"));
        form.setText(intent.getStringExtra("FORM"));
        num_member = intent.getIntExtra("MEMBER",0);
        ID = intent.getStringExtra("ID");

        RadioButton one = (RadioButton) findViewById(R.id.one);
        one.setId(R.id.id_one);
        RadioButton team = (RadioButton) findViewById(R.id.team);
        team.setId(R.id.id_team);
        if(num_member == 1){
            member.check(R.id.id_one);
        }else{
            member.check(R.id.id_team);
            text_member.setText(String.valueOf(num_member));
        }


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

        CSDUpdate();//화면을 텍스트 뷰에 업데이트 해줌 - 대회시작
        JSDUpdate();//화면을 텍스트 뷰에 업데이트 해줌 - 참가시작
        JEDUpdate();//화면을 텍스트 뷰에 업데이트 해줌 - 참가마감

        text.setMovementMethod(new ScrollingMovementMethod()); //textview 내부에서 스크롤을 만들어주는 메소드

    }

    public void seek(View view){
        AlertDialog.Builder seek = new AlertDialog.Builder(SettingActivity.this);

        seek.setTitle("확인");
        seek.setMessage("내용을 변경 하시고, 완료를 누르지 않으셨다면 모든 내용이 삭제됩니다.");
        seek.setPositiveButton("이동", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SettingActivity.this, SeekplayerActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        seek.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        seek.show();
    }

    public void name_change(View view){
        //빌더 선언
        AlertDialog.Builder c_name = new AlertDialog.Builder(SettingActivity.this);
        c_name.setTitle("대회 이름을 다시 설정해 주세요.");

        //빌더 속에 들어갈 에디트텍스트 선언
        final EditText co_name = new EditText(SettingActivity.this);
        //빌더 속에 에디트텍스트 삽입
        c_name.setView(co_name);

        //변경 버튼 생성
        c_name.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //co_name이 공백일 경우
                if(co_name.getText().toString().length() == 0){
                    //공백일 땐 아무일도 일어나지 않음
                    dialog.dismiss();
                }else{
                    //공백이 아니라 속에 무언가가 있을 경우 에디트텍스트 내용으로 바꾼다.
                    C_NAME = co_name.getText().toString();
                    name.setText("대회 이름 : " + C_NAME);
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

    public void category_change(View view){
        AlertDialog.Builder c_category = new AlertDialog.Builder(SettingActivity.this);

        final String arry[] = getResources().getStringArray(R.array.game_list);
        c_category.setTitle("대회 종목을 선택하세요.")
                .setSingleChoiceItems(arry, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 각 리스트를 선택했을때
                                ca_item = whichButton;
                            }
                        }).setPositiveButton("변경",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 변경 버튼 클릭시
                        CATEGORY = arry[ca_item];
                        category.setText("대회 종목 : " + CATEGORY);
                        dialog.dismiss();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소 버튼 클릭시
                        dialog.dismiss();
                    }
                });
        c_category.show();
    }

    public void form_change(View view){
        AlertDialog.Builder c_form = new AlertDialog.Builder(SettingActivity.this);

        final String arry[] = getResources().getStringArray(R.array.form_list);
        c_form.setTitle("대회 형식을 선택하세요.")
                .setSingleChoiceItems(arry, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 각 리스트를 선택했을때
                                fo_item = whichButton;
                            }
                        }).setPositiveButton("변경",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 변경 버튼 클릭시
                        FORM = arry[fo_item];
                        form.setText("대회 형식 : " + FORM);
                        dialog.dismiss();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소 버튼 클릭시
                        dialog.dismiss();
                    }
                });
        c_form.show();
    }




    //대회 시작 날짜 온클릭 메소드
    public void csdate_change(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.csdate_change:
                //여기서 리스너도 등록함
                new DatePickerDialog(SettingActivity.this, mDateSetListenerCSD, mYearCSD, mMonthCSD, mDayCSD).show();
                break;
        }
    }

    //참가 시작 날짜 온클릭 메소드
    public void jsdate_change(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.jsdate_change:
                //여기서 리스너도 등록함
                new DatePickerDialog(SettingActivity.this, mDateSetListenerJSD, mYearJSD, mMonthJSD, mDayJSD).show();
                break;
        }
    }

    //참가 마감 날짜 온클릭 메소드
    public void jedate_change(View view){
        switch(view.getId()){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case R.id.jedate_change:
                //여기서 리스너도 등록함
                new DatePickerDialog(SettingActivity.this, mDateSetListenerJED, mYearJED, mMonthJED, mDayJED).show();
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
        if (csfirst == 0) {
            csfirst++;
        } else {
            CSDATE = String.format("%d-%02d-%02d", mYearCSD, mMonthCSD + 1, mDayCSD);
            csdate.setText("대회 일시 : " + CSDATE);
        }
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
        if (jsfirst == 0) {
            jsfirst++;
        } else {
            JSDATE = String.format("%d-%02d-%02d", mYearJSD, mMonthJSD + 1, mDayJSD);
            jsdate.setText("참가 시작 : " + JSDATE);
        }
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
        if (jefirst == 0) {
            jefirst++;
        } else {
            JEDATE = String.format("%d-%02d-%02d", mYearJED, mMonthJED + 1, mDayJED);
            jedate.setText("참가 종료 : " + JEDATE);
        }
    }

    public void text_change(View view){
        //빌더 선언
        AlertDialog.Builder c_text = new AlertDialog.Builder(SettingActivity.this);
        c_text.setTitle("대회 내용을 다시 입력해 주세요.");

        //빌더 속에 들어갈 에디트텍스트 선언
        final EditText co_text = new EditText(SettingActivity.this);
        //빌더 속에 에디트텍스트 삽입
        c_text.setView(co_text);

        //변경 버튼 생성
        c_text.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //co_name이 공백일 경우
                if(co_text.getText().toString().length() == 0){
                    //공백일 땐 아무일도 일어나지 않음
                    dialog.dismiss();
                }else{
                    //공백이 아니라 속에 무언가가 있을 경우 에디트텍스트 내용으로 바꾼다.
                    C_TEXT = co_text.getText().toString();
                    text.setText(C_TEXT);
                    dialog.dismiss();
                }
            }
        });
        //취소를 누르면 아무일 없음
        c_text.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        c_text.show();
    }

    //개인전 선택했을 경우 팀인원 적는 에디터에 대한 포커스를 얻지 못함.
    public void one(View view){
        check_member = 1;
        text_member.setText(null);
        text_member.setFocusableInTouchMode(false);
        text_member.setFocusable(false);
        num_member = check_member;
        MEMBER = String.valueOf(num_member);
    }

    //팀전을 선택했을 경우 팀 인원을 적을 수 있다.
    public void team(View view){
        check_member = 0;
        text_member.setFocusableInTouchMode(true);
    }

    public void onBack(View view){
        finish();
    }
    public void onChangeContest(View view){

        if(num_member != 1){
            MEMBER = text_member.getText().toString();
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        AlertDialog.Builder check = new AlertDialog.Builder(SettingActivity.this);
                        check.setTitle("확인");
                        check.setMessage("정말로 변경 하시겠습니까?");
                        check.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                                //FLAG_ACTIVITY_CLEAR_TOP은 새로 생성하려는 액티비티와 동일한 액티비티가 스택에 있을 경우
                                //동일한 액티비티 위의 모든 액티비티를 종료시키고 기존 액티비티를 새로 생겅된 액티비티로
                                //교체하는 플래그
                                //intent.putExtra("ID", ID);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                            }
                        });
                        check.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        builder.setMessage("대회생성에 실패하였습니다")
                                .setNegativeButton("다시 시도",null)
                                .create()
                                .show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        ContestRequest contestRequest = new ContestRequest(ID,C_NAME,CATEGORY,CSDATE, JSDATE,JEDATE,C_TEXT,FORM,MEMBER, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
        queue.add(contestRequest);
        */
    }
}
