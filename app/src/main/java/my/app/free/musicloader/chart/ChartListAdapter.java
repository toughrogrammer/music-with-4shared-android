package my.app.free.musicloader.chart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import my.app.free.musicloader.R;

/**
 * Created by loki on 2014. 5. 21..
 */
public class ChartListAdapter extends ArrayAdapter<ChartItem> {

    private final String TAG = "ChartListAdapter";

    private Context _context;

    public ChartListAdapter(Context context, int resource, ArrayList<ChartItem> objects) {
        super(context, resource, objects);

        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_chart, parent, false);
        }

        ChartItem item = this.getItem(position);
        if (item != null) {
            TextView rank = (TextView) view.findViewById(R.id.list_item_chart_rank);
            rank.setText((position + 1) + "");
            TextView title = (TextView) view.findViewById(R.id.list_item_chart_title);
            title.setText(item._music._title);
        }

        return view;
    }
}
