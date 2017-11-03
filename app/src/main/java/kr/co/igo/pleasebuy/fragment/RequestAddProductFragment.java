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
import kr.co.igo.pleasebuy.adapter.NoticeAdapter;
import kr.co.igo.pleasebuy.adapter.RequestAddProductAdapter;
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.model.Notice;
import kr.co.igo.pleasebuy.model.RequestAddProduct;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.FavoriteDetailActivity;
import kr.co.igo.pleasebuy.ui.RequestAddProductDetailActivity;
import kr.co.igo.pleasebuy.ui.RequestAddProductEditActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2016-09-29.
 */
public class RequestAddProductFragment extends BaseFragment {
    @Bind(R.id.lv_list) ListView lv_list;

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    private RequestAddProductAdapter mAdapter;
    private List<RequestAddProduct> mList = new ArrayList<RequestAddProduct>();

    public RequestAddProductFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_add_product, container, false);
        ButterKnife.bind(this, view);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(getActivity());

        mAdapter = new RequestAddProductAdapter(getActivity(), mList);
        lv_list.setAdapter(mAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("boardId", mList.get(position).getBoardId() + "");
                Intent intent = new Intent(getActivity(), RequestAddProductDetailActivity.class);
                intent.putExtra("boardId", mList.get(position).getBoardId());
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        getList();
    }

    @OnClick({R.id.ib_add})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ib_add:
                break;
        }
    }


    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.BOARD_NOTICE, param, new RequestHandler(getActivity(), uuid) {
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
                                RequestAddProduct item = new RequestAddProduct();
                                item.setBoardId(obj.optInt("boardId"));
                                item.setName(obj.optString("title"));
                                item.setEtc(obj.optString("contents"));
                                item.setUnit(obj.optString("userName"));
                                item.setRegDate(obj.optLong("regDate"));
                                item.setImgUrl(ApplicationData.getImgPrefix() + obj.optString("imageUrl"));

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
