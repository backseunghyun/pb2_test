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
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.model.Notice;
import kr.co.igo.pleasebuy.model.RequestAddProduct;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class RequestAddProductAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<RequestAddProduct> mList = new ArrayList<RequestAddProduct>();
    private RequestAddProduct m;
    private ViewHolder holder;


    public RequestAddProductAdapter(Activity c, List<RequestAddProduct> list) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_request_add_product_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);

        holder.tv_date.setText(CommonUtils.ConvertDate(m.getRegDate()));
        holder.tv_title.setText(m.getName());
        holder.tv_etc.setText(m.getEtc());

        holder.vPosition = position;


        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.ll_rootlayout)   LinearLayout ll_rootlayout;
        @Bind(R.id.tv_date)         TextView tv_date;
        @Bind(R.id.tv_title)        TextView tv_title;
        @Bind(R.id.tv_etc)          TextView tv_etc;

        private int vPosition;
        private Activity vActivity;
        private List<RequestAddProduct> vList = new ArrayList<RequestAddProduct>();

        public ViewHolder(View view, Activity c, List<RequestAddProduct> list) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }


    }
}