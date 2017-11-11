package eu.inloop.dashpageindicatorsampleapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import eu.inloop.dashpageindicator.EasyPageIndicator;
import eu.inloop.dashpageindicatorsampleapp.R;
import eu.inloop.dashpageindicatorsampleapp.adapter.MainActivityAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MainActivityAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewPager = findViewById(R.id.view_pager);

        mPagerAdapter = new MainActivityAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        ((EasyPageIndicator)findViewById(R.id.page_indicator1)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator2)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator3)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator4)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator5)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator6)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator7)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator8)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator9)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator10)).setViewPager(mViewPager);
        ((EasyPageIndicator)findViewById(R.id.page_indicator11)).setViewPager(mViewPager);

    }
}
