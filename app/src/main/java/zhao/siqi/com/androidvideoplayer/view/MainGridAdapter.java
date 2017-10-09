package zhao.siqi.com.androidvideoplayer.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import zhao.siqi.com.androidvideoplayer.R;

/**
 * @author Bill
 */
public class MainGridAdapter extends BaseAdapter {

    private Context mContext;// 上下文对象

    /**
     * 菜图标数组0
     **/
    private int[] GroupIconId0 = {
            R.mipmap.followup_table,
            R.mipmap.followup_plan,
            R.mipmap.followup_depart_affair,
            R.mipmap.followup_patient_education};

    /**
     * 菜单名称数组0
     **/
    private String[] GroupNameId0 = {"VideoView播放器", "SurfaceView播放器", "Vitamio播放器", "Bilibili播放器"};

    /**
     * 构造方法
     *
     * @param context // 上下文对象
     */
    public MainGridAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // 初始化布局控件
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_followuptool_gridview, null);
            holder.groupName = (TextView) convertView.findViewById(R.id.group_textView);
            holder.groupIcon = (ImageView) convertView.findViewById(R.id.group_imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        chooseData(holder, GroupNameId0[position], GroupIconId0[position]);

        return convertView;
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param text
     * @param resid
     */
    private void chooseData(ViewHolder holder, String text, int resid) {
        holder.groupName.setText(text);
        holder.groupIcon.setBackgroundResource(resid);
    }

    /**
     * 获取item总数
     */
    @Override
    public int getCount() {

        return GroupIconId0.length;
    }

    /**
     * 获取某一个Item的内容
     */
    @Override
    public Object getItem(int position) {
        return GroupNameId0[position];
    }

    /**
     * 获取当前item的ID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        /**
         * 父Item名称
         **/
        TextView groupName;
        /**
         * 父Item图标
         **/
        ImageView groupIcon;
    }

}
