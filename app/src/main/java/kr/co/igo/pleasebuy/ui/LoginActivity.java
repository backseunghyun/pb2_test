package kr.co.igo.pleasebuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by baekseunghyun on 16/09/2017.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_id)       EditText et_id;
    @Bind(R.id.et_password) EditText et_password;
    @Bind(R.id.iv_image)    ImageView iv_image;

    private static Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Glide.with(this)
                .load("http://bbaeggun100.vps.phps.kr:8080/pleasebuy2/static/splash.jpg")
                .centerCrop()
                .error(null)
                .into(iv_image);
    }

    @OnClick({R.id.btn_login, R.id.tv_info})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_login:
                if (vaildation()) {
                    login();
                }
                break;
            case R.id.tv_info:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"dikeipd@igoperation"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
//                email.putExtra(Intent.EXTRA_SUBJECT,"보내질 email 제목");
//                email.putExtra(Intent.EXTRA_TEXT,"보낼 email 내용을 미리 적어 놓을 수 있습니다.\n");
                startActivity(email);
                break;
        }
    }

    private boolean vaildation(){
        if (et_id.getText().toString().length() == 0 ||
            et_password.getText().toString().length() == 0) {
            return false;
        }
        return true;
    }



    private void login() {
        String gcmToken = preference.getStringPreference(Preference.PREFS_KEY.PUSH_DEVICE_ID);

        RequestParams param = new RequestParams();
        param.put("loginId", et_id.getText().toString());
        param.put("password", et_password.getText().toString());
        param.put("pushDeviceId", gcmToken);

        APIManager.getInstance().callAPI(APIUrl.PUBLIC_LOGIN, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {
                    preference.setStringPreference(Preference.PREFS_KEY.ENC_USER_ID, response.optString("encUserId"));
                    preference.setStringPreference(Preference.PREFS_KEY.USER_ID, response.optJSONObject("user").optString("userId"));
                    preference.setStringPreference(Preference.PREFS_KEY.LOGIN_ID, response.optJSONObject("user").optString("loginId"));
                    preference.setStringPreference(Preference.PREFS_KEY.USER_NAME, response.optJSONObject("user").optString("ownerName"));
                    moveMain();
                }
            }
        });
    }

    private void moveMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
