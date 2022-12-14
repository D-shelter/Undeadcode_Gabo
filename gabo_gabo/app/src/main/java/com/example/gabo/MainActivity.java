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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
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
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


//?????? ????????????
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final String ROOT_URL = "http://192.168.21.187:5013/profileUpload";
    //??????????????? ??????
    private HideTreasureFrag fragmHideTreasure; //????????????
    private trsListview fragTresureListview;  //???????????????
    private mypageFrag mypageFrag; //???????????????

    private FragmentManager fm;
    private BottomNavigationView navi;
    private String user_location;

    private RequestQueue queue;
    private StringRequest stringRequest;

    //?????? ?????????
    private String t_id;

    private String user_id;

    private String mainhost;

    private String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //LoginActivity ?????? ID??? ????????????
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
        mainhost = intent.getStringExtra("mainhost");

        //??????????????? ???????????? ??????
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fragmHideTreasure = new HideTreasureFrag();
        fragTresureListview = new trsListview();
        mypageFrag = new mypageFrag();

        fm = getSupportFragmentManager();
        /*fm.beginTransaction().replace(R.id.frame,fragmHideTreasure).commit();*/







        /*------------------------------------------??????--------------------------------------------*/
        //???????????????
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("nww1ikjspq")
        );
        //?????? ?????? ??????
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.frame);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.frame, mapFragment).commit();
        }
        // ????????????
        // OnMapReady?????? NaverMap ????????? ?????????.
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);





        /*------------------------------------------???????????????--------------------------------------------*/
        navi = findViewById(R.id.Navi);
        navi.getMenu().getItem(2).setChecked(true);
        //??? ?????? ?????? ??????????????? ?????? ???
        MapFragment finalMapFragment = mapFragment;
        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectId = item.getItemId();
                if (selectId==R.id.page1){
                    Bundle bundle = new Bundle();
                    bundle.putString("user_location", user_location);
                    bundle.putString("mainhost",mainhost);
                    bundle.putString("user_id",user_id);
                    fragTresureListview.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.frame,fragTresureListview).commit();
                }else if (selectId==R.id.page2){
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id",user_id);
                    bundle.putString("user_location",user_location);
                    bundle.putString("mainhost",mainhost);
                    fragmHideTreasure.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.frame,fragmHideTreasure).commit();
                }else if (selectId==R.id.page3){
                    fm.beginTransaction().replace(R.id.frame,finalMapFragment).commit();
                    // ????????????
                    // OnMapReady?????? NaverMap ????????? ?????????.
                    finalMapFragment.getMapAsync(MainActivity.this::onMapReady);
                    locationSource = new FusedLocationSource(MainActivity.this,LOCATION_PERMISSION_REQUEST_CODE);
                    // ???????????? ??????
                    MainActivity.this.naverMap = naverMap;
                    naverMap.setLocationSource(locationSource); // ?????? ??????
                    //?????? ?????? ????????? ??? ?????? ??????
                    ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE);
                    // bottomDialog.show(fm,"Test");  ?????? ??????(???) ????????? ???????????? ???????????????.????????? ????????? ????????????
                    sendRequest1();

                }else if (selectId==R.id.page4){
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id",user_id);
                    bundle.putString("mainhost",mainhost);
                    mypageFrag = new mypageFrag();
                    mypageFrag.setArguments(bundle);

                    fm.beginTransaction().replace(R.id.frame,mypageFrag).commit();
//                    bottomDialog.show(fm,"Test");
                }
                return true;
            }
        });

        sendRequest1();

        /** ?????? ?????? ??????
         BottomNavigationView bottom_btn = findViewById(R.id.page3);
         bottom_btn.performClick();
         */

        Intent takeBitmap = getIntent();
        pic = takeBitmap.getStringExtra("bitmapPic");
        uploadPic(StringToBitmap(pic));
    }



    //marker ?????? ???
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {


        Marker marker = new Marker();
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_blue));
        marker.setWidth(1);
        marker.setHeight(1);
        marker.setPosition(new LatLng(35.146678,126.922288));

        // ???????????? ??????
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); // ?????? ??????
        //?????? ?????? ????????? ??? ?????? ??????
        ActivityCompat.requestPermissions(this,PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        LocationButtonView locationButtonView = findViewById(R.id.hlocation);
        locationButtonView.setMap(naverMap);

        sendRequest1();



    }




    // ?????? ?????????
    InfoWindow infoWindow = new InfoWindow();

    // ?????? ???????????? ???????????? ?????????
    public void setInfoWindow(BottomSheetDialogFrag bottomDialog) {
        bottomDialog.show(fm,"Test");
    }

    // ????????? ???????????? ?????? ?????? ??????
    public void setNaverMap(NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setOnMapClickListener((coord,point) -> {
            infoWindow.close();
        });
    }
    Overlay.OnClickListener listener = overlay -> {
        Marker marker = (Marker)overlay;

        if (marker.getInfoWindow() == null) {
            t_id = marker.getCaptionText();
            sendRequest2();
//            setInfoWindow(bottomDialog);
        } else {
            // ?????? ?????? ????????? ?????? ?????? ???????????? ?????? ??????
            infoWindow.close();
        }

        return true;
    };




    // ????????? ???????????? ?????????
