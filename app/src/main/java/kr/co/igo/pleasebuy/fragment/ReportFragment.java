package kr.co.igo.pleasebuy.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necistudio.vigerpdf.VigerPDF;
import com.necistudio.vigerpdf.adapter.VigerAdapter;
import com.necistudio.vigerpdf.manage.OnResultListener;
import com.necistudio.vigerpdf.utils.ViewPagerZoomHorizontal;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.FragmentName;

/**
 * Created by Back on 2016-09-29.
 */
public class ReportFragment extends BaseFragment {
    @Bind(R.id.tv_mon)      TextView tv_mon;
    @Bind(R.id.viewPager)   ViewPagerZoomHorizontal viewPager;

    private ArrayList<Bitmap> itemData;
    private VigerAdapter adapter;
    private VigerPDF vigerPDF;

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
                break;
            case R.id.rl_right:
                break;
            case R.id.tv_mon:
                break;
        }
    }

    private void init(){
        vigerPDF = new VigerPDF(getContext());

        itemData = new ArrayList<>();
        adapter = new VigerAdapter(getContext(), itemData);
        viewPager.setAdapter(adapter);
    }

    private void getData(){


        itemData.clear();
        adapter.notifyDataSetChanged();

        fromNetwork("http://www.pdf995.com/samples/pdf.pdf");
    }

    private void fromNetwork(String endpoint) {
        itemData.clear();
        adapter.notifyDataSetChanged();
        vigerPDF.cancle();
        vigerPDF.initFromNetwork(endpoint, new OnResultListener() {
            @Override
            public void resultData(Bitmap data) {
                Log.e("data", "run");
                itemData.add(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void progressData(int progress) {
                Log.e("data", "" + progress);
                // TODO: 2017-11-06 프로그래스바 처리 필요
            }

            @Override
            public void failed(Throwable t) {
            }
        });
    }
}
