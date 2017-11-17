package kr.co.igo.pleasebuy.trunk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.trunk.api.APIManager;

/**
 * Created by Back on 2016-10-19.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected UUID uuid = UUID.randomUUID();
    private static UUID lastUUID = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        APIManager.getInstance().register(uuid);
        lastUUID = uuid;

        Log.d(TAG, "onRestart");
        Log.d(TAG, "uuid: " + uuid.toString());
        Log.d(TAG, "lastUUID: " + lastUUID.toString());

        // 앱 재실행, Sleep 모드에서 복귀하는 경우
        if(uuid.equals(lastUUID)) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        APIManager.getInstance().unregister(uuid);

        Log.d(TAG, "onStop");
        Log.d(TAG, "uuid: " + uuid.toString());
        Log.d(TAG, "lastUUID: " + lastUUID.toString());

        // 홈 버튼, Sleep 모드로 빠지는 경우
        if(uuid.equals(lastUUID)) {
        }
    }

    protected void showError(String msg){
        ConfirmPopup popup = new ConfirmPopup(getContext());
        popup.setTitle(getResources().getString(R.string.s_popup_title));
        popup.setContent(msg);
        popup.show();
    }
}
