package kr.co.igo.pleasebuy.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import kr.co.igo.pleasebuy.adapter.FavoriteDetailAdapter;
import kr.co.igo.pleasebuy.adapter.FavoriteEditAdapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.Preference;

public class FavoriteEditActivity extends BaseActivity {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.lv_list)     ListView lv_list;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;
    @Bind(R.id.et_name)     EditText et_name;

    private FavoriteEditAdapter mAdapter;
    private List<Product> mList = new ArrayList<Product>();

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;
    private int favoriteGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_edit);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        mAdapter = new FavoriteEditAdapter(this, mList);
        lv_list.setAdapter(mAdapter);

        tv_title.setText("즐겨찾기 편집");

        if(getIntent().hasExtra("favoriteGroupId")) {
            favoriteGroupId = getIntent().getIntExtra("favoriteGroupId",0);
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save, R.id.ib_add, R.id.rl_cart})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_save:
                break;
            case R.id.ib_add:
                startActivity(new Intent(this, FavoriteEditAddActivity.class));
                break;
            case R.id.rl_cart:
                startActivity(new Intent(this, OrderActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
        getList();
    }

    private void getList() {
        RequestParams param = new RequestParams();
        param.put("favoriteGroupId", favoriteGroupId);

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP_DETAILS, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONObject favoriteGroup = response.getJSONObject("favoriteGroup");
                        et_name.setText(favoriteGroup.optString("name"));

                        JSONArray jsonArray = response.getJSONArray("list");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Product item = new Product();
                                item.setFavoriteId(obj.optString("favoriteId"));
                                item.setProductName(obj.optString("productName"));
                                item.setUnit(obj.optString("unit"));
                                item.setPrice(obj.optString("price"));
                                item.setProductId(obj.optString("productId"));

                                item.setManufacturer(obj.optString("manufacturer"));
                                item.setOrigin(obj.optString("origin"));
                                item.setSelected(false);

                                mList.add(item);
                            }
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void cartAdd(){
        String productIds = "";
        int count = 0;

        for(int i=0; i<mList.size(); i++) {
            if(mList.get(i).isSelected()) {
                count ++;
                productIds += productIds.equals("") ? mList.get(i).getProductId() : "," + mList.get(i).getProductId();
            }
        }

        if (productIds.equals("")) {
            Toast.makeText(FavoriteEditActivity.this, getResources().getString(R.string.s_cart_add_validation), Toast.LENGTH_SHORT).show();
        } else {
            cartAddProduct(productIds, count);
        }
    }

    public void cartAddProduct(String productIds, final int count) {
        RequestParams param = new RequestParams();
        param.put("productIds", productIds);

        APIManager.getInstance().callAPI(APIUrl.CART_ADD_PRODUCT, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(FavoriteEditActivity.this, String.format(getResources().getString(R.string.s_cart_add_success), count), Toast.LENGTH_SHORT).show();
                        setCartCount(response.optInt("cntProductInCart", 0));
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private void setCartCount(int num){
        if (num > 0) {
            tv_count.setText(num + "");
            rl_cart.setEnabled(false);
        } else {
            tv_count.setText("");
            rl_cart.setEnabled(true);
        }
        preference.setIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART, num);
    }

}