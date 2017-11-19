package kr.co.igo.pleasebuy.ui;


import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.fragment.BoardFragment;
import kr.co.igo.pleasebuy.fragment.FavoriteFragment;
import kr.co.igo.pleasebuy.fragment.HomeFragment;
import kr.co.igo.pleasebuy.fragment.NoticeFragment;
import kr.co.igo.pleasebuy.fragment.OrderFragment;
import kr.co.igo.pleasebuy.fragment.OrderHistoryFragment;
import kr.co.igo.pleasebuy.fragment.ReportFragment;
import kr.co.igo.pleasebuy.fragment.RequestAddProductFragment;
import kr.co.igo.pleasebuy.fragment.SettingFragment;
import kr.co.igo.pleasebuy.fragment.StatisticsFragment;
import kr.co.igo.pleasebuy.model.OrderHistoryDate;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.Preference;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.v_block)     View v_block;
    @Bind(R.id.v_block2)    public View v_block2;
    @Bind(R.id.iv_menu)     ImageView iv_memu;
    @Bind(R.id.iv_cart)     ImageView iv_cart;
    @Bind(R.id.iv_title)    ImageView iv_title;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.v_main_line) View v_main_line;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;
    @Bind(R.id.rl_cart_check)       RelativeLayout rl_cart_check;
    @Bind(R.id.rl_favorite_edit)    RelativeLayout rl_favorite_edit;

    private TextView tv_name;
    private LinearLayout ll_home;
    private LinearLayout ll_order;
    private LinearLayout ll_favorite;
    private LinearLayout ll_add;
    private LinearLayout ll_graph;
    private LinearLayout ll_history;
    private LinearLayout ll_board;
    private LinearLayout ll_noti;
    private LinearLayout ll_report;
    private LinearLayout ll_setting;
    private TextView     tv_logout;
    private FrameLayout  fl_close;


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    private SlidingMenu menu;

    private FragmentName currentFragment;

    public HomeFragment homeFragment = new HomeFragment();
    public OrderFragment orderFragment = new OrderFragment();
    public FavoriteFragment favoriteFragment = new FavoriteFragment();
    public RequestAddProductFragment requestAddProductFragment = new RequestAddProductFragment();
    public StatisticsFragment statisticsFragment = new StatisticsFragment();
    public OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
    public BoardFragment boardFragment = new BoardFragment();
    public NoticeFragment noticeFragment = new NoticeFragment();
    public ReportFragment reportFragment = new ReportFragment();
    public SettingFragment settingFragment = new SettingFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        initView();
        setSlideMenu();

        mainFragmentReplace(FragmentName.HOME);
    }

    private void initView(){
        v_block.setVisibility(View.GONE);
    }

    private void setSlideMenu(){
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.7f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_menu);
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                v_block.setVisibility(View.VISIBLE);
            }
        });
        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                v_block.setVisibility(View.GONE);
            }
        });


        View menuView = menu.getMenu();
        tv_name = (TextView)menuView.findViewById(R.id.tv_name);
        ll_home = (LinearLayout)menuView.findViewById(R.id.ll_home);
        ll_order = (LinearLayout)menuView.findViewById(R.id.ll_order);
        ll_favorite = (LinearLayout)menuView.findViewById(R.id.ll_favorite);
        ll_add = (LinearLayout)menuView.findViewById(R.id.ll_add);
        ll_graph = (LinearLayout)menuView.findViewById(R.id.ll_graph);
        ll_history = (LinearLayout)menuView.findViewById(R.id.ll_history);
        ll_board = (LinearLayout)menuView.findViewById(R.id.ll_board);
        ll_noti = (LinearLayout)menuView.findViewById(R.id.ll_noti);
        ll_report = (LinearLayout)menuView.findViewById(R.id.ll_report);
        ll_setting = (LinearLayout)menuView.findViewById(R.id.ll_setting);
        tv_logout = (TextView)menuView.findViewById(R.id.tv_logout);
        fl_close = (FrameLayout)menuView.findViewById(R.id.fl_close);

        ll_home.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        ll_favorite.setOnClickListener(this);
        ll_add.setOnClickListener(this);
        ll_graph.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_board.setOnClickListener(this);
        ll_noti.setOnClickListener(this);
        ll_report.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        fl_close.setOnClickListener(this);

        tv_name.setText(preference.getStringPreference(Preference.PREFS_KEY.USER_NAME));

    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            initHeader();

            if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
                backPressCloseSystem.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @OnClick({R.id.rl_menu, R.id.rl_cart, R.id.rl_cart_check, R.id.rl_favorite_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu:
                menu.showMenu();
                break;
            case R.id.rl_cart:
                startActivityForResult(new Intent(this, OrderActivity.class), 100);
                break;
            case R.id.rl_cart_check:
                startActivityForResult(new Intent(this, OrderActivity.class), 100);
                break;
            case R.id.rl_favorite_edit:
                startActivity(new Intent(this, FavoriteDeleteActivity.class));
                break;
            case R.id.fl_close:
                menu.showContent();
                break;
            case R.id.tv_logout:
                logoutPopup();
                break;
            case R.id.ll_home:
                mainFragmentReplace(FragmentName.HOME);
                break;
            case R.id.ll_order:
                mainFragmentReplace(FragmentName.ORDER);
                break;
            case R.id.ll_favorite:
                mainFragmentReplace(FragmentName.FAVORITE);
                break;
            case R.id.ll_add:
                mainFragmentReplace(FragmentName.ADD);
                break;
            case R.id.ll_graph:
                mainFragmentReplace(FragmentName.GRAPH);
                break;
            case R.id.ll_history:
                mainFragmentReplace(FragmentName.HISTORY);
                break;
            case R.id.ll_board:
                mainFragmentReplace(FragmentName.BOARD);
                break;
            case R.id.ll_noti:
                mainFragmentReplace(FragmentName.NOTI);
                break;
            case R.id.ll_report:
                mainFragmentReplace(FragmentName.REPORT);
                break;
            case R.id.ll_setting:
                mainFragmentReplace(FragmentName.SETTING);
                break;
        }
    }

    public void initHeader(){
        iv_title.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.GONE);
        rl_cart.setVisibility(View.VISIBLE);
        rl_cart_check.setVisibility(View.GONE);
        rl_favorite_edit.setVisibility(View.GONE);
    }

    public void setHeaderHome(){
        iv_title.setVisibility(View.GONE);
        tv_title.setVisibility(View.VISIBLE);
        rl_cart.setVisibility(View.GONE);
        rl_cart_check.setVisibility(View.VISIBLE);
        rl_favorite_edit.setVisibility(View.GONE);
    }

    public void setHeaderFavorite(){
        iv_title.setVisibility(View.GONE);
        tv_title.setVisibility(View.VISIBLE);
        rl_cart.setVisibility(View.GONE);
        rl_cart_check.setVisibility(View.GONE);
        rl_favorite_edit.setVisibility(View.VISIBLE);
    }

    public void setHederTitle(String name){
        iv_title.setVisibility(View.GONE);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(name);
    }

    public void mainFragmentReplace(FragmentName index) {
        currentFragment = index;
        menu.showContent();

        initHeader();

//        if (currentFragment != index) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (index) {
                case HOME: // 홈
                    ft.replace(R.id.fl_container, homeFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
                    break;
                case ORDER: // 주문하기
                    ft.replace(R.id.fl_container, orderFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();

                    setHeaderHome();
                    break;
                case FAVORITE: // 즐겨찾기
                    ft.replace(R.id.fl_container, favoriteFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
                    setHeaderFavorite();
                    break;
                case ADD: // 상품추가 요청
                    ft.replace(R.id.fl_container, requestAddProductFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
//                    showError("준비중입니다.");
                    break;
                case GRAPH: // 통계
//                    ft.replace(R.id.fl_container, statisticsFragment)
//                            .setBreadCrumbTitle(index.value())
//                            .addToBackStack(null)
//                            .commit();

                    showError("준비중입니다.");
                    break;
                case HISTORY: // 주문 히스토리
                    ft.replace(R.id.fl_container, orderHistoryFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
//                    showError("준비중입니다.");
                    break;
                case BOARD: // 게시판
                    ft.replace(R.id.fl_container, boardFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
                    break;
                case NOTI: // 공지사항
                    ft.replace(R.id.fl_container, noticeFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
                    break;
                case REPORT: // 월간리포트
                    ft.replace(R.id.fl_container, reportFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
//                    showError("준비중입니다.");
                    break;
                case SETTING: // 설정
                    ft.replace(R.id.fl_container, settingFragment)
                            .setBreadCrumbTitle(index.value())
                            .addToBackStack(null)
                            .commit();
                    break;

//            }
//            currentFragment = index;
        }
    }

    public void setCartCount(int num, int cnt){
//        if (num > 0) {
//            tv_count.setText(num + "");
//            tv_count.setVisibility(View.VISIBLE);
//            rl_cart_check.setEnabled(true);
//        } else {
//            tv_count.setText("");
//            tv_count.setVisibility(View.GONE);
//            rl_cart_check.setEnabled(false);
//        }
//
//        preference.setIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART, num);

        if (cnt > 0) {
            tv_count.setText(cnt + "");
            tv_count.setVisibility(View.VISIBLE);
            rl_cart_check.setEnabled(true);
        } else {
            tv_count.setText("");
            tv_count.setVisibility(View.GONE);
            rl_cart_check.setEnabled(false);
        }

        preference.setIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART, cnt);
    }

    public void cartAddProduct(int productId, final int cnt) {
        RequestParams param = new RequestParams();
        param.put("productIds", productId);

        APIManager.getInstance().callAPI(APIUrl.CART_ADD_PRODUCT, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        setCartCount(response.optInt("cntProductInCart", 0), cnt);
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    public void cartRemoveProduct(int productId, final int cnt) {
        RequestParams param = new RequestParams();
        param.put("productIds", productId);

        APIManager.getInstance().callAPI(APIUrl.CART_REMOVE, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        setCartCount(response.optInt("cntProductInCart", 0), cnt);
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            if(resultCode == -1) {      // 주문하기 step2 에서 상품 추가 눌렀을시
                if (currentFragment != FragmentName.ORDER) {
                    mainFragmentReplace(FragmentName.ORDER);
                }
            } else if (resultCode == 1) {   // 주문하기 step3 에서 완료 시
                if (currentFragment != FragmentName.HOME) {
                    mainFragmentReplace(FragmentName.HOME);
                }
            }
        }
    }

    public void orderHistoryChangeData(OrderHistoryDate date){
        orderHistoryFragment.orderHistoryChangeData(date);
    }

    public void logoutPopup() {
        final TwoButtonPopup popup = new TwoButtonPopup(this);
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_popup_title));
        popup.setContent(getResources().getString(R.string.s_ask_logout));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    logout();
                }
            }
        });
        popup.show();
    }

    private void logout() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.PUBLIC_LOGOUT, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {

                    preference.removeValuePreference(Preference.PREFS_KEY.ENC_USER_ID);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
