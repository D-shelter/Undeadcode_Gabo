package com.example.gabo;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/*-------------보물 등록페이지의 프레그먼트-----------------*/
public class HideTreasureFrag extends Fragment {

    private TextView tv_category;
    private String[] treasureCategory;

    //카테고리선택 스피너(드롭다운)
    private Spinner treasureCategorySpinner;

    private ImageView img_addphoto;  //사진등록버튼
    private Dialog dialog_camera;   //사진등록하기 다이얼로그
    private Dialog dialog_addtrcomplete; //보물등록완료 다이얼로그

    private static final String TAG = "Treasure";

    Bitmap bitmap;

    // 보물 사진 보여지는곳
    private ImageView iv_UserPhoto;

    // '보물 사진 등록'
    private TextView tv_picadd ;
    private boolean upload = false;

    //사진등록 취소버튼
    private TextView dialog_btn_cancle;

    //등록신청버튼
    private TextView btn_add_treasure;

    private HashTagEditTextView tagTextView; //입력된 해시태그를 배열로 추출하기 위한 변수
    FrameLayout hash_hint_frame;

    private String cate;

    private RequestQueue queue;
    private StringRequest stringRequest;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.trs_add_lyt,container,false);
        iv_UserPhoto = view.findViewById(R.id.iv_UserPhoto);
        tv_picadd = view.findViewById(R.id.tv_picadd);

        // bundle을 이용해서 액티비티에서 변수값 받기




        /*--------------------보물등록 카테고리---------------------*/
        //스피너 위치 tv의 아이디
        tv_category = view.findViewById(R.id.tv_category);
        //스피너에 들어갈 데이터
        treasureCategory = new String[]{"카테고리선택","디지털기기", "패션의류/잡화", "뷰티", "출산/유아", "식품","생활용품","홈인테리어","생활가전","스포츠/레저","자동차용품","도서/음반/DVD","완구/취미","문구/오피스","반려동물용품","헬스/건강","여행/티켓","식물"};
        //스피너 객체 생성
        treasureCategorySpinner = view.findViewById(R.id.selectCategorySpinner);

        //배열 어뎁터 생성

        // ArrayAdapter 매개변수
        // 1. Context 페이지 정보
        // getApplicationContext()
        // getActivity()
        // Fragement를 출력하고 있는 Activity를 다운캐스팅 (MainActivity)getActivity()

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.trs_add_category_spinner_txt, treasureCategory);
        //배열 어뎁터 설정. 드롭다운 클릭시 선택창
        categoryAdapter.setDropDownViewResource(R.layout.trs_add_category_dropdown_lyt);
        //설정한 어뎁터 스피너에 셋팅
        treasureCategorySpinner.setAdapter(categoryAdapter);
        //스피너에서 선택했을 경우 이벤트 처리
        treasureCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                tv_category.setText(treasureCategory[i]);
                cate = String.valueOf(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*---------------------------------------------해시태그 배열로 추출------------------------------------------*/
        tagTextView = view.findViewById(R.id.editTextTextPersonName);
        String[] hashTagArray = tagTextView.getInsertTag();
        /*---------------------------------------------해시태그입력시 힌트 숨기기------------------------------------------*/
        hash_hint_frame=view.findViewById(R.id.hash_hint_frame);
        tagTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력하기 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //변화가 있을 때
                hash_hint_frame.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력이 끝났을 때
            }
        });

        /*---------------------------------------------사진등록버튼 클릭시기능------------------------------------------*/
        img_addphoto = view.findViewById(R.id.img_addphoto);
        img_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCamera();
            }
        });


        /*-----------------------------------------등록신청 버튼------------------------------------*/
        btn_add_treasure = view.findViewById(R.id.btn_add_treasure);
        btn_add_treasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카테고리 선택하지 않았을때
                if (tv_category.getText().toString().equals("카테고리선택")){
                    // 토스트로 선택해달라고 띄움
                    Toast.makeText(getContext().getApplicationContext(),"카테고리를 선택해주세요",Toast.LENGTH_SHORT).show();
                // 해쉬태그가 3개가 안됐을때
                } else if (tagTextView.getText().toString().length()-tagTextView.getText().toString().replace("#","").length()!=3){
                    // 해쉬태그 등록해달라고 토스트 띄움
                    Toast.makeText(getContext().getApplicationContext(),"해쉬태그를 3개 달아주세요",Toast.LENGTH_SHORT).show();
                // 사진 업로드 안했을때
                } else if (upload == false){
                    // 사진 업로드 해달라고 토스트 띄움
                    Toast.makeText(getContext().getApplicationContext(),"사진을 업로드 해주세요",Toast.LENGTH_SHORT).show();
                } else{

                    sendRequest();
                }

            }
        });

        return view;


    }
    /*------------------보물찾기 완료 다이얼로그-----------------*/
    private void openAddtrCompleteDialog() {
        dialog_addtrcomplete = new Dialog(getContext());
        dialog_addtrcomplete.setContentView(R.layout.win_layout_dialog);
        dialog_addtrcomplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageViewClose=dialog_addtrcomplete.findViewById(R.id.imageViewClose);
        TextView addcomplete_close=dialog_addtrcomplete.findViewById(R.id.addcomplete_close);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_addtrcomplete.dismiss();
            }
        });
        addcomplete_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_addtrcomplete.dismiss();
            }
        });
        dialog_addtrcomplete.show();

    }



    /*-------사진 첨부 다이얼로그 디자인 함수-------*/
    public void showDialogCamera(){
        dialog_camera = new Dialog(getContext());
        dialog_camera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_camera.setContentView(R.layout.dialog_camera_lyt);
        dialog_camera.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //권한설정요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            }
            else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //사진촬영 버튼
        TextView dialog_btn_cam = dialog_camera.findViewById(R.id.dialog_btn_cam);
        dialog_btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);
            }
        });

        //취소 버튼
        dialog_btn_cancle = dialog_camera.findViewById(R.id.dialog_btn_cancle);
        dialog_btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_camera.dismiss();
            }
        });

        dialog_camera.show();

    }


    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                Bundle extras = result.getData().getExtras();
                bitmap = (Bitmap) extras.get("data");
                resizeBitmapImage(bitmap,10);
                iv_UserPhoto.setImageBitmap(bitmap);
                dialog_btn_cancle.callOnClick();
                tv_picadd.setText("");
                img_addphoto.setImageResource(0);
                upload = true;

            }
        }
    });


    /**
     * Bitmap이미지의 가로, 세로 사이즈를 리사이징 한다.
     *
     * @param source 원본 Bitmap 객체
     * @param maxResolution 제한 해상도
     * @return 리사이즈된 이미지 Bitmap 객체
     */
    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
    {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }


    public void sendRequest() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(getContext().getApplicationContext());
        // 서버에 요청할 주소
        String url = "http://192.168.21.252:5013/addtreasure";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
