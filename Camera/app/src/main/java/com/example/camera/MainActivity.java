package com.example.camera;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static float fangda = 1.5f;
    private Camera.AutoFocusCallback myAutoFocusCallback = null;
    private SurfaceView myView;
    private Button btn;
    private ImageView imageView;
    private Camera camera;
    private RelativeLayout layout ;

    private Timer mTimer = new Timer (true);
    private TimerTask mTimerTask = new TimerTask () {
        @Override
        public void run() {
            camera.autoFocus (myAutoFocusCallback);//尝试对焦

            Log.v ("尝试对焦", "time:" + System.currentTimeMillis () / 1000);
        }
    };

    Handler handler = new Handler () {
        @Override
        public void handleMessage(Message msg) {
            postmsg ((String) msg.obj);
        }
    };
    int count = 0;
    private View mLayout;
    private ParallelViewHelper parallelViewHelper;
    private View imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        requestWindowFeature (Window.FEATURE_NO_TITLE);//去掉标题
        setContentView (R.layout.activity_main);

        //窗口布满全局
        getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initView ();

        initListener ();

       parallelViewHelper = new ParallelViewHelper(MainActivity.this,imageView2);
//        post("123");
        myAutoFocusCallback = new Camera.AutoFocusCallback () {
            public void onAutoFocus(boolean success, Camera
                    camera) {
// TODO Auto-generated method stub
                if (success)//success表示对焦成功
                {
                    Log.i ("tag", "对焦成功");
//myCamera.setOneShotPreviewCallback(null);
                } else {
//未对焦成功
                    Log.i ("tag", "对焦失败");
                }
            }
        };
    }


    //初始化View
    private void initView() {
        myView = findViewById (R.id.myView);
        btn = findViewById (R.id.btn);
        imageView = findViewById (R.id.imageView);
        layout = findViewById(R.id.layout);
        imageView2 = findViewById(R.id.imageView2);
    }





    //初始化监听
    private void initListener() {
        SurfaceHolder holder = myView.getHolder ();
        holder.setFixedSize (176, 155);
        holder.setKeepScreenOn (true);
        holder.setType (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback (new TakePictureSurfaceCallback ());


        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    //关闭计时器
                    mTimer.cancel ();
                    mTimer = null;
                    mTimerTask.cancel ();
                    mTimerTask = null;

                    //拍照
                    camera.takePicture (null, null, new TakePictureCallback ());


                }
            }
        });
    }





    //设置回掉函数
    private final class TakePictureSurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                camera = Camera.open ();
                if (camera == null) {
                    int cameraCount = Camera.getNumberOfCameras ();
                    camera = Camera.open (cameraCount - 1);
                }

                Camera.Parameters parames = camera.getParameters ();
                parames.setJpegQuality (80);
                WindowManager wm = (WindowManager) MainActivity.this
                        .getSystemService (MainActivity.this.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay ().getWidth ();
                int height = wm.getDefaultDisplay ().getHeight ();
                parames.setPictureSize (width, height);
                parames.setPreviewFrameRate (5);

//                camera.setDisplayOrientation(90);


                //设置预览
//                camera.setPreviewDisplay(myView.getHolder());
//                continuous-picture


                camera.setPreviewDisplay (surfaceHolder);
                camera.setPreviewCallback (new Camera.PreviewCallback () {
                    @Override
                    public void onPreviewFrame(byte[] bytes, Camera camera) {

                    }
                });

                //开启预览
                camera.startPreview ();
                mTimer.schedule (mTimerTask, 5000, 3000);//每5秒执行一次定时器中的方法，log.v打印输出。


            } catch (Exception e) {
                e.printStackTrace ();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (camera != null) {
                camera.release ();
                camera = null;
            }
        }
    }

    private final class TakePictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            ByteArrayOutputStream os = new ByteArrayOutputStream (bytes.length);

            Log.e ("123", "111111111111111");
//            if (tempData != null && tempData.length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray (bytes, 0, bytes.length);



            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
