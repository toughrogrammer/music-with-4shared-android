package my.app.free.musicloader.search;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.R;

/**
 * Created by loki on 2014. 5. 18..
 */
public class FragmentSearch extends Fragment {

    private EditText _editQuery;
    private Button _searchBtn;
    private ListView _resultList;
    ResultAdapter _adapter;

    Bot4Shared _bot;

    public FragmentSearch(Bot4Shared bot) {
        super();

        _bot = bot;
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
                SearchAsyncTask task = new SearchAsyncTask(_bot, _adapter);
                task.execute(_editQuery.getText().toString());
            }
        });

        _adapter = new ResultAdapter(getActivity(),
                R.layout.list_item_search_result,
                new ArrayList<SearchResultItem>());
        _resultList = (ListView) view.findViewById(R.id.fragment_search_list_result);
        _resultList.setAdapter(_adapter);

        return view;
    }
}
