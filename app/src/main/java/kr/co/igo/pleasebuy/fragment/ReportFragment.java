package kr.co.igo.pleasebuy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.FavoriteAdapter;
import kr.co.igo.pleasebuy.model.Product;
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
public class ReportFragment extends BaseFragment implements DownloadFile.Listener {
    @Bind(R.id.tv_mon)      TextView tv_mon;
    @Bind(R.id.pdfViewPager) PDFViewPager pdfViewPager;

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
//        adapter.close();
    }

    @OnClick({R.id.ib_left, R.id.ib_right, R.id.tv_mon})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ib_left:
                break;
            case R.id.ib_right:
                break;
            case R.id.tv_mon:
                break;
        }
    }


    @Override
    public void onSuccess(String url, String destinationPath) {
//        adapter = new PDFPagerAdapter(this, "AdobeXMLFormsSamples.pdf");
//        remotePDFViewPager.setAdapter(adapter);
//        setContentView(remotePDFViewPager);
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    private void getData(){

        RemotePDFViewPager remotePDFViewPager =
                new RemotePDFViewPager(getActivity(), "http://partners.adobe.com/public/developer/en/xml/AdobeXMLFormsSamples.pdf", this);
    }
}
