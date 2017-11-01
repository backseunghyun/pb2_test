package kr.co.igo.pleasebuy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.OrderHistoryAdapter;
import kr.co.igo.pleasebuy.adapter.OrderHistoryDateAdapter;
import kr.co.igo.pleasebuy.adapter.OrderStep3Adapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.FragmentName;

/**
 * Created by baekseunghyun on 09/10/2017.
 */

public class OrderHistoryFragment extends BaseFragment {
    @Bind(R.id.rv_date_list) RecyclerView rv_date_list;
    @Bind(R.id.lv_list) ListView lv_list;

    private OrderHistoryAdapter mAdapter;
    private List<Product> mList = new ArrayList<Product>();

    private OrderHistoryDateAdapter nAdapter;
    private List<Product> nList = new ArrayList<Product>();
    private LinearLayoutManager mLayoutManager;


    public OrderHistoryFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new OrderHistoryAdapter(getActivity(), mList);
        lv_list.setAdapter(mAdapter);



        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_date_list.setLayoutManager(mLayoutManager);
        nAdapter = new OrderHistoryDateAdapter(getActivity(), nList);
        rv_date_list.setAdapter(nAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.rl_order_cancel})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.rl_order_cancel:
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getList();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.HISTORY.tag());
        }
    }

    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_LIST, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("favoriteList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Product item = new Product();
                                item.setFavoriteId(obj.optString("favoriteId"));
                                item.setProductName(obj.optString("productName"));
                                item.setUnit(obj.optString("unit"));
                                item.setPrice(obj.optString("price"));
                                item.setProductId(obj.optString("productId"));
                                item.setSelectedCount(obj.optInt("quantity"));
                                item.setImgUrl(ApplicationData.getImgPrefix() + obj.optString("imageUrl"));

                                item.setManufacturer(obj.optString("manufacturer"));
                                item.setOrigin(obj.optString("origin"));

                                mList.add(item);
                            }
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
