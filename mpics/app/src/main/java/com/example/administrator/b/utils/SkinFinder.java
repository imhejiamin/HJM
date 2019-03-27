package com.example.administrator.b.utils;

public interface SkinFinder {

    /**
     *  <p>find skin pixel in YCrCb color space<p/>
     * @param y 16~235
     * @param Cr 15~240
     * @param Cb 15~240
     * @return
     */
    public boolean yCrCbSkin(int y, int Cr, int Cb);

}