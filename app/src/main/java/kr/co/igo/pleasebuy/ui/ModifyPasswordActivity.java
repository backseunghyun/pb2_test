package kr.co.igo.pleasebuy.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2016-10-12.
 */
public class ModifyPasswordActivity extends BaseActivity {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.et_password)     EditText et_password;
    @Bind(R.id.et_new_password)  EditText et_new_password;
    @Bind(R.id.et_ack_password)   EditText et_ack_password;
    @Bind(R.id.tv_confirm)           TextView tv_confirm;
    @Bind(R.id.rl_cart)         RelativeLayout rl_cart;
    @Bind(R.id.tv_count)     TextView tv_count;


    public Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);

        tv_confirm.setEnabled(false);

        tv_title.setText(getResources().getString(R.string.s_modify_password_title));

    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
    }

    @OnTextChanged({R.id.et_password, R.id.et_new_password, R.id.et_ack_password})
    public void onTextChanged(Editable s) {
        if (et_password.getText().length()==0 || et_new_password.getText().length()==0 || et_ack_password.getText().length()==0) {
            tv_confirm.setEnabled(false);
        } else {
            tv_confirm.setEnabled(true);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                passwordAuthCode(et_password.getText().toString(), et_new_password.getText().toString(), et_ack_password.getText().toString());
                break;
        }
    }

    private void passwordAuthCode(String prePassword, String password, String password2) {
        RequestParams param = new RequestParams();
        param.put("prePassword", prePassword);
        param.put("password", password);
        param.put("password2", password2);

        tv_confirm.setEnabled(false);
        APIManager.getInstance().callAPI(APIUrl.PASSWORD_MODIFY, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                tv_confirm.setEnabled(true);
                if (response != null && response.optInt("code") == 0) {
                    showModifySuccess();
                }
            }
        });
    }

    private void showModifySuccess() {
        ConfirmPopup popup = new ConfirmPopup(this);
//        notiPopup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(getResources().getString(R.string.s_password_modify_success));
        popup.show();
    }

    private void setCartCount(int num){
        if (num > 0) {
            tv_count.setText(num + "");
            tv_count.setVisibility(View.VISIBLE);
            rl_cart.setEnabled(false);
        } else {
            tv_count.setText("");
            tv_count.setVisibility(View.GONE);
            rl_cart.setEnabled(true);
        }
        preference.setIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART, num);
    }
}
