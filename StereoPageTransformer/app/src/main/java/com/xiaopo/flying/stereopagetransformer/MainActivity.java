package com.xiaopo.flying.stereopagetransformer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int[] photoResIds = { R.drawable.p4, R.drawable.p2, R.drawable.p3, R.drawable.p1 };
    private OverViewPager pager;
    private PhotoPageAdapter adapter;
    private float deviceWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        pager = (OverViewPager) findViewById(R.id.view_pager);
        adapter = new PhotoPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(false, new StereoPagerTransformer(deviceWidth));
        pager.setOnOverScrollListener(new OverViewPager.OnOverScrollListener() {
            @Override
            public void onOverScroll(float overScroll) {
                Log.d(TAG, "onOverScroll: pager.childCount --> " + pager.getChildCount());
                Log.d(TAG, "onOverScroll: overScroll --> " + overScroll);
            }
        });
    }

    public class PhotoPageAdapter extends FragmentStatePagerAdapter {

        public PhotoPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(photoResIds[position]);
        }

        @Override
        public int getCount() {
            return photoResIds.length;
        }
    }
}
