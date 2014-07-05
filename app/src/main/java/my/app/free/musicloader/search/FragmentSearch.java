package my.app.free.musicloader.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.R;
import my.app.free.musicloader.download.OnNewItemDownload;

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
    private OnNewItemDownload _onNewItemStart;

    public FragmentSearch(Bot4Shared bot) {
        super();

        _bot = bot;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _onNewItemStart = (OnNewItemDownload) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        _editQuery = (EditText) view.findViewById(R.id.fragment_search_edit_query);
        _editQuery.setText("give love");
        _editQuery.setMaxLines(1);

        _searchBtn = (Button) view.findViewById(R.id.fragment_search_search);
        _searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _adapter.clear();

                Toast.makeText(getActivity(), "검색 시작", Toast.LENGTH_SHORT).show();

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
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("정말 다운로드 하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SearchResultItem row = _adapter.getItem(i);
                        _onNewItemStart.OnAdd(row._music);
                    }
                })
                .setNegativeButton("아니오", null);
        builder.create().show();
    }
}