//    LatLng coord = new LatLng(35.146678,126.922288);
    // ???????????? ?????? ?????? ??????
    // Toast.makeText(context,
    //    "??????: " + coord.latitude + ", ??????: " + coord.longitude,
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

            // ?????? ????????? Toast??? ?????????
//            naverMap.addOnLocationChangeListener(location ->
//                    Toast.makeText(this,
//                            location.getLatitude() + ", " + location.getLongitude(),
//                            Toast.LENGTH_SHORT).show());
            // ????????? ????????? ????????? user_location??? ??????????????? ?????????
            naverMap.addOnLocationChangeListener(location ->
                    user_location = location.getLatitude()+","+location.getLongitude());


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // ?????? ?????? ????????????
    public void sendRequest1() {
        // Volley Lib ????????? ???????????? ??????
        queue = Volley.newRequestQueue(this.getApplicationContext());
        // ????????? ????????? ??????
        String url = mainhost +"mappage";
        // ?????? ????????? ??????
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // ?????????????????? ???????????? ???
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
                String[] info = response.split("/");
                //System.out.println(info.length);
                for (int i = 0; i < info.length; i++) {
                    //System.out.println(info[i]);
                    for (int j = 0; j <info[i].length();j++){
                        String [] info2 = info[i].split(",");
                        // ?????? ????????? ????????? ???????????? ??????
                        if (!info2[6].equals("None")){ break; }
                        // ?????? ???????????? ???????????? ??????
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
            // ???????????? ?????? ????????? ??????
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override //response??? UTF8??? ??????????????? ????????????
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

            // ?????? ???????????? ???????????? ???
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

    // ?????? ?????? ????????????
    public void sendRequest2() {
        // Volley Lib ????????? ???????????? ??????
        queue = Volley.newRequestQueue(this.getApplicationContext());
        // ????????? ????????? ??????
        String url = mainhost+"clicktrs";
        // ?????? ????????? ??????
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // ?????????????????? ???????????? ???
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
                if (like.equals("None")){
                    like = "0";
                }

                Bundle bundle = new Bundle();
                bundle.putString("t_id",t_id);
                bundle.putString("mainhost",mainhost);
                bundle.putString("userid",user_id);
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
                //????????????
                BottomSheetDialogFrag bottomDialog = new BottomSheetDialogFrag();
                bottomDialog.setArguments(bundle);
                setInfoWindow(bottomDialog);





            }
        }, new Response.ErrorListener() {
            // ???????????? ?????? ????????? ??????
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override //response??? UTF8??? ??????????????? ????????????
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

            // ?????? ???????????? ???????????? ???
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadPic(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        String res = new String(response.data);
                        Log.d("response", res);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                params.put("id", new DataPart("admin","".getBytes())); // string??? ?????? ?????? ??? ????????? ""??? byte??? ????????? ?????????.
                // ????????? ?????? ?????? new DataPart??? name??????
                return params;
            }


        };

        // adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    /* * Bitmap??? String????????? ?????? * */
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
