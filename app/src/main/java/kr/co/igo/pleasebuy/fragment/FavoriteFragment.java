package kr.co.igo.pleasebuy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import kr.co.igo.pleasebuy.adapter.FavoriteAdapter;
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.FavoriteDetailActivity;
import kr.co.igo.pleasebuy.ui.FavoriteEditActivity;
import kr.co.igo.pleasebuy.ui.FavoriteEditAddActivity;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2016-09-29.
 */
public class FavoriteFragment extends BaseFragment {
    @Bind(R.id.lv_list)     ListView lv_list;

    private FavoriteAdapter mAdapter;
    private List<Favorite> mList = new ArrayList<Favorite>();
    public Preference preference;

    public FavoriteFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        preference = new Preference();

        mAdapter = new FavoriteAdapter(getActivity(), mList);
        lv_list.setAdapter(mAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("favoriteGroupId", mList.get(position).getFavoriteGroupId() + "");
                Intent intent = new Intent(getActivity(), FavoriteDetailActivity.class);
                intent.putExtra("favoriteGroupId", mList.get(position).getFavoriteGroupId());
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.ib_add})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.ib_add:
                startActivity(new Intent(getActivity(), FavoriteEditAddActivity.class));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getList();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.FAVORITE.tag());
            ((MainActivity)getActivity()).setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
        }

    }

    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.FAVORITE_GROUP, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("list");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Favorite item = new Favorite();
                                item.setFavoriteGroupId(obj.optInt("favoriteGroupId"));
                                item.setName(obj.optString("name"));
                                item.setProductNames(obj.optString("productNames").equals("null") ? "" : obj.optString("productNames"));
                                item.setRegDate(obj.optLong("regDate"));
                                item.setUpdateDate(obj.optLong("updateDate"));

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
