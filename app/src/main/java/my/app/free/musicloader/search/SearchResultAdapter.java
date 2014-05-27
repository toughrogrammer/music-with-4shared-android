package my.app.free.musicloader.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import my.app.free.musicloader.R;

/**
 * Created by loki on 2014. 5. 19..
 */
public class SearchResultAdapter extends ArrayAdapter<SearchResultItem> {

    private Context _context;
    private ArrayList<SearchResultItem> _items;

    public SearchResultAdapter(Context context, int resource, ArrayList<SearchResultItem> objects) {
        super(context, resource, objects);

        _context = context;
        _items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_search_result, parent, false);
        }

        SearchResultItem item = _items.get(position);
        if (item != null) {
            TextView title = (TextView) view.findViewById(R.id.list_item_search_result_title);
            title.setText(item._music._title);
        }

        return view;
    }

}