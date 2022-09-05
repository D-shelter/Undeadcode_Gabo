package com.example.gabo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.PathOverlay;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/*-------------------핀 선택시 뜨는 유저 댓글 바텀시트 프래그먼트 --------------------------*/

public class BottomSheetDialogFrag extends BottomSheetDialogFragment {
    private MainActivity main;
    BottomSheetBehavior bBehavior;
    private NaverMap naverMap;

    private ListView tc_list;
    private trsCommentAdapter tcAdapter = new trsCommentAdapter();

    //위치안내버튼
    private TextView btn_howtogo;
    //찾았다버튼
    private TextView btn_find;
    private Dialog win_dialog; //정답축하합니다 다이얼로그
    private Dialog findquizDailog;

    // 숨긴사람 이름
    private TextView ti_tv_comment;

    // 숨긴 보물의 해쉬태그
    private TextView ti_tv_tag1, ti_tv_tag2, ti_tv_tag3;

    // 메인엑티비티에서 번들로 가져온값을 저장하려고 만듬
    private String cate,key1,key2,key3,hideuser,latitude,longitude,hidedate,like,user_location;

    //trs_comment_bottom_sheet_lyt 레이아웃에서 변경한 값을 넣어줄 아이디를 넣어줄 변수
    private TextView ti_tv_when,ti_tv_like;

    // 하트 이미지
    private ImageView ti_img_like;
    //

    private BottomSheetDialogFrag bottomSheetDialogFrag;

    private RequestQueue queue;
    private StringRequest stringRequest;

    private ArrayList<String> comments;

    private String mainhost;

    private String user_id;


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

        // 메인액티비티에서 서버 리스폰 받아서 가져옴
        user_id = getArguments().getString("userid");
        cate = getArguments().getString("cate");
        key1 =getArguments().getString("key1","0");
        key2 = getArguments().getString("key2","0");
        key3 = getArguments().getString("key3","0");
        hideuser = getArguments().getString("hideuser","0");
        latitude = getArguments().getString("latitude");
        longitude = getArguments().getString("longitude");
        hidedate = getArguments().getString("hidedate","0");
        like = getArguments().getString("like","0");
        user_location = getArguments().getString("userlocation");
        mainhost = getArguments().getString("mainhost");

        String[] user_lo = user_location.split(",");
        Double user_latitude = Double.parseDouble(user_lo[0]);
        Double user_longitude = Double.parseDouble(user_lo[1]);

        Double t_latitude = Double.parseDouble(latitude);
        Double t_longitude = Double.parseDouble(longitude);

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(),R.layout.trs_comment_bottom_sheet_lyt,null);

        dialog.setContentView(view);
        bBehavior = BottomSheetBehavior.from((View)view.getParent());

        ti_tv_comment = view.findViewById(R.id.ti_tv_comment);
        ti_tv_tag1 = view.findViewById(R.id.ti_tv_tag1);
        ti_tv_tag2 = view.findViewById(R.id.ti_tv_tag2);
        ti_tv_tag3 = view.findViewById(R.id.ti_tv_tag3);
        ti_tv_when = view.findViewById(R.id.ti_tv_when);
        ti_img_like = view.findViewById(R.id.ti_img_like);
        ti_tv_like = view.findViewById(R.id.ti_tv_like);

        ti_tv_comment.setText(hideuser);
        ti_tv_tag1.setText("#"+key1);
        ti_tv_tag2.setText("#"+key2);
        ti_tv_tag3.setText("#"+key3);
        ti_tv_when.setText(hidedate+"에 숨김");
        ti_tv_like.setText(like);

        sendRequest();


        bottomSheetDialogFrag = new BottomSheetDialogFrag();

        /*---------------------하뚜 클릭--------------------- */
        ti_img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest_like();
            }
        });



        /*---------------------찾았다 버튼--------------------- */
        btn_find = view.findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext().getApplicationContext(),"찾았다!",Toast.LENGTH_SHORT).show();
//                openWinDialog();
                openquizDialog();
            }
        });



        /*---------------------찾았다 버튼--------------------- */
        btn_find = view.findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext().getApplicationContext(),"찾았다!",Toast.LENGTH_SHORT).show();
