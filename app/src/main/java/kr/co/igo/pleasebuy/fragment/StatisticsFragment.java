package kr.co.igo.pleasebuy.fragment;

import android.content.DialogInterface;
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
        setChartAll();
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

    private void setChartAll() {
//        lc_chart1.setOnChartValueSelectedListener(getActivity());

        lc_chart1.setDrawGridBackground(false);
        lc_chart1.getDescription().setEnabled(false);
        lc_chart1.setDrawBorders(false);

        lc_chart1.getAxisLeft().setEnabled(false);
        lc_chart1.getAxisRight().setDrawAxisLine(false);
        lc_chart1.getAxisRight().setDrawGridLines(false);
        lc_chart1.getXAxis().setDrawAxisLine(false);
        lc_chart1.getXAxis().setDrawGridLines(false);

        // enable touch gestures
        lc_chart1.setTouchEnabled(true);

        // enable scaling and dragging
        lc_chart1.setDragEnabled(false);
        lc_chart1.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lc_chart1.setPinchZoom(false);

        Legend l = lc_chart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "");
        llXAxis.setLineWidth(0.6f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
        llXAxis.setLineColor(getResources().getColor(R.color.c_cecece));

        XAxis xAxis = lc_chart1.getXAxis();
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.addLimitLine(llXAxis);

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


        YAxis leftAxis = lc_chart1.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lc_chart1.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(31, 100);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        lc_chart1.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
//        Legend l = lc_chart1.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }


    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
//            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.ic_noti)));
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (lc_chart1.getData() != null &&
                lc_chart1.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)lc_chart1.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lc_chart1.getData().notifyDataChanged();
            lc_chart1.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
//            set1 = new LineDataSet(values, "DataSet 1");
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.RED);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(0.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lc_chart1.setData(data);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.ib_settting})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.ib_settting:
                showCalendar();
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
