package kr.co.igo.pleasebuy.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.gcm.RegistrationIntentService;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.Preference;

public class SplashActivity extends BaseActivity {
    @Bind(R.id.iv_image)    ImageView iv_image;

    private static final String TAG = "SplashActivity";
    private static final String KEY_FIRST_CONNECTION = "Ajpark.user.firstconnection";

    private static Preference preference;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        preference = new Preference();

        Glide.with(this)
                .load("http://bbaeggun100.vps.phps.kr:8080/pleasebuy2/static/splash.jpg")
                .centerCrop()
                .error(null)
                .into(iv_image);

        extras = getIntent().getExtras();
        if (extras != null) {
            Log.wtf("SplashActivity extras notificationId", String.valueOf(extras.getInt("notificationId", -1)));
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "RegistrationBroadcastReceiver : true");
            }
        };

        init();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

            PermissionListener permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    start();
                }

                @Override
                public void onPermissionDenied(ArrayList<String> arrayList) {
                    start();
                }
            };

            new TedPermission(this)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("앨범 접근을 거절하시면 서비스를 이용 할 수 없습니다.\n설정 변경 부탁드립니다.")
//                    .setDeniedMessage("위치 접근을 거절하시면 서비스를 이용 할 수 없습니다.\n설정 변경 부탁드립니다.")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            ,android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .check();
        }
    }

    private void init(){
        if(preference.getStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE) == null) {
            preference.setStringPreference(Preference.PREFS_KEY.IS_LIST_VISIBLE, Preference.TRUE);
        }
    }

    private void start(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                String enUserId = preference.getStringPreference(Preference.PREFS_KEY.ENC_USER_ID);
                String gcmToken = preference.getStringPreference(Preference.PREFS_KEY.PUSH_DEVICE_ID);
                String deviceId = preference.getStringPreference(Preference.PREFS_KEY.DEVICE_ID);

//                if (!preference.getBooleanPreference(Preference.PREFS_KEY.INTRO_FINISH)) {
////                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
////                    startActivity(i);
////                    finish();
//                } else if (!preference.isContains(KEY_FIRST_CONNECTION)) {
//                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
                    if(!TextUtils.isEmpty(enUserId)) {
                        autoLogin(enUserId, gcmToken, deviceId);
                    }else{
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                        if (extras != null) {
//                            i.putExtras(extras);
//                        }
                        startActivity(i);
                        finish();
                    }
//                }
            }
        }, 2500);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
        this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
//        iv_spinner.setBackgroundResource(R.drawable.anim_splash_spinner);
//        AnimationDrawable frameAnimation = (AnimationDrawable) iv_spinner.getBackground();
//        frameAnimation.start();
//        super.onWindowFocusChanged(hasFocus);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, ApplicationData.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }


    private void autoLogin(String encUserId, String pushDeviceId, String deviceId) {
        RequestParams param = new RequestParams();
        param.put("encUserId", encUserId);
        param.put("pushDeviceId", pushDeviceId);
        param.put("deviceId", deviceId);

        APIManager.getInstance().callAPI(APIUrl.PUBLIC_AUTO_LOGIN, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null && response.optInt("code") == 0) {
                    preference.setStringPreference(Preference.PREFS_KEY.ENC_USER_ID, response.optString("encUserId"));
                    preference.setStringPreference(Preference.PREFS_KEY.USER_ID, response.optJSONObject("user").optString("loginId"));
                    preference.setStringPreference(Preference.PREFS_KEY.USER_NAME, response.optJSONObject("user").optString("ownerName"));
                    moveMain();
                }
            }
        });
    }

    private void moveMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
