package kr.co.igo.pleasebuy.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Store;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2017-02-27.
 */
public class ModifyInformationActivity extends BaseActivity {
    @Bind(R.id.iv_back)     ImageView iv_back;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.et_storeName)    EditText et_storeName;
    @Bind(R.id.et_name) EditText et_name;
    @Bind(R.id.et_address) EditText et_address;
    @Bind(R.id.et_tel) EditText et_tel;
    @Bind(R.id.et_phone) EditText et_phone;
    @Bind(R.id.et_etc) EditText et_etc;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;
    @Bind(R.id.tv_save)     TextView tv_save;


    public Preference preference;
    private InputMethodManager imm;
    private Store store = new Store();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        ButterKnife.bind(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        tv_title.setText(getResources().getString(R.string.s_menu_setting_modify_information));

        getStoreInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save, R.id.rl_cart})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_save:
                modifyInformation();
                break;
            case R.id.rl_cart:
                if (tv_count.getVisibility() != View.GONE) {
                    startActivity(new Intent(this, OrderActivity.class));
                }
                break;
        }
    }

    @OnTextChanged({R.id.et_storeName, R.id.et_name, R.id.et_address, R.id.et_phone})
    public void onTextChanged(Editable s) {

        if(et_storeName.getText().length() > 0 &&
                et_name.getText().length() > 0 &&
                et_address.getText().length() > 0 &&
                et_phone.getText().length() > 0 ) {
            tv_save.setEnabled(true);
        } else {
            tv_save.setEnabled(false);
        }
    }

    @OnTouch({R.id.ll_rootlayout})
    public boolean onTouch(View view) {
//        hideSoftKeyboard();
        return false;
    }



    private void getStoreInfo() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.STORE_INFO, param, new RequestHandler(this, uuid) {
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
                        et_storeName.setText(store.getStoreName());
                        et_name.setText(store.getOwnerName());
                        et_address.setText(store.getAddress());
                        et_phone.setText(store.getPhone());
                        et_tel.setText(store.getTel());
                        et_etc.setText(store.getStoreEtc());
                    }
                }
            }
        });
    }

    private void modifyInformation() {
        RequestParams param = new RequestParams();
        param.put("storeId", store.getStoreId());
        param.put("storeName", et_storeName.getText().toString());
        param.put("ownerName", et_name.getText().toString());
        param.put("address", et_address.getText().toString());
        param.put("phone", et_phone.getText().toString());
        param.put("tel", et_tel.getText().toString());
        param.put("storeEtc", et_etc.getText().toString());

        tv_save.setEnabled(false);
        APIManager.getInstance().callAPI(APIUrl.MODIFY_INFORMATION, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                tv_save.setEnabled(true);
                if (response != null && response.optInt("code") == 0) {
                    showSuccess();
                }
            }
        });
    }

    private void showSuccess() {
        ConfirmPopup popup = new ConfirmPopup(this);
//        notiPopup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_popup_title));
        popup.setContent(getResources().getString(R.string.s_info_update_success));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        popup.show();

    }

    // 키보드 숨기기
    private void hideSoftKeyboard() {
        imm.hideSoftInputFromWindow(et_storeName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_name.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_address.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_tel.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_phone.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_etc.getWindowToken(), 0);
    }

    private void setCartCount(int num){
        if (num > 0) {
            tv_count.setText(num + "");
            tv_count.setVisibility(View.VISIBLE);
            rl_cart.setEnabled(true);
        } else {
            tv_count.setText("");
            tv_count.setVisibility(View.GONE);
            rl_cart.setEnabled(false);
        }
        preference.setIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART, num);
    }
}