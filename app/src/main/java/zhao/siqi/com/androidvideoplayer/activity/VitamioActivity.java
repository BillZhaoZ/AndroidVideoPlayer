package zhao.siqi.com.androidvideoplayer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import zhao.siqi.com.androidvideoplayer.BaseActivity;
import zhao.siqi.com.androidvideoplayer.R;
import zhao.siqi.com.androidvideoplayer.view.VideoAddress;

/**
 * Vitamio集成
 */
public class VitamioActivity extends BaseActivity {

    @BindView(R.id.vitamio)
    VideoView videoView;
    @BindView(R.id.buffer_percent)
    TextView percentTv;
    @BindView(R.id.net_speed)
    TextView netSpeedTv;

    private int mVideoLayout = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查vitamio框架是否可用   还需要验证
       /* if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }*/

        setContentView(R.layout.activity_vitamio);
        ButterKnife.bind(this);

        //显示缓冲百分比的TextView
        percentTv = (TextView) findViewById(R.id.buffer_percent);

        //显示下载网速的TextView
        netSpeedTv = (TextView) findViewById(R.id.net_speed);

        //初始化加载库文件
        if (Vitamio.isInitialized(this)) {
            videoView = (VideoView) findViewById(R.id.vitamio);
            videoView.setVideoURI(Uri.parse(VideoAddress.getInstance().url1));
            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);

            MediaController controller = new MediaController(this);
            videoView.setMediaController(controller);
            videoView.setBufferSize(10240); //设置视频缓冲大小。默认1024KB，单位byte
            videoView.requestFocus();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                    //mediaPlayer.setLooping(true);
                }
            });

            videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    percentTv.setText("已缓冲：" + percent + "%");
                }

            });

            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    switch (what) {
                        //开始缓冲
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            percentTv.setVisibility(View.VISIBLE);
                            netSpeedTv.setVisibility(View.VISIBLE);
                            mp.pause();
                            break;

                        //缓冲结束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            percentTv.setVisibility(View.GONE);
                            netSpeedTv.setVisibility(View.GONE);
                            mp.start(); //缓冲结束再播放
                            break;

                        //正在缓冲
                        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                            netSpeedTv.setText("当前网速:" + extra + "kb/s");
                            break;
                    }

                    return true;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
