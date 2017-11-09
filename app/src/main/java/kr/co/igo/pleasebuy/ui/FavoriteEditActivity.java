package kr.co.igo.pleasebuy.ui;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.StringUtils;
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
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.Dir;
import kr.co.igo.pleasebuy.util.Preference;

public class FavoriteEditActivity extends BaseActivity {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.lv_list)     ListView lv_list;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;
    @Bind(R.id.et_name)     EditText et_name;

    private FavoriteEditAdapter mAdapter;
    private List<Product> mList = new ArrayList<Product>();
    private List<Product> nList = new ArrayList<Product>();

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;
    private int favoriteGroupId = -1;

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
            getList();
        } else {
            if (getIntent().hasExtra("productIds")) {
                Bundle bundle = getIntent().getExtras();
                List<Product> tList = new ArrayList<Product>();
                tList = bundle.getParcelableArrayList("productIds");
                mList.clear();
                nList.clear();
                for (Product aTList : tList) {
                    if (aTList.getIsInCart() > 0) {
                        mList.add(aTList);
                        nList.add(aTList);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void onBackPressed() {
        checkedFinish();
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save, R.id.ib_add, R.id.rl_cart})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back:
                checkedFinish();
                break;
            case R.id.tv_cancel:
                checkedFinish();
                break;
            case R.id.tv_save:
                String productIds = "";
                for(int i=0; i<mList.size(); i++) {
                    productIds += productIds.equals("") ? mList.get(i).getProductId() : "," + mList.get(i).getProductId();
                }

                if (et_name.getText().toString().length() == 0) {
                    showError(getResources().getString(R.string.s_update_favorite_error_no_name));
                } else if (productIds.equals("")) {
                    showError(getResources().getString(R.string.s_update_favorite_error_no_products));
                } else {
                    if (favoriteGroupId != -1) {
                        update(productIds);
                    } else {
                        add(productIds);
                    }
                }
                break;
            case R.id.rl_cart:
                startActivity(new Intent(this, OrderActivity.class));
                break;

            case R.id.ib_add:
                Intent intent = new Intent(this,FavoriteEditAddActivity.class);
                intent.putParcelableArrayListExtra("productIds", (ArrayList<? extends Parcelable>) mList);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (data.hasExtra("productIds")) {
                        Bundle bundle = data.getExtras();
                        List<Product> tList = new ArrayList<Product>();
                        tList = bundle.getParcelableArrayList("productIds");
                        mList.clear();
                        for (Product aTList : tList) {
                            if (aTList.getIsInCart() > 0) {
                                mList.add(aTList);
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
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
                        nList.clear();

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
                                nList.add(item);
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

    public void add(String productIds) {
        RequestParams param = new RequestParams();
        param.put("name", et_name.getText().toString());
        param.put("productIds", productIds);

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP_ADD, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        showSaveSuccess();
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    public void update(final String productIds) {
        RequestParams param = new RequestParams();
        param.put("favoriteGroupId", favoriteGroupId);
        param.put("name", et_name.getText().toString());

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP_UPDATE, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        updateFavorite(productIds);
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    public void updateFavorite(String productIds) {
        RequestParams param = new RequestParams();
        param.put("favoriteGroupId", favoriteGroupId);
        param.put("productIds", productIds);

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP_UPDATE_FAVORITE, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        showSaveSuccess();
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private void deleteFavorite(String productId) {
        for(int i=0; i<mList.size(); i++) {
            if (mList.get(i).getProductId().equals(productId)) {
                mList.remove(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void confirmDeletePopup(final String productId, String name){
        final TwoButtonPopup popup = new TwoButtonPopup(this);
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(name + getResources().getString(R.string.s_ask_delete_favorite));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    deleteFavorite(productId);
                }
            }
        });
        popup.show();
    }

    private void confirmFinishPopup(){
        final TwoButtonPopup popup = new TwoButtonPopup(this);
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(getResources().getString(R.string.s_ask_update_favorite_finish));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    finish();
                }
            }
        });
        popup.show();
    }


    private void showSaveSuccess(){
        ConfirmPopup popup = new ConfirmPopup(this);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(getResources().getString(R.string.s_update_favorite_success));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        popup.show();
    }

    private void checkedFinish() {
        boolean b = false;
        if (mList.size() == nList.size()){
            List<Product> tList = new ArrayList<Product>();
            tList.addAll(mList);

            for (int i = 0; i < nList.size(); i++) {
                if (tList.containsAll(nList)) {
                    tList.removeAll(nList);
                }
            }

            if (tList.size() > 0) {
                b = true;
            }
        } else {
            b = true;
        }

        if (b) {
            confirmFinishPopup();
        } else {
            finish();
        }
    }

    private void showError(String msg){
        ConfirmPopup popup = new ConfirmPopup(this);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(msg);
        popup.show();
    }

}
