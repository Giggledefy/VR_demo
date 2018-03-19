package com.example.camera;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Camera.AutoFocusCallback myAutoFocusCallback = null;
    private SurfaceView myView;
    private Button btn;
    private ImageView imageView;
    private Camera camera;

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
            imageView.setVisibility (View.VISIBLE);
            myView.setVisibility (View.GONE);
            imageView.setImageBitmap (bitmap);

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
                        .url ("http://47.93.61.83:80")
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
                    }
                });
            }
        }).start ();
    }


}
