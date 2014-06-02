package my.app.free.musicloader.chart;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.R;
import my.app.free.musicloader.download.OnNewItemDownload;
import my.app.free.musicloader.search.SearchResultItem;

/**
 * Created by loki on 2014. 5. 21..
 */
public class FragmentChart extends Fragment implements AdapterView.OnItemClickListener {

    private final String TAG = "FragmentChart";

    private PullToRefreshListView _chartList;
    private ChartListAdapter _adapter;
    private Bot4Shared _bot;
    private OnNewItemDownload _onNewItemStart;

    public FragmentChart(Bot4Shared bot) {
        super();

        _bot = bot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        _adapter = new ChartListAdapter(getActivity(),
                R.layout.list_item_chart,
                new ArrayList<ChartItem>());

        _chartList = (PullToRefreshListView) view.findViewById(R.id.fragment_chart_list);
        _chartList.setOnItemClickListener(this);
        _chartList.setAdapter(_adapter);
        _chartList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                ChartAsyncTask task = new ChartAsyncTask(getActivity(), _bot, _adapter, _chartList);
                task.execute();
            }
        });
        _chartList.setRefreshing();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _onNewItemStart = (OnNewItemDownload) activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ChartItem row = _adapter.getItem(i);
        _onNewItemStart.OnAdd(row._music);
    }
}
