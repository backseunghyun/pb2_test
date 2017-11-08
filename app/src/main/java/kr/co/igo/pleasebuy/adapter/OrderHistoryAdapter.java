package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class OrderHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Product> mList = new ArrayList<Product>();
    private Product m;
    private ViewHolder holder;


    public OrderHistoryAdapter(Activity c, List<Product> list) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_order_history_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);

        holder.tv_name.setText(m.getProductName());
        holder.tv_price.setText(CommonUtils.getNumberThreeEachFormatWithWon(Integer.parseInt(m.getPrice()) * m.getSelectedCount()));
        holder.tv_etc.setText(m.getOrigin() + "/" + m.getUnit());

        Glide.with(activity)
                .load( m.getImgUrl())
                .centerCrop()
//                .fallback(activity.getResources().getDrawable(R.drawable.img_park_default))
//                .error(activity.getResources().getDrawable(R.drawable.img_park_default))
                .into(holder.iv_image);

        holder.tv_count.setText(m.getSelectedCount() + "개");

        holder.tv_count.setSelected(m.getSelectedCount() != Integer.parseInt(m.getQuantity()));
        holder.tv_price.setSelected(m.getSelectedCount() != Integer.parseInt(m.getQuantity()));

        holder.vPosition = position;


        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.ll_rootlayout)   LinearLayout ll_rootlayout;
        @Bind(R.id.iv_image)        ImageView iv_image;
        @Bind(R.id.tv_name)         TextView tv_name;
        @Bind(R.id.tv_etc)          TextView tv_etc;
        @Bind(R.id.tv_count)        TextView tv_count;
        @Bind(R.id.tv_price)        TextView tv_price;

        private int vPosition;
        private Activity vActivity;
        private List<Product> vList = new ArrayList<Product>();

        public ViewHolder(View view, Activity c, List<Product> list) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }


    }
}