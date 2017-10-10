package zhao.siqi.com.androidvideoplayer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bill on 2017/10/10.
 */

public class Utils {

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

}
