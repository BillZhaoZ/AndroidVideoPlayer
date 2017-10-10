package zhao.siqi.com.androidvideoplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import zhao.siqi.com.androidvideoplayer.R;
import zhao.siqi.com.androidvideoplayer.Utils;
import zhao.siqi.com.androidvideoplayer.view.VideoAddress;

import static zhao.siqi.com.androidvideoplayer.Utils.formatTime;

/**
 * SurfaceView + MediaPlayer
 * <p>
 * 后期需要添加自定义控制栏
 */
public class SMActivity extends AppCompatActivity {

    private SurfaceView surfaceView = null;
    private SurfaceHolder surfaceHolder = null;
    private MediaPlayer mediaPlayer = null;
    private ImageView imageView_main_show = null;

    private PopupWindow popupWindow;

    private ImageView imageView_play;
    private SeekBar seekBar;
    private TextView textView_playTime;
    private TextView textView_duration;
    private String filePath = null;

    // 自动隐藏自定义播放器控制条的时间
    private static final int HIDDEN_TIME = 5000;
    private float densityRatio = 1.0f; // 密度比值系数（密度比值：一英寸中像素点除以160）
    private MyVideoBroadcastReceiver receiver = null;

    // 设置定时器
    private Timer timer = null;
    private final static int WHAT = 0;

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // 又回到了主线程
            showOrHiddenController();
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case WHAT:
                    if (mediaPlayer != null) {
                        int currentPlayer = mediaPlayer.getCurrentPosition();
                        if (currentPlayer > 0) {
                            mediaPlayer.getCurrentPosition();
                            textView_playTime.setText(Utils.formatTime(currentPlayer));

                            // 让seekBar也跟随改变
                            int progress = (int) ((currentPlayer / (float) mediaPlayer.getDuration()) * 100);
                            seekBar.setProgress(progress);
                        } else {
                            textView_playTime.setText("00:00");
                            seekBar.setProgress(0);
                        }
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sm);
        ButterKnife.bind(this);

        initView();

        initMediaPlayer();

        initController();

        // 动态注册广播接受者
        receiver = new MyVideoBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("SurfaceViewMediaPlayer"));
    }

    /**
     * 初始化控制器
     * popwindow
     */
    private void initController() {
        View controllerView = getLayoutInflater().inflate(R.layout.layout_pop, null);

        // 初始化popopWindow
        popupWindow = new PopupWindow(controllerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        imageView_play = (ImageView) controllerView.findViewById(R.id.imageView_play);
        seekBar = (SeekBar) controllerView.findViewById(R.id.seekbar);
        textView_playTime = (TextView) controllerView.findViewById(R.id.textView_playtime);
        textView_duration = (TextView) controllerView.findViewById(R.id.textView_totaltime);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 表示手指拖动seekbar完毕，手指离开屏幕会触发以下方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 让计时器延时执行
                handler.postDelayed(r, HIDDEN_TIME);


            }

            // 在手指正在拖动seekBar，而手指未离开屏幕触发的方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 让计时器取消计时
                handler.removeCallbacks(r);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    int playtime = progress * mediaPlayer.getDuration() / 100;
                    mediaPlayer.seekTo(playtime);
                }

            }
        });


        // 点击播放的时候,判断是播放还是暂停
        imageView_play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (imageView_main_show.getVisibility() == View.VISIBLE) {
                    imageView_main_show.setVisibility(View.GONE);
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imageView_play.setImageResource(R.drawable.icon_direct_p);
                } else {
                    mediaPlayer.start();
                    imageView_play.setImageResource(R.drawable.icon_direct_n);
                }

            }
        });
    }

    /**
     * 显示和隐藏控制器
     */
    private void showOrHiddenController() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            // 将dp转换为px
            int controllerHeightPixel = (int) (densityRatio * 50);
            popupWindow.showAsDropDown(surfaceView, 0, -controllerHeightPixel);
            // 延时执行
            handler.postDelayed(r, HIDDEN_TIME);
        }
    }

    /**
     * 初始化mediaPlayer
     */
    private void initMediaPlayer() {
        // 视频文件路径
        filePath = VideoAddress.getInstance().localPath;

        if (mediaPlayer == null) {
            // 1,创建MediaPlay对象
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                // mediaPlayer.start();
                mediaPlayer.setLooping(false);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 表示准备完成，设置总的时长，使用时间格式化工具
                // String duration = mediaPlayer.getDuration() ;
                textView_duration.setText(formatTime(mediaPlayer.getDuration()));

                // 初始化定时器
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(WHAT);
                    }
                }, 0, 1000);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 发送广播，播放下一首歌曲
                Intent intent = new Intent();
                intent.setAction("SurfaceViewMediaPlayer");
                sendBroadcast(intent);
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        densityRatio = getResources().getDisplayMetrics().density; // 表示获取真正的密度

        imageView_main_show = (ImageView) findViewById(R.id.imageView_main_play);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_main);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mediaPlayer != null) {
                    mediaPlayer.setDisplay(surfaceHolder);
                    // mediaPlayer.start() ;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });

        // 设置屏幕的触摸监听
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // 表示在点击的瞬间就显示控制条
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showOrHiddenController();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 设置控件的监听事件
     *
     * @param v
     */
    public void clickButton(View v) {

        switch (v.getId()) {
            case R.id.imageView_main_play:
                imageView_main_show.setVisibility(View.GONE);
                mediaPlayer.start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 解绑广播
        unregisterReceiver(receiver);
        timer.cancel();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 广播接收器
     */
    class MyVideoBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SurfaceViewMediaPlayer")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_launcher)
                        .setTitle("提示")
                        .setMessage("视屏播放完毕，是否播放")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mediaPlayer.reset();

                                try {
                                    mediaPlayer.setDataSource(filePath);
                                    mediaPlayer.prepare();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mediaPlayer.setLooping(false);
                                mediaPlayer.start();
                            }
                        }).show();
            }
        }
    }

}
