package com.example.administrator.b.utils;

import android.graphics.Bitmap;

public class PhotoClass {
    private Bitmap Gray_photo;
    private Bitmap theshold_photo;
    private Bitmap Sumiao_photo;
    private Bitmap Lunkuo_photo;
    private Bitmap Huaijiu_photo;
    private Bitmap Polaroid_photo;
    private Bitmap Gaussian_photo;
    private Bitmap FuDiao_photo;
    private Bitmap LianHuanHua_photo;
    private Bitmap Beauty_photo;
    private Bitmap Beauty2_photo;
    private Bitmap Wind_photo;
    private Bitmap Summer_photo;
    private Bitmap Winter_photo;
    private Bitmap Cloudy_photo;

    public PhotoClass(Bitmap photo){
        Gray_photo = null;
        theshold_photo = null;
        Sumiao_photo = null;
        Lunkuo_photo = null;
        Huaijiu_photo = null;
        Polaroid_photo = null;
        Gaussian_photo = null;
        FuDiao_photo = null;
        LianHuanHua_photo = null;
        Beauty_photo = null;
        Beauty2_photo = null;
        Wind_photo = null;
        Summer_photo = null;
        Winter_photo = null;
        Cloudy_photo = null;
    }

    public void put_Gray_photo(Bitmap photo){
        this.Gray_photo = photo;
    }

    public void put_put_theshold_photo(Bitmap photo){
        this.theshold_photo = photo;
    }

    public void put_Sumiao_photo(Bitmap photo){
        this.Sumiao_photo = photo;
    }

    public void put_Huaijiu_photo(Bitmap photo){
        this.Huaijiu_photo = photo;
    }

    public void put_Polaroid_photo(Bitmap photo){
        this.Polaroid_photo = photo;
    }

    public void put_Gaussian_photo(Bitmap photo){
        this.Gaussian_photo = photo;
    }

    public void put_FuDiao_photo(Bitmap photo){
        this.FuDiao_photo = photo;
    }

    public void put_LianHuanHua_photo(Bitmap photo){
        this.LianHuanHua_photo = photo;
    }

    public void put_Lunkuo_photo(Bitmap photo){
        this.Lunkuo_photo = photo;
    }

    public void put_Beauty_photo(Bitmap photo){ this.Beauty_photo = photo;}

    public void put_Beauty2_photo(Bitmap photo){ this.Beauty2_photo = photo;}

    public void put_Wind_photo(Bitmap photo){
        this.Wind_photo = photo;
    }

    public void put_Summer_photo(Bitmap photo){
        this.Summer_photo = photo;
    }

    public void put_Winter_photo(Bitmap photo){
        this.Winter_photo = photo;
    }

    public void put_Cloudy_photo(Bitmap photo){
        this.Cloudy_photo = photo;
    }




    public Bitmap get_Lunkuo_photo(){
        return this.Lunkuo_photo;
    }

    public Bitmap get_Gray_photo(){
        return this.Gray_photo;
    }

    public Bitmap get_Huaijiu_photo(){
        return this.Huaijiu_photo;
    }

    public Bitmap get_Polaroid_photo(){
        return this.Polaroid_photo;
    }

    public Bitmap get_Sumiao_photo(){
        return this.Sumiao_photo;
    }

    public Bitmap get_theshold_photo(){
        return this.theshold_photo;
    }

    public Bitmap get_Gaussian_photo(){
        return this.Gaussian_photo;
    }

    public Bitmap get_LianHuanHua_photo(){
        return this.LianHuanHua_photo;
    }

    public Bitmap get_FuDiao_photo(){
        return this.FuDiao_photo;
    }

    public Bitmap get_Beauty_photo(){ return this.Beauty_photo;}

    public Bitmap get_Beauty2_photo(){ return this.Beauty2_photo;}

    public Bitmap get_Wind_photo(){ return this.Wind_photo;}

    public Bitmap get_Summer_photo(){ return this.Summer_photo;}

    public Bitmap get_Winter_photo(){ return this.Winter_photo;}

    public Bitmap get_Cloudy_photo(){ return this.Cloudy_photo;}
}
