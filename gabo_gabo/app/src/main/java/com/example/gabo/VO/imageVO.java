package com.example.gabo.VO;

public class imageVO {
    private String imageUrl;

    imageVO(){

    }

    public imageVO(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
