package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class OrderStep1Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Product> mList = new ArrayList<Product>();
    private Product m;
    private ViewHolder holder;
    private TextView mView;
    private InputMethodManager imm;


    public OrderStep1Adapter(Activity c, List<Product> list, TextView view) {
        this.activity = c;
        this.layoutInflater = LayoutInflater.from(c);
        this.mList = list;
        this.mView = view;
        this.imm = (InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
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
            convertView = layoutInflater.inflate(R.layout.adapter_order_step_1_item, parent, false);
            holder = new ViewHolder(convertView, activity, mList, mView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        m = mList.get(position);
        holder.vPosition = position;
        holder.tv_name.setText(m.getProductName());
        holder.tv_price.setText(CommonUtils.getNumberThreeEachFormatWithWon(Integer.parseInt(m.getPrice()) * m.getSelectedCount()));

        String etc = m.getOrigin() + "/" + m.getUnit();
        if (m.getOrigin().equals("") || m.getOrigin().equals("-")) {
            etc = m.getUnit();
        }
        holder.tv_etc.setText(etc);

        Glide.with(activity)
                .load( m.getImgUrl())
                .centerCrop()
//                .fallback(activity.getResources().getDrawable(R.drawable.img_park_default))
//                .error(activity.getResources().getDrawable(R.drawable.img_park_default))
                .into(holder.iv_image);

        holder.et_count.setText(m.getSelectedCount() + "");
        holder.et_count.setTag(position);
        holder.et_count.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Product t = mList.get((Integer) v.getTag());
                    if (((EditText)v).getText().toString().length() > 0) {
                        t.setSelectedCount(Integer.parseInt(((EditText)v).getText().toString()));
                    } else {
                        t.setSelectedCount(1);
                    }
                    holder.setTotalPrice();
                    notifyDataSetChanged();
                    imm.hideSoftInputFromWindow(((EditText)v).getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        holder.et_count.setSelected(m.getSelectedCount() != Integer.parseInt(m.getQuantity()));
        holder.tv_price.setSelected(m.getSelectedCount() != Integer.parseInt(m.getQuantity()));




        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.ll_rootlayout)   LinearLayout ll_rootlayout;
        @Bind(R.id.iv_image)        ImageView iv_image;
        @Bind(R.id.tv_name)         TextView tv_name;
        @Bind(R.id.tv_etc)          TextView tv_etc;
        @Bind(R.id.iv_minus)        ImageView iv_minus;
        @Bind(R.id.et_count)        EditText et_count;
        @Bind(R.id.iv_plus)         ImageView iv_plus;
        @Bind(R.id.tv_price)        TextView tv_price;
        @Bind(R.id.rl_delete)       RelativeLayout rl_delete;

        private int vPosition;
        private Activity vActivity;
        private List<Product> vList = new ArrayList<Product>();
        private TextView vView;

        public ViewHolder(View view, Activity c, List<Product> list, TextView tv) {
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
            vView = tv;
        }

        @OnClick({R.id.iv_minus, R.id.iv_plus, R.id.rl_delete})
        public void onClick(View v) {
            Product m = vList.get(vPosition);
            switch (v.getId()) {
                case R.id.iv_minus:
                    m.setSelectedCount(m.getSelectedCount() == 1 ? 1 : m.getSelectedCount()-1);
                    setTotalPrice();
                    notifyDataSetChanged();
                    imm.hideSoftInputFromWindow(et_count.getWindowToken(), 0);
                    break;
                case R.id.iv_plus:
                    m.setSelectedCount(m.getSelectedCount()+1);
                    setTotalPrice();
                    notifyDataSetChanged();
                    imm.hideSoftInputFromWindow(et_count.getWindowToken(), 0);
                    break;
                case R.id.rl_delete:
                    delete(m.getCartId(), m.getProductId(), vPosition);
                    break;
            }
        }

        @OnTextChanged({R.id.et_count})
        public void onTextChanged(Editable s) {
            Product m = vList.get(vPosition);
            if (s.toString().length() > 0) {
                m.setSelectedCount(Integer.parseInt(s.toString()));
            } else {
                m.setSelectedCount(0);
            }
        }

        public void setTotalPrice(){
            int price = 0;
            for(int i=0; i < vList.size() ;i++) {
                price += Integer.parseInt(vList.get(i).getPrice()) * vList.get(i).getSelectedCount();
            }
            vView.setText(CommonUtils.getNumberThreeEachFormatWithWon(price));
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