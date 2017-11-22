package kr.co.igo.pleasebuy.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by 10wonders on 2017-11-20.
 */

public class TermsActivity extends BaseActivity {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.tv_terms)    TextView tv_terms;
    @Bind(R.id.tv_pp)       TextView tv_pp;
    @Bind(R.id.wv_webview)  WebView wv_webview;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
        preference = new Preference();
        setInitVeiw();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
    }

    private void setInitVeiw() {
        tv_title.setText("이용약관 및 개인정보 취급방침");

        setWebview(1);
    }

    private void setWebview(int num){
        if(num == 1){
            tv_terms.setSelected(true);
            tv_pp.setSelected(false);
            wv_webview.loadUrl("http://bbaeggun100.vps.phps.kr:8080/pleasebuy2/policy/service");
        }else if(num ==2){
            tv_terms.setSelected(false);
            tv_pp.setSelected(true);
            wv_webview.loadUrl("http://bbaeggun100.vps.phps.kr:8080/pleasebuy2/policy/privacy");
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_terms, R.id.tv_pp, R.id.rl_cart})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_terms:
                setWebview(1);
                break;
            case R.id.tv_pp:
                setWebview(2);
                break;
            case R.id.rl_cart:
                if (tv_count.getVisibility() != View.GONE) {
                    startActivity(new Intent(this, OrderActivity.class));
                }
                break;
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
            CookieSyncManager.getInstance().sync();
        }

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
