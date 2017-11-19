package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.ui.FavoriteEditAddActivity;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by Back on 2017-02-27.
 */
public class OrderGridAdapter extends RecyclerView.Adapter<OrderGridAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Product> mList = new ArrayList<Product>();
    private Product m;
    private ViewHolder holder;


    public OrderGridAdapter(Activity c, List<Product> list) {
        this.activity = c;
        this.layoutInflater = LayoutInflater.from(c);
        this.mList = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_order_grid, parent, false);

        ViewHolder holder = new ViewHolder(view, activity, mList);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        m = mList.get(position);

        holder.tv_name.setText(m.getProductName());
        holder.tv_price.setText(CommonUtils.getNumberThreeEachFormatWithWon(m.getPrice()));
        holder.tv_etc.setText(m.getOrigin() + (m.getOrigin().equals("") ? "" : "/") + m.getUnit());

        Glide.with(activity)
                .load( m.getImgUrl())
                .centerCrop()
//                .fallback(activity.getResources().getDrawable(R.drawable.img_park_default))
//                .error(activity.getResources().getDrawable(R.drawable.img_park_default))
                .into(holder.iv_image);

        if (m.getIsInCart() > 0) {
            holder.ll_bg.setSelected(true);
        } else {
            holder.ll_bg.setSelected(false);
        }

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
        @Bind(R.id.iv_image)        ImageView iv_image;
        @Bind(R.id.tv_name)         TextView tv_name;
        @Bind(R.id.tv_etc)          TextView tv_etc;
        @Bind(R.id.tv_price)        TextView tv_price;


        private int vPosition;
        private Activity vActivity;
        private List<Product> vList = new ArrayList<Product>();


        public ViewHolder(View view, Activity c, List<Product> list) {
            super(view);
            ButterKnife.bind(this, view);
            vActivity = c;
            vList = list;
        }

        @OnClick({R.id.ll_bg})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_bg:
                    Product m = vList.get(vPosition);
                    if(vActivity instanceof MainActivity) {
                        if (m.getIsInCart() == 0) {
                            m.setIsInCart(1);

                            int cnt = 0;
                            for (int i=0; i<vList.size();i++){
                                if (vList.get(i).getIsInCart() > 0) {
                                    cnt++;
                                }
                            }


                            ((MainActivity) vActivity).cartAddProduct(Integer.parseInt(m.getProductId()), cnt);
                        } else {
                            m.setIsInCart(0);

                            int cnt = 0;
                            for (int i=0; i<vList.size();i++){
                                if (vList.get(i).getIsInCart() > 0) {
                                    cnt++;
                                }
                            }


                            ((MainActivity) vActivity).cartRemoveProduct(Integer.parseInt(m.getProductId()), cnt);
                        }
                        notifyDataSetChanged();
                    } else if (vActivity instanceof FavoriteEditAddActivity){
                        if (m.getIsInCart() == 0) {
                            m.setIsInCart(1);
                        } else {
                            m.setIsInCart(0);
                        }
                        ((FavoriteEditAddActivity)vActivity).cartProductCheck();
                        notifyDataSetChanged();
                    }
                    break;
            }
        }

    }
}