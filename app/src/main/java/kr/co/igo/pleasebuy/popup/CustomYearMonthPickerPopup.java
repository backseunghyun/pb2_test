package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.MonthListAdapter;
import kr.co.igo.pleasebuy.adapter.YearListAdapter;
import me.kaelaela.verticalviewpager.VerticalViewPager;
import java.util.Date;

public class CustomYearMonthPickerPopup extends Dialog {
    @Bind(R.id.vp_year_list)    VerticalViewPager vp_year_list;
    @Bind(R.id.vp_month_list)   VerticalViewPager vp_month_list;
    @Bind(R.id.cal_a_ok)    TextView cal_a_ok;
    @Bind(R.id.cal_a_no)    TextView cal_a_no;

    private ArrayList<String> years;
    private ArrayList<String> months;
    private int year;
    private String month;

    private YearListAdapter adapter1;
    private MonthListAdapter adapter2;

    private String resultDate = "";
    private String result = "";

    public CustomYearMonthPickerPopup(Context context, String year, String month) {
        super(context);
        this.year = Integer.parseInt(year);
        this.month = month;
        this.resultDate = year + month;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_custom_date_picker_popup);
        ButterKnife.bind(this);

        years = new ArrayList<>();
        months = new ArrayList<>();

        for(int i = 1; i < 13; i ++) {
            months.add(String.valueOf(i));
        }

        for(int i = (year - 10); i < (year + 11); i++) {
            years.add(String.valueOf(i));
        }

        adapter1 = new YearListAdapter(getContext(), years);
        adapter2 = new MonthListAdapter(getContext(), months);

        vp_year_list.setAdapter(adapter1);
        vp_month_list.setAdapter(adapter2);

        vp_year_list.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                year = Integer.parseInt(years.get(position));
            }

            @Override
            public void onPageSelected(int position) {
                year = Integer.parseInt(years.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_month_list.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                month = setTime(Integer.parseInt(months.get(position)));
            }

            @Override
            public void onPageSelected(int position) {
                month = setTime(Integer.parseInt(months.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for(int i = 0; i < years.size(); i++) {
            if(years.get(i).equals(String.valueOf(year))) {
                vp_year_list.setCurrentItem(i);
            }
        }

        for(int i = 0; i < months.size(); i++) {
            if(Integer.parseInt(months.get(i)) == Integer.parseInt(month)) {
                vp_month_list.setCurrentItem(i);
            }
        }
    }

    @OnClick ({R.id.cal_a_ok, R.id.cal_a_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cal_a_ok:
                result = "ok";
                dismiss();
                break;
            case R.id.cal_a_no:
                result = "cancel";
                dismiss();
                break;
        }
    }

    private String setTime(int time) {
        String hour;
        if(time > 9) {
            hour = String.valueOf(time);
        } else {
            hour = "0" + String.valueOf(time);
        }
        return hour;
    }

    public String getResult() {
        return this.result;
    }

    public String getResultDate() {
        resultDate = String.valueOf(year) + month;
        return this.resultDate;
    }

    public String getResultMonth(String delimeter) {
        resultDate = String.valueOf(year) + delimeter + month;
        return this.resultDate;
    }
}

