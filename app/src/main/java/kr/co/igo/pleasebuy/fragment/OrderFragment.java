package kr.co.igo.pleasebuy.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.OrderPagerAdapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.MyPagerIndicator;
import kr.co.igo.pleasebuy.util.MyPagerTitleView;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2016-09-29.
 */
public class OrderFragment extends BaseFragment {
    @Bind(R.id.rl_change)   RelativeLayout rl_change;
    @Bind(R.id.et_search)   EditText et_search;
    @Bind(R.id.b_search)    Button b_search;
    @Bind(R.id.magic_indicator)     MagicIndicator magic_indicator;
    @Bind(R.id.view_pager)  ViewPager view_pager;

    private List<Product> mList = new ArrayList<Product>();
    private List<String> mDataList = new ArrayList<>();
    private OrderPagerAdapter mAdapter;

    private MagicIndicator mMagicIndicator;
    private CommonNavigator mCommonNavigator;

    private static Preference preference;


    public OrderFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        preference = new Preference();

        mAdapter = new OrderPagerAdapter(getActivity(), mDataList, mList);
        mAdapter.setIsList(preference.getStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE).equals(preference.TRUE));

        setInit();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.ORDER.tag());
        }
    }

    private void setInit() {

        view_pager.setAdapter(mAdapter);
        magic_indicator.setBackgroundColor(Color.WHITE);
        mCommonNavigator = new CommonNavigator(getActivity());
        mCommonNavigator.setSkimOver(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#666666"));
                clipPagerTitleView.setClipColor(Color.parseColor("#f33b2d"));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_pager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magic_indicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(magic_indicator, view_pager);

        getCategory();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.rl_change, R.id.b_search})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.rl_change:
                setListChange();
                break;
            case R.id.b_search:
                break;

        }
    }

    private void setListChange(){
        String b;
        Log.d(preference.getStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE), "");
        if (preference.getStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE).equals(preference.TRUE)) {
            b = preference.FALSE;
        } else {
            b = preference.TRUE;
        }
        preference.setStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE, b);
        mAdapter.setIsList(preference.getStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE).equals(preference.TRUE));
//        mAdapter.notifyDataSetChanged();
        setInit();
    }

    private void getCategory() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.USER_CATEGORY_LIST, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mDataList.clear();

                        mDataList.add("전체");

                        JSONArray jsonArray = response.getJSONArray("categoryList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                String item = obj.optString("value");
                                mDataList.add(item);
                            }
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
//                    mCommonNavigator.notifyDataSetChanged();
//                    mAdapter.notifyDataSetChanged();
                    getList();
                }
            }
        });
    }

    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.PRODUCT_LIST, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("searchResult");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Product item = new Product();
                                item.setProductId(obj.optString("productId"));
                                item.setProductName(obj.optString("productName"));
                                item.setUnit(obj.optString("unit"));
                                item.setPrice(obj.optString("pricing"));
                                item.setCategoryId(obj.optString("categoryId"));
                                item.setIsInCart(obj.optInt("isInCart"));
//                                item.setImgUrl(ApplicationData.getImgPrefix() + obj.optString("imageUrl"));
                                item.setImgUrl("http://bbaeggun100.vps.phps.kr:8080/pleasebuy/static/product/3.jpg");

                                item.setManufacturer(obj.optString("manufacturer"));
                                item.setOrigin(obj.optString("origin"));

                                item.setCategoryValue(obj.optString("categoryValue"));

                                mList.add(item);
                            }
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
                    mCommonNavigator.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}