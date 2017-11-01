package kr.co.igo.pleasebuy.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.RequestParams;

import java.io.IOException;

import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.Preference;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private Preference preference;

    public RegistrationIntentService() {
        super(TAG);
        preference = new Preference();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            preference.setStringPreference(Preference.PREFS_KEY.PUSH_DEVICE_ID, token);
            preference.setStringPreference(Preference.PREFS_KEY.DEVICE_ID, ApplicationData.DEVICE_ID);
            sendRegistrationToServer(token);

            subscribeTopics(token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    /**
     * Persist registration to servers.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        String deviceId = preference.getStringPreference(Preference.PREFS_KEY.DEVICE_ID);
        RequestParams param = new RequestParams();
        param.put("pushDeviceId", token);
        param.put("deviceId", deviceId);
        APIManager.getInstance().callSyncAPI(APIUrl.PUSH_DEVICE_UPDATE, param, new RequestHandler(this, false) {});
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

}
