package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;

/**
 * Created by baekseunghyun on 08/10/2017.
 */

public class RegisterPopup extends Dialog {
    @Bind(R.id.et_storeName)    EditText et_storeName;
    @Bind(R.id.et_name)         EditText et_name;
    @Bind(R.id.et_tel)          EditText et_tel;

    private boolean isConfirm;

    public RegisterPopup(Context c){
        super(c);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.popup_register, null, false);

        setContentView(view);
        ButterKnife.bind(this);

        isConfirm = false;

    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                if (et_storeName.getText().toString().replaceAll(" ", "").equals("") ||
                    et_storeName.getText().toString().replaceAll(" ", "").equals("")) {

                }
                isConfirm = true;
                dismiss();
                break;
        }
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public String getStoreName() {
        return et_storeName.getText().toString();
    }

    public String getName() {
        return et_name.getText().toString();
    }

    public String getTel() {
        return et_tel.getText().toString();
    }
}

