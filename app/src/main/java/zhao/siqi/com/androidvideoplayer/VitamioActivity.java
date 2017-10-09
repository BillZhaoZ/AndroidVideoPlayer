package zhao.siqi.com.androidvideoplayer;

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

    private String url1 = "http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv";
    private String url2 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    private String url3 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private String url4 = "http://42.96.249.166/live/388.m3u8";
    private String url5 = "http://live.3gv.ifeng.com/zixun.m3u8";

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
            videoView.setVideoURI(Uri.parse(url2));
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
