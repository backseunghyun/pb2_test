package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Notice;

/**
 * Created by Back on 2017-02-27.
 */
public class NoticeAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Notice> mList = new ArrayList<Notice>();
    private Notice m;
    private ViewHolder holder;


    public NoticeAdapter(Activity c, List<Notice> list) {
        this.activity = c;
        this.layoutInflater = LayoutInflater.from(c);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_notice_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);

        holder.vPosition = position;




        return convertView;
    }

    class ViewHolder  {
        @Bind(R.id.ll_rootlayout)   LinearLayout ll_rootlayout;
        @Bind(R.id.tv_date)         TextView tv_date;
        @Bind(R.id.tv_title)        TextView tv_title;
        @Bind(R.id.tv_content)      TextView tv_content;
        @Bind(R.id.tv_name)         TextView tv_name;

        private int vPosition;
        private Activity vActivity;
        private List<Notice> vList = new ArrayList<Notice>();

        public ViewHolder(View view, Activity c, List<Notice> list) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }


    }
}