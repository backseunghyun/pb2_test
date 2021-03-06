package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class FavoriteAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Favorite> mList = new ArrayList<Favorite>();
    private Favorite m;
    private ViewHolder holder;


    public FavoriteAdapter(Activity c, List<Favorite> list) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_favorite_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);

        if (m.getUpdateDate() == 0) {
            holder.tv_date.setText(CommonUtils.ConvertDate(m.getRegDate()));
        } else {
            holder.tv_date.setText(CommonUtils.ConvertDate(m.getUpdateDate()));
        }
        holder.tv_title.setText(m.getName());
        holder.tv_products.setText(m.getProductNames());
        holder.tv_count.setText("총 " + m.getCntOfProduct() +"개 품목");

        holder.vPosition = position;

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.ll_rootlayout)   LinearLayout ll_rootlayout;
        @Bind(R.id.tv_date)         TextView tv_date;
        @Bind(R.id.tv_title)        TextView tv_title;
        @Bind(R.id.tv_products)     TextView tv_products;
        @Bind(R.id.tv_count)        TextView tv_count;

        private int vPosition;
        private Activity vActivity;
        private List<Favorite> vList = new ArrayList<Favorite>();

        public ViewHolder(View view, Activity c, List<Favorite> list) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }


    }
}