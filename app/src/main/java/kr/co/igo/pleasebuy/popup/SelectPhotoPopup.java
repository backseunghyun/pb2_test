package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;

/**
 * Created by Back on 2016-10-04.
 */
public class SelectPhotoPopup extends Dialog {
    private Context mContext;
    private String result;

    public SelectPhotoPopup(Context c){
        super(c);
        mContext = c;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.popup_select_photo, null, false);

        setContentView(view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_new, R.id.ll_gallery, R.id.ll_del, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_new:
                setResult("new");
                break;
            case R.id.ll_gallery:
                setResult("gallery");
                break;
            case R.id.ll_del:
                setResult("del");
                break;
            case R.id.tv_cancel:
                setResult("cancel");
                break;
        }
        dismiss();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
