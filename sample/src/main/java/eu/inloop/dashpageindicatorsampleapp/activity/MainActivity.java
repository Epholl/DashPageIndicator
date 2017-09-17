package eu.inloop.dashpageindicatorsampleapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import eu.inloop.dashpageindicator.DashPageIndicator;
import eu.inloop.dashpageindicatorsampleapp.R;
import eu.inloop.dashpageindicatorsampleapp.adapter.MainActivityAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MainActivityAdapter mPagerAdapter;

    private DashPageIndicator mPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewPager = findViewById(R.id.view_pager);
        this.mPageIndicator = findViewById(R.id.page_indicator);

        mPagerAdapter = new MainActivityAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        mPageIndicator.setViewPager(mViewPager);
    }
}
