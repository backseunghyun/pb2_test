package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class OrderStep2Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Product> mList = new ArrayList<Product>();
    private Product m;
    private ViewHolder holder;
    private TextView mView;
    private TextView mView2;


    public OrderStep2Adapter(Activity c, List<Product> list, TextView view, TextView view2) {
        this.activity = c;
        this.layoutInflater = LayoutInflater.from(c);
        this.mList = list;
        this.mView = view;
        this.mView2 = view2;
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
            convertView = layoutInflater.inflate(R.layout.adapter_order_step_2_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList, mView, mView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);

        holder.tv_name.setText(m.getProductName());
        holder.tv_price.setText(CommonUtils.getNumberThreeEachFormatWithWon(Integer.parseInt(m.getPrice()) * m.getSelectedCount()));

        String etc = m.getOrigin() + "/" + m.getUnit();
        if (m.getOrigin().equals("") || m.getOrigin().equals("-")) {
            etc = m.getUnit();
        }
        holder.tv_etc.setText(etc);

        holder.tv_count.setText(m.getSelectedCount() + "");

        holder.tv_count.setSelected(m.getSelectedCount() != Integer.parseInt(m.getQuantity()));
        holder.tv_price.setSelected(m.getSelectedCount() != Integer.parseInt(m.getQuantity()));

        holder.vPosition = position;


        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.rl_delete)       RelativeLayout rl_delete;
        @Bind(R.id.tv_name)         TextView tv_name;
        @Bind(R.id.tv_etc)          TextView tv_etc;
        @Bind(R.id.iv_minus)        ImageView iv_minus;
        @Bind(R.id.tv_count)        TextView tv_count;
        @Bind(R.id.iv_plus)         ImageView iv_plus;
        @Bind(R.id.tv_price)        TextView tv_price;

        private int vPosition;
        private Activity vActivity;
        private List<Product> vList = new ArrayList<Product>();

        private TextView vView;
        private TextView vView2;

        public ViewHolder(View view, Activity c, List<Product> list, TextView tv, TextView tv2) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
            vView = tv;
            vView2 = tv2;
        }

        @OnClick({R.id.rl_delete, R.id.iv_minus, R.id.iv_plus})
        public void onClick(View v) {
            Product m;
            m = vList.get(vPosition);
            switch (v.getId()) {
                case R.id.rl_delete:
                    delete(m.getCartId(), m.getProductId(), vPosition);
                    break;
                case R.id.iv_minus:
                    m.setSelectedCount(m.getSelectedCount() == 1 ? 1 : m.getSelectedCount()-1);
                    notifyDataSetChanged();
                    setTotalPrice();
                    break;
                case R.id.iv_plus:
                    m.setSelectedCount(m.getSelectedCount()+1);
                    notifyDataSetChanged();
                    setTotalPrice();
                    break;
            }
        }

        private void setTotalPrice(){
            int price = 0;
            for(int i=0; i < vList.size() ;i++) {
                price += Integer.parseInt(vList.get(i).getPrice()) * vList.get(i).getSelectedCount();
            }
            vView.setText(CommonUtils.getNumberThreeEachFormatWithWon(price));
            vView2.setText(vList.size() + "개");
        }

        private void delete(String cartIds, String productIds, final int index) {
            RequestParams param = new RequestParams();
            param.put("cartIds", cartIds);
            param.put("productIds", productIds);

            APIManager.getInstance().callAPI(APIUrl.CART_REMOVE, param, new RequestHandler(vActivity) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null && response.optInt("code") == 0) {
                        vList.remove(index);
                        notifyDataSetChanged();
                        setTotalPrice();
                    }
                }
            });
        }


    }
}