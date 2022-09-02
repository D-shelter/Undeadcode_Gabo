package com.example.gabo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


//메인 액티비티
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    //프레그먼트 객체
    private HideTreasureFrag fragmHideTreasure; //보물등록
    private trsListview fragTresureListview;  //보물리스트
    private mypageFrag mypageFrag; //마이페이지

    private FragmentManager fm;
    private BottomNavigationView navi;
    private String user_location;

    private RequestQueue queue;
    private StringRequest stringRequest;

    //보물 아이디
    private String t_id;

    // 보물정보창 여닫이
    private boolean t_info = false;

    private String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LoginActivity 에서 ID값 가져오기
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("id");

        //기본상단바 안보이게 하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fragmHideTreasure = new HideTreasureFrag();
        fragTresureListview = new trsListview();
        mypageFrag = new mypageFrag();

        fm = getSupportFragmentManager();
        /*fm.beginTransaction().replace(R.id.frame,fragmHideTreasure).commit();*/







        /*------------------------------------------지도--------------------------------------------*/
        //네이버지도
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("nww1ikjspq")
        );
        //지도 객체 생성
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.frame);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.frame, mapFragment).commit();
        }
        // 현재위치
        // OnMapReady에서 NaverMap 객체를 받는다.
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);





        /*------------------------------------------네비게이션--------------------------------------------*/
        navi = findViewById(R.id.Navi);
        navi.getMenu().getItem(2).setChecked(true);
        //맵 객체 다시 생성하래서 다시 함
        MapFragment finalMapFragment = mapFragment;
        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectId = item.getItemId();
                if (selectId==R.id.page1){
                    fm.beginTransaction().replace(R.id.frame,fragTresureListview).commit();
                }else if (selectId==R.id.page2){
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id",user_id);
                    bundle.putString("user_location",user_location);
                    fragmHideTreasure.setArguments(bundle);
                    System.out.println(user_location);
                    fm.beginTransaction().replace(R.id.frame,fragmHideTreasure).commit();
                }else if (selectId==R.id.page3){
                    fm.beginTransaction().replace(R.id.frame,finalMapFragment).commit();
                    // 현재위치
                    // OnMapReady에서 NaverMap 객체를 받는다.
                    finalMapFragment.getMapAsync(MainActivity.this::onMapReady);
                    locationSource = new FusedLocationSource(MainActivity.this,LOCATION_PERMISSION_REQUEST_CODE);
                    // 현재위치 표시
                    MainActivity.this.naverMap = naverMap;
                    naverMap.setLocationSource(locationSource); // 현재 위치
                    //현재 위치 표시할 때 권한 확인
                    ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE);
                    // bottomDialog.show(fm,"Test");  보물 정보(핀) 누르면 바텀시트 튀어나오기.필요한 곳에다 옮겨쓰기
                    sendRequest1();

                }else if (selectId==R.id.page4){
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id",user_id);
                    mypageFrag = new mypageFrag();
                    mypageFrag.setArguments(bundle);

                    fm.beginTransaction().replace(R.id.frame,mypageFrag).commit();
//                    bottomDialog.show(fm,"Test");
                }
                return true;
            }
        });

        sendRequest1();

        /** 처음 바텀 적용
         BottomNavigationView bottom_btn = findViewById(R.id.page3);
         bottom_btn.performClick();
         */



    }



    //marker 찍는 법
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {


        Marker marker = new Marker();
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_blue));
        marker.setWidth(1);
        marker.setHeight(1);
        marker.setPosition(new LatLng(35.146678,126.922288));

        // 현재위치 표시
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); // 현재 위치
        //현재 위치 표시할 때 권한 확인
        ActivityCompat.requestPermissions(this,PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        LocationButtonView locationButtonView = findViewById(R.id.hlocation);
        locationButtonView.setMap(naverMap);

        sendRequest1();



    }




    // 마커 정보창
    InfoWindow infoWindow = new InfoWindow();

    // 보물 정보창을 보여주는 메서드
    public void setInfoWindow(BottomSheetDialogFrag bottomDialog) {
        bottomDialog.show(fm,"Test");
    }

    // 지도를 클릭하면 정보 창을 닫음
    public void setNaverMap(NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setOnMapClickListener((coord,point) -> {
            infoWindow.close();
            t_info = false;
        });
    }
    Overlay.OnClickListener listener = overlay -> {
        Marker marker = (Marker)overlay;

        if (marker.getInfoWindow() == null) {
            t_id = marker.getCaptionText();
            sendRequest2();
//            setInfoWindow(bottomDialog);
        } else {
            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
            infoWindow.close();
            t_info = false;
        }

        return true;
    };




    // 좌표를 나타내는 클래스
