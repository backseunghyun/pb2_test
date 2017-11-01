package kr.co.igo.pleasebuy.trunk.api;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.trunk.widget.LoadingProgressDialog;

import org.json.JSONObject;

import java.util.UUID;

/**
 * 공통 JsonHttpResponseHandler
 */
public class RequestHandler extends JsonHttpResponseHandler {

    private Context context = null;
    private View view = null;
    private UUID uuid = null;
    public LoadingProgressDialog progress = null;
    private boolean fromUIthread;

    public RequestHandler(Context context) {
        this.context = context;
        this.fromUIthread = true;
    }

    public RequestHandler(Context context, UUID uuid) {
        this.uuid = uuid;
        this.context = context;
        this.fromUIthread = true;
    }

    public RequestHandler(View view, UUID uuid) {
        this.view = view;
        this.uuid = uuid;
        this.context = view.getContext();
        this.fromUIthread = true;
    }

    public RequestHandler(Context context, boolean fromUIthread) {
        this.context = context;
        this.fromUIthread = fromUIthread;
    }

    @Override
    public void onStart() {
        if(view != null)
            view.setClickable(false);

        if (!isNetworkAvailable()) {
            showErrorDialog("네트워크 오류");
        } else {
            if(fromUIthread) {
                progress = LoadingProgressDialog.show(context, "", "");
                progress.hide();
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        // 취소된 request 인지 확인
                        if(!isAvailableRequest()) return;

                        if(progress != null) progress.show();
                    }}, 1500);  // 1500 milliseconds

                setTouchImpossible();
            }
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // 취소된 request 인지 확인
        if(!isAvailableRequest()) return;

        showErrorMessageByResponseCode(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        // 취소된 request 인지 확인
        if(!isAvailableRequest()) return;

        if (isNetworkAvailable()) {
            showErrorDialog("네트워크 오류");
        }
    }

    @Override
    public void onFinish() {
        setTouchPossible();

        // 취소된 request 인지 확인
        if(!isAvailableRequest()) {
            return;
        }

        if(view != null)
            view.setClickable(true);

        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    /**
     * 응답코드에 의한 에러메시지를 보여줌
     */
    private void showErrorMessageByResponseCode(JSONObject response) {
        if (response != null) {
            int code        = response.optInt("code");
            String errMsg   = response.optString("errmsg");
            String errMsgVariable   = response.optString("errmsgvariable");

            if (code != 0) {
                switch (code) {
                    case 257 : // 서버 점검
                        // FIXME : 점검 메시지 출력 후 확인 버튼 누르면 앱 종료
                        showKillDialog(errMsg);
                        break;
                    case 258 :  // 강제 업데이트
                        // FIXME : 업데이트 안내 메시지 출력 후 확인 버튼 누르면 업데이트 URL로 이동
                        final String url = response.optString("url");
                        showUpdateDialog(errMsg, url);
                        break;
                    case 522:
                        break;
                    default:
                        Log.wtf("ErrorMessageByResponseCode", code + " - " + errMsg);
                        Log.wtf("ErrorMessageByResponseCode", code + " - " + errMsgVariable);
                        showErrorDialog(errMsg);
                        break;
                }
            }
        }
        else {
            showErrorDialog("네트워크 오류");
        }
    }

    /**
     * 오류 팝업 창
     * @param message 메시지
     */
    private void showErrorDialog(String message){
        // FIXME : 적절한 오류창
        if(fromUIthread) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle(R.string.app_name);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.show();

            setTouchPossible();
        }
    }

    private boolean isAvailableRequest() {
        if(uuid != null && !APIManager.getInstance().isAvailableRequest(uuid)) return false;
        else return true;
    }

    private void setTouchPossible() {
        if (context != null && view == null && context instanceof Activity) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void setTouchImpossible() {
        if (context != null && view == null && context instanceof Activity) {
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    /**
     * 서버 점검
     * @param message 메시지
     */
    private void showKillDialog(String message){
        if(fromUIthread) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
            alertDialog.show();

            setTouchPossible();
        }
    }

    /**
     * 강제 업데이트
     * @param message 메시지
     * @param url 마켓 주소
     */
    private void showUpdateDialog(String message, final String url){
        if(fromUIthread) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (context != null && context instanceof Activity) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
//                            System.exit(0);
                        }
                        }
                    });
            alertDialog.show();

            setTouchPossible();
        }
    }
}
