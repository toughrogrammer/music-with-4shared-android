package my.app.free.musicloader;

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

        _searchBtn = (Button) view.findViewById(R.id.fragment_search_search);
        _searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        JSONObject json = _bot.Search(_editQuery.getText().toString());
                        try {
                            JSONArray array = json.getJSONArray("files");

                            for( int i = 0; i < array.length(); i ++ ) {
                                JSONObject data = array.getJSONObject(i);
                                Log.d("Result", data.getString("downloadPage"));

                                ResultItem item = new ResultItem(data.getString("downloadPage"));
                                _adapter.add(item);
                            }

                            _adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                };
                asyncTask.execute();
            }
        });

        ArrayList<ResultItem> dataList = new ArrayList<ResultItem>();
        dataList.add(new ResultItem("abc"));
        dataList.add(new ResultItem("def"));
        _adapter = new ResultAdapter(getActivity(), R.layout.list_item_search_result, dataList);
        _resultList = (ListView) view.findViewById(R.id.fragment_search_list_result);
        _adapter.notifyDataSetChanged();

        return view;
    }


    private class ResultItem {
        public String _title;

        public ResultItem(String title) {
            _title = title;
        }
    }


    private class ResultAdapter extends ArrayAdapter<ResultItem> {

        private Context _context;
        private ArrayList<ResultItem> _items;

        public ResultAdapter(Context context, int resource, ArrayList<ResultItem> objects) {
            super(context, resource, objects);

            _context = context;
            _items = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_search_result, parent, false);
            }

            ResultItem item = _items.get(position);
            if( item != null ) {
                TextView title = (TextView) view.findViewById(R.id.list_item_search_result_title);
                title.setText(item._title);
            }

            return view;
        }
    }

}
