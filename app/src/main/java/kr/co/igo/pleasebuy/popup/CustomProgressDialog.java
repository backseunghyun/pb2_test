package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.daimajia.numberprogressbar.NumberProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;



public class CustomProgressDialog extends Dialog {
    @Bind(R.id.number_progress_bar) NumberProgressBar number_progress_bar;

    public CustomProgressDialog(Context c){
        super(c);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.popup_custom_progress, null, false);

        setContentView(view);
        ButterKnife.bind(this);

        number_progress_bar.setProgress(0);
    }

    public void setProgress(int pos) {
        number_progress_bar.setProgress(pos);
    }
}