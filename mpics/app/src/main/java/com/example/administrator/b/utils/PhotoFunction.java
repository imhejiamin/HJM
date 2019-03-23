package com.example.administrator.b.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PhotoFunction {

    public PhotoFunction(){}

    //减淡函数
    private int colordodge(int A, int B) {
        return  Math.min(A+(A*B)/(255-B+1),255);
    }



    //灰度化方法，直接使用 opencv 的 Imgproc.COLOR_RGB2GRAY 这个灰度处理方法
    public Bitmap RGB2Gray(Bitmap photo) {
        Mat RGBMat = new Mat();
        Bitmap grayBitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.RGB_565);//RGB_565就是R为5位，G为6位，B为5位共16位
        Utils.bitmapToMat(photo, RGBMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(RGBMat, RGBMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(RGBMat, grayBitmap);
        return grayBitmap;
    }

    //素描滤镜
    public Bitmap SuMiao(Bitmap photo){
        Mat SM = new Mat();
        Mat SM1 = new Mat();
        int w = photo.getWidth();
        int h = photo.getHeight();
        Bitmap sumiaoMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Bitmap SMB = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Bitmap SMB1 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Utils.bitmapToMat(photo, SM);

        Imgproc.cvtColor(SM, SM, Imgproc.COLOR_RGB2GRAY);  //灰度化
        Core.bitwise_not(SM,SM1);      //颜色取反
        Imgproc.GaussianBlur(SM1,SM1,new Size(13,13),0,0);//高斯模糊

        Utils.matToBitmap(SM, SMB); //这是灰度化的图
        Utils.matToBitmap(SM1, SMB1); //这是灰度化后取反再模糊的图

        int[] pixels = new int[w * h];
        int[] pixels_1 = new int[w * h];
        int[] pixels_2 = new int[w * h];
        int index = 0;
        int index_1 = 0;
        SMB.getPixels(pixels,0,w,0,0,w,h);
        SMB1.getPixels(pixels_1,0,w,0,0,w,h);
        for(int i = 0;i < w * h;i++){

            index = pixels[i];
            index_1 = pixels_1[i];

            int CR = colordodge(Color.red(index),Color.red(index_1));
            int CG = colordodge(Color.green(index),Color.green(index_1));
            int CB = colordodge(Color.blue(index),Color.blue(index_1));
            CR = CR > 255 ? 255 : CR;
            CG = CG > 255 ? 255 : CG;
            CB = CB > 255 ? 255 : CB;

            pixels_2[i] = Color.rgb(CR, CG, CB);
        }
        sumiaoMap.setPixels(pixels_2,0,w,0,0,w,h);
        return sumiaoMap;
    }

    //黑白,改成木刻
    public Bitmap theshold(Bitmap photo){
        Mat mat = new Mat();
        Bitmap thes = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.bitmapToMat(photo, mat);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
        //Imgproc.GaussianBlur(mat,mat,new Size(13,13),0,0);
        //Imgproc.Canny(mat,mat,70,210);
        Core.bitwise_not(mat,mat);//bitwise_not是对二进制数据进行“非”操作，即对图像（灰度图像或彩色图像均可）每个像素值进行二进制“非”操作，~1=0，~0=1
        Imgproc.threshold(mat,mat,120,255,Imgproc.THRESH_BINARY_INV);
        Utils.matToBitmap(mat,thes);
        return thes;
    }

    //轮廓
    public Bitmap Lunkuo(Bitmap photo){
        Mat mat = new Mat();
        Mat Cmat = new Mat();
        //Mat Bmat = new Mat();
        Bitmap resultBitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.bitmapToMat(photo, mat);
        Imgproc.Canny(mat,Cmat,50,200);  //cany边缘检测
        Core.bitwise_not(Cmat,Cmat); //反色处理，因为canny边缘检测后底色，轮廓颜色为白
        Utils.matToBitmap(Cmat, resultBitmap);
        return resultBitmap;
    }

    //怀旧色滤镜
    public Bitmap HuaiJiu(Bitmap photo){
        int w = photo.getWidth();
        int h = photo.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[w * h];
        int[] pixels_1 = new int[w * h];
        int index = 0;
        photo.getPixels(pixels,0,w,0,0,w,h);//取得photo像素矩阵（其实是数组）到pixels
        for(int i = 0;i < w * h ;i++){

            index = pixels[i];
            int AR =(int)(0.393*Color.red(index) + 0.769*Color.green(index) + 0.189*Color.blue(index));
            int AG =(int)(0.349*Color.red(index) + 0.686*Color.green(index) + 0.168*Color.blue(index));
            int AB =(int)(0.272*Color.red(index) + 0.534*Color.green(index) + 0.131*Color.blue(index));
            AR = AR > 255 ? 255 : AR;
            AG = AG > 255 ? 255 : AG;
            AB = AB > 255 ? 255 : AB;
            pixels_1[i] = Color.rgb(AR, AG, AB);
        }
        resultBitmap.setPixels(pixels_1,0,w,0,0,w,h);
        return resultBitmap;
    }

    //连环画滤镜
    public Bitmap LianHuanHua(Bitmap photo){
        Mat mat = new Mat();
        int w = photo.getWidth();
        int h = photo.getHeight();
        int[] pixels = new int[w * h];
        int[] pixels_1 = new int[w * h];
        int index = 0;
        photo.getPixels(pixels,0,w,0,0,w,h);//取得photo像素矩阵（其实是数组）到pixels
        Bitmap resultBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        for(int i = 0;i < w * h ;i++){

            index = pixels[i];
            int AR =Math.abs(Color.red(index) - Color.blue(index) + Color.green(index)+ Color.green(index)) * Color.red(index) / 256;
            int AG =Math.abs(Color.red(index) - Color.green(index) + Color.blue(index) + Color.blue(index)) * Color.red(index) / 256;
            int AB =Math.abs(Color.red(index) - Color.blue(index) + Color.blue(index) + Color.blue(index)) * Color.green(index) / 256;
            AR = AR > 255 ? 255 : AR;
            AG = AG > 255 ? 255 : AG;
            AB = AB > 255 ? 255 : AB;
            pixels_1[i] = Color.rgb(AR, AG, AB);

        }
        resultBitmap.setPixels(pixels_1,0,w,0,0,w,h);
        Utils.bitmapToMat(resultBitmap, mat);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(mat,resultBitmap);
        return resultBitmap;
    }


    //熔铸滤镜(换成了宝丽来滤镜）
    public Bitmap RongZhu(Bitmap photo){
        int w = photo.getWidth();
        int h = photo.getHeight();
        int[] pixels = new int[w * h];
        int[] pixels_1 = new int[w * h];
        int index = 0;
        photo.getPixels(pixels,0,w,0,0,w,h);//取得photo像素矩阵（其实是数组）到pixels
        Bitmap resultBitmap  = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        for(int i = 0;i < w * h;i++){
            index = pixels[i];
            //熔铸的原理，觉得效果不好看，颜色突兀
//            int AR =Color.red(index)*128/(Color.blue(index)+Color.green(index)+1);
//            int AG =Color.green(index)*128/(Color.blue(index)+Color.red(index)+1);
//            int AB =Color.blue(index)*128/(Color.red(index)+Color.green(index)+1);
            int AR = (int) (1.438 * Color.red(index) + (-0.062) * Color.green(index) + (-0.062) * Color.blue(index));
            int AG = (int) ((-0.122) * Color.red(index) + 1.378 * Color.green(index) + (-0.122) * Color.blue(index));
            int AB = (int) ((-0.016) * Color.red(index) + (-0.016) * Color.green(index) + 1.483 * Color.blue(index));
            int AA = (int) ((-0.03) * Color.red(index) + 0.05 * Color.green(index) + (-0.02) * Color.blue(index));
            AR = AR > 255 ? 255 : AR;
            AG = AG > 255 ? 255 : AG;
            AB = AB > 255 ? 255 : AB;
            AA = AA > 255 ? 255 : AA;
            pixels_1[i] = Color.argb(AA,AR, AG, AB);
        }
        resultBitmap.setPixels(pixels_1,0,w,0,0,w,h);
        return resultBitmap;

    }

    //冰冻滤镜,改成高斯模糊
    public Bitmap BingDong(Bitmap photo){
        int width = photo.getWidth();
        int height = photo.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Mat mat = new Mat(width, height, CvType.CV_8UC4);
        Utils.bitmapToMat(photo, mat);
        Imgproc.GaussianBlur(mat, mat, new Size(13, 13),0,0);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

    //浮雕滤镜
    public Bitmap FuDiao(Bitmap photo){
        int[] pixels = new int[photo.getHeight() * photo.getWidth()];
        int[] newPixels = new int[photo.getHeight() * photo.getWidth()];
        photo.getPixels(pixels,0,photo.getWidth(),0,0,photo.getWidth(),photo.getHeight());//取得photo像素矩阵（其实是数组）到pixels
        Bitmap fudiao  = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        for(int i = 1;i<photo.getHeight()-1;i++){
            for( int j = 1;j<photo.getWidth()-1;j++){
                //int A = photo.getPixel(i-1,j-1);
                //int B = photo.getPixel(i+1,j+1);
                int A = pixels[(i-1)*photo.getWidth() + j - 1];
                int B = pixels[(i+1)*photo.getWidth() + j + 1];
                int AR =Color.red(B)-Color.red(A)+128;
                int AG =Color.green(B)-Color.green(A)+128;
                int AB =Color.blue(B)-Color.blue(A)+128;
                AR = AR > 255 ? 255 : AR;
                AG = AG > 255 ? 255 : AG;
                AB = AB > 255 ? 255 : AB;
                newPixels[i*photo.getWidth()+j]= Color.rgb(AR, AG, AB);
            }
        }
        fudiao.setPixels(newPixels,0,photo.getWidth(),0,0,photo.getWidth(),photo.getHeight());
        return fudiao;
    }

    public Bitmap Beauty(Bitmap photo , Float... bilityTraversal) {
        Bitmap resultBitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        if (bilityTraversal[0] > 0) {
            // 磨皮美颜算法
            int dx = (int) bilityTraversal[0].floatValue() * 5; // 双边滤波参数之一
            double fc = bilityTraversal[0] * 12.5; // 双边滤波参数之一
            double p = 0.1f; // 透明度
            Mat image = new Mat();
            Mat dst = new Mat();
            Mat matBilFilter = new Mat();
            Mat matGaussSrc = new Mat();
            Mat matGaussDest = new Mat();
            Mat matTmpDest = new Mat();
            Mat matSubDest = new Mat();
            Mat matTmpSrc = new Mat();

            // 双边滤波
            Utils.bitmapToMat(photo, image);
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2BGR);
            Imgproc.bilateralFilter(image, matBilFilter, dx, fc, fc);

            Core.subtract(matBilFilter, image, matSubDest);
            Core.add(matSubDest, new Scalar(128, 128, 128, 128), matGaussSrc);

            // 高斯模糊
            Imgproc.GaussianBlur(matGaussSrc, matGaussDest, new Size(2 * bilityTraversal[0] - 1, 2 * bilityTraversal[0] - 1), 0, 0);
            matGaussDest.convertTo(matTmpSrc, matGaussDest.type(), 2, -255);
            Core.add(image, matTmpSrc, matTmpDest);
            Core.addWeighted(image, p, matTmpDest, 1 - p, 0.0, dst);

            Core.add(dst, new Scalar(10, 10, 10), dst);
            Utils.matToBitmap(dst, resultBitmap);
        }
        return resultBitmap;
    }




}
