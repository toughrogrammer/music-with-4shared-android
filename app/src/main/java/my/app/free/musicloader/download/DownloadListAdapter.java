package my.app.free.musicloader.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import my.app.free.musicloader.R;

/**
 * Created by loki on 2014. 5. 21..
 */
public class DownloadListAdapter extends ArrayAdapter<DownloadListItem> {

    private final String TAG = "DownloadListAdapter";

    private Context _context;
    private ArrayList<DownloadListItem> _items;

    public DownloadListAdapter(Context context, int resource, ArrayList<DownloadListItem> objects) {
        super(context, resource, objects);

        _context = context;
        _items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_download, parent, false);
        }

        DownloadListItem item = _items.get(position);
        if (item != null) {
            TextView title = (TextView) view.findViewById(R.id.list_item_download_title);
            title.setText(item._title);

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.list_item_download_progressBar);
            progressBar.setMax(100);
            progressBar.setProgress((int) (item._ratio * 100));
        }

        return view;
    }
}
