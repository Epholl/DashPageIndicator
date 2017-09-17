package eu.inloop.dashpageindicatorsampleapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.inloop.dashpageindicatorsampleapp.R;

/**
 * Created by Tomáš Isteník on 17/09/2017.
 */

public class MainActivityAdapter extends PagerAdapter {

    private static final int[] COLORS = {
            R.color.viewPager1,
            R.color.viewPager2,
            R.color.viewPager3,
            R.color.viewPager4,
            R.color.viewPager5
    };

    private Context mContext;

    public MainActivityAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_empty, container, false);
        layout.setBackgroundColor(mContext.getResources().getColor(COLORS[position]));
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return COLORS.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
