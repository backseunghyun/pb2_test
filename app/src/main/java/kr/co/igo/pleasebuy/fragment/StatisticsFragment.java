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
import kr.co.igo.pleasebuy.popup.CustomYearMonthPickerPopup;
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

        initChart(lc_chart1);
        initChart(lc_chart2);

        getData();

        return view;
    }

    private void setInit() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sSdf = new SimpleDateFormat("yyyy.MM");
        SimpleDateFormat eSdf = new SimpleDateFormat("yyyy.MM.dd");

//        toDate = eSdf.format(date);
//        fromDate = sSdf.format(date) + ".01";
//        tv_date.setText(fromDate + " ~ " + toDate);

        fromDate = sSdf.format(date);
        tv_date.setText(fromDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.GRAPH.tag());
        }
    }

    private void initChart(LineChart chart) {
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(true);
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

        // x-axis limit line
        XAxis xAxis = chart.getXAxis();
        for (int i=1; i<6; i++) {
            LimitLine llXAxis = new LimitLine(5f*i, "");
            llXAxis.setLineWidth(0.6f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLineColor(getResources().getColor(R.color.c_cecece));

            xAxis.addLimitLine(llXAxis);
        }

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);

        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        chart.getAxisRight().setEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        chart.animateX(2500);

        l.setForm(Legend.LegendForm.LINE);
    }

    private void setData(LineChart chart, ArrayList<Entry> values1, ArrayList<Entry> values2) {
//        setData(chart, values1, values2);

        LineDataSet set1;
        LineDataSet set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)chart.getData().getDataSetByIndex(0);
            set1.setValues(values1);

            set2 = (LineDataSet)chart.getData().getDataSetByIndex(1);
            set2.setValues(values2);


            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values1, "");
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
                Drawable drawable2 = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                set2.setFillDrawable(drawable2);
            } else {
                set2.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            LineData data = new LineData(dataSets);

            chart.setData(data);
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
        final CustomYearMonthPickerPopup popup = new CustomYearMonthPickerPopup(getActivity(),  tv_date.getText().toString().substring(0, 4), tv_date.getText().toString().substring(5, 7));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(popup.getResult().equals("ok")) {
                    String fromDate = popup.getResultDate();
                    tv_date.setText(fromDate.substring(0, 4) + "." + fromDate.substring(4, 6));

                    initChart(lc_chart1);
                    initChart(lc_chart2);

                    getData();
                }
            }
        });
        popup.show();
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
        ArrayList<Entry> values1 = new ArrayList<Entry>();
        ArrayList<Entry> values2 = new ArrayList<Entry>();
        for (int i = 1; i < 31; i++) {
            float val = (float) (Math.random() * 100);
            values1.add(new Entry(i, val));
        }

        for (int i = 1; i < 16; i++) {
            float val = (float) (Math.random() * 100);
            values2.add(new Entry(i, val));
        }

        setData(lc_chart1, values1, values2);

        setData(lc_chart2, values1, values2);
    }
}
