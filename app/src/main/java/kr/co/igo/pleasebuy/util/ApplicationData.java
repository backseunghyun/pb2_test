package kr.co.igo.pleasebuy.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import kr.co.igo.pleasebuy.R;

public class ApplicationData  {

    public static String SERVER_PREFIX = getServerPrefix();
    public static String IMG_PREFIX = getImgPrefix();
    public static String APP_VERSION = getAppVersion();
    public static String APP_STORE = "GOOGLE_USER";
    public static String DEVICE_ID = getDeviceID();
    public static String BASE64_ENCODED_PUBLICKEY = ""; // FIXME : In-App 설정시 꼭 채워야 하는 값 (Google Play Store Console에서 확인 가능)

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public final static int RC_SIGN_IN = 9001;

    public ApplicationData() {
    }

    public static String getServerPrefix() {
        return AppController.getInstance().getResources().getString(R.string.serverUrl);
    }

    public static String getImgPrefix() {
        return AppController.getInstance().getResources().getString(R.string.imgPrefix);
    }

    private static String getAppVersion() {
        String result = "0.99";
        try {
            Context context = AppController.getInstance().getApplicationContext();
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            result = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static synchronized String getDeviceID() {
        Context context = AppController.getInstance().getApplicationContext();
        String id = Preference.getStringPreference(Preference.PREFS_KEY.DEVICE_ID);
        if (id != null) return id;

        UUID uuid = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Preference.setStringPreference(Preference.PREFS_KEY.DEVICE_ID, uuid.toString());

        return uuid.toString();
    }
}
