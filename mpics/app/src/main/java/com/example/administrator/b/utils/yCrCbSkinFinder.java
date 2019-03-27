package com.example.administrator.b.utils;

public class yCrCbSkinFinder implements SkinFinder {
    @Override
    public boolean yCrCbSkin(int y, int Cr, int Cb) {
      // return   y> 30 &&(70 <Cb && Cb < 170) && (120 <Cr && Cr < 150);// 暂时效果还可以
        //return (y > 80)&& (85 <Cb && Cb < 135) && (135 <Cr && Cr < 180);//书上提供的判定算法，效果较差
        //return   (y > 40)&& (70 <Cb && Cb < 170) && (110 <Cr && Cr < 150); //这个效果也不错
        return   (y > 50 && y < 200)&& (100 <Cb && Cb < 170) && (110 <Cr && Cr < 150);//能克制白色背景,采用这个
    }

}
