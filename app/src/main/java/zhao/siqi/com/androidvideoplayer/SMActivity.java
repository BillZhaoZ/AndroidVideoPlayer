package zhao.siqi.com.androidvideoplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * SurfaceView + MediaPlayer
 */
public class SMActivity extends BaseActivity {

    private SurfaceView sv;
    private MediaPlayer player;
    private static int currentPositon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm);

        sv = (SurfaceView) findViewById(R.id.sv);

        // 拿到SurfaceView的控制器
        final SurfaceHolder sh = sv.getHolder();


        /**
         * 设置SurfaceView的回调。 SurfaceView是个重量级组件，只要不可见就不会创建该组件。
         * 所以要设置一个侦听，等待SurfaceView组件可见在播放视频，然而SurfaceView是没有侦听的，只要回调。
         * 一定要注意，别导错了包（import android.view.SurfaceHolder.Callback;）
         */
        sh.addCallback(new SurfaceHolder.Callback() {

            // SurfaceView销毁时调用
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 每次SurfaceView销毁时，同时停止播放视频
                if (player != null) {

                    // currentPositon如果是静态变量，则该变量与进程同存亡。所以不论是home还是返回键，只要进程不死，该变量就存在
                    // currentPositon如果不是静态变量，仅仅是个全局变量，则该变量与Activity共存亡。即home建该变量存在；若返回键该变量死亡，视频从头播放。
                    currentPositon = player.getCurrentPosition();

                    player.stop();
                    player.release();
                    player = null;
                }
            }

            // SurfaceView创建时调用
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // SurfaceView一旦不可见就会销毁。所以每次进入播放界面，就会创建一个SurfaceView.每次推出播放界面，就会销毁SurfaceView,但是MediaPlayer不会销毁，需要手动代码销毁
                // 所以，要判断一下MediaPlayer的对象是不是空，是空就创建一个播放视频，不是空就什么也不做，继续播放前面的视频
                if (player == null) {
                    // 创建MediaPlayer对象
                    player = new MediaPlayer();

                    // 重置
                    player.reset();

                    try {
                        // 设置需要加载的资源.加载网络资源需要异步准备，加载本地资源可以同步准备
                        // player.setDataSource("http://222.20.39.47:8080/AllResourceWeb/s2.3gp");
                        player.setDataSource("/storage/emulated/0/Movies/Starry_Night.mp4");

                        // 指定视频播放的组件。如果没有设置，视频是无法播放的
                        player.setDisplay(sh);

                        // 异步准备.如果是本地视频，可以直接同步准备后就播放，不需要设置监听
                        player.prepareAsync();

                        // 设置侦听，用来判断当前子线程是否加载资源完毕
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                player.start();

                                // 寻找到上次播放的位置，继续播放
                                player.seekTo(currentPositon);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // SurfaceView结构改变时调用
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }
}
