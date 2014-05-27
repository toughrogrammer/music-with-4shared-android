package my.app.free.musicloader.download;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.R;
import my.app.free.musicloader.search.SearchResultItem;

/**
 * Created by loki on 2014. 5. 21..
 */
public class FragmentDownload extends Fragment implements AdapterView.OnItemClickListener, OnListItemProgressUpdate {

    DownloadListAdapter _adapter;
    Bot4Shared _bot;
    private String TAG = "FragmentDownload";
    private ListView _downloadList;

    public FragmentDownload(Bot4Shared bot) {
        super();

        _bot = bot;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        _adapter = new DownloadListAdapter(getActivity(),
                R.layout.list_item_download,
                new ArrayList<DownloadListItem>());

        _downloadList = (ListView) view.findViewById(R.id.fragment_download_list);
        _downloadList.setOnItemClickListener(this);
        _downloadList.setAdapter(_adapter);

        View v = _downloadList.getChildAt(0);
        if (v != null) {
            ProgressBar bar = (ProgressBar) view.findViewById(R.id.list_item_download_progressBar);
            bar.setProgress(50);
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DownloadListItem row = _adapter.getItem(i);
//        String path = Bot4Shared.GeneratePath(row._music._title);
    }

    public void ReceiveNewItem(SearchResultItem item) {
        DownloadListItem row = new DownloadListItem(item._music, _adapter.getCount());

        String path = Bot4Shared.GeneratePath(row._music._title);
        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(_bot, item._music, path, row, this);
        downloadAsyncTask.execute();

        _adapter.add(row);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void OnProgressUpdate(int position, int progress) {
        View view = _downloadList.getChildAt(position);
        if (view != null) {
            ProgressBar bar = (ProgressBar) view.findViewById(R.id.list_item_download_progressBar);
            bar.setProgress(progress);
        }
    }
}
