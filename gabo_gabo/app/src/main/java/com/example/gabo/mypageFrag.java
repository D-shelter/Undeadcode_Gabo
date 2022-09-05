package com.example.gabo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class mypageFrag extends Fragment {

    LinearLayout myfind_open ; //내가 찾은 보물 버튼
    LinearLayout myfind_bignum; //내가 찾은 보물 숫자 버튼

    LinearLayout myhide_open; //내가 숨긴 보물 버튼
    LinearLayout myhide_bignum; //내가 숨긴 보물 숫자 버튼

    LinearLayout mycomment_open; //게시물 모아보기 버튼


    TextView mypage_tv_findnum, mypage_tv_hidenum, mypage_tv_likenum, mypage_tv_level, mypage_tv_nickname,mypage_tv_like;
    String hidetrs,findtrs,like,level;


    MyFindTreasureFrag myFindTreasureFrag; //내가 찾은 보물 바텀시트 프래그먼트
    MyHideTreasureFrag myHideTreasureFrag; //내가 숨긴 보물 바텀시트 프래그먼트
    MyCommentsFrag myCommentsFrag; //게시물 모아보기 바텀시트 프래그먼트

    FragmentManager fmm ;

    private RequestQueue queue;
    private StringRequest stringRequest;

    private String mainhost;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_lyt,container,false);

        fmm=getActivity().getSupportFragmentManager();

        mainhost = getArguments().getString("mainhost");


        /*---------내가찾은보물 열기-----------*/
        myFindTreasureFrag = new MyFindTreasureFrag();
        myfind_open = view.findViewById(R.id.myfind_open);
        myfind_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFindTreasureFrag.show(fmm,"showmyfind");
            }
        });
        myfind_bignum = view.findViewById(R.id.myfind_bignum);
        myfind_bignum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFindTreasureFrag.show(fmm,"showmyfind");
            }
        });

        /*---------내가 숨긴 보물 열기-----------*/
        myHideTreasureFrag = new MyHideTreasureFrag();
        myhide_open = view.findViewById(R.id.myhide_open);
        myhide_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHideTreasureFrag.show(fmm,"showmyhide");
            }
        });
        myhide_bignum = view.findViewById(R.id.myhide_bignum);
        myhide_bignum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHideTreasureFrag.show(fmm,"showmyhide");
            }
        });

        /*------------게시물 모아보기-----------*/
        myCommentsFrag = new MyCommentsFrag();
        mycomment_open = view.findViewById(R.id.mycomment_open);
        mycomment_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCommentsFrag.show(fmm,"showmycomments");
            }
        });

        sendRequestmypage();

        return view;



    }



    //마이페이지 리퀘스트
    public void sendRequestmypage(){
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this.getActivity());
        // 서버에 요청할 주소
        String url = mainhost+"mypage";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue",response);
                String[] res = response.split(",");
                String hidetrs = res[1];
                String findtrs = res[0];
                String like = res[2];
                String level = res[3];
                if (like.equals("None")){
                    like = "0";
                }

                String user_id;
                user_id = getArguments().getString("user_id");


                mypage_tv_findnum = getActivity().findViewById(R.id.mypage_tv_findnum);
                mypage_tv_hidenum = getActivity().findViewById(R.id.mypage_tv_hidenum);
                mypage_tv_likenum = getActivity().findViewById(R.id.mypage_tv_likenum);
                mypage_tv_level = getActivity().findViewById(R.id.mypage_tv_level);
                mypage_tv_nickname = getActivity().findViewById(R.id.mypage_tv_nickname);
                mypage_tv_like = getActivity().findViewById(R.id.mypage_tv_like);

                mypage_tv_findnum.setText(hidetrs);
                mypage_tv_hidenum.setText(findtrs);
                mypage_tv_likenum.setText(like);
                mypage_tv_level.setText("Lv "+level);
                mypage_tv_nickname.setText(user_id);
                mypage_tv_like.setText(like);

            }
        }, new Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
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

                String user_id;
                user_id = getArguments().getString("user_id");

                params.put("id",user_id);

                return params;
            }
        };


        //TAG는 필수 아님
        String TAG = "ojy";
        stringRequest.setTag(TAG);
        queue.add(stringRequest);


    }

    //내가 숨긴 보물 리퀘스트
    public void sendRequestmyhide(){
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this.getActivity());
        // 서버에 요청할 주소
        String url =mainhost+"myhidepage";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue",response);

                String user_id;
                user_id = getArguments().getString("user_id");

                String[] res = response.split(",");

                String myhide_location = res[0];
                String myhide_hint1 = res[1];
                String myhide_hint2 = res[2];
                String myhide_hint3 = res[3] ;
                String myhide_date = res[4] ;
                String myhide_like = res[5];

                //myHideAdapter로 보내야함

            }
        }, new Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
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

                String user_id;
                user_id = getArguments().getString("user_id");

                params.put("id",user_id);

                return params;
            }
        };


        //TAG는 필수 아님
        String TAG = "ojy";
        stringRequest.setTag(TAG);
        queue.add(stringRequest);


    }



}
