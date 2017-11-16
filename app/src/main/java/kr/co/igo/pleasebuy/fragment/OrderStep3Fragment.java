package kr.co.igo.pleasebuy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.OrderStep2Adapter;
import kr.co.igo.pleasebuy.adapter.OrderStep3Adapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.CommonUtils;

/**
 * Created by baekseunghyun on 09/10/2017.
 */

public class OrderStep3Fragment extends BaseFragment {
    @Bind(R.id.lv_list) ListView lv_list;
    @Bind(R.id.tv_totalOfOrderPrice)    TextView tv_totalOfOrderPrice;
    @Bind(R.id.tv_cntProductInCart)     TextView tv_cntProductInCart;
    @Bind(R.id.tv_date)                 TextView tv_date;
    @Bind(R.id.tv_storeName)            TextView tv_storeName;
    @Bind(R.id.tv_address)              TextView tv_address;
    @Bind(R.id.tv_etc)                  TextView tv_etc;

    private OrderStep3Adapter mAdapter;
    private List<Product> mList = new ArrayList<Product>();
    public int orderInfoId;

    public OrderStep3Fragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_step_3, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new OrderStep3Adapter(getActivity(), mList);
        lv_list.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.rl_confirm})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.rl_confirm:
                getActivity().setResult(1);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    public void setData() {
        RequestParams param = new RequestParams();
        param.put("orderInfoId", orderInfoId);

        APIManager.getInstance().callAPI(APIUrl.ORDER_DETAIL, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("orderDetailList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Product item = new Product();
                                item.setCartId(obj.optString("cartId"));
                                item.setProductId(obj.optString("productId"));
                                item.setProductName(obj.optString("productName"));
                                item.setUnit(obj.optString("unit"));
                                item.setPrice(obj.optString("price"));
                                item.setCategoryId(obj.optString("categoryId"));
                                item.setIsInCart(obj.optInt("isInCart"));
                                item.setImgUrl(ApplicationData.getImgPrefix() + obj.optString("imageUrl"));

                                item.setManufacturer(obj.optString("manufacturer"));
                                item.setOrigin(obj.optString("origin"));

                                item.setCategoryValue(obj.optString("categoryValue"));
                                item.setQuantity(obj.optString("quantity"));
                                item.setSelectedCount(obj.optInt("quantity"));

                                mList.add(item);
                            }
                        }

                        JSONObject obj = response.getJSONObject("orderInfo");
                        tv_date.setText(obj.optString("deliveryReqDate").equals("") ? "" : obj.optString("deliveryReqDate"));
                        tv_storeName.setText(obj.optString("ownerName"));
                        tv_address.setText(obj.optString("address"));
                        tv_etc.setText(obj.optString("etc"));
                        tv_cntProductInCart.setText(obj.optInt("totalQuantity", 0) + "개");
                        tv_totalOfOrderPrice.setText(CommonUtils.getNumberThreeEachFormatWithWon(obj.optInt("totalPrice", 0)));
                    }
                } catch (JSONException ignored) {
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
