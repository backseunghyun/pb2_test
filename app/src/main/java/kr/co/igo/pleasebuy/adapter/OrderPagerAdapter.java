package kr.co.igo.pleasebuy.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Product;

/**
 * Created by baekseunghyun on 21/10/2017.
 */

public class OrderPagerAdapter extends PagerAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<String> mDataList;
    private String m;
    private boolean isList;

    private OrderListAdapter mListAdapter;
    private OrderGridAdapter mGridAdapter;
    private List<Product> mList = new ArrayList<Product>();
    private LinearLayoutManager mLayoutManager;

    public OrderPagerAdapter(Activity context, List<String> dataList, List<Product> list) {
        this.activity = context;
        this.layoutInflater = LayoutInflater.from(context);
        mDataList = dataList;
        this.mList = list;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = null;
        RecyclerView rv_list;
        if (isList) {
            v = layoutInflater.inflate(R.layout.viewpager_list, null);
            rv_list = (RecyclerView)v.findViewById(R.id.rv_list);
            mLayoutManager = new LinearLayoutManager(activity);
            rv_list.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_list.setLayoutManager(mLayoutManager);
        } else {
            v = layoutInflater.inflate(R.layout.viewpager_gird, null);
            rv_list = (RecyclerView)v.findViewById(R.id.rv_list);

            mLayoutManager = new GridLayoutManager(activity, 3);
            mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            rv_list.setLayoutManager(mLayoutManager);
        }

        m = mDataList.get(position);


        List<Product> tList = new ArrayList<Product>();

        tList.clear();
        for(int i=0; i<mList.size(); i++) {
            Product item = mList.get(i);

            if (mDataList.get(position).equals("전체") || item.getCategoryValue().equals(mDataList.get(position))) {
                tList.add(item);
            }
        }

        if (isList) {
            mListAdapter = new OrderListAdapter(activity, tList);
            rv_list.setAdapter(mListAdapter);
        } else {
            mGridAdapter = new OrderGridAdapter(activity, tList);
            rv_list.setAdapter(mGridAdapter);
        }


        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }


    public void setIsList(boolean b) {
        isList = b;
    }
}
