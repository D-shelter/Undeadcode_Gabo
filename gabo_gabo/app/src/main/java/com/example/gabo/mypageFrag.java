package com.example.gabo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

public class mypageFrag extends Fragment {

    LinearLayout myfind_open ; //내가 찾은 보물 버튼
    LinearLayout myfind_bignum; //내가 찾은 보물 숫자 버튼

    LinearLayout myhide_open; //내가 숨긴 보물 버튼
    LinearLayout myhide_bignum; //내가 숨긴 보물 숫자 버튼

    LinearLayout mycomment_open; //게시물 모아보기 버튼


    MyFindTreasureFrag myFindTreasureFrag; //내가 찾은 보물 바텀시트 프래그먼트
    MyHideTreasureFrag myHideTreasureFrag; //내가 숨긴 보물 바텀시트 프래그먼트
    MyCommentsFrag myCommentsFrag; //게시물 모아보기 바텀시트 프래그먼트

    FragmentManager fmm ;

    private RequestQueue queue;
    private StringRequest stringRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_lyt,container,false);

        fmm=getActivity().getSupportFragmentManager();


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

        return view;


    }
}
