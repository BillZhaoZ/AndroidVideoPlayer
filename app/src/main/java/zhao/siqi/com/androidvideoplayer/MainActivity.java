package zhao.siqi.com.androidvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import butterknife.ButterKnife;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

   /* @BindView(R.id.tv_vv)
    TextView mTvVv;
    @BindView(R.id.tv_sm)
    TextView mTvSm;
    @BindView(R.id.lv_list)
    TextView mLvList;
    @BindView(R.id.tv_bilibili)
    TextView mBili;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        NoScroolGridView gridView = (NoScroolGridView) findViewById(R.id.grid_view);
        MainGridAdapter gridViewAdapter = new MainGridAdapter(this);

        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;

        switch (position) {

            case 0://videoview
                intent = new Intent(this, VideoViewActivity.class);
                startActivity(intent);
                break;

            case 1://surfaceview
                intent = new Intent(this, SMActivity.class);
                startActivity(intent);
                break;

            case 2:// vitamio
                intent = new Intent(this, VitamioActivity.class);
                startActivity(intent);
                break;

            case 3:// bibili
                intent = new Intent(this, BiliBliActivity.class);
                startActivity(intent);
                break;
        }

    }

    /*// 跳转到videoview
    @OnClick(R.id.tv_vv)
    public void jumpToVideoView() {

        Intent intent = new Intent(this, VideoViewActivity.class);
        startActivity(intent);
    }

    // 跳转到surfaceView
    @OnClick(R.id.tv_sm)
    public void jumpToSM() {

        Intent intent = new Intent(this, SMActivity.class);
        startActivity(intent);
    }

    // vitamio
    @OnClick(R.id.lv_list)
    public void jumpToVitamio() {

        Intent intent = new Intent(this, VitamioActivity.class);
        startActivity(intent);
    }

    // Bilibili
    @OnClick(R.id.tv_bilibili)
    public void jumpToBili() {

        Intent intent = new Intent(this, BiliBliActivity.class);
        startActivity(intent);
    }*/
}
