package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
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
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;

/**
 * Created by Back on 2017-02-27.
 */
public class OrderHistoryDateAdapter extends RecyclerView.Adapter<OrderHistoryDateAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Product> mList = new ArrayList<Product>();
    private Product m;
    private ViewHolder holder;


    public OrderHistoryDateAdapter(Activity c, List<Product> list) {
        this.activity = c;
        this.layoutInflater = LayoutInflater.from(c);
        this.mList = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_order_history_date_item, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        m = mList.get(position);

        holder.tv_date.setText("01");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_bg)           LinearLayout ll_bg;
        @Bind(R.id.tv_date)         TextView tv_date;
        @Bind(R.id.tv_satus)        TextView tv_satus;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}