package kr.co.igo.pleasebuy.trunk.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.HashMap;
import java.util.UUID;

import kr.co.igo.pleasebuy.util.ApplicationData;


/**
 * 공통으로 사용하는 API 클래스
 */
public class APIManager {

    private static APIManager instance = null;

    private AsyncHttpClient client = null;
    private HashMap<UUID, Boolean> uuidMap = null;

    public synchronized static APIManager getInstance() {
        if(instance == null) {
            instance = new APIManager();
        }
        return instance;
    }

    public APIManager() {
        uuidMap = new HashMap<>();

        client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(3000);
        client.setResponseTimeout(60000);
        client.setMaxRetriesAndTimeout(2, 3000);
    }

    /**
     * async API call
     */
    public void callAPI(APIUrl url, RequestParams param, AsyncHttpResponseHandler responseHandler) {
        if(param == null) param = new RequestParams();
        param.put("appStore", ApplicationData.APP_STORE);
        param.put("appVersion", ApplicationData.APP_VERSION);
        param.put("deviceId", ApplicationData.DEVICE_ID);

//        Log.wtf("callAPI", ApplicationData.SERVER_PREFIX + url.getUrl() + "&" + param.toString());
        client.post(ApplicationData.SERVER_PREFIX + url.getUrl(), param, responseHandler);
    }

    /**
     * sync API call
     */
    public void callSyncAPI(APIUrl url, RequestParams param, AsyncHttpResponseHandler responseHandler) {
        if(param == null) param = new RequestParams();
        param.put("appStore", ApplicationData.APP_STORE);
        param.put("appVersion", ApplicationData.APP_VERSION);
        param.put("deviceId", ApplicationData.DEVICE_ID);

        responseHandler.setUseSynchronousMode(true);
        responseHandler.setUsePoolThread(true);
        client.post(ApplicationData.SERVER_PREFIX + url.getUrl(), param, responseHandler);
    }

    public void register(UUID uuid) {
        uuidMap.put(uuid, true);
    }

    public void unregister(UUID uuid) {
        uuidMap.remove(uuid);
    }

    public boolean isAvailableRequest(UUID uuid) {
        return uuidMap.containsKey(uuid);
    }

    public void cancelRequest() {
        client.cancelAllRequests(true);
    }
}