package kr.co.igo.pleasebuy.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.tsengvn.typekit.Typekit;

public class AppController extends Application implements Application.ActivityLifecycleCallbacks {
    private static int numActivity = 0;
    private static int numVisibleActivity = 0;
    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        instance = this;

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSans-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSansKR-Bold-Alphabetic.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "fonts/NotoSansKR-Medium-Alphabetic.ttf"))
                .addCustom2(Typekit.createFromAsset(this, "fonts/Roboto-Regular.ttf"))
                .addCustom3(Typekit.createFromAsset(this, "fonts/Roboto-Medium.ttf"))
                .addCustom4(Typekit.createFromAsset(this, "fonts/Roboto-Bold.ttf"));
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++numVisibleActivity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        --numVisibleActivity;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        --numActivity;
    }

    public static synchronized AppController getInstance() {
        return instance;
    }

    public static boolean isRunning() {
        return numActivity > 0;
    }

    public static boolean isVisibleRunning() {
        return numVisibleActivity > 0;
    }
}
