package zhao.siqi.com.androidvideoplayer.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import zhao.siqi.com.androidvideoplayer.R;
import zhao.siqi.com.androidvideoplayer.view.VideoAddress;

/**
 * 集成bili
 * ijkPlayer
 */
public class BiliBliActivity extends AppCompatActivity {

    private PlayerView player;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bili_bli);
        ButterKnife.bind(this);

        mContext = this;

        /*
         * 获取播放资源
         */
        List<VideoijkBean> videoList = initRes();

        /*
         * 播放器
         */
        intiPlayer(videoList);
    }

    /**
     * 初始化播放器
     *
     * @param videoList
     */
    private void intiPlayer(List<VideoijkBean> videoList) {
        player = new PlayerView(this)
                .setTitle("电影名称：我不知道啊")
                .setScaleType(PlayStateParams.fitparent)
                .setShowSpeed(true)
                .hideMenu(false)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        // 缩略图显示
                        Glide.with(mContext)
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.color.collect_color)
                                .into(ivThumbnail);
                    }
                })

                //.setPlaySource(VideoAddress.getInstance().urlBili)  // 单个视频
                .setPlaySource(videoList) // 多个分辨率  同一个视频资源
                .startPlay();
    }

    /**
     * 初始化视频资源
     *
     * @return
     */
    @NonNull
    private List<VideoijkBean> initRes() {
        List<VideoijkBean> videoList = new ArrayList<>();

        String url1 = VideoAddress.getInstance().urlBili1;
        String url2 = VideoAddress.getInstance().urlBili2;

        VideoijkBean m1 = new VideoijkBean();
        m1.setStream("标清");
        m1.setUrl(url1);

        VideoijkBean m2 = new VideoijkBean();
        m2.setStream("高清");
        m2.setUrl(url2);

        videoList.add(m1);
        videoList.add(m2);
        return videoList;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
