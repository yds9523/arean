package com.hfad.myplanner;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MakeContestSubActivity extends AppCompatActivity implements OnMapReadyCallback //OmMapReadyCallback : 지도가 로딩 완료되었음을 알려주는 리스너
{

    //google map을 사용하기 위해 전현변수로 선언
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    LocationListener locationListener; //위치정보 리스너
    LocationManager locationManager;

    //GEO코딩을 위해 전역변수로 선언
    GoogleMap map;
    EditText edit;

    //위치정보를 받아오는 변수
    private FusedLocationProviderClient mFusedLocationClient;
    //퍼미션 승락 확인을 위한 변수
    private  static final int REQEST_CODE_PERMISSIONS = 1000;

    String address; // 주소를 텍스트뷰에 넣기위해 전역변수로 뺌
    TextView text; // intent로 보내기 위해 전역변수로

    int member; // 팀전인지 개인전인지 알기위한 변수
    EditText editText; // intent로 보내기 위해 전역변수로
    String MEMBER; // 바로 intent로 못 보내서 만들고 보내줘야함.
    String S_MEMBER;
    String location ;
    String c_text;

    String place;

    String C_NUM;
    //다음으로 넘길 주소가 값인지 null인지 구분하기 위해 선언
    String ADDRESS;

    boolean fromchecked;
    boolean linechecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contest_sub);


        //위치정보 관리자 객체 생성
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fragmentManager = getFragmentManager();
        //fragement객체 생성
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        //구글맵을 비동기적인 방식으로 로딩
        mapFragment.getMapAsync(this);

        //위치정보를 받아오기 위해 선언
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        edit = (EditText) findViewById(R.id.edit);
        //레이아웃이 켜지면 권한설정을 물어본다.
        onLastLocationButtonClicked();

        text = (TextView)findViewById(R.id.address); // 전역변의수 address 정의
        editText = (EditText)findViewById(R.id.member);
        RadioGroup formgroup = (RadioGroup)findViewById(R.id.formgroup);
        RadioButton pri = (RadioButton)findViewById(R.id.pri);
        RadioButton team  = (RadioButton)findViewById(R.id.team);

        RadioGroup place = (RadioGroup)findViewById(R.id.place);
        RadioButton online = (RadioButton)findViewById(R.id.online);
        RadioButton offline = (RadioButton)findViewById(R.id.offline);


        Intent intent = getIntent();
        C_NUM = intent.getStringExtra("C_NUM");
        if(!C_NUM.equals("null")){
            S_MEMBER = intent.getStringExtra("S_MEMBER");
            int MEMBER = intent.getIntExtra("MEMBER",0);
            if(S_MEMBER.equals("개인전")){
                formgroup.check(pri.getId());
                member =1;
            }else if(S_MEMBER.contains("팀")){
                formgroup.check(team.getId());
                member = 0;
                editText.setFocusableInTouchMode(true);
                editText.setText(String.valueOf(MEMBER));
            }

            location = intent.getStringExtra("ADDRESS");
            if (location.equals("")) {
                place.check(online.getId());
            } else {
                place.check(offline.getId());
                edit.setText(location);
                text.setText(location);
            }
        }
    }

    //지도가 로딩완료되었을 때 호출
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        //지도 종류 설정
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //지도에 줌 컨트롤 표시 여부
        map.getUiSettings().setZoomControlsEnabled(true);
        //퍼미션 체크 했으면 현재위치 아이콘 만들어줌
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }


    public void search(View v) {
        //사용자가 입력한 주소
        place = edit.getText().toString();
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
                Toast.makeText(this, "해당되는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Address addr = list.get(0);
                double lat = addr.getLatitude(); // 위도
                double log = addr.getLongitude(); // 경도

                String []splitStr = list.get(0).toString().split(",");
                address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 지도에 검색한 곳의 주소를 저장.

                LatLng geoPoint = new LatLng(lat, log);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(geoPoint, 15));
                MarkerOptions marker = new MarkerOptions();
                marker.position(geoPoint);
                marker.title(edit.getText().toString());
                marker.snippet(address); //snippet부분에 주소를 출력.
                map.clear();
                map.addMarker(marker);

                map.setOnInfoWindowClickListener(infoWindowClickListener); // 정보창(말풍선 클릭 리스너
            }
        }
    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            text.setText("주소 : " + address);
        }
    };

    //개인전 선택했을 경우 팀인원 적는 에디터에 대한 포커스를 얻지 못함.
    public void one(View view){
        member = 1;
        editText.setText(null);
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        MEMBER = String.valueOf(member);
        fromchecked = false;
    }

    //팀전을 선택했을 경우 팀 인원을 적을 수 있다.
    public void more(View view){
        member = 0;
        editText.setFocusableInTouchMode(true);
        fromchecked = true;
    }

    //온라인을 선택할 경우 지도와 주소를 적는 에디트텍스트 사용이 불가능하다.
    public void online(View view){
        edit.setText("");
        edit.setFocusableInTouchMode(false);
        edit.setFocusable(false);
        text.setText(null);
        linechecked = false;
    }

    public void offline(View view){
        edit.setFocusableInTouchMode(true);
        linechecked = true;
    }

    //뒤로 버튼을 누르면 현재 엑티비티가 끝난다.
    public void onBack(View view) {
        finish();
    }


    //MakeContestSubSeActivity로 넘어간다.
    public void onNext(View view) {

        if(fromchecked && TextUtils.isEmpty(editText.getText())){
            Toast.makeText(this, "팀 인원을 적어주세요.", Toast.LENGTH_SHORT).show();
        }else if(linechecked && TextUtils.isEmpty(text.getText())){
            Toast.makeText(this, "주소를 입력하거나 마커의 주소를 눌러주세요.", Toast.LENGTH_SHORT).show();
        }else if(editText.getText().toString() == "0" || editText.getText().toString() == "1"){
            Toast.makeText(this, "팀전 인원은 2인 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
        }else{
            //앞에서 받았던 것
            Intent frontintent = getIntent();
            String C_NUM = frontintent.getStringExtra("C_NUM");
            int U_NUM = frontintent.getIntExtra("U_NUM", 0);
            String ID = frontintent.getStringExtra("ID");
            String C_NAME = frontintent.getStringExtra("C_NAME");
            String CATEGORY = frontintent.getStringExtra("CATEGORY");
            String CSDATE = frontintent.getStringExtra("CSDATE");
            String JSDATE = frontintent.getStringExtra("JSDATE");
            String JEDATE = frontintent.getStringExtra("JEDATE");
            String FORM = frontintent.getStringExtra("FORM");
            //개인전이면 member가 1이 되고, 그러면 1값을 넘겨준다. 아니면 에디트텍스트에 있는 값을 넘겨준다.
            if (member == 1) {
                MEMBER = "1";
            } else {
                MEMBER = editText.getText().toString();
            }
            if (edit.getText().toString() != null) {
                ADDRESS = text.getText().toString(); // 주소 정보를 받아 다음으로 넘겨줌
            } else {
                ADDRESS = null;
            }

            Intent intent = new Intent(this, MakeContestSubSeActivity.class);
            intent.putExtra("ID", ID);
            intent.putExtra("C_NUM", C_NUM);
            intent.putExtra("U_NUM", U_NUM);
            intent.putExtra("C_NAME", C_NAME);
            intent.putExtra("CATEGORY", CATEGORY);
            intent.putExtra("CSDATE", CSDATE);
            intent.putExtra("JSDATE", JSDATE);
            intent.putExtra("JEDATE", JEDATE);
            intent.putExtra("ADDRESS", ADDRESS);
            intent.putExtra("FORM", FORM);
            intent.putExtra("MEMBER", MEMBER);
            intent.putExtra("c_text", frontintent.getStringExtra("c_text"));
            intent.putExtra("ID", frontintent.getStringExtra("ID"));
            intent.putExtra("NAME", frontintent.getStringExtra("NAME"));
            intent.putExtra("u", frontintent.getIntExtra("u", 0));
            intent.putExtra("AC",frontintent.getIntExtra("AC", 0));

            startActivity(intent);
        }
    }

    //퍼미션 체크
    public void onLastLocationButtonClicked() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(MakeContestSubActivity.this);
            builder.setMessage("현재 위치 확인을 위해 필요합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    })
                    .create()
                    .show();*/
            //다이얼로그가 떠서 사용자가 권한 승낙, 거절할 수 있다.
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQEST_CODE_PERMISSIONS);
            return;
        }
        //마지막 GPS에 기록된 위치 정보를 가져온다.
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                }
            }
        });
    }
    //권한에 대한 응답
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQEST_CODE_PERMISSIONS:
                //권한 체크 한번 더
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치 확인 권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
