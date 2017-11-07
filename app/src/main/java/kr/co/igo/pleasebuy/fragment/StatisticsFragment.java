package kr.co.igo.pleasebuy.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        lc_chart1.setDragEnabled(true);
        lc_chart1.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lc_chart1.setPinchZoom(false);

        Legend l = lc_chart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
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
    }

    private void getData(){

    }

}
