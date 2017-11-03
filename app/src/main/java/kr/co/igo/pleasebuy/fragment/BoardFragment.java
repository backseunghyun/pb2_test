package kr.co.igo.pleasebuy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

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
import kr.co.igo.pleasebuy.adapter.BoardAdapter;
import kr.co.igo.pleasebuy.adapter.NoticeAdapter;
import kr.co.igo.pleasebuy.model.Notice;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.BoardEditActivity;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2016-09-29.
 */
public class BoardFragment extends BaseFragment {
    @Bind(R.id.lv_list)     ExpandableListView lv_list;

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    private BoardAdapter mAdapter;
    private List<Notice> mList = new ArrayList<Notice>();

    public BoardFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        ButterKnife.bind(this, view);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(getActivity());

        mAdapter = new BoardAdapter(getActivity(), mList);
        lv_list.setAdapter(mAdapter);

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
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.BOARD.tag());
        }
    }

    @OnClick({R.id.ib_add})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.ib_add:
                startActivity(new Intent(getActivity(), BoardEditActivity.class));
                break;
        }
    }


    private void getList() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {
                    mList.clear();
                    try {
                        JSONArray jsonArray = response.getJSONArray("list");
                        for (int i=0; i < jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Notice item = new Notice();
                            item.setBoardId(obj.optInt("boardId"));
                            item.setTitle(obj.optString("title"));
                            item.setRegDate(obj.optLong("regDate"));
                            item.setContents(obj.optString("contents"));
                            item.setWiter(obj.optString("userName"));
                            item.setUserID(obj.optString("userID"));

                            mList.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }



}
