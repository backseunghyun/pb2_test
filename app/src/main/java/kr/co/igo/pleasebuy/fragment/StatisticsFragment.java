package kr.co.igo.pleasebuy.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.FavoriteAdapter;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.popup.CalendarTwoPopup;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.ui.StatisticsDetailAllActivity;
import kr.co.igo.pleasebuy.ui.StatisticsDetailCategoryActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.FragmentName;

/**
 * Created by Back on 2016-09-29.
 */
public class StatisticsFragment extends BaseFragment {
    @Bind(R.id.tv_date)         TextView tv_date;
    @Bind(R.id.ib_settting)     ImageButton ib_settting;
    @Bind(R.id.gl_category)     GridLayout gl_category;
    @Bind(R.id.lc_chart1)       LineChart lc_chart1;
    @Bind(R.id.lc_chart2)       LineChart lc_chart2;
    @Bind(R.id.tv_category)     TextView tv_category;

    private List<String> mDataList = new ArrayList<>();

    private String fromDate;
    private String toDate;

    public StatisticsFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);

        setInit();
        setCategoryItem();
        setChart(lc_chart1);
        setChart(lc_chart2);
        return view;
    }

    private void setInit() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sSdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat eSdf = new SimpleDateFormat("yyyy-MM-dd");

        toDate = eSdf.format(date);
        fromDate = sSdf.format(date) + ".01";

        tv_date.setText(fromDate + " ~ " + toDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.GRAPH.tag());
        }
    }

    private void setChart(LineChart chart) {
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
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        chart.getAxisRight().setEnabled(false);

        // add data
        setData(chart, 31, 100);

        chart.animateX(2500);

        l.setForm(Legend.LegendForm.LINE);
    }


    private void setData(LineChart chart, int count, float range) {
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
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.RED);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormSize(0.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart.setData(data);
        }


        ArrayList<Entry> values2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values2.add(new Entry(i, val));
        }

        LineDataSet set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set2 = (LineDataSet)chart.getData().getDataSetByIndex(0);
            set2.setValues(values2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set2 = new LineDataSet(values2, "");
            set2.setDrawIcons(false);
            set2.setColor(Color.BLUE);
            set2.setCircleColor(Color.BLUE);
            set2.setLineWidth(1f);
            set2.setCircleRadius(3f);
            set2.setDrawCircleHole(true);
            set2.setValueTextSize(0f);
            set2.setDrawFilled(true);
            set2.setFormSize(0.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                set2.setFillDrawable(drawable);
            } else {
                set2.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets2 = new ArrayList<ILineDataSet>();
            dataSets2.add(set2);

            LineData data2 = new LineData(dataSets2);

            chart.setData(data2);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.ib_settting, R.id.lc_chart1, R.id.lc_chart2})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.ib_settting:
                showCalendar();
                break;
            case R.id.lc_chart1:
                Intent intent1 = new Intent(getActivity(), StatisticsDetailAllActivity.class);
                intent1.putExtra("date", tv_date.getText());
                startActivity(intent1);
                break;
            case R.id.lc_chart2:
                Intent intent2 = new Intent(getActivity(), StatisticsDetailCategoryActivity.class);
                intent2.putExtra("date", tv_date.getText());
                intent2.putExtra("name", tv_category.getText());
                startActivity(intent2);
                break;
        }
    }


    private void showCalendar(){
        final CalendarTwoPopup calendarPopup = new CalendarTwoPopup(getActivity());
        calendarPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (calendarPopup.getBtn_result().equals("ok")) {
                    fromDate = calendarPopup.getStartDate();
                    toDate = calendarPopup.getEndDate();
                    tv_date.setText(fromDate + " ~ " + toDate);
                    getData();
                }
            }
        });
        calendarPopup.show();
    }


    private void setCategoryItem() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.USER_CATEGORY_LIST, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        gl_category.removeAllViews();
                        mDataList.clear();

                        JSONArray jsonArray = response.getJSONArray("categoryList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                View v = getActivity().getLayoutInflater().inflate(R.layout.item_statistics_category, null);
                                LinearLayout ll_item = (LinearLayout) v.findViewById(R.id.ll_item);
                                TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
                                ImageView iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
                                TextView tv_percent = (TextView) v.findViewById(R.id.tv_percent);

                                String item = obj.optString("value");
                                mDataList.add(item);

                                tv_title.setText(item);
                                tv_percent.setText(i + "%");
                                iv_icon.setActivated(i%2 ==0);
                                tv_percent.setActivated(i%2 ==0);

                                ll_item.setTag(i);
                                ll_item.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        selectedCategory((Integer) v.getTag());
                                    }
                                });
                                gl_category.addView(v);
                            }
                        }

                        selectedCategory(0);
                    }
                } catch (JSONException ignored) {
                } finally {
                }
            }
        });

    }

    private void selectedCategoryClear(){
        for (int i=0; i<gl_category.getChildCount(); i++) {
            gl_category.getChildAt(i).setSelected(false);
        }
    }

    private void selectedCategory(int pos) {
        selectedCategoryClear();
        gl_category.getChildAt(pos).setSelected(true);
        tv_category.setText(mDataList.get(pos));
    }

    private void getData(){

    }

}