//                openWinDialog();
                openquizDialog();
            }
        });

        /*---------------------위치안내 버튼--------------------- */
        btn_howtogo = view.findViewById(R.id.btn_howtogo);
        btn_howtogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(),"위치안내",Toast.LENGTH_SHORT).show();

            }

        });
        

        /*유저 댓글 리스트뷰*/
        tc_list = view.findViewById(R.id.user_comment_listview);
        tcAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e1),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "gil-dong","#나무아래","#작은소품","#파란색","1분전에 숨김","38","친구들이랑 밥먹으러 가다 찾았어요!"

        );

        tcAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e2),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "gil-dong","#전봇대","#조명","#골드","08/11/22 06:10 PM에 찾음","5","강아지랑 산책하다 찾았어요!꿀잼.."
        );

        tcAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e3),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "JellyBean","#상자속","#텀블러","#노란색","08/06/22 09:00 PM에 찾음","25","텀블러 겟함! 감사감사"
        );

        tcAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e4),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "mmthcoffee","#카페앞","#문구류","#핑크","07/25/22 10:00 PM에 찾음","40","예쁜 핑크색펜이었어요"
        );
        tcAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e5),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "bean","#커피창고","#음료","#커피색","07/20/11 12:45 PM에 찾음","10","커피쿠폰이었음 굿"
        );
        tcAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e6),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "heets","#의자옆","#문구류","#보라색","07/11/10 10:00 PM에 찾음","55","득템 ㄳ"
        );

        tc_list.setAdapter(tcAdapter);
        ListView user_comment_listview = getActivity().findViewById(R.id.user_comment_listview);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    /*---------------------------------------보물찾기 퀴즈 다이얼로그 실행---------------------------------------*/
    private void openquizDialog(){
        findquizDailog = new Dialog(getContext());
        findquizDailog.setContentView(R.layout.dialog_findquiz);
        findquizDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageViewClose=findquizDailog.findViewById(R.id.imageViewClose);
        TextView tv_findquiz1 = findquizDailog.findViewById(R.id.tv_findquiz1);
        TextView tv_findquiz2 = findquizDailog.findViewById(R.id.tv_findquiz2);
        TextView tv_findquiz3 = findquizDailog.findViewById(R.id.tv_findquiz3);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findquizDailog.dismiss();
                Toast.makeText(getContext(), "Dialog Close", Toast.LENGTH_SHORT).show();
            }
        });
        tv_findquiz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWinDialog();
                findquizDailog.dismiss();
            }
        });
        tv_findquiz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWinDialog();
                findquizDailog.dismiss();
            }
        });
        tv_findquiz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWinDialog();
                findquizDailog.dismiss();
            }
        });
        findquizDailog.show();

    }


    /*-----------------------------------보물찾기 정답 축하합니다 다이얼로그 실행-------------------------------------*/

    private void openWinDialog() {
        win_dialog = new Dialog(getContext());
        win_dialog.setContentView(R.layout.win_layout_dialog);
        win_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageViewClose=win_dialog.findViewById(R.id.imageViewClose);
        TextView tvOk=win_dialog.findViewById(R.id.tvOk);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                win_dialog.dismiss();
                Toast.makeText(getContext(), "Dialog Close", Toast.LENGTH_SHORT).show();
            }
        });
        /*보물찾기 완료 버튼을 누르게 되면
        * 찾았다 버튼이 코멘트 남기기로 바뀐다*/
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                win_dialog.dismiss();
                btn_find.setText("코멘트 남기기");
                btn_find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "코멘트남기기 화면으로 넘어가기", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        win_dialog.show();

    }

    public void sendRequest() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(getContext().getApplicationContext());
        // 서버에 요청할 주소
        String url = mainhost+"commentlist";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
                comments.add(response);
                System.out.println("어디보자"+comments.get(0));

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
//                System.out.println("숨긴유저"+hideuser);
                params.put("hide_user", hideuser);

                return params;
            }
        };


        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }

    public void sendRequest_like() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(getContext().getApplicationContext());
        // 서버에 요청할 주소
        String url = mainhost+"like";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);


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
//                System.out.println("숨긴유저"+hideuser);
                params.put("user_id",user_id);
                params.put("hide_user", hideuser);

                return params;
            }
        };


        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }

}
