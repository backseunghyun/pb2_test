package kr.co.igo.pleasebuy.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import kr.co.igo.pleasebuy.adapter.FavoriteAdapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.LoginActivity;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.ui.ModifyInformationActivity;
import kr.co.igo.pleasebuy.ui.ModifyPasswordActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.FragmentName;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Back on 2016-09-29.
 */
public class SettingFragment extends BaseFragment {
    @Bind(R.id.iv_push)     ImageView iv_push;

    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    public SettingFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        pushGet();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.SETTING.tag());
        }
    }

    @OnClick({R.id.iv_push, R.id.rl_setting2, R.id.rl_setting4, R.id.rl_setting5, R.id.rl_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_push:
                pushSet(iv_push.isSelected() ? "N" : "Y");
                break;
            case R.id.rl_setting2:
                startActivity(new Intent(getActivity(), ModifyInformationActivity.class));
                break;
            case R.id.rl_setting4:
                startActivity(new Intent(getActivity(), ModifyPasswordActivity.class));
                break;
            case R.id.rl_setting5:
//                startActivity(new Intent(getActivity(), TermsActivity.class));
                break;
            case R.id.rl_logout:
                logoutPopup();
                break;
        }
    }

    private void logoutPopup() {

        final TwoButtonPopup popup = new TwoButtonPopup(getActivity());
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
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

        APIManager.getInstance().callAPI(APIUrl.PUBLIC_LOGOUT, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {

                    preference.removeValuePreference(Preference.PREFS_KEY.ENC_USER_ID);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        });
    }


    private void pushSet(String push) {
        RequestParams param = new RequestParams();
        param.put("push", push);

        APIManager.getInstance().callAPI(APIUrl.PUSH_SET, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        String push = response.optString("push");
                        iv_push.setSelected(push.equals("Y"));
                        iv_push.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void pushGet() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.PUSH_GET, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        String push = response.optString("push");
                        iv_push.setSelected(push.equals("Y"));
                        iv_push.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
