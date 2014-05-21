package my.app.free.musicloader.search;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.R;
import my.app.free.musicloader.download.DownloadAsyncTask;

/**
 * Created by loki on 2014. 5. 18..
 */
public class FragmentSearch extends Fragment implements AdapterView.OnItemClickListener {

    SearchResultAdapter _adapter;
    Bot4Shared _bot;
    private String TAG = "FragmentSearch";
    private EditText _editQuery;
    private Button _searchBtn;
    private ListView _resultList;

    // AsyncTask 한 번만 생성해서 다시 execute 하는 방식으로 했는데 이게 맞는건지는 모르겠음.
    // 그냥 매번 새로 Task 만들어도 문제는 없을거라 봄. (메모리 낭비 될까봐 이렇게 함.)
    private DownloadAsyncTask downloadAsyncTask;

    public FragmentSearch(Bot4Shared bot) {
        super();

        _bot = bot;
        downloadAsyncTask = new DownloadAsyncTask(_bot);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        _editQuery = (EditText) view.findViewById(R.id.fragment_search_edit_query);
        _editQuery.setText("love don't die");
        _editQuery.setMaxLines(1);

        _searchBtn = (Button) view.findViewById(R.id.fragment_search_search);
        _searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _adapter.clear();
                SearchAsyncTask task = new SearchAsyncTask(_bot, _adapter);
                task.execute(_editQuery.getText().toString());
            }
        });

        _adapter = new SearchResultAdapter(getActivity(),
                R.layout.list_item_search_result,
                new ArrayList<SearchResultItem>());
        _resultList = (ListView) view.findViewById(R.id.fragment_search_list_result);
        _resultList.setAdapter(_adapter);
        _resultList.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SearchResultItem row = _adapter.getItem(i);
        String path = Bot4Shared.GeneratePath(row._title);
        downloadAsyncTask.execute(row._link, path);
    }
}
