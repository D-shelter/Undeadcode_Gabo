package com.example.gabo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabo.VO.myHideTreasureVO;

import java.util.ArrayList;

public class myHideAdapter extends BaseAdapter {

    private ArrayList<myHideTreasureVO> items = new ArrayList<>();

    @Override
    public int getCount() {return items.size();}

    @Override
    public Object getItem(int i) { return items.get(i); }

    @Override
    public long getItemId(int i) { return i;}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        if (view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.myhide_listview_lyt,viewGroup,false);
        }

        myHideTreasureVO vo = items.get(i);
        ImageView myhide_img_profile = view.findViewById(R.id.myhide_img_profile);
        ImageView myhide_img_like = view.findViewById(R.id.myhide_img_like);
        TextView myhide_tv_km = view.findViewById(R.id.myhide_tv_who);
        TextView myhide_tv_tag1 = view.findViewById(R.id.myhide_tv_tag1);
        TextView myhide_tv_tag2 = view.findViewById(R.id.myhide_tv_tag2);
        TextView myhide_tv_tag3 = view.findViewById(R.id.myhide_tv_tag3);
        TextView myhide_tv_when = view.findViewById(R.id.myhide_tv_when);
        TextView myhide_tv_like = view.findViewById(R.id.myhide_tv_like);

        myhide_img_profile.setImageDrawable(vo.getMyhide_img_profile());
        myhide_img_like.setImageDrawable(vo.getMyhide_img_like());
        myhide_tv_km.setText(vo.getMyhide_tv_km());
        myhide_tv_tag1.setText(vo.getMyhide_tv_tag1());
        myhide_tv_tag2.setText(vo.getMyhide_tv_tag2());
        myhide_tv_tag3.setText(vo.getMyhide_tv_tag3());
        myhide_tv_when.setText(vo.getMyhide_tv_when());
        myhide_tv_like.setText(vo.getMyhide_tv_like());

        return view;
    }

    public void addItems(Drawable myhide_img_profile, Drawable myhide_img_like, String myhide_tv_km, String myhide_tv_tag1, String myhide_tv_tag2, String myhide_tv_tag3, String myhide_tv_when, String myhide_tv_like){
        myHideTreasureVO vo = new myHideTreasureVO(myhide_img_profile, myhide_img_like, myhide_tv_km, myhide_tv_tag1, myhide_tv_tag2, myhide_tv_tag3, myhide_tv_when, myhide_tv_like);
        this.items.add(vo);
    }
}
