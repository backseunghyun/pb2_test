package kr.co.igo.pleasebuy.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.util.AppController;
import kr.co.igo.pleasebuy.util.Preference;


/**
 * Created by evan on 2016. 12. 2..
 */
public class NotificationActivity extends BaseActivity {
    public final int EVENT = 100;               // 이벤트
    public final int NOTICE = 101;              // 공지사항
    public final int USER_PUSH = 102;           // AJPass 입차


    public AppController appController;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        appController = (AppController)getApplicationContext();
        preference = new Preference();

        moveToScreen();
    }

    private void moveToScreen()
    {
        Intent intent   = null;
        Bundle extras   = getIntent().getExtras();
        String id = "";

        int code = -1;

        // 푸시
        if (extras != null) {
            code = extras.getInt("notificationId", -1);
            id = extras.getString("id");
        }

        if(getIntent()!=null){
            Uri uri = getIntent().getData();
            if(uri != null){
                Log.wtf("uri.getHost().", uri.getHost());
                Log.wtf("uri.getQueryParameter().", uri.getQueryParameter("Host"));
            }
        }

        Log.wtf("moveToScreen code", String.valueOf(code));

        if (AppController.isVisibleRunning()) {
            if (code != -1) {
                Log.wtf("moveToScreen isRunning code", String.valueOf(code));
                switch (code) {
                    case NOTICE:
//                        intent = new Intent(this, NoticeDetailActivity.class);
//                        intent.putExtra("noticeId", id);
                        break;
                    case USER_PUSH:
//                        intent = new Intent(this, MainActivity.class);
//                        intent.putExtra("type", USER_PUSH);
                        break;
                }
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
            if (extras != null) {
                intent.putExtras(extras);
            }
        }

        if (intent != null)
        {
            startActivity(intent);
        }

        finish();
    }
}
