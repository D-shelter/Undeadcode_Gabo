package com.example.gabo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabo.VO.myFindtreasureVO;

import java.util.ArrayList;

/*--------------------------마이 페이지 > 내가 찾은 보물 리스트 어댑터---------------------------*/
public class myFindAdapter extends BaseAdapter {

    private ArrayList<myFindtreasureVO> items = new ArrayList<>();

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int i) { return items.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if (view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.myfind_listview_lyt,viewGroup,false);
        }

        myFindtreasureVO vo= items.get(i);
        ImageView myfind_img_profile = view.findViewById(R.id.myfind_img_profile);
        ImageView myfind_img_like = view.findViewById(R.id.myfind_img_like);
        TextView myfind_tv_userid = view.findViewById(R.id.myfind_tv_userid);
        TextView myfind_tv_tag1 = view.findViewById(R.id.myfind_tv_tag1);
        TextView myfind_tv_tag2 = view.findViewById(R.id.myfind_tv_tag2);
        TextView myfind_tv_tag3 = view.findViewById(R.id.myfind_tv_tag3);
        TextView myfind_tv_when = view.findViewById(R.id.myfind_tv_when);
        TextView myfind_tv_like = view.findViewById(R.id.myfind_tv_like);
        TextView myfind_tv_km = view.findViewById(R.id.myfind_tv_km);


        myfind_img_profile.setImageDrawable(vo.getMyfind_img_profile());
        myfind_img_like.setImageDrawable(vo.getMyfind_img_like());
        myfind_tv_userid.setText(vo.getMyfind_tv_userid());
        myfind_tv_tag1.setText(vo.getMyfind_tv_tag1());
        myfind_tv_tag2.setText(vo.getMyfind_tv_tag2());
        myfind_tv_tag3.setText(vo.getMyfind_tv_tag3());
        myfind_tv_when.setText(vo.getMyfind_tv_when());
        myfind_tv_like.setText(vo.getMyfind_tv_like());
        myfind_tv_km.setText(vo.getMyfind_tv_km());

        return view;
    }

    public void addItems(Drawable myfind_img_profile, Drawable myfind_img_like, String myfind_tv_userid, String myfind_tv_tag1, String myfind_tv_tag2, String myfind_tv_tag3, String myfind_tv_when, String myfind_tv_like, String myfind_tv_km){
        myFindtreasureVO vo = new myFindtreasureVO(myfind_img_profile, myfind_img_like, myfind_tv_userid, myfind_tv_tag1, myfind_tv_tag2, myfind_tv_tag3, myfind_tv_when, myfind_tv_like,myfind_tv_km);
        this.items.add(vo);
    }
}
