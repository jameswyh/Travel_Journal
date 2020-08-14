package com.example.james_000.traveljournal.New;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Utils.AppConstant;
import com.example.james_000.traveljournal.Utils.BitmapUtils;
import com.example.james_000.traveljournal.Utils.CameraUtil;
import com.example.james_000.traveljournal.Utils.SystemUtils;

import java.io.File;
import java.io.IOException;

import static com.example.james_000.traveljournal.Utils.AppConstant.RESULT_CODE.RESULT_OK;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback,View.OnClickListener{
    private static final String TAG = "CameraFragment";
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private Context context;
    protected Activity activity;

    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private LinearLayout home_custom_top_relative;
    private ImageView camera_delay_time;
    private View homeCustom_cover_top_view;
    private View homeCustom_cover_bottom_view;
    private View home_camera_cover_top_view;
    private View home_camera_cover_bottom_view;
    private ImageView flash_light;
    private TextView camera_delay_time_text;
    private ImageView camera_square;
    private int index;
    //底部高度 主要是计算切换正方形时的动画高度
    private int menuPopviewHeight;
    //动画高度
    private int animHeight;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    //延迟时间
    private int delay_time;
    private int delay_time_temp;
    private boolean isview = false;
    private boolean is_camera_delay;
    private ImageView camera_frontback;
    private ImageView camera_close;
    private RelativeLayout homecamera_bottom_relative;
    private ImageView img_camera;
    private int picHeight;


    private static final int RESULT_IMAGE=100;
    //设置MIME码：表示image所有格式的文件均可
    private static final String IMAGE_TYPE="image/*";

    private String imagePath;
    private Cursor cursor;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        context = getActivity();

        ImageView gallery = (ImageView) view.findViewById(R.id.img_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化Intent,传入ACTION_PICK,表示从Item中选取一个数据返回
                Intent intent=new Intent(Intent.ACTION_PICK,null);
                //设置Data和Type属性，前者是URI：表示系统图库的URI,后者是MIME码
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_TYPE);
                //启动这个intent所指向的Activity
                startActivityForResult(intent,RESULT_IMAGE);
            }
        });



//        mTextureView = (TextureView) view.findViewById(R.id.camera_texture_view);
//        mButton = (ImageButton) view.findViewById(R.id.capture_btn);

        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_camera = (ImageView) view.findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);

        //关闭相机界面按钮
//        camera_close = (ImageView) view.findViewById(R.id.camera_close);
//        camera_close.setOnClickListener(this);


        //top 的view
        home_custom_top_relative = (LinearLayout) view.findViewById(R.id.home_custom_top_relative);
        home_custom_top_relative.setAlpha(0.5f);

        //前后摄像头切换
        camera_frontback = (ImageView) view.findViewById(R.id.camera_frontback);
        camera_frontback.setOnClickListener(this);

//        //延迟拍照时间
//        camera_delay_time = (ImageView) view.findViewById(R.id.camera_delay_time);
//        camera_delay_time.setOnClickListener(this);

        //正方形切换
        camera_square = (ImageView) view.findViewById(R.id.camera_square);
        camera_square.setOnClickListener(this);


        //切换正方形时候的动画
        homeCustom_cover_top_view = view.findViewById(R.id.homeCustom_cover_top_view);
        homeCustom_cover_bottom_view = view.findViewById(R.id.homeCustom_cover_bottom_view);

        homeCustom_cover_top_view.setAlpha(0.5f);
        homeCustom_cover_bottom_view.setAlpha(0.5f);

        //拍照时动画
        home_camera_cover_top_view = view.findViewById(R.id.home_camera_cover_top_view);
        home_camera_cover_bottom_view = view.findViewById(R.id.home_camera_cover_bottom_view);
        home_camera_cover_top_view.setAlpha(1);
        home_camera_cover_bottom_view.setAlpha(1);

        //闪光灯
        flash_light = (ImageView) view.findViewById(R.id.flash_light);
        flash_light.setOnClickListener(this);


        homecamera_bottom_relative = (RelativeLayout) view.findViewById(R.id.homecamera_bottom_relative);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        menuPopviewHeight = screenHeight - screenWidth * 4 / 3;
        animHeight = (screenHeight - screenWidth - menuPopviewHeight - SystemUtils.dp2px(context, 44)) / 2;

//        //这里相机取景框我这是为宽高比3:4 所以限制底部控件的高度是剩余部分
        RelativeLayout.LayoutParams bottomParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuPopviewHeight);
        bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        homecamera_bottom_relative.setLayoutParams(bottomParam);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                //相册
                //通过获取当前应用的contentResolver对象来查询返回的data数据
                cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                //将cursor指针移动到数据首行
                cursor.moveToFirst();
                //获取字段名为_data的数据
                imagePath = cursor.getString(cursor.getColumnIndex("_data"));


                isview = false;


//                intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth);
//                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
//                getActivity().setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
                cursor.close();

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        Intent intent = new Intent(getActivity(), IMGEditActivity.class);
                        intent.putExtra("mPicPath", imagePath);
                        intent.putExtra(AppConstant.KEY.IMG_PATH, imagePath);
                        startActivity(intent);
                    }
                }, 10);

            }
        }





