package com.hfad.myplanner;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContestActivity extends AppCompatActivity implements OnMapReadyCallback {


    //google map을 사용하기 위해 전현변수로 선언
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    LocationListener locationListener; //위치정보 리스너
    LocationManager locationManager;

    //GEO코딩을 위해 전역변수로 선언
    GoogleMap map;

    //위치정보를 받아오는 변수
    private FusedLocationProviderClient mFusedLocationClient;

    String address; // 주소를 텍스트뷰에 넣기위해 전역변수로 뺌

    int member; // 팀전인지 개인전인지 알기위한 변수
    String MEMBER; // 바로 intent로 못 보내서 만들고 보내줘야함.

    String place;
    String NAME;
    String text;

    int fc = 0;

    int u;

    String ID;
    String U_NAME;

    //시간받아오기 위해
    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
    String nowtime;
    String JEDATE;

    Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);



        //위치정보 관리자 객체 생성
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fragmentManager = getFragmentManager();
        //fragement객체 생성
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        //구글맵을 비동기적인 방식으로 로딩
        mapFragment.getMapAsync(this);

        //위치정보를 받아오기 위해 선언
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
        LinearLayout createlayout = (LinearLayout)findViewById(R.id.createlayout);

        TextView name = (TextView) findViewById(R.id.contest_name);
        TextView category = (TextView) findViewById(R.id.contest_category);
        TextView csdate = (TextView) findViewById(R.id.contest_csdate);
        TextView jsdate = (TextView) findViewById(R.id.contest_jsdate);
        TextView jedate = (TextView) findViewById(R.id.contest_jedate);
        TextView form = (TextView) findViewById(R.id.contest_form);
        TextView member = (TextView) findViewById(R.id.contest_member);
        TextView c_text = (TextView) findViewById(R.id.contest_text);
        TextView location = (TextView)findViewById(R.id.location);
        Button board = (Button) findViewById(R.id.BoardButton);
        join = (Button)findViewById(R.id.joinButton);
        Button managemember = (Button)findViewById(R.id.manage_member);
        Button schedule = (Button)findViewById(R.id.manage_sc);
        Button schedulebutton = (Button)findViewById(R.id.schedulebutton);
        Button settingbutton = (Button)findViewById(R.id.settingbutton);
        Button deletebutton = (Button)findViewById(R.id.delete);

        final Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        category.setText(intent.getStringExtra("category"));
        csdate.setText(intent.getStringExtra("csdate"));
        jsdate.setText(intent.getStringExtra("jsdate"));
        jedate.setText(intent.getStringExtra("jedate"));
        form.setText(intent.getStringExtra("FORM"));
        member.setText(intent.getStringExtra("S_MEMBER"));
        c_text.setText(intent.getStringExtra("c_text"));
        location.setText(intent.getStringExtra("LOCATION"));

        final int U_NUM = intent.getIntExtra("U_NUM",0);
        final String C_NUM = intent.getStringExtra("C_NUM");
        final int MEMBER = intent.getIntExtra("MEMBER",0);
        u = intent.getIntExtra("u",0);
        NAME = intent.getStringExtra("name");
        ID = intent.getStringExtra("ID");
        JEDATE = jedate.getText().toString();
        U_NAME = intent.getStringExtra("U_NAME");

        text = "\n" + "게임 종류 : " + category.getText().toString() + "\n" + "대회 날짜  : " + csdate.getText().toString() + "\n" + "참가 시작 날짜  : " + jsdate.getText().toString() + "\n" + "참가 마감 날짜  : " + jedate.getText().toString() + "\n" + "대회 형식 날짜  : " + form.getText().toString() + "\n"  + "대회 설명  : " + c_text.getText().toString() + "\n";

        if(U_NUM == u){
            createlayout.setVisibility(View.VISIBLE);
            fc =1;

        }

        //사용자가 입력한 주소
        place = location.getText().toString();

        if(place.equals("")  || place.equals("null")) {

            layout.setVisibility(View.GONE);
        }

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ContestActivity.this, BoardActivity.class);
                intent1.putExtra("C_NUM", C_NUM);
                intent1.putExtra("U_NUM", U_NUM);
                intent1.putExtra("u",u);
                intent1.putExtra("U_NAME", U_NAME);
                startActivity(intent1);
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(U_NUM ==0){
                    Toast.makeText(ContestActivity.this, "로그인해주세요", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent2 = new Intent(ContestActivity.this, JoinActivity.class);
                    intent2.putExtra("MEMBER",MEMBER);
                    intent2.putExtra("U_NUM",U_NUM);
                    intent2.putExtra("C_NUM", C_NUM);
                    intent2.putExtra("u", u);
                    intent2.putExtra("ID", ID);

                    startActivity(intent2);
                }

            }
        });
        managemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContestActivity.this, ManageMember.class);
                intent.putExtra("C_NUM",C_NUM);
                intent.putExtra("MEMBER",MEMBER);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContestActivity.this, Schedule.class);
                intent.putExtra("C_NUM",C_NUM);
                intent.putExtra("U_NUM",U_NUM);
                intent.putExtra("fc",fc);
                intent.putExtra("MEMBER",MEMBER);
                startActivity(intent);
            }
        });

        schedulebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ContestActivity.this, AddMatch.class);
                intent1.putExtra("C_NUM",C_NUM);
                intent1.putExtra("MEMBER",MEMBER);
                startActivity(intent1);
            }
        });

        settingbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1 = new Intent(ContestActivity.this, MakeContestActivity.class);
                intent1.putExtra("C_NUM",C_NUM);
                intent1.putExtra("name",intent.getStringExtra("name"));
                intent1.putExtra("category",intent.getStringExtra("category"));
                intent1.putExtra("csdate",intent.getStringExtra("csdate"));
                intent1.putExtra("jsdate",intent.getStringExtra("jsdate"));
                intent1.putExtra("jedate",intent.getStringExtra("jedate"));
                intent1.putExtra("FORM",intent.getStringExtra("FORM"));
                intent1.putExtra("S_MEMBER",intent.getStringExtra("S_MEMBER"));
                intent1.putExtra("c_text",intent.getStringExtra("c_text"));
                intent1.putExtra("C_NAME",intent.getStringExtra("name"));
                intent1.putExtra("U_NUM",intent.getIntExtra("U_NUM",0));
                intent1.putExtra("MEMBER",intent.getIntExtra("MEMBER",0));
                intent1.putExtra("ADDRESS",intent.getStringExtra("LOCATION"));
                //수정 후 대회 정보로 돌아오기 위해 추가.
                intent1.putExtra("ID",intent.getStringExtra("ID"));
                intent1.putExtra("u",u);


                startActivity(intent1);
            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(ContestActivity.this);
                builder.setMessage("대회를 삭제하시겠습니까?" +"\n"+
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
                                                        Toast.makeText(ContestActivity.this, "대회를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ContestActivity.this, MainActivity.class);
                                                        intent.putExtra("U_NUM", U_NUM);
                                                        intent.putExtra("ID",ID);
                                                        intent.putExtra("NAME",NAME);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ContestActivity.this);
                                                        builder.setMessage("대회를 삭제할 수 없습니다")
                                                                .setNegativeButton("다시 시도", null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        DeleteContestRequest loginRequst = new DeleteContestRequest(C_NUM, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(ContestActivity.this);
                                        queue.add(loginRequst);
                                    }
                                })
                        .setNegativeButton("아니오",null)
                        .create()
                        .show();

            }
        });

        getDay();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // actionbar 메뉴 호출
        getMenuInflater().inflate(R.menu.contest_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plan");
                //File docsfile = new File(getFilesDir(), "docs");
                //File newFile = new File(docsfile, "contest.text");
                //Uri uri = FileProvider.getUriForFile(
                //getApplicationContext(), getApplicationContext().getPackageName() + ".provider", newFile) ;

                try
                {
                    share.putExtra(Intent.EXTRA_SUBJECT, NAME);
                    share.putExtra(Intent.EXTRA_TEXT, "대회이름 : " + text);
                    //share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(share, " 공유"));
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "공유에 실패 했습니다.", Toast.LENGTH_LONG).show();;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //지도가 로딩완료되었을 때 호출
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        //지도 종류 설정
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //지도에 줌 컨트롤 표시 여부
        map.getUiSettings().setZoomControlsEnabled(true);

        geo();
    }

    public void geo(){
        //GEO(지오코딩) - 주소와 좌표를 매핑
        Geocoder corder = new Geocoder(this);
        List<Address> list = null;
        try {
            //getFromLocationName(주소, 계산할 좌표 갯수)
            list = corder.getFromLocationName(place, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
            } else {
                Address addr = list.get(0);
                double lat = addr.getLatitude(); // 위도
                double log = addr.getLongitude(); // 경도

                String[] splitStr = list.get(0).toString().split(",");
                address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 지도에 검색한 곳의 주소를 저장.

                LatLng geoPoint = new LatLng(lat, log);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(geoPoint, 15));
                MarkerOptions marker = new MarkerOptions();
                marker.position(geoPoint);
                marker.snippet(address); //snippet부분에 주소를 출력.
                map.clear();
                map.addMarker(marker);

                GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                    }
                };

                map.setOnInfoWindowClickListener(infoWindowClickListener); // 정보창(말풍선 클릭 리스너


            }
        }
    }
    private void getDay(){

        Date time = new Date();
        nowtime = format.format(time);
        try {
            Date nd = format.parse(nowtime);
            Date jed = format.parse(JEDATE);

            int compare = jed.compareTo(nd);

            if(compare >= 0 ){
                join.setVisibility(View.VISIBLE);
            }else{
                join.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void  onBackPressed(){
        Intent intent = new Intent(ContestActivity.this, MainActivity.class);
        String P_NUM = null;
        intent.putExtra("P_NUM",P_NUM);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
