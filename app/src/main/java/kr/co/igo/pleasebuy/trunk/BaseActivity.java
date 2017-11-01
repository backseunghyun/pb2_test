package kr.co.igo.pleasebuy.trunk;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.UUID;

import kr.co.igo.pleasebuy.trunk.api.APIManager;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    protected UUID uuid = UUID.randomUUID();
    private static UUID lastUUID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        APIManager.getInstance().register(uuid);
        lastUUID = uuid;
    }

   @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
        Log.d(TAG, "uuid: " + uuid.toString());
        Log.d(TAG, "lastUUID: " + lastUUID.toString());

        // 앱 재실행, Sleep 모드에서 복귀하는 경우
        if(uuid.equals(lastUUID)) {
        }

        lastUUID = uuid;
    }

    @Override
    protected void onStop() {
        super.onStop();
        APIManager.getInstance().unregister(uuid);

        Log.d(TAG, "onStop");
        Log.d(TAG, "uuid: " + uuid.toString());
        Log.d(TAG, "lastUUID: " + lastUUID.toString());

        // 홈 버튼, Sleep 모드로 빠지는 경우
        if(uuid.equals(lastUUID)) {
        }
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
