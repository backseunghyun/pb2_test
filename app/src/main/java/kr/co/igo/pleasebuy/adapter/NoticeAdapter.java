package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Notice;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class NoticeAdapter extends BaseExpandableListAdapter {
    private Context context;

    private List<Notice> mList = new ArrayList<Notice>();
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private Notice m;

    public NoticeAdapter(Context context, List<Notice> list) {
        this.context = context;
        this.mList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getGroupCount() {
        return this.mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mList.get(groupPosition).getTitle();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mList.get(groupPosition).getContents();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.adapter_notice_item_title, parent, false);
            viewHolder.ll_rootlayout = (LinearLayout) convertView.findViewById(R.id.ll_rootlayout);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        m = mList.get(groupPosition);

        viewHolder.tv_title.setText(m.getTitle());
        viewHolder.tv_date.setText(CommonUtils.ConvertDate(m.getRegDate()));
        viewHolder.tv_summary.setText(m.getContents());
        viewHolder.tv_name.setText(m.getWiter());

        if (isExpanded) {
            viewHolder.tv_summary.setVisibility(View.GONE);
        } else {
            viewHolder.tv_summary.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.adapter_notice_item_contents, parent, false);
            viewHolder.tv_contents = (TextView) convertView.findViewById(R.id.tv_contents);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_contents.setText(mList.get(groupPosition).getContents());
        Glide.with(context)
                .load( m.getImgUrl())
//                .centerCrop()
                .into(viewHolder.iv_image);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewHolder {
        LinearLayout ll_rootlayout;
        TextView tv_title;
        TextView tv_date;
        TextView tv_summary;
        TextView tv_name;

        TextView tv_contents;
        ImageView iv_image;
    }
}