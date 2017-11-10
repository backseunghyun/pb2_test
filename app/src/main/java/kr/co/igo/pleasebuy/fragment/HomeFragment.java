package kr.co.igo.pleasebuy.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.Preference;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Back on 2016-09-29.
 */
public class HomeFragment extends BaseFragment {
    @Bind(R.id.tv_mon)  TextView tv_mon;
    @Bind(R.id.tv_dateFul)  TextView tv_dateFul;
    @Bind(R.id.tv_totalPrice)   TextView tv_totalPrice;
    @Bind(R.id.tv_preOrderDate) TextView tv_preOrderDate;
    @Bind(R.id.tv_preOrderPrice)    TextView tv_preOrderPrice;
    @Bind(R.id.tv_noti)     TextView tv_noti;


    public HomeFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        gatData();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).initHeader();
        }
    }

    @OnClick({R.id.ll_order, R.id.ll_favorite, R.id.ll_add, R.id.ll_graph, R.id.ll_history, R.id.ll_board,
              R.id.ll_noti, R.id.ll_report, R.id.ll_setting})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.ll_order:
                moveFragment(FragmentName.ORDER);
                break;
            case R.id.ll_favorite:
                moveFragment(FragmentName.FAVORITE);
                break;
            case R.id.ll_add:
                moveFragment(FragmentName.ADD);
                break;
            case R.id.ll_graph:
                moveFragment(FragmentName.GRAPH);
                break;
            case R.id.ll_history:
                moveFragment(FragmentName.HISTORY);
                break;
            case R.id.ll_board:
                moveFragment(FragmentName.BOARD);
                break;
            case R.id.ll_noti:
                moveFragment(FragmentName.NOTI);
                break;
            case R.id.ll_report:
                moveFragment(FragmentName.REPORT);
                break;
            case R.id.ll_setting:
                moveFragment(FragmentName.SETTING);
                break;

        }
    }

    private void moveFragment(FragmentName index){
        if (getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).mainFragmentReplace(index);
        }
    }


    private void initView(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sSdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat eSdf = new SimpleDateFormat("yyyy-MM-dd");

        tv_mon.setText(date.getMonth() + "월내역");
        tv_dateFul.setText(sSdf.format(date) + ".01 ~ " + eSdf.format(date));
    }


    private void gatData() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.MAIN, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {

                        JSONObject bbs = response.getJSONObject("bbs");
                        tv_noti.setText(bbs.optString("title"));


                        tv_totalPrice.setText(CommonUtils.getNumberThreeEachFormat(102000));

                        tv_preOrderDate.setText("2017-01-01");
                        tv_preOrderPrice.setText(CommonUtils.getNumberThreeEachFormat(30000));

                        if (getActivity() instanceof MainActivity) {
                            int countProductInCart = response.optInt("cntOfProductInCart", 0);
                            ((MainActivity)getActivity()).setCartCount(countProductInCart, countProductInCart);
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
                }
            }
        });
    }

}
