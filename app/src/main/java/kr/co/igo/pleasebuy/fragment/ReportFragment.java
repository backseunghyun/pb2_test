package kr.co.igo.pleasebuy.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.loopj.android.http.RequestParams;
import com.necistudio.vigerpdf.VigerPDF;
import com.necistudio.vigerpdf.adapter.VigerAdapter;
import com.necistudio.vigerpdf.manage.OnResultListener;
import com.necistudio.vigerpdf.utils.ViewPagerZoomHorizontal;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
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
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.model.MonthlyReport;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.popup.CustomProgressDialog;
import kr.co.igo.pleasebuy.popup.CustomYearMonthPickerPopup;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.FragmentName;

/**
 * Created by Back on 2016-09-29.
 */
public class ReportFragment extends BaseFragment {
    @Bind(R.id.tv_mon)      TextView tv_mon;
    @Bind(R.id.viewPager)   ViewPagerZoomHorizontal viewPager;
    @Bind(R.id.rl_left)     RelativeLayout rl_left;
    @Bind(R.id.rl_right)    RelativeLayout rl_right;
    @Bind(R.id.tv_page)     TextView tv_page;

    private ArrayList<Bitmap> itemData;
    private VigerAdapter adapter;
    private VigerPDF vigerPDF;

    private List<MonthlyReport> mList = new ArrayList<MonthlyReport>();

    private Date mDateMonth;

    private CustomProgressDialog progress  = null;

    public ReportFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, view);
        init();
        getData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.REPORT.tag());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vigerPDF != null) vigerPDF.cancle();
    }

    @OnClick({R.id.rl_left, R.id.rl_right, R.id.tv_mon})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_left:
                moveMonth(-1);
                loadPdf();
                break;
            case R.id.rl_right:
                moveMonth(1);
                loadPdf();
                break;
            case R.id.tv_mon:
                showSelectMonth();
                break;
        }
    }

    private void init(){
        progress = new CustomProgressDialog(getActivity());
        progress.setCancelable(false);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sSdf = new SimpleDateFormat("yyyy.MM");

        tv_mon.setText(sSdf.format(date));

        vigerPDF = new VigerPDF(getContext());

        itemData = new ArrayList<>();

    }

    private void getData() {
        RequestParams param = new RequestParams();

        APIManager.getInstance().callAPI(APIUrl.MONTHLY_REPORT, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("monthlyReportList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                MonthlyReport item = new MonthlyReport();
                                item.setYearMonth(obj.optString("yearMonth"));
                                item.setUrl(ApplicationData.getImgPrefix() + obj.optString("url"));

                                mList.add(item);
                            }
                        }

                        loadPdf();
                    }
                } catch (JSONException ignored) {
                } finally {
                }
            }
        });
    }



    private void loadPdf(){

        String mon = tv_mon.getText().toString().replace(".", "-");
        String url = "";

        for(int i=0; i< mList.size(); i++) {
            if (mList.get(i).getYearMonth().equals(mon)) {
                url = mList.get(i).getUrl();
                break;
            }
        }

        if(url.equals("")) {
            showError("등록된 레포트가 없습니다.");
        } else {
            fromNetwork(url);
        }
    }

    private void fromNetwork(String endpoint) {
        itemData.clear();
        vigerPDF.cancle();
        progress.show();
        progress.setProgress(0);
        vigerPDF.initFromNetwork(endpoint, new OnResultListener() {
            @Override
            public void resultData(Bitmap data) {
                itemData.add(data);
                adapter = new VigerAdapter(getContext(), itemData);
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        tv_page.setText(position+1 + "/" + itemData.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tv_page.setText(1 + "/" + itemData.size());
                progress.dismiss();
            }

            @Override
            public void progressData(int pos) {
                progress.setProgress(pos);
            }

            @Override
            public void failed(Throwable t) {
                progress.dismiss();
            }
        });


    }

    private void moveMonth(int i) {
        try {
            mDateMonth = new SimpleDateFormat("yyyy.MM").parse(tv_mon.getText().toString());
            Calendar preCal = Calendar.getInstance();

            preCal.setTime(mDateMonth);
            preCal.add(Calendar.MONTH, i);
            mDateMonth = preCal.getTime();

            DateFormat format = new SimpleDateFormat("yyyy.MM");
            tv_mon.setText(format.format(mDateMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    private void showSelectMonth(){
        final CustomYearMonthPickerPopup popup = new CustomYearMonthPickerPopup(getActivity(),  tv_mon.getText().toString().substring(0, 4), tv_mon.getText().toString().substring(5, 7));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(popup.getResult().equals("ok")) {
                    String fromDate = popup.getResultDate();
                    tv_mon.setText(fromDate.substring(0, 4) + "." + fromDate.substring(4, 6));
                    loadPdf();
                }
            }
        });
        popup.show();
    }
}