//                String[] info = response.split(",");
//                System.out.println(info[0]);
                if (response.equals("등록신청완료")){
                    Toast.makeText(getContext().getApplicationContext(),"등록신청완료",Toast.LENGTH_LONG).show();
                    upload = false;
                    openAddtrCompleteDialog();
                } else {
                    Toast.makeText(getContext().getApplicationContext(),"등록 실패",Toast.LENGTH_LONG).show();
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
                String[] keys = tagTextView.getText().toString().split("#");
                String key1 = keys[1].replace(" ","");
                String key2 = keys[2].replace(" ","");
                String key3 = keys[3].replace(" ","");
                String user_location = HideTreasureFrag.this.getArguments().getString("user_location");
                String[] location = user_location.split(",");
                String hideuser = HideTreasureFrag.this.getArguments().getString("user_id");


                System.out.println("해쉬태그 : " +tagTextView.getText().toString());
                System.out.println(cate);
                System.out.println("h1 : " +key1);
                System.out.println("h2 : " +key2);
                System.out.println("h3 : " +key3);
                System.out.println(location[0]);
                System.out.println(location[1]);
                System.out.println(hideuser);
                System.out.println(String.valueOf(bitmap));


                params.put("cate", cate);
                params.put("key1", key1);
                params.put("key2", key2);
                params.put("key3", key3);
                params.put("hideuser", hideuser);
                params.put("latitude",location[0]);
                params.put("longitude",location[1]);
                params.put("img", String.valueOf(bitmap));


                return params;
            }
        };


        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }
}
