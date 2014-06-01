package my.app.free.musicloader;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.TitlePageIndicator;

import java.util.Locale;

import my.app.free.musicloader.chart.FragmentChart;
import my.app.free.musicloader.download.FragmentDownload;
import my.app.free.musicloader.download.OnNewItemDownload;
import my.app.free.musicloader.search.FragmentSearch;
import my.app.free.musicloader.search.SearchResultItem;


public class MainActivity extends Activity implements OnNewItemDownload {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    Bot4Shared _bot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent i = getIntent();
//        _bot = (Bot4Shared) i.getSerializableExtra("bot");

        // 원래라면 아이디 패스워드 제대로 넣어야 되는데, 현재 아이디 필요 없이 다운로드를 받도록 하고 있어서 안 넣어도 된다.
        _bot = new Bot4Shared("", "");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Bind the title indicator to the adapter
        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
        titleIndicator.setViewPager(mViewPager);
        titleIndicator.setTextColor(0xff000000);
        titleIndicator.setSelectedColor(0xff000000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnAdd(SearchResultItem item) {
        FragmentDownload downFrag = (FragmentDownload) mSectionsPagerAdapter.getItem(SectionsPagerAdapter.FRAGMENT_DOWNLOAD);
        downFrag.ReceiveNewItem(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public static final int NUM_OF_FRAGMENT = 3;
        public static final int FRAGMENT_CHART = 0;
        public static final int FRAGMENT_SEARCH = 1;
        public static final int FRAGMENT_DOWNLOAD = 2;

        private SparseArray<Fragment> _fragmentArray;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            _fragmentArray = new SparseArray<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = _fragmentArray.get(position);
            if (frag != null)
                return frag;

            switch (position) {
                case FRAGMENT_CHART:
                    frag = new FragmentChart(_bot);
                    break;
                case FRAGMENT_SEARCH:
                    frag = new FragmentSearch(_bot);
                    break;
                case FRAGMENT_DOWNLOAD:
                    frag = new FragmentDownload(_bot);
                    break;
            }
            _fragmentArray.put(position, frag);

            return frag;
        }

        @Override
        public int getCount() {
            return NUM_OF_FRAGMENT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case FRAGMENT_CHART:
                    return getString(R.string.title_section1).toUpperCase(l);
                case FRAGMENT_SEARCH:
                    return getString(R.string.title_section2).toUpperCase(l);
                case FRAGMENT_DOWNLOAD:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
