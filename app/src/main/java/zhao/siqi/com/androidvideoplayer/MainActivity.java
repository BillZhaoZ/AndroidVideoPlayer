package zhao.siqi.com.androidvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_vv)
    TextView mTvVv;
    @BindView(R.id.tv_sm)
    TextView mTvSm;
    @BindView(R.id.lv_list)
    TextView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    // 跳转到videoview
    @OnClick(R.id.tv_vv)
    public void jumpToVideoView() {

        Intent intent = new Intent(this, VideoViewActivity.class);
        startActivity(intent);
    }


}