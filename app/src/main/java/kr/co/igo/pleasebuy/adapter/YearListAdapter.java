package kr.co.igo.pleasebuy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.igo.pleasebuy.R;


/**
 * Created by Back on 2016-12-21.
 */
public class YearListAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> years;

    public YearListAdapter(Context context, ArrayList<String> years) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.years = years;
    }

    @Override
    public int getCount() {
        return this.years.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = null;
        v = layoutInflater.inflate(R.layout.year_month_item, container, false);

        TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_date.setText(years.get(position));

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object view) {
        pager.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
