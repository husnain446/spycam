package com.byteshaft.wrecspycam;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends ActionBarActivity implements MaterialTabListener {
    private ViewPager pager;
    MaterialTabHost tabHost;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E90FF")));
        setContentView(R.layout.activity_main);
        res = this.getResources();
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // swipe to change tab
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
    }


    @Override
    public void onTabReselected(MaterialTab tab) {
    }


    @Override
    public void onTabUnselected(MaterialTab tab) {
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            Fragment result = new Fragment();
            switch (num) {
                case 0:
                    result = new VideoPlayerFragment();
                    break;
                case 1:
                    result = new SoundRecorderFragment();
                    break;
                case 2:
                    result = new ImagesFragment();
            }
            return result;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "tab 1";
                case 1:
                    return "tab 2";
                case 2:
                    return "tab 3";
                default:
                    return null;
            }
        }
    }


    private Drawable getIcon(int position) {
        switch (position) {
            case 0:
                return res.getDrawable(R.drawable.video_recorder);
            case 1:
                return res.getDrawable(R.drawable.sound_recorder);
            case 2:
                return res.getDrawable(R.drawable.images);
        }
        return null;
    }
}