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
import com.necistudio.vigerpdf.VigerPDF;
import com.necistudio.vigerpdf.adapter.VigerAdapter;
import com.necistudio.vigerpdf.manage.OnResultListener;
import com.necistudio.vigerpdf.utils.ViewPagerZoomHorizontal;


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
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Favorite;
import kr.co.igo.pleasebuy.popup.CustomProgressDialog;
import kr.co.igo.pleasebuy.popup.CustomYearMonthPickerPopup;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.ui.MainActivity;
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

    private List<String> mList = new ArrayList<String>();

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
            getData();
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
                getData();
                break;
            case R.id.rl_right:
                moveMonth(1);
                getData();
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


        mList.add("http://www.africau.edu/images/default/sample.pdf");
        mList.add("http://che.org.il/wp-content/uploads/2016/12/pdf-sample.pdf");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("http://che.org.il/wp-content/uploads/2016/12/pdf-sample.pdf");
        mList.add("http://www.pdf995.com/samples/pdf.pdf");
//        mList.add("http://bbaeggun100.vps.phps.kr:8080/pleasebuy2/static/report/1.pdf");
//        mList.add("http://bbaeggun100.vps.phps.kr:8080/pleasebuy2/static/report/2.pdf");
        mList.add("http://www.pdf995.com/samples/pdf.pdf");
        mList.add("https://files.nc.gov/ncdhhs/documents/files/Sample-Form-1095B-2016-01-21.pdf");
        mList.add("https://www.collegeofsanmateo.edu/library/docs/MLAWorksCited7.pdf");
    }

    private void getData(){
        int i = 0;
        switch (tv_mon.getText().toString().substring(5, 7)){
            case "01":  i =0;   break;
            case "02":  i =1;   break;
            case "03":  i =2;   break;
            case "04":  i =3;   break;
            case "05":  i =4;   break;
            case "06":  i =5;   break;
            case "07":  i =6;   break;
            case "08":  i =7;   break;
            case "09":  i =8;   break;
            case "10":  i =9;   break;
            case "11":  i =10;   break;
            case "12":  i =11;   break;
        }

        fromNetwork(mList.get(i));
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
                    getData();
                }
            }
        });
        popup.show();
    }
}
