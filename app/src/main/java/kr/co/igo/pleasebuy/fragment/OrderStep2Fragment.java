package kr.co.igo.pleasebuy.fragment;

import android.app.Activity;
import android.content.DialogInterface;
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
import kr.co.igo.pleasebuy.adapter.FavoriteAdapter;
import kr.co.igo.pleasebuy.adapter.OrderStep1Adapter;
import kr.co.igo.pleasebuy.adapter.OrderStep2Adapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.model.Store;
import kr.co.igo.pleasebuy.popup.DeliveryMemoPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.OrderActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.FragmentName;

/**
 * Created by baekseunghyun on 09/10/2017.
 */

public class OrderStep2Fragment extends BaseFragment {
    @Bind(R.id.lv_list) ListView lv_list;
    @Bind(R.id.tv_totalOfOrderPrice)    TextView tv_totalOfOrderPrice;
    @Bind(R.id.tv_cntProductInCart)     TextView tv_cntProductInCart;
    @Bind(R.id.tv_date)                 TextView tv_date;
    @Bind(R.id.tv_storeName)            TextView tv_storeName;
    @Bind(R.id.tv_address)              TextView tv_address;
    @Bind(R.id.tv_etc)                  TextView tv_etc;

    private OrderStep2Adapter mAdapter;
    private List<Product> mList = new ArrayList<Product>();
    private Store store = new Store();

    private int cntProductInCart;
    private int totalOfOrderPrice;

    public OrderStep2Fragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_step_2, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new OrderStep2Adapter(getActivity(), mList, tv_totalOfOrderPrice);
        lv_list.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.ib_add, R.id.rl_next, R.id.tv_etc})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.ib_add:
                getActivity().setResult(-1);
                getActivity().finish();
                break;
            case R.id.rl_next:
                final TwoButtonPopup popup = new TwoButtonPopup(getActivity());
                popup.setCancelable(false);
                popup.setTitle(getResources().getString(R.string.s_confirm));
                popup.setContent(getResources().getString(R.string.s_ask_order));
                popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(popup.isConfirm()){
                            update();
                        }
                    }
                });
                popup.show();
                break;
            case R.id.tv_etc:
                final DeliveryMemoPopup popup2 = new DeliveryMemoPopup(getActivity());
                popup2.setTitle(getResources().getString(R.string.s_confirm));
                popup2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(popup2.isConfirm()){
                            tv_etc.setText(popup2.getContent());
                        }
                    }
                });
                popup2.show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getList();
        getStoreInfo();
    }

    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.CART_LIST, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("userList");
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
//                                item.setImgUrl(ApplicationData.getImgPrefix() + obj.optString("imageUrl"));
                                item.setImgUrl("http://bbaeggun100.vps.phps.kr:8080/pleasebuy/static/product/3.jpg");

                                item.setManufacturer(obj.optString("manufacturer"));
                                item.setOrigin(obj.optString("origin"));

                                item.setCategoryValue(obj.optString("categoryValue"));
                                item.setQuantity(obj.optString("quantity"));
                                item.setSelectedCount(obj.optInt("quantity"));

                                mList.add(item);
                            }
                        }

                        totalOfOrderPrice = response.optInt("totalOfOrderPrice");
                        cntProductInCart = response.optInt("cntProductInCart");

                        tv_cntProductInCart.setText(cntProductInCart + "개");
                        tv_totalOfOrderPrice.setText(CommonUtils.getNumberThreeEachFormatWithWon(totalOfOrderPrice));
                    }
                } catch (JSONException ignored) {
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getStoreInfo() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.STORE_INFO, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {
                    try {
                        JSONObject obj = response.getJSONObject("store");
                        store.setStoreId(obj.optString("storeId"));
                        store.setStoreName(obj.optString("storeName"));
                        store.setAddress(obj.optString("address"));
                        store.setTel(obj.optString("tel"));
                        store.setPhone(obj.optString("phone"));
                        store.setOwnerName(obj.optString("ownerName"));
                        store.setStoreEtc(obj.optString("storeEtc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        tv_storeName.setText(store.getStoreName());
                        tv_address.setText(store.getAddress());
                    }
                }
            }
        });
    }

    private void update(){
        StringBuilder cartIds = new StringBuilder();
        StringBuilder quantitys = new StringBuilder();

        for(int i=0; i < mList.size(); i++){
            cartIds.append(cartIds.toString().equals("") ? mList.get(i).getCartId() : "," + mList.get(i).getCartId());
            quantitys.append(quantitys.toString().equals("") ? mList.get(i).getSelectedCount() : "," + mList.get(i).getSelectedCount());
        }

        RequestParams param = new RequestParams();
        param.put("storeName", store.getStoreName());
        param.put("address", store.getAddress());
        param.put("tel", store.getTel());
        param.put("phone", store.getPhone());
        param.put("ownerName", store.getOwnerName());
        param.put("storeEtc", store.getStoreEtc());
        param.put("etc", tv_etc.getText());


        param.put("cartIds", cartIds.toString());
        param.put("quantitys", quantitys.toString());

        APIManager.getInstance().callAPI(APIUrl.ORDER_ORDER, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        if(getActivity() instanceof OrderActivity) {
                            ((OrderActivity)getActivity()).mainFragmentReplace(FragmentName.ORDER_STEP_3);
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
