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
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.OrderHistoryDate;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.ui.MainActivity;

/**
 * Created by Back on 2017-02-27.
 */
public class OrderHistoryDateAdapter extends RecyclerView.Adapter<OrderHistoryDateAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<OrderHistoryDate> mList = new ArrayList<OrderHistoryDate>();
    private OrderHistoryDate m;


    public OrderHistoryDateAdapter(Activity c, List<OrderHistoryDate> list) {
        this.activity = c;
        this.layoutInflater = LayoutInflater.from(c);
        this.mList = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_order_history_date_item, parent, false);

        ViewHolder holder = new ViewHolder(view, activity, mList);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        m = mList.get(position);

        holder.tv_date.setText(m.getDate());
        holder.tv_satus.setText(m.getStatus());

        holder.ll_bg.setSelected(m.isSelected());
        holder.ll_bg.setActivated(m.isActivated());
        holder.vPosition = position;
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

        private int vPosition;
        private Activity vActivity;
        private List<OrderHistoryDate> vList = new ArrayList<OrderHistoryDate>();

        public ViewHolder(View view, Activity c, List<OrderHistoryDate> list) {
            super(view);
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }

        @OnClick({R.id.ll_bg})
        public void onClick(View v) {
            OrderHistoryDate m = vList.get(vPosition);
            switch (v.getId()) {
                case R.id.ll_bg:
                    clear();
                    m.setSelected(true);
                    if(vActivity instanceof MainActivity) {
                        ((MainActivity)vActivity).orderHistoryChangeData(m.getOrderInfoId());
                    }
                    notifyDataSetChanged();
                    break;
            }
        }

        public void clear(){
            for (int i=0; i<vList.size(); i++) {
                vList.get(i).setSelected(false);
            }
        }

    }
}