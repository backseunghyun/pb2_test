package kr.co.igo.pleasebuy.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.fragment.HomeFragment;
import kr.co.igo.pleasebuy.fragment.OrderStep1Fragment;
import kr.co.igo.pleasebuy.fragment.OrderStep2Fragment;
import kr.co.igo.pleasebuy.fragment.OrderStep3Fragment;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by baekseunghyun on 09/10/2017.
 */

public class OrderActivity extends BaseActivity {
    @Bind(R.id.fl_container) FrameLayout fl_container;


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;
    private FragmentName currentFragment;
    public OrderStep1Fragment orderStep1Fragment = new OrderStep1Fragment();
    public OrderStep2Fragment orderStep2Fragment = new OrderStep2Fragment();
    public OrderStep3Fragment orderStep3Fragment = new OrderStep3Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        mainFragmentReplace(FragmentName.ORDER_STEP_1);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 2) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @OnClick({R.id.ib_back})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }

    public void mainFragmentReplace(FragmentName index) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case ORDER_STEP_1:
                ft.replace(R.id.fl_container, orderStep1Fragment)
                        .setBreadCrumbTitle(index.value())
                        .addToBackStack(null)
                        .commit();
                break;
            case ORDER_STEP_2: // 주문하기
                ft.replace(R.id.fl_container, orderStep2Fragment)
                        .setBreadCrumbTitle(index.value())
                        .addToBackStack(null)
                        .commit();
                break;
            case ORDER_STEP_3: // 즐겨찾기
                ft.replace(R.id.fl_container, orderStep3Fragment)
                        .setBreadCrumbTitle(index.value())
                        .addToBackStack(null)
                        .commit();
                break;

        }
    }

    public void setStep3Data(int id){
        orderStep3Fragment.orderInfoId = id;
    }
}
