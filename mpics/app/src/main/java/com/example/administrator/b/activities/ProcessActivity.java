package com.example.administrator.b.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.b.BaseActivity;
import com.example.administrator.b.R;
import com.example.administrator.b.utils.PhotoClass;
import com.example.administrator.b.utils.PhotoFunction;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener {

    private CascadeClassifier cascadeClassifier;

    private static final String SumiaoString = "Sumiao";//素描
    private static final String GrayString = "Gray"; //灰度化
    private static final String thresholdString = "threshold";//二值化
    private static final String LunkuoString = "Lunkuo";//轮廓
    private static final String HuaijiuString = "Huaijiu"; //怀旧
    private static final String LianhuanhuaFString = "LianhuanhuaF"; //连环画
    private static final String BingDongString = "Bingdong"; //冰冻
    private static final String RongzhuString = "Rongzhu"; //熔铸
    private static final String FudiaoString = "Fudiao";//浮雕

    private Bitmap photo;
    private Bitmap src;
    private PhotoFunction func;
    private PhotoClass pc;
    private Message ms;
    private ProgressDialog progressDialog;
   private Uri imageUri = null;
   private String mFilePath;
   private FileInputStream is = null;

    private ImageView process_back;
    private ImageView process_save;
    private ImageView process_photo;
    private ImageButton process_contrast;

    private Button button_initial;
    private Button button_Sketch;
    private Button button_Lunkuo;
    private Button button_Fudiao;
    private Button button_Gray;
    private Button button_Theshold;
    private Button button_Lianhuanhua;
    private Button button_Huaijiu;
    private Button button_Bingdong;
    private Button button_Rongzhu;
    private Button button_face;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    initializeOpenCVDependencies();
                    //Toast.makeText(ProcessActivity.this,"成功打开opencv", Toast.LENGTH_LONG).show();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_process);

        init();
        showInitialPhoto();
    }
    private void init(){
        src = null;
        func = new PhotoFunction();

        process_back = (ImageView)findViewById(R.id.process_back);
        process_save = (ImageView)findViewById(R.id.process_save);
        process_photo = (ImageView)findViewById(R.id.process_photo);
        process_contrast = (ImageButton)findViewById(R.id.process_contrast);

        button_initial = (Button)findViewById(R.id.button_initial);
        button_Bingdong = (Button)findViewById(R.id.button_frozen);
        button_Fudiao = (Button)findViewById(R.id.button_embossment);
        button_Gray = (Button)findViewById(R.id.button_gray);
        button_Huaijiu= (Button)findViewById(R.id.button_reminiscence);
        button_Sketch = (Button)findViewById(R.id.button_sketch);
        button_Rongzhu= (Button)findViewById(R.id.button_casting);
        button_Lunkuo =  (Button)findViewById(R.id.button_outline);
        button_Theshold = (Button)findViewById(R.id.button_binaryzation);
        button_Lianhuanhua = (Button)findViewById(R.id.button_cartoon);
        button_face= (Button)findViewById(R.id.button_face_recognition);

        process_back.setOnClickListener(this);
        process_save.setOnClickListener(this);
        button_initial.setOnClickListener(this);
        button_face.setOnClickListener(this);
        button_Bingdong.setOnClickListener(this);
        button_Fudiao.setOnClickListener(this);
        button_Gray.setOnClickListener(this);
        button_Huaijiu.setOnClickListener(this);
        button_Theshold.setOnClickListener(this);
        button_Rongzhu.setOnClickListener(this);
        button_Lunkuo.setOnClickListener(this);
        button_Lianhuanhua.setOnClickListener(this);
        button_Sketch.setOnClickListener(this);

        process_contrast.setOnTouchListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.process_back:
                ProcessActivity.this.finish();
                break;
            case R.id.process_save:
                Dialog dia = new AlertDialog.Builder(ProcessActivity.this)
                        .setMessage("确定保存当前图片吗？")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dia,int whichButton){
                                saveImage();
                            }})
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dia,int whichButton){}
                        }).create();
                dia.show();
                break;
            case R.id.button_initial:
                process_photo.setImageBitmap(photo);
                break;
            case R.id.button_face_recognition:
                process_photo.setImageBitmap(FaceDetect(photo));
                break;
            case R.id.button_sketch:
                if(pc.get_Sumiao_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    SumiaoThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_Sumiao_photo());
                break;
            case R.id.button_binaryzation:
                if(pc.get_theshold_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    ThesThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_theshold_photo());
                break;
            case R.id.button_outline:
                if(pc.get_Lunkuo_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    LunKuoThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_Lunkuo_photo());
                break;
            case R.id.button_casting:
                if(pc.get_Rongzhu_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    RongzhuThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_Rongzhu_photo());
                break;
            case R.id.button_gray:
                if(pc.get_Gray_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    GrayThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_Gray_photo());
                break;
            case R.id.button_embossment:
                if(pc.get_FuDiao_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    FudiaoThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_FuDiao_photo());
                break;
            case R.id.button_frozen:
                if(pc.get_BingDong_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    BingDongThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_BingDong_photo());
                break;
            case R.id.button_cartoon:
                if(pc.get_LianHuanHua_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    LianhuanhuaThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_LianHuanHua_photo());
                break;
            case R.id.button_reminiscence:
                if(pc.get_Huaijiu_photo()==null){
                    progressDialog = ProgressDialog.show(ProcessActivity.this, "", "加载滤镜中，请稍后……");
                    HuaijiuThread();
                }
                else
                    process_photo.setImageBitmap(pc.get_Huaijiu_photo());
                break;
            default:break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.process_contrast:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        process_photo.setImageBitmap(src);
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        src = convertViewToBitmap();
                        process_photo.setImageBitmap(photo);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.obj==SumiaoString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_Sumiao_photo());
            }
            else if(msg.obj==BingDongString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_BingDong_photo());
            }
            else if(msg.obj==thresholdString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_theshold_photo());
            }
            else if(msg.obj==LianhuanhuaFString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_LianHuanHua_photo());
            }
            else if(msg.obj==LunkuoString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_Lunkuo_photo());
            }
            else if(msg.obj==RongzhuString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_Rongzhu_photo());
            }
            else if(msg.obj==GrayString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_Gray_photo());
            }
            else if(msg.obj==FudiaoString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_FuDiao_photo());
            }
            else if(msg.obj==HuaijiuString) {
                progressDialog.dismiss();
                process_photo.setImageBitmap(pc.get_Huaijiu_photo());
            }
        }
    };
    private void SumiaoThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_Sumiao_photo(func.SuMiao(photo));
                ms = new Message();
                ms.obj = SumiaoString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void GrayThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_Gray_photo(func.RGB2Gray(photo));
                ms = new Message();
                ms.obj = GrayString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }
    private void ThesThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_put_theshold_photo(func.theshold(photo));
                ms = new Message();
                ms.obj = thresholdString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void LunKuoThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_Lunkuo_photo(func.Lunkuo(photo));
                ms = new Message();
                ms.obj = LunkuoString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void HuaijiuThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_Huaijiu_photo(func.HuaiJiu(photo));
                ms = new Message();
                ms.obj = HuaijiuString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void LianhuanhuaThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_LianHuanHua_photo(func.LianHuanHua(photo));
                Message ms = new Message();
                ms.obj =LianhuanhuaFString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void RongzhuThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_Rongzhu_photo(func.RongZhu(photo));
                ms = new Message();
                ms.obj =RongzhuString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void BingDongThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_BingDong_photo(func.BingDong(photo));
                ms = new Message();
                ms.obj =BingDongString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    private void FudiaoThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.put_FuDiao_photo(func.FuDiao(photo));
                ms = new Message();
                ms.obj =FudiaoString;
                handler.sendMessage(ms);// 执行耗时的方法之后发送消给handler
            }
        }).start();
    }

    //保存图片
    private void saveImage(){
        Bitmap bmp = convertViewToBitmap();
        File appDir = new File(Environment.getExternalStorageDirectory(), "photo");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
        Toast.makeText(ProcessActivity.this,"保存成功",Toast.LENGTH_LONG).show();
    }

    //人脸检测函数
    private Bitmap FaceDetect(Bitmap photo){
        Mat srcmap = new Mat();
        Mat graymap = new Mat();
        MatOfRect faces = new MatOfRect();
        int FaceSize = (int)(photo.getWidth()*0.2);
        Utils.bitmapToMat(photo,srcmap);
        Imgproc.cvtColor(srcmap,graymap,Imgproc.COLOR_RGBA2RGB);
        if (cascadeClassifier != null ){
            cascadeClassifier.detectMultiScale(graymap,faces,1.1, 2, 2,
                    new Size(FaceSize,FaceSize),new Size());
        }
        else {
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i <facesArray.length; i++)
            Imgproc.rectangle(srcmap, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);

        Bitmap result = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.RGB_565);;
        Utils.matToBitmap(srcmap,result);
        return result;
    }

    private void showInitialPhoto(){
        String permission = getIntent().getStringExtra("permission");
        if(permission.equalsIgnoreCase("camera_permission"))
            openCamera(); //打开相机
        else if (permission.equalsIgnoreCase("file_permission"))
            openFile(); //打开相册文件夹
    }




    //打开相机
    private void openCamera(){
        //查看内存卡状态
        String Envior = Environment.getExternalStorageState();
        if (Envior.equals(Environment.MEDIA_MOUNTED)) {

            photo = null;
            //启用相机程序
//            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//            startActivityForResult(intent,1);
            File outputImage = new File(getExternalCacheDir(),"output_image.jpg");//放在关联缓存目录
            try {
                if(outputImage.exists()){
                    outputImage.delete();
                }
                outputImage.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            if(Build.VERSION.SDK_INT >= 24){
                imageUri = FileProvider.getUriForFile(ProcessActivity.this,"com.example.b.fileprovider",outputImage);
            }
            else{
                imageUri = Uri.fromFile(outputImage);
            }
//            Log.d("aaaaa","imageURI"+imageUri.toString());
//            Log.d("bbb","outputImage"+outputImage.getAbsolutePath().toString());
            //启用相机程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(intent,1);


        }
        else
            Toast.makeText(ProcessActivity.this,"内存不可用",Toast.LENGTH_SHORT).show();
    }

    //打开相册文件
    private void openFile(){
        photo =null;
        Intent ie2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ie2,2);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:{ //打开相机
                    if(resultCode == RESULT_OK){
                        try {
                            //将拍摄的照片显示出来
                            Bitmap bitmap  = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            process_photo.setImageBitmap(bitmap);//显示
                            photo = bitmap;
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                        pc = new PhotoClass(photo);
                        process_photo.setImageBitmap(photo);
                    }
                    else this.finish();

                    break;
                }

                case 2:{
                    //打开相册并选择照片，这个方式选择单张
                    // 获取返回的数据，这里是android自定义的Uri地址
                    if(resultCode == RESULT_OK){
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        // 获取选择照片的数据视图
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        // 从数据视图中获取已选择图片的路径
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        // 将图片显示到界面上

                        photo = getScaleBitmap(picturePath);
                        pc = new PhotoClass(photo);
                        process_photo.setImageBitmap(photo);
                    }else this.finish();

                    break;
                }
                default:
                    break;
            }

    }


    private Bitmap getScaleBitmap(String wallpaperPath) {
        Bitmap bm = BitmapFactory.decodeFile(wallpaperPath);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth=dm.widthPixels;
        /*
         * 图片的宽度 （用于判断当前图判是否过大，避免OOM使得imageview无法显示）
         * 适用于 720P，其他分辨率需要重新修改
         */
        int mFixedWidth = 2870;
        //图片的高度 （用于客户预置特别大图，重新处理bitmap）
        int mFixedHeight = 1920;
        if(bm.getWidth()<=screenWidth){
            return bm;
        }else{
            //Bitmap bmp=Bitmap.createScaledBitmap(bm, screenWidth, bm.getHeight()*screenWidth/bm.getWidth(), true);
            if (bm.getWidth()<= mFixedWidth) {
                return bm;
            }
            Bitmap bmp=Bitmap.createScaledBitmap(bm, bm.getWidth()*mFixedHeight/bm.getHeight(), mFixedHeight, true);
            return bmp;
        }
    }



    public Bitmap convertViewToBitmap(){
        process_photo.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(process_photo.getDrawingCache());
        process_photo.setDrawingCacheEnabled(false);
        process_photo.setImageBitmap(bitmap);
        return bitmap;
    }

    //加载Opencv
    private void initializeOpenCVDependencies(){
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the get
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
    }



    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d("initial_error", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("initial_error", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


}
