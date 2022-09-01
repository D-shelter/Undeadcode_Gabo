package com.example.gabo.VO;


import android.graphics.drawable.Drawable;

/* ---------마이페이지 > 내가 찾은 보물 VO-----------*/
public class myFindtreasureVO {

    private Drawable myfind_img_profile;
    private Drawable myfind_img_like;
    private String myfind_tv_userid;
    private String myfind_tv_tag1;
    private String myfind_tv_tag2;
    private String myfind_tv_tag3;
    private String myfind_tv_when;
    private String myfind_tv_like;
    private String myfind_tv_km;

    public myFindtreasureVO(Drawable myfind_img_profile, Drawable myfind_img_like, String myfind_tv_userid, String myfind_tv_tag1, String myfind_tv_tag2, String myfind_tv_tag3, String myfind_tv_when, String myfind_tv_like, String myfind_tv_km) {
        this.myfind_img_profile = myfind_img_profile;
        this.myfind_img_like = myfind_img_like;
        this.myfind_tv_userid = myfind_tv_userid;
        this.myfind_tv_tag1 = myfind_tv_tag1;
        this.myfind_tv_tag2 = myfind_tv_tag2;
        this.myfind_tv_tag3 = myfind_tv_tag3;
        this.myfind_tv_when = myfind_tv_when;
        this.myfind_tv_like = myfind_tv_like;
        this.myfind_tv_km = myfind_tv_km;
    }

    public Drawable getMyfind_img_profile() {
        return myfind_img_profile;
    }

    public void setMyfind_img_profile(Drawable myfind_img_profile) {
        this.myfind_img_profile = myfind_img_profile;
    }

    public Drawable getMyfind_img_like() {
        return myfind_img_like;
    }

    public void setMyfind_img_like(Drawable myfind_img_like) {
        this.myfind_img_like = myfind_img_like;
    }

    public String getMyfind_tv_userid() {
        return myfind_tv_userid;
    }

    public void setMyfind_tv_userid(String myfind_tv_userid) {
        this.myfind_tv_userid = myfind_tv_userid;
    }

    public String getMyfind_tv_tag1() {
        return myfind_tv_tag1;
    }

    public void setMyfind_tv_tag1(String myfind_tv_tag1) {
        this.myfind_tv_tag1 = myfind_tv_tag1;
    }

    public String getMyfind_tv_tag2() {
        return myfind_tv_tag2;
    }

    public void setMyfind_tv_tag2(String myfind_tv_tag2) {
        this.myfind_tv_tag2 = myfind_tv_tag2;
    }

    public String getMyfind_tv_tag3() {
        return myfind_tv_tag3;
    }

    public void setMyfind_tv_tag3(String myfind_tv_tag3) {
        this.myfind_tv_tag3 = myfind_tv_tag3;
    }

    public String getMyfind_tv_when() {
        return myfind_tv_when;
    }

    public void setMyfind_tv_when(String myfind_tv_when) {
        this.myfind_tv_when = myfind_tv_when;
    }

    public String getMyfind_tv_like() {
        return myfind_tv_like;
    }

    public void setMyfind_tv_like(String myfind_tv_like) {
        this.myfind_tv_like = myfind_tv_like;
    }

    public String getMyfind_tv_km() {
        return myfind_tv_km;
    }

    public void setMyfind_tv_km(String myfind_tv_km) {
        this.myfind_tv_km = myfind_tv_km;
    }
}
