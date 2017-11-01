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

public class BoardEditActivity extends BaseActivity {
    @Bind(R.id.iv_back)     ImageView iv_back;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.et_subject)  EditText et_subject;
    @Bind(R.id.et_contents) EditText et_contents;


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        tv_title.setText(getResources().getString(R.string.s_menu_board_add));




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
