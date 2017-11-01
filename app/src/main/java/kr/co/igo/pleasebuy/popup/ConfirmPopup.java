package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;

/**
 * Created by baekseunghyun on 08/10/2017.
 */

public class ConfirmPopup extends Dialog {
    @Bind(R.id.tv_content) TextView tv_content;
    @Bind(R.id.tv_title)    TextView tv_title;

    public ConfirmPopup(Context c){
        super(c);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.popup_confirm, null, false);

        setContentView(view);
        ButterKnife.bind(this);

        tv_content.setText("");
    }

    @OnClick({R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                dismiss();
                break;
        }
    }

    public void setContent(String str) {
        tv_content.setText(str);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }
}

