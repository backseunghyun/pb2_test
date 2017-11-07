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

import com.prolificinteractive.materialcalendarview.*;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yeonggyun on 2016-11-03.
 */

public class CalendarTwoPopup extends Dialog {
    @Bind(R.id.cal_a)       MaterialCalendarView cal_a;
    @Bind(R.id.cal_b)       MaterialCalendarView cal_b;
    @Bind(R.id.cal_a_ok)    TextView cal_a_ok;
    @Bind(R.id.cal_a_no)    TextView cal_a_no;

    public CalendarTwoPopup(Context context) {
        super(context);
    }
    String languageToLoad = "ko";

    long cur;
    String resul;
    Date day;
    Date date1;
    Date date2;
    SimpleDateFormat simpleDateFormat;
    CalendarDay iu;
    CalendarDay iuu;
    String select_day = "";
    String select_day1 = "";
    String btn_result = "";
    SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(R.layout.popup_cal_two);
        ButterKnife.bind(this);
        //한글로 언어 설정
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getContext().getResources().updateConfiguration(config,getContext().getResources().getDisplayMetrics());


        final DateFormat format = new SimpleDateFormat("yyyy년 MM월");
        TitleFormatter DEFAULT_TITLE_FORMATTER = new DateFormatTitleFormatter(format);
        cal_a.setTitleFormatter(DEFAULT_TITLE_FORMATTER);
        cal_b.setTitleFormatter(DEFAULT_TITLE_FORMATTER);

        cur = System.currentTimeMillis();
        day = new Date(cur);
        date1 = new Date();
        date2 = new Date();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

        cal_a.setSelectedDate(day);
        cal_b.setSelectedDate(day);

        cal_a.setSelected(true);
        cal_b.setSelected(true);

        iuu = cal_a.getSelectedDate();
        iu = cal_b.getSelectedDate();

        select_day = simpleDateFormat.format(day);
        select_day1 = simpleDateFormat.format(day);


        // set Min Date
        Calendar minCal = Calendar.getInstance();
        minCal.setTime(day);
        minCal.add(Calendar.YEAR, -1);

        // set Max Date
        Calendar maxCal = Calendar.getInstance();
        maxCal.setTime(day);
        maxCal.add(Calendar.YEAR, +2);

        cal_a.state().edit().setMinimumDate(minCal.getTime()).commit();
        cal_a.state().edit().setMaximumDate(maxCal.getTime()).commit();
        cal_b.state().edit().setMinimumDate(minCal.getTime()).commit();
        cal_b.state().edit().setMaximumDate(maxCal.getTime()).commit();


//        // set Disabled Date
//        ArrayList<CalendarDay> enabledDates = new ArrayList<>();
//        Calendar disbleCal = Calendar.getInstance();
//        disbleCal.setTime(day);
//        for(int i = 1; i<=365 ; i++) {
//            disbleCal.add(Calendar.DATE, -1);
//            enabledDates.add(new CalendarDay(disbleCal.getTime()));
//        }
//
//        cal_a.addDecorator(new DayEnableDecorator(enabledDates));
//        cal_b.addDecorator(new DayEnableDecorator(enabledDates));


        OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                iuu = cal_a.getSelectedDate();
                select_day = simpleDateFormat.format(date.getDate());
                date1 = date.getDate();

                long a = date1.getTime();
                long b = date2.getTime();

                if (a > b) {
                    cal_b.setSelectedDate(date1);
                    cal_b.setSelected(true);
                    cal_b.getSelectedDate();
                    select_day1 = simpleDateFormat.format(date.getDate());
                }
            }
        };
        OnDateSelectedListener onDateSelectedListener1 = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                iu = cal_b.getSelectedDate();
                select_day1 = simpleDateFormat.format(cal_b.getSelectedDate().getDate());
                date2 = date.getDate();

                long a = date1.getTime();
                long b = date2.getTime();
                if (a > b) {
                    cal_b.setSelectedDate(date1);
                    cal_b.setSelected(true);
                    cal_b.getSelectedDate();
                    select_day1 = simpleDateFormat.format(date1);
                }
            }
        };

        cal_a.setOnDateChangedListener(onDateSelectedListener);
        cal_b.setOnDateChangedListener(onDateSelectedListener1);



        cal_a_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_result = "ok";
                dismiss();
            }
        });
        cal_a_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_result = "cancel";
                dismiss();
            }
        });
    }

    public String getBtn_result() {
        return this.btn_result;
    }

    public String getStartDate() {
        return this.select_day;
    }

    public String getEndDate() {
        return this.select_day1;
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
