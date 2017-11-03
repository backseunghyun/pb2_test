package kr.co.igo.pleasebuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by baekseunghyun on 15/10/2017.
 */

public class RequestAddProductDetailActivity extends BaseActivity {
    @Bind(R.id.iv_back)     ImageView iv_back;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.iv_image)    ImageView iv_image;
    @Bind(R.id.tv_name)     TextView tv_name;
    @Bind(R.id.tv_unit)     TextView tv_unit;
    @Bind(R.id.tv_etc)      TextView tv_etc;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    private int boardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add_product_detail);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        tv_title.setText(getResources().getString(R.string.s_menu_request_add_product));

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
                finish();
                break;
            case R.id.tv_save:
                Intent intent = new Intent(this, RequestAddProductEditActivity.class);
                intent.putExtra("boardId", boardId);
                startActivity(intent);
                break;
        }
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
                        tv_name.setText(item.optString("title"));
                        tv_unit.setText(item.optString("userName"));
                        tv_etc.setText(item.optString("contents"));
                        Glide.with(RequestAddProductDetailActivity.this)
                                .load(ApplicationData.getImgPrefix() + item.optString("imageUrl"))
//                                .centerCrop()
                                .listener(requestListener)
                                .into(iv_image);
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            iv_image.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resouorce, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            iv_image.setVisibility(View.VISIBLE);
            return false;
        }
    };

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
