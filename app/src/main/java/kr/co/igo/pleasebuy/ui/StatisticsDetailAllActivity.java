package kr.co.igo.pleasebuy.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by baekseunghyun on 10/11/2017.
 */

public class StatisticsDetailAllActivity extends BaseActivity {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;
    @Bind(R.id.tv_date)     TextView tv_date;

    @Bind(R.id.lc_chart1)   LineChart lc_chart1;
    @Bind(R.id.lc_chart2)   LineChart lc_chart2;
    @Bind(R.id.lc_chart3)   LineChart lc_chart3;
    @Bind(R.id.lc_chart4)   LineChart lc_chart4;
    @Bind(R.id.lc_chart5)   LineChart lc_chart5;
    @Bind(R.id.lc_chart6)   LineChart lc_chart6;
    @Bind(R.id.lc_chart7)   LineChart lc_chart7;

    private static Preference preference;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_detail_all);
        ButterKnife.bind(this);

        if(getIntent().hasExtra("date")) {
            tv_date.setText(getIntent().getStringExtra("date"));
        }

        setChart(lc_chart1, Color.RED);
        setChart(lc_chart2, Color.BLUE);
        setChart(lc_chart3, Color.YELLOW);
        setChart(lc_chart4, Color.GREEN);
        setChart(lc_chart5, Color.GRAY);
        setChart(lc_chart6, Color.CYAN);
        setChart(lc_chart7, Color.MAGENTA);
    }

    @OnClick({R.id.rl_back})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }

    private void setChart(LineChart chart, int color) {
//        lc_chart1.setOnChartValueSelectedListener(getActivity());

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        // x-axis limit line
        XAxis xAxis = chart.getXAxis();
        for (int i=1; i<6; i++) {
            LimitLine llXAxis = new LimitLine(5f*i, "");
            llXAxis.setLineWidth(0.6f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLineColor(getResources().getColor(R.color.c_cecece));

            xAxis.addLimitLine(llXAxis);
        }


        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        chart.getAxisRight().setEnabled(false);

        // add data
        setData(chart, 31, 100, color);

        chart.animateX(2500);

        l.setForm(Legend.LegendForm.LINE);
    }


    private void setData(LineChart chart, int count, float range, int color) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setColor(color);
            set1.setCircleColor(color);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormSize(0.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = null;
                if (color == Color.RED) {
                    drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                } else if(color == Color.BLUE) {
                    drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                }
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart.setData(data);
        }
    }
}
