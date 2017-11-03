package kr.co.igo.pleasebuy.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by baekseunghyun on 15/10/2017.
 */

public class RequestAddProductEditActivity extends BaseActivity {
    @Bind(R.id.iv_back)     ImageView iv_back;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.rl_add)      RelativeLayout rl_add;
    @Bind(R.id.iv_image)    ImageView iv_image;
    @Bind(R.id.iv_add)      ImageView iv_add;
    @Bind(R.id.et_name)     EditText et_name;
    @Bind(R.id.et_unit)     EditText et_unit;
    @Bind(R.id.et_etc)      EditText et_etc;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;
    private int boardId;
    private String name;
    private String unit;
    private String etc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add_product_edit);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        tv_title.setText(getResources().getString(R.string.s_menu_request_add_product));
        name = "";
        unit = "";
        etc = "";

        if(getIntent().hasExtra("boardId")) {
            boardId = getIntent().getIntExtra("boardId",0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
        getData();
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_cancel:
                checkData();
                break;
            case R.id.tv_save:
                break;
        }
    }

    public void checkData(){
        if (!et_name.getText().toString().equals(name) ||
            !et_unit.getText().toString().equals(unit) ||
            !et_etc.getText().toString().equals(etc)) {
            showConfirm();
        } else {
            finish();
        }
    }

    private void showConfirm() {
        final TwoButtonPopup popup = new TwoButtonPopup(this);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(getResources().getString(R.string.s_confirm));
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

    private void getData() {
        RequestParams param = new RequestParams();
        param.put("boardId", boardId);

        APIManager.getInstance().callAPI(APIUrl.BOARD_NOTICE_DETAIL, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {

                        JSONObject item = response.getJSONObject("item");
                        name = item.optString("title");
                        unit = item.optString("userName");
                        etc = item.optString("contents");

                        et_name.setText(name);
                        et_unit.setText(unit);
                        et_etc.setText(etc);
                        Glide.with(RequestAddProductEditActivity.this)
                                .load(ApplicationData.getImgPrefix() + item.optString("imageUrl"))
                                .centerCrop()
                                .into(iv_image);
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private void add() {
        RequestParams param = new RequestParams();
        param.put("title", et_name.getText().toString());
        param.put("contents", et_unit.getText().toString());
        param.put("contents", et_etc.getText().toString());
        param.put("img", et_name.getText().toString());

        param.setForceMultipartEntityContentType(true);

        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS_ADD, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        showSuccess(getResources().getString(R.string.s_request_add_success));
                    } else {
                        Toast.makeText(RequestAddProductEditActivity.this, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void update() {
        RequestParams param = new RequestParams();
        param.put("boardId", boardId);
        param.put("title", et_name.getText().toString());
        param.put("contents", et_unit.getText().toString());
        param.put("contents", et_etc.getText().toString());
        param.put("img", et_name.getText().toString());
        param.setForceMultipartEntityContentType(true);

        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS_UPDATE, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        showSuccess(getResources().getString(R.string.s_request_update_success));
                    } else {
                        Toast.makeText(RequestAddProductEditActivity.this, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showSuccess(String msg) {
        ConfirmPopup popup = new ConfirmPopup(this);
//        notiPopup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(msg);
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        popup.show();
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
