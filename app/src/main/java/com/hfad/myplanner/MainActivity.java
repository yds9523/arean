package com.hfad.myplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //로그인에서 넘겨주는 아이디를 받기위한 변수. 이것이 null이면 비로그인 상태, 아니면 로그인 상태
    int U_NUM;
    String ID;
    String PW;
    String NAME;
    String P_NUM = null;

    private long backKeyPressedTime = 0; // 앱 종료 위한 백 버튼 누른 시간
    private Toast toast; // 첫번째 백 버튼일 때 토스를 보여주기 위한 변수 선언

    //자동 저장 아뒤 비번을 받아올때 필요한 함수
    SharedPreferences auto;
    SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //네이게이션 헤드를 변경하기 위한 메소드
        View nav_header_view = navigationView.getHeaderView(0);

        //위의 메소드로 네비게이션 헤드 속 버튼과 텍스트뷰를 받아옴
        Button Nbutton = (Button) nav_header_view.findViewById(R.id.name_button);
        Button Lbutton = (Button) nav_header_view.findViewById(R.id.login_button);

        //로그인 하면 로그인 액티비티에서 아이디를 인텐트로 보내준다.
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        U_NUM = intent.getIntExtra("U_NUM",0);
        PW = intent.getStringExtra("PW");
        NAME = intent.getStringExtra("U_NAME");

        //자동로그인을 위한 선언
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        //자동 로그인 아이디 받기
        if(ID == null){
           ID = auto.getString("id",null);
           PW = auto.getString("pw",null);
           U_NUM = auto.getInt("u_num",0);
           NAME = auto.getString("name", null);
        }

        Nbutton.setText(NAME);

        if ( U_NUM == 0) {
            //Bimage.setVisibility(View.VISIBLE);
            //Aimage.setVisibility(View.INVISIBLE);
            Lbutton.setVisibility(View.VISIBLE);
            Nbutton.setVisibility(View.INVISIBLE);
        } else if (U_NUM != 0) {
            //Bimage.setVisibility(View.INVISIBLE);
            //Aimage.setVisibility(View.VISIBLE);
            Lbutton.setVisibility(View.INVISIBLE);
            Nbutton.setVisibility(View.VISIBLE);
        }


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
                    ListView contest_list = (ListView) findViewById(R.id.contest_list);
                    contest_list.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity.this, ContestActivity.class);
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
                    contest_list.setOnItemClickListener(itemClickListener);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ContestListRequest contestRequst = new ContestListRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(contestRequst);





        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                            ListView contest_list = (ListView) findViewById(R.id.contest_list);
                            contest_list.setAdapter(adapter);
                            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MainActivity.this, ContestActivity.class);
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
                            contest_list.setOnItemClickListener(itemClickListener);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ContestListRequest contestRequst = new ContestListRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(contestRequst);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        if(ID != null && PW != null) {
            loginEvent();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
            // 2000 milliseconds = 2 seconds
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
            // 현재 표시된 Toast 취소
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                super.onBackPressed();
                toast.cancel();
            }
        }
    }

    // 첫번째 뒤로가기 일 때 토스를 띄어줄 함수(왠지 모르지만 이렇게 안하고 직접하면 안뜸)
    public void showGuide() {
        toast = Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //검색 버튼을 클릭하면 searchview 길이 꽉차게 늘려주기
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        //검색 버튼 틀릭했을 때 searchview에 힌트 추가
        searchView.setQueryHint("게임 대회를 입력하시오");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("TEXT",s);
                intent.putExtra("U_NUM",U_NUM);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //검색버튼 눌렀을 때 이벤트 제어
        if (id == R.id.action_search) {
            //To Do : 검색했을  때 쿼리 구현
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void myPage(View v){

        Intent intent = new Intent(this, MyPageActivity.class);
        intent.putExtra("U_NUM", U_NUM);
        intent.putExtra("ID", ID);
        intent.putExtra("U_NAME", NAME);
        startActivity(intent);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent fintent = getIntent();
        String ID = fintent.getStringExtra("ID");
        //U_NUM = fintent.getIntExtra("U_NUM",0);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("ID", ID);
            intent.putExtra("U_NUM", U_NUM);
            startActivity(intent);
        } else if (id == R.id.nav_make_contest) {
            if (U_NUM == 0) {
                Toast.makeText(MainActivity.this, "로그인해주세요", Toast.LENGTH_SHORT).show();
            } else if (U_NUM != 0) {
                String n = "null";
                //activitiy_main_drawer의 nav_make_contest를 누르면 대회생성 화면으로 넘어간다.
                Intent intent = new Intent(this, MakeContestActivity.class);
                intent.putExtra("U_NUM", U_NUM);
                intent.putExtra("NAME", NAME);
                intent.putExtra("category",n);
                intent.putExtra("FORM",n);
                intent.putExtra("C_NUM",n);
                startActivity(intent);
            }
        } else if (id == R.id.nav_manage) {
            if (U_NUM == 0) {
                Toast.makeText(MainActivity.this, "로그인해주세요", Toast.LENGTH_SHORT).show();
            } else if (U_NUM != 0) {
                Intent intent = new Intent(this, ManageActivity.class);
                intent.putExtra("ID",ID);
                intent.putExtra("U_NUM", U_NUM);
                startActivity(intent);
            }
        } else if (id == R.id.nav_logout) {
            if (U_NUM == 0) {
                Toast.makeText(MainActivity.this, "로그인해주세요", Toast.LENGTH_SHORT).show();
            } else if (U_NUM != 0) {
                    U_NUM = 0;
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences.Editor editor = auto.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //nav_header_main의 로그인을 누르면 Login화면으로 넘어간다.
    public void onLoginButton(View view) {
        //로그인 버튼을 눌러도 네비게이션이 사라짐
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //네이게이션 헤드를 변경하기 위한 메소드
        View nav_header_view = navigationView.getHeaderView(0);

        //위의 메소드로 네비게이션 헤드 속 버튼과 텍스트뷰를 받아옴
        Button Nbutton = (Button) nav_header_view.findViewById(R.id.name_button);
        Button Lbutton = (Button) nav_header_view.findViewById(R.id.login_button);

        //로그인 하면 로그인 액티비티에서 아이디를 인텐트로 보내준다.
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        U_NUM = intent.getIntExtra("U_NUM",0);
        PW = intent.getStringExtra("PW");
        NAME = intent.getStringExtra("U_NAME");

        //자동로그인을 위한 선언
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        //자동 로그인 아이디 받기
        if(ID == null){
            ID = auto.getString("id",null);
            PW = auto.getString("pw",null);
            U_NUM = auto.getInt("u_num",0);
            NAME = auto.getString("name", null);
        }

        Nbutton.setText(NAME);

        if ( U_NUM == 0) {
            Lbutton.setVisibility(View.VISIBLE);
            Nbutton.setVisibility(View.INVISIBLE);
        } else if (U_NUM != 0) {
            //Bimage.setVisibility(View.INVISIBLE);
            //Aimage.setVisibility(View.VISIBLE);
            Lbutton.setVisibility(View.INVISIBLE);
            Nbutton.setVisibility(View.VISIBLE);
        }


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
                    ListView contest_list = (ListView) findViewById(R.id.contest_list);
                    contest_list.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity.this, ContestActivity.class);
                            intent.putExtra("name", adapter.getName(position));
                            intent.putExtra("category", adapter.getCategory(position));
                            intent.putExtra("csdate", adapter.getCSDATE(position));
                            intent.putExtra("jsdate", adapter.getJSDATE(position));
                            intent.putExtra("jedate", adapter.getJEDATE(position));
                            intent.putExtra("c_text", adapter.getC_TEXT(position));
                            intent.putExtra("C_NUM", adapter.getC_NUM(position));
                            intent.putExtra("U_NUM",U_NUM);
                            intent.putExtra("U_NAME",NAME);
                            intent.putExtra("ID", ID);
                            intent.putExtra("S_MEMBER", adapter.getS_MEMBER(position));
                            intent.putExtra("FORM", adapter.getFORM(position));
                            intent.putExtra("MEMBER",adapter.getMEMBER(position));
                            intent.putExtra("LOCATION",adapter.getLocation(position));
                            intent.putExtra("u",adapter.getU_NUM(position));
                            startActivity(intent);
                        }
                    };
                    contest_list.setOnItemClickListener(itemClickListener);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ContestListRequest contestRequst = new ContestListRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(contestRequst);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                            ListView contest_list = (ListView) findViewById(R.id.contest_list);
                            contest_list.setAdapter(adapter);
                            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MainActivity.this, ContestActivity.class);
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
                            contest_list.setOnItemClickListener(itemClickListener);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ContestListRequest contestRequst = new ContestListRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(contestRequst);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //P_NUM받으면 그곳으로 이동
        P_NUM = getIntent().getStringExtra("P_NUM");
        if (P_NUM != null) {

            Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String S_MEMBER;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Intent intent  = new Intent(MainActivity.this, ContestActivity.class);
                        intent.putExtra("name", jsonResponse.getString("C_NAME"));
                        intent.putExtra("category", jsonResponse.getString("CATEGORY"));
                        intent.putExtra("csdate", jsonResponse.getString("CSDATE"));
                        intent.putExtra("jsdate", jsonResponse.getString("JSDATE"));
                        intent.putExtra("jedate", jsonResponse.getString("JEDATE"));
                        intent.putExtra("c_text", jsonResponse.getString("C_TEXT"));
                        intent.putExtra("C_NUM", jsonResponse.getString("C_NUM"));
                        intent.putExtra("U_NUM",U_NUM);
                        intent.putExtra("ID", ID);
                        if(jsonResponse.getInt("MEMBER") != 1){
                            S_MEMBER = jsonResponse.getInt("MEMBER") + "인 팀전" ;
                        }else {
                            S_MEMBER = "개인전";
                        }
                        intent.putExtra("S_MEMBER", S_MEMBER);
                        intent.putExtra("FORM", jsonResponse.getString("FORM"));
                        intent.putExtra("MEMBER",jsonResponse.getInt("MEMBER"));
                        intent.putExtra("LOCATION",jsonResponse.getString("LOCATION"));
                        intent.putExtra("u",jsonResponse.getInt("U_NUM"));
                        startActivity(intent);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            C_numContest contestRequst1 = new C_numContest(P_NUM, responseListener1);
            RequestQueue queue1 = Volley.newRequestQueue(MainActivity.this);
            queue1.add(contestRequst1);

        }

    }

    void passPushTokenToServer(){

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String token = instanceIdResult.getToken();

                Map<String,Object> map = new HashMap<>();
                map.put("pushToken",token);

                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
            }
        });
    }

    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(ID, PW)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           //로그인 성공했을 때
                           passPushTokenToServer();
                       }else{
                           return;
                       }
                    }
                });
    }


}