//    LatLng coord = new LatLng(35.146678,126.922288);
    // 토스트로 위도 경도 출력
    // Toast.makeText(context,
    //    "위도: " + coord.latitude + ", 경도: " + coord.longitude,
    //    Toast.LENGTH_SHORT).show();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            }else{
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }

            // 현재 위치를 Toast로 알려줌
//            naverMap.addOnLocationChangeListener(location ->
//                    Toast.makeText(this,
//                            location.getLatitude() + ", " + location.getLongitude(),
//                            Toast.LENGTH_SHORT).show());
            // 위치가 바뀌면 좌표를 user_location에 저장해주는 메서드
            naverMap.addOnLocationChangeListener(location ->
                    user_location = location.getLatitude()+","+location.getLongitude());


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // 보물 갱신 리퀘스트
    public void sendRequest1() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this.getApplicationContext());
        // 서버에 요청할 주소
        String url = "http://192.168.21.252:5013/mappage";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
                String[] info = response.split("/");
                System.out.println(info.length);
                for (int i = 0; i < info.length; i++) {
                    System.out.println(info[i]);
                    for (int j = 0; j <info[i].length();j++){
                        String [] info2 = info[i].split(",");
                        // 찾은 유저가 있으면 마커표시 안함
                        if (info2[6].equals("null")){ break; }
                        // 승인 안됐으면 마커표시 안함
                        if (info2[7].equals("0")){break;}

                        double lati = Double.valueOf(info2[8]);
                        double longi = Double.valueOf(info2[9]);
                        Marker marker = new Marker();
                        marker.setCaptionText(info2[0]);
                        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_blue));
                        marker.setWidth(100);
                        marker.setHeight(115);
                        marker.setPosition(new LatLng(lati,longi));
                        marker.setOnClickListener(listener);
                        marker.setMap(naverMap);

                    }


//                    Marker marker = new Marker();
//                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_blue));
//                    marker.setWidth(100);
//                    marker.setHeight(100);
//                    marker.setPosition(new LatLng(35.146678,126.922288));
//                    marker.setPosition(new LatLng(35.146939,126.922313));
//                    marker.setMap(naverMap);
                }

            }
        }, new Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            // 보낼 데이터를 저장하는 곳
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }

    // 보물 클릭 리퀘스트
    public void sendRequest2() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this.getApplicationContext());
        // 서버에 요청할 주소
        String url = "http://192.168.21.252:5013/clicktrs";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
                String[] info = response.split(",");

                System.out.println(response);

                String cate = info[0];
                String key1 = info[1];
                String key2 = info[2];
                String key3 = info[3];
                String hideuser = info[4];
                String latitude = info[6];
                String longitude = info[7];
                String hidedate = info[8].substring(2,16);
                String like = info[9];
                t_info = true;

                Bundle bundle = new Bundle();
                bundle.putString("cate",cate);
                bundle.putString("key1",key1);
                bundle.putString("key2",key2);
                bundle.putString("key3",key3);
                bundle.putString("hideuser",hideuser);
                bundle.putString("latitude",latitude);
                bundle.putString("longitude",longitude);
                bundle.putString("hidedate",hidedate);
                bundle.putString("like",like);
                bundle.putString("userlocation",user_location);
                //바텀시트
                BottomSheetDialogFrag bottomDialog = new BottomSheetDialogFrag();
                bottomDialog.setArguments(bundle);
                setInfoWindow(bottomDialog);





            }
        }, new Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            // 보낼 데이터를 저장하는 곳
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("t_id",t_id);
                return params;
            }
        };

        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }


}
