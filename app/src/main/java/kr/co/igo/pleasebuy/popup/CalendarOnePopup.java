package kr.co.igo.pleasebuy.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.util.CommonUtils;

import com.prolificinteractive.materialcalendarview.*;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yeonggyun on 2016-11-11.
 */

public class CalendarOnePopup extends Dialog {
    @Bind(R.id.cal_b)       MaterialCalendarView cal_b;
    @Bind(R.id.cal_b_ok)    TextView cal_b_ok;
    @Bind(R.id.cal_b_no)    TextView cal_b_no;

    private boolean b;

    public CalendarOnePopup(Context context) {
        super(context);
        this.b = false;
    }

    public CalendarOnePopup(Context context, boolean b) {
        super(context);
        this.b = b;
    }
    String languageToLoad = "ko";
    String select_day;
    SimpleDateFormat sdf;
    Date cDate;
    long now;
    String result;
    CalendarDay iu;

    String result1;
    String select_day1;
    SimpleDateFormat sdf1;

    private Date selectedDate;
    private boolean isOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(R.layout.popup_cal_one);
        ButterKnife.bind(this);
        //한글로 언어 설정
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getContext().getResources().updateConfiguration(config,getContext().getResources().getDisplayMetrics());

        now = System.currentTimeMillis();
        cDate = new Date(now);
        if (b) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(cDate);
            cal.add(Calendar.DATE, +1);
            cDate = cal.getTime();
        }
        sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf1 = new SimpleDateFormat("yyyyMMdd");
        selectedDate = cDate;

        Date today = new Date();
        select_day = CommonUtils.getTimeStampString(today, "yyyy.MM.dd");

        DateFormat format = new SimpleDateFormat("yyyy년 MM월");
        TitleFormatter DEFAULT_TITLE_FORMATTER = new DateFormatTitleFormatter(format);
        cal_b.setTitleFormatter(DEFAULT_TITLE_FORMATTER);

        cal_b.setSelectedDate(cDate);
        cal_b.setSelected(true);

        cal_b.getSelectedDate();
        select_day = sdf.format(cDate);
        select_day1 = sdf1.format(cDate);
        result = select_day;
        result1 = select_day1;


        // set Min Date
        Calendar minCal = Calendar.getInstance();
        minCal.setTime(cDate);
        minCal.add(Calendar.YEAR, -1);

        // set Max Date
        Calendar maxCal = Calendar.getInstance();
        maxCal.setTime(cDate);
        maxCal.add(Calendar.YEAR, +2);

        cal_b.state().edit().setMinimumDate(minCal.getTime()).commit();
        cal_b.state().edit().setMaximumDate(maxCal.getTime()).commit();


        // set Disabled Date
        ArrayList<CalendarDay> enabledDates = new ArrayList<>();
        Calendar disbleCal = Calendar.getInstance();
        disbleCal.setTime(cDate);


        for(int i = 1; i<=365 ; i++) {
            disbleCal.add(Calendar.DATE, -1);
            enabledDates.add(new CalendarDay(disbleCal.getTime()));
        }

        cal_b.addDecorator(new DayEnableDecorator(enabledDates));



        OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                Calendar tCal = Calendar.getInstance();
                tCal.setTime(cDate);

                if (tCal.after(date)) {
                    cal_b.setDateSelected(date, false);
                } else {
                    iu = cal_b.getSelectedDate();
                    select_day = sdf.format(date.getDate()).toString();
                    select_day1 = sdf1.format(date.getDate()).toString();

                    selectedDate = date.getDate();
                }

            }
        };

        cal_b.setOnDateChangedListener(onDateSelectedListener);

        cal_b_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOk = true;
                result = select_day;
                result1 = select_day1;
                dismiss();
            }
        });
        cal_b_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOk = false;
                dismiss();
            }
        });
    }
    public String getResult() {
        return result;
    }
    public String getResult1(){
        return  result1;
    }

    public boolean isOk() {
        return isOk;
    }
    public Date getSelectedDate() {
        return selectedDate;
    }


    public class DayEnableDecorator implements DayViewDecorator {
        private HashSet<CalendarDay> dates;

        public DayEnableDecorator(Collection<CalendarDay> dates) {
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }
}
