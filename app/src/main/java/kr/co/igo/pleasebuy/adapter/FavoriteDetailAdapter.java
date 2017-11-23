package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class FavoriteDetailAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Product> mList = new ArrayList<Product>();
    private Product m;
    private ViewHolder holder;


    public FavoriteDetailAdapter(Activity c, List<Product> list) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_favorite_detail_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);

        holder.tv_name.setText(m.getProductName());
        holder.tv_price.setText(CommonUtils.getNumberThreeEachFormatWithWon(m.getPrice()));

        String etc = m.getOrigin() + "/" + m.getUnit();
        if (m.getOrigin().equals("") || m.getOrigin().equals("-")) {
            etc = m.getUnit();
        }
        holder.tv_etc.setText(etc);

        holder.iv_check.setSelected(m.isSelected());
        if(m.isSelected()) {
            holder.ll_rootlayout.setBackgroundColor(activity.getResources().getColor(R.color.c_fddbd8));
        } else {
            holder.ll_rootlayout.setBackgroundColor(activity.getResources().getColor(R.color.c_ffffff));
        }

        holder.vPosition = position;


        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.ll_rootlayout)   LinearLayout ll_rootlayout;
        @Bind(R.id.iv_check)        ImageView iv_check;
        @Bind(R.id.tv_name)         TextView tv_name;
        @Bind(R.id.tv_etc)          TextView tv_etc;
        @Bind(R.id.tv_price)        TextView tv_price;

        private int vPosition;
        private Activity vActivity;
        private List<Product> vList = new ArrayList<Product>();

        public ViewHolder(View view, Activity c, List<Product> list) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }
        @OnClick({R.id.ll_rootlayout, R.id.iv_check})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_rootlayout:
                case R.id.iv_check:
                    Product m = vList.get(vPosition);
                    Log.d("click", m.getProductName() + " - " + m.getProductId());
                    m.setSelected(!m.isSelected());
                    notifyDataSetChanged();
                    break;
            }
        }

    }
}