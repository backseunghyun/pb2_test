package kr.co.igo.pleasebuy.ui;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import kr.co.igo.pleasebuy.adapter.FavoriteDeleteAdapter;
import kr.co.igo.pleasebuy.adapter.FavoriteEditAdapter;
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.Preference;

public class FavoriteDeleteActivity extends BaseActivity {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.lv_list)     ListView lv_list;

    private FavoriteDeleteAdapter mAdapter;
    private List<Favorite> mList = new ArrayList<Favorite>();
    private List<Favorite> nList = new ArrayList<Favorite>();
    private List<Favorite> dList = new ArrayList<Favorite>();

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_delete);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        mAdapter = new FavoriteDeleteAdapter(this, mList);
        lv_list.setAdapter(mAdapter);

        tv_title.setText("즐겨찾기 수정");

        getList();
    }

    @Override
    public void onBackPressed() {
        checkedFinish();
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save})
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
                for(int i=0; i<dList.size(); i++) {
                    productIds += productIds.equals("") ? dList.get(i).getFavoriteGroupId() : "," + dList.get(i).getFavoriteGroupId();
                }

                if(productIds.equals("")) {
                    finish();
                } else {
                    delete(productIds);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();
                        nList.clear();

                        JSONArray jsonArray = response.getJSONArray("list");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Favorite item = new Favorite();
                                item.setFavoriteGroupId(obj.optInt("favoriteGroupId"));
                                item.setName(obj.optString("name"));
                                item.setProductNames(obj.optString("productNames").equals("null") ? "" : obj.optString("productNames"));
                                item.setRegDate(obj.optLong("regDate"));
                                item.setUpdateDate(obj.optLong("updateDate"));
                                item.setCntOfProduct(obj.optInt("cntOfProduct", 0));

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

    public void delete(String productIds) {
        RequestParams param = new RequestParams();
        param.put("favoriteGroupIds", productIds);

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP_REMOVE, param, new RequestHandler(this, uuid) {
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


    private void deleteFavorite(String favoriteGroupId) {
        for(int i=0; i<mList.size(); i++) {
            if (String.valueOf(mList.get(i).getFavoriteGroupId()).equals(favoriteGroupId)) {
                dList.add(mList.get(i));
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
            List<Favorite> tList = new ArrayList<Favorite>();
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
