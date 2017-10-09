package zhao.siqi.com.androidvideoplayer.view;

/**
 * Created by Bill on 2017/10/9.
 */

public class VideoAddress {

    private static VideoAddress instance = null;

    public static VideoAddress getInstance() {

        if (instance == null) {
            synchronized (VideoAddress.class) {
                instance = new VideoAddress();
            }

        }
        return instance;
    }

    public String url1 = "http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv";
    public String url2 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    public String url3 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    public String url4 = "http://42.96.249.166/live/388.m3u8";
    public String url5 = "http://live.3gv.ifeng.com/zixun.m3u8";


    public String localPath = "/storage/emulated/0/Movies/Starry_Night.mp4"; // 手机本地视频路径
}
