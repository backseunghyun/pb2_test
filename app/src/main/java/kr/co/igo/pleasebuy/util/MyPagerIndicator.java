package kr.co.igo.pleasebuy.util;

import android.content.Context;
import android.view.View;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * Created by baekseunghyun on 21/10/2017.
 */

public class MyPagerIndicator extends View implements IPagerIndicator {

    public MyPagerIndicator(Context context) {
        super(context);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
    }
}
