package kr.co.igo.pleasebuy.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Notice;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
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
    @Bind(R.id.tv_save)     TextView tv_save;


    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;
    private int boardId;
    private Notice item = new Notice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        tv_title.setText(getResources().getString(R.string.s_menu_board_add));

        if (getIntent().hasExtra("boardId")) {
            tv_title.setText(getResources().getString(R.string.s_menu_board_edit));
            tv_save.setText("수정");
            boardId = getIntent().getIntExtra("boardId",0);
            getData();

        }

    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    private boolean validation(){
        String msg = "";
        if (et_subject.getText().length() == 0) {
            msg = "제목을 입력해 주세요.";
        } else if (et_subject.getText().length() == 0) {
            msg = "내용을 입력해 주세요.";
        }

        if(!msg.equals("")) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

        return msg.equals("");
    }

    private void save() {
        if (validation()) {
            confirmPopup(tv_save.getText().equals("수정"));
        }
    }

    private void confirmPopup(final boolean b) {

        final TwoButtonPopup popup = new TwoButtonPopup(this);
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        if (b) {
            popup.setContent(getResources().getString(R.string.s_ask_board_bbs_edit));
        } else {
            popup.setContent(getResources().getString(R.string.s_ask_board_bbs_add));
        }
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    if (b) {
                        bbsEdit();
                    } else {
                        bbsAdd();
                    }
                }
            }
        });
        popup.show();
    }

    private void bbsAdd(){
        RequestParams param = new RequestParams();
        param.put("title", et_subject.getText());
        param.put("contents", et_contents.getText());
        param.setForceMultipartEntityContentType(true);
        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS_ADD, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(BoardEditActivity.this, "게시글을 등록 하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BoardEditActivity.this, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bbsEdit(){
        RequestParams param = new RequestParams();
        param.put("boardId", boardId);
        param.put("title", et_subject.getText());
        param.put("contents", et_contents.getText());
        param.setForceMultipartEntityContentType(true);

        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS_UPDATE, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(BoardEditActivity.this, "게시글을 수정 하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BoardEditActivity.this, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getData(){
        RequestParams param = new RequestParams();
        param.put("boardId", boardId);

        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS_DETAIL, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {
                    try {
                        JSONObject obj = response.getJSONObject("item");
                        item.setBoardId(obj.optInt("boardId"));
                        item.setTitle(obj.optString("title"));
                        item.setRegDate(obj.optLong("regDate"));
                        item.setContents(obj.optString("contents"));
                        item.setWiter(obj.optString("userName"));
                        item.setUserID(obj.optString("userID"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        et_subject.setText(item.getTitle());
                        et_contents.setText(item.getContents());
                    }
                }
            }
        });

    }



}
