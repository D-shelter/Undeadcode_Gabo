package com.example.gabo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/*--------------------------마이페이지 > 내가 찾은 보물 리스트 프래그먼트---------------------------*/
public class MyFindTreasureFrag extends BottomSheetDialogFragment {

    BottomSheetBehavior bBehavior;

    private ListView myFindList;
    private myFindAdapter myFindAdapter = new myFindAdapter();

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
        View view = View.inflate(getContext(),R.layout.myfindtreasure,null);

        dialog.setContentView(view);
        bBehavior = BottomSheetBehavior.from((View)view.getParent());


        /*----내가 찾은 보물 리스트뷰---*/
        myFindList = view.findViewById(R.id.myfind_list);
        myFindAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e1),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "gil-dong", "#나무밑","장난감","노란색","08/11/22 06:10 PM에 숨김","38","2km"
        );
        myFindAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e1),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "mammoth", "#나무밑","장난감","노란색","08/11/22 06:10 PM에 숨김","38","2km"
        );
        myFindAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e1),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "happyhome", "#나무밑","장난감","노란색","08/11/22 06:10 PM에 숨김","38","2km"
        );
        myFindAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e1),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "buzzz", "#나무밑","장난감","노란색","08/11/22 06:10 PM에 숨김","38","2km"
        );
        myFindAdapter.addItems(
                ContextCompat.getDrawable(getActivity(),R.drawable.e1),
                ContextCompat.getDrawable(getActivity(),R.drawable.like_full),
                "molang", "#나무밑","장난감","노란색","08/11/22 06:10 PM에 숨김","38","2km"
        );
        myFindList.setAdapter(myFindAdapter);
        ListView myfind_list = getActivity().findViewById(R.id.myfind_list);

        return dialog;

    }

    @Override
    public void onStart() {
        super.onStart();
    }


}
