package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import kr.co.igo.pleasebuy.R;

/**
 * Created by baekseunghyun on 08/10/2017.
 */

public class DeliveryMemoPopup extends Dialog {
    @Bind(R.id.et_contents) EditText et_contents;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.tv_size)     TextView tv_size;

    private boolean isConfirm;

    public DeliveryMemoPopup(Context c){
        super(c);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.popup_delivery_memo, null, false);

        setContentView(view);
        ButterKnife.bind(this);

        isConfirm = false;

        et_contents.setText("");
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                isConfirm = true;
                dismiss();
                break;
        }
    }

    @OnTextChanged({R.id.et_contents})
    public void onTextChanged(Editable s) {
        tv_size.setText(et_contents.getText().toString().length() + "");
    }

    public String getContent() {
        return et_contents.getText().toString();
    }

    public boolean isConfirm() {
        return isConfirm;
    }
}