//            }else if(requestCode==RESULT_CAMERA){
//                //相机
//                Intent intent=new Intent(MainActivity.this,ResultActvity.class);
//                //由于拍照的时候设置了一个保存路径，所以直接放入该路径
//                intent.putExtra("mPicPath",TEMP_IMAGE_PATH);
//                startActivity(intent);
//            }
            }





    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                if (isview) {
                        switch (light_num) {
                            case 0:
                                //关闭
                                CameraUtil.getInstance().turnLightOff(mCamera);
                                break;
                            case 1:
                                CameraUtil.getInstance().turnLightOn(mCamera);
                                break;
                            case 2:
                                //自动
                                CameraUtil.getInstance().turnLightAuto(mCamera);
                                break;
                        }
                        captrue();
                    isview = false;
                }
                break;

            case R.id.camera_square:
                if (index == 0) {
                    camera_square_0();
                } else if (index == 1) {
                    camera_square_1();
                }
                break;

            //前后置摄像头拍照
            case R.id.camera_frontback:
                switchCamera();
                break;

            //退出相机界面 释放资源
//            case R.id.camera_close:
//                getActivity().finish();
//                break;

            //闪光灯
            case R.id.flash_light:
                if(mCameraId == 1){
                    //前置
                    Toast.makeText(context, "请切换为后置摄像头开启闪光灯", Toast.LENGTH_SHORT).show();
                    return;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                switch (light_num) {
                    case 0:
                        //打开
                        light_num = 1;
                        flash_light.setImageResource(R.drawable.btn_camera_flash_on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                        mCamera.setParameters(parameters);
                        break;
                    case 1:
                        //自动
                        light_num = 2;
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.btn_camera_flash_auto);
                        break;
                    case 2:
                        //关闭
                        light_num = 0;
                        //关闭
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.btn_camera_flash_off);
                        break;
                }

                break;

//            //延迟拍照时间
//            case R.id.camera_delay_time:
//                switch (delay_time) {
//                    case 0:
//                        delay_time = 3;
//                        delay_time_temp = delay_time;
//                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_3);
//                        break;
//
//                    case 3:
//                        delay_time = 5;
//                        delay_time_temp = delay_time;
//                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_5);
//                        break;
//
//                    case 5:
//                        delay_time = 10;
//                        delay_time_temp = delay_time;
//                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_10);
//                        break;
//
//                    case 10:
//                        delay_time = 0;
//                        delay_time_temp = delay_time;
//                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_0);
//                        break;
//
//                }
       }
    }

    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        Log.d(TAG, "switchCamera: "+ mCameraId);
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    /**
     * 正方形拍摄
     */
    public void camera_square_0() {
        camera_square.setImageResource(R.drawable.btn_camera_size1_n);

        //属性动画
        ValueAnimator anim = ValueAnimator.ofInt(0, animHeight);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                Params.setMargins(0, SystemUtils.dp2px(context, 44), 0, 0);
                homeCustom_cover_top_view.setLayoutParams(Params);

                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                bottomParams.setMargins(0, screenHeight - menuPopviewHeight - currentValue, 0, 0);
                homeCustom_cover_bottom_view.setLayoutParams(bottomParams);
            }

        });
        anim.start();

        homeCustom_cover_top_view.bringToFront();
        home_custom_top_relative.bringToFront();
        homeCustom_cover_bottom_view.bringToFront();
        index++;
    }

    /**
     * 长方形方形拍摄
     */
    public void camera_square_1() {
        camera_square.setImageResource(R.drawable.btn_camera_size2_n);

        ValueAnimator anim = ValueAnimator.ofInt(animHeight, 0);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                Params.setMargins(0, SystemUtils.dp2px(context, 44), 0, 0);
                homeCustom_cover_top_view.setLayoutParams(Params);

                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                bottomParams.setMargins(0, screenHeight - menuPopviewHeight - currentValue, 0, 0);
                homeCustom_cover_bottom_view.setLayoutParams(bottomParams);
            }
        });
        anim.start();
        index = 0;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
//            CameraUtil.getInstance().setCameraDisplayOrientation(getActivity(), mCameraId, camera);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            isview = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isview = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);

                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);

                if (index == 1) {
                    //正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, animHeight + SystemUtils.dp2px(context, 44), screenWidth, screenWidth);
                } else {
                    //正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, 0, screenWidth, screenWidth * 4/3);
                }

                String img_path = getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +
                        File.separator + System.currentTimeMillis() + ".jpeg";

                BitmapUtils.saveJPGE_After(context, saveBitmap, img_path, 100);

                if(!bitmap.isRecycled()){
                    bitmap.recycle();
                }

                if(!saveBitmap.isRecycled()){
                    saveBitmap.recycle();
                }


                Intent intent = new Intent(getActivity(), IMGEditActivity.class);
                intent.putExtra("mPicPath", img_path);
                intent.putExtra(AppConstant.KEY.IMG_PATH, img_path);
                startActivity(intent);
//                Intent intent = new Intent(context, ShowPicActivity.class);
//                intent.putExtra(AppConstant.KEY.IMG_PATH, img_path);
////                intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth);
////                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
////                getActivity().setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
//                startActivity(intent);

                //这里打印宽高 就能看到 CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 200);
                // 这设置的最小宽度影响返回图片的大小 所以这里一般这是1000左右把我觉得
//                Log.d("bitmapWidth==", bitmap.getWidth() + "");
//                Log.d("bitmapHeight==", bitmap.getHeight() + "");
            }
        });
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
//        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 800);
//        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Camera.Size optionSize = CameraUtil.getInstance().getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), surfaceView.getHeight(), surfaceView.getWidth());//获取一个最为适配的camera.size
        parameters.setPreviewSize(optionSize.width,optionSize.height);

        Camera.Size pictrueSize = CameraUtil.getInstance().getOptimalPreviewSize(parameters.getSupportedPictureSizes(), surfaceView.getHeight(), surfaceView.getWidth());
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        camera.setParameters(parameters);

        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
        //params.gravity = Gravity.CENTER;
//        surfaceView.setLayoutParams(params);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

}


