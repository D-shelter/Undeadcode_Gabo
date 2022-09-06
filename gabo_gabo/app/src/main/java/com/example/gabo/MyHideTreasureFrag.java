package com.example.gabo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/*---------------------------마이페이지 > 내가 숨긴 보물 리스트 프래그먼트-------------------------------------*/
public class MyHideTreasureFrag extends BottomSheetDialogFragment {

    BottomSheetBehavior behavior;
    private myHideAdapter myHideAdapter;
    private ListView myhide_list;

    private RequestQueue queue;
    private StringRequest stringRequest;

    private String mainhost, user_id;
    private String list_name, list_key1, list_key2, list_key3, list_finddate, list_like;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*바텀시트 라운드 투명배경을 위한 스타일 설정*/
        setStyle(
                STYLE_NORMAL,R.style.TransparentBottomSheetDialogFragment
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(),R.layout.myhidetreasure,null);

        dialog.setContentView(view);
        behavior = BottomSheetBehavior.from((View) view.getParent());
        mainhost = getArguments().getString("mainhost");
        user_id = getArguments().getString("user_id");

        /*----내가 숨긴 보물 리스트 뷰---*/
//        myhide_list = view.findViewById(R.id.myhide_list);
//        myHideAdapter.addItems(
//                ContextCompat.getDrawable(getActivity(),R.drawable.e3),
//                ContextCompat.getDrawable(getActivity(),R.drawable.like_blank),
//                "2km","#전봇대","#물병","하얀색","08/11/22 10:00AM에 숨김","12"
//        );
//        myHideAdapter.addItems(
//                ContextCompat.getDrawable(getActivity(),R.drawable.e3),
//                ContextCompat.getDrawable(getActivity(),R.drawable.like_blank),
//                "2km","#전봇대","#물병","하얀색","08/11/22 10:00AM에 숨김","12"
//        );
//        myHideAdapter.addItems(
//                ContextCompat.getDrawable(getActivity(),R.drawable.e3),
//                ContextCompat.getDrawable(getActivity(),R.drawable.like_blank),
//                "2km","#전봇대","#물병","하얀색","08/11/22 10:00AM에 숨김","12"
//        );
//        myHideAdapter.addItems(
//                ContextCompat.getDrawable(getActivity(),R.drawable.e3),
//                ContextCompat.getDrawable(getActivity(),R.drawable.like_blank),
//                "2km","#전봇대","#물병","하얀색","08/11/22 10:00AM에 숨김","12"
//        );
//        myHideAdapter.addItems(
//                ContextCompat.getDrawable(getActivity(),R.drawable.e3),
//                ContextCompat.getDrawable(getActivity(),R.drawable.like_blank),
//                "2km","#전봇대","#물병","하얀색","08/11/22 10:00AM에 숨김","12"
//        );
//        myhide_list.setAdapter(myHideAdapter);
//        ListView myhide_list = getActivity().findViewById(R.id.myhide_list);

        requestIhided();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //내가 찾은 보물 조회
    public void requestIhided(){
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this.getActivity());
        // 서버에 요청할 주소
        String url = mainhost+"findtrs";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                myHideAdapter = new myHideAdapter();
                myhide_list = view.findViewById(R.id.myhide_list);
                Log.v("resultValue", response);
                String[] info = response.split("/");
                for (int i = 0; i < info.length; i++) {
//                    System.out.println(info[i]);
                    String [] info2 = info[i].split(",");
                    for (int j = 0; j <info[i].length();j++){
                        list_name = info2[0];
                        list_key1 = info2[1];
                        list_key2 = info2[2];
                        list_key3 = info2[3];
                        list_finddate = info2[4];
                        list_like = info2[5];
                        if (list_name.equals("None")){list_name = "No founded yet";}
                        if (list_finddate.equals("None")){list_finddate = "";}
                        if (list_like.equals("None")){list_like = "0";}
                    }
                    myHideAdapter.addItems(
                        ContextCompat.getDrawable(getActivity(),R.drawable.e3),
                        ContextCompat.getDrawable(getActivity(),R.drawable.like_blank),
                        list_name,list_key1,list_key2,list_key3,list_finddate+"에 찾음",list_like
                    );
                }
                System.out.println("aaaaa"+list_name);
                myhide_list.setAdapter(myHideAdapter);

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
