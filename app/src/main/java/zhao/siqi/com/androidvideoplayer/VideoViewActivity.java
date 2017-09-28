package zhao.siqi.com.androidvideoplayer;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * videoview 实现播放效果
 */
public class VideoViewActivity extends AppCompatActivity {

    @BindView(R.id.vv)
    VideoView vv;

    private MediaController mc;
    private static int secondCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view);

        ButterKnife.bind(this);

        vv = (VideoView) findViewById(R.id.vv);
        final File file = new File("/storage/emulated/0/Movies/Starry_Night.mp4");

        // 创建一个MediaController。
        // 这里的Context一定要传入this，不能用getApplicationContext().否则一点击屏幕就闪退
        mc = new MediaController(this);

        // 开创建SurfaceView的控制器
        SurfaceHolder holder = vv.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if (file.exists()) {
                    // 设置要播放的视频
                    vv.setVideoPath(file.getPath());

                    // 设置VideoView与MediaController相关联
                    vv.setMediaController(mc);

                    // 设置VideoView获取焦点
                    vv.requestFocus();

                    // 将背景图片设为透明
                    // vv.getBackground().setAlpha(0);

                    // 开始播放视频（如果上次有播放记录，接着上次播放）
                    vv.start();
                    vv.seekTo(secondCurrentPosition);

                } else {
                    Toast.makeText(getApplicationContext(), "视频不存在", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }


            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        // 设置播放完毕后循环播放
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                mp.start();
            }
        });

        // 增加监听上一个和下一个的切换事件，默认这两个按钮是不显示的
        mc.setPrevNextListeners(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 这里直接弹出吐司，开发中这里应当切换到下一首
                Toast.makeText(VideoViewActivity.this, "下一个", Toast.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(VideoViewActivity.this, "上一个", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //当Activity退出界面(无论Activity是否销毁了)，记录当前视频播放进度
    @Override
    protected void onPause() {
        if (vv.isPlaying()) {
            secondCurrentPosition = vv.getCurrentPosition();
            vv.pause();
            vv.suspend();
        }

        super.onPause();
    }

}
