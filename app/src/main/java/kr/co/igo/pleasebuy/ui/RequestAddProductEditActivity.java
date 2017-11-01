package kr.co.igo.pleasebuy.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
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


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add_product_edit);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        tv_title.setText(getResources().getString(R.string.s_menu_request_add_product));




    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_save:
                break;
        }
    }

}