//            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            Matrix matrix = new Matrix();
            matrix.postScale(fangda,fangda);
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);


            imageView.setVisibility (View.VISIBLE);
            myView.setVisibility (View.GONE);
            imageView.setImageBitmap (bitmap1);

            parallelViewHelper.start();


            final String base64Str = encodeImage (bitmap);
            Log.d ("alert", "获取到照片了，开始http");
            Log.d ("base", "base64是：" + base64Str);
            //该写http了

            Message message = new Message ();
            message.obj = base64Str;
            handler.sendMessage (message);
//            }
        }
    }


    //设定TextView的位置
    @NonNull
    private RelativeLayout.LayoutParams getLayoutParams(List<PositionBean.WordsResultBean> list,
                                                        int i) {
        PositionBean.WordsResultBean.LocationBean location = list.get(i).getLocation();
        int height = location.getHeight();
        int left = location.getLeft();
        int width = location.getWidth();
        int top = location.getTop();
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.height = (int) (height*fangda);
        params.leftMargin = (int) (left*fangda);
        params.width = (int) (width*fangda);
        params.topMargin = (int) (top*fangda);
        return params;
    }

    public String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        //读取图片到ByteArrayOutputStream
        bitmap.compress (Bitmap.CompressFormat.PNG, 40, baos); //参数如果为100那么就不压缩
        byte[] bytes = baos.toByteArray ();
        String strbm = Base64.encodeToString (bytes, Base64.DEFAULT);
        return strbm;
    }

    public void postmsg(final String base64Str) {
        new Thread (new Runnable () {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient ();

                FormEncodingBuilder builder = new FormEncodingBuilder ();
                builder.add ("image", base64Str);
                builder.add ("lang", "CHN_ENG");

                //我的台湾服务器
//                Request request = new Request.Builder()
//                        .post(builder.build())
//                        .url("http://104.199.209.182:8080")
//                        .build();
                //阿里云租赁的服务器
                Request request = new Request.Builder ()
                        .post (builder.build ())
                        .url ("http://47.94.153.125:80")
                        .build ();


                Call call = client.newCall (request);
                call.enqueue (new Callback () {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.e ("ta", "失败了", e);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String result = response.body ().string ();
                        Log.d ("Tag", result);

                        PositionBean positionBean = FastJsonUtil.changeJsonToBean(result,
                                PositionBean.class);
                        final List<PositionBean.WordsResultBean> list = positionBean
                                .getWords_result();

                        for (int i = 0; i < list.size(); i++) {
                            final RelativeLayout.LayoutParams params = getLayoutParams
                                    (list, i);
                            final TextView textView = new TextView(MainActivity.this);
                            textView.setTextColor(Color.YELLOW);
//                            if (BaiDuOcrLanguageBean.TYPE.equals(OcrLanguageBean.TYPE)) {
                                textView.setText(list.get(i).getWords());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layout.addView(textView, params);

//
                                    }
                                });

//                            } else {
//                                final int finalI = i;
//                                LanguageRequestUtils.translation(CameraLanguageActicity
//                                                .this, BaiDuOcrLanguageBean.TYPE, OcrLanguageBean
//                                                .TYPE,
//                                        list.get(finalI).getWords(), new
//                                                TranslateCallBack() {
//                                                    @Override
//                                                    public void onResult(int status, String
//                                                            yuanWen, String
//                                                                                 yiWen) {
//                                                        RelativeLayout.LayoutParams params =
//                                                                getLayoutParams(list, finalI);
//                                                        TextView textView = new TextView
//                                                                (CameraLanguageActicity.this);
//                                                        textView.setTextColor(Color.YELLOW);
//                                                        textView.setText(yiWen);
//                                                        layout.addView(textView, params);
//                                                    }
//                                                });
//                            }
                        }

                    }
                });
            }
        }).start ();
    }



    @Override
    protected void onPause() {
        super.onPause();
        parallelViewHelper.stop();
    }
}
