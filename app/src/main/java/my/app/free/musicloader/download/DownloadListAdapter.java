package my.app.free.musicloader.download;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
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
            title.setText(item._music._title);

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.list_item_download_progressBar);
            progressBar.setMax(100);
            progressBar.setProgress((int) (item._ratio * 100));
            if( (int) (item._ratio * 100) == 100 ) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            ImageButton playBtn = (ImageButton) view.findViewById(R.id.list_item_download_btn_play);
            playBtn.setTag(item._music._title);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = (String) view.getTag();
                    String path = Bot4Shared.GeneratePath(tag);

                    Log.e(TAG, path);

                    MediaPlayer mp = new MediaPlayer();
                    try {
                        FileInputStream fis = new FileInputStream(path);
                        mp.setDataSource(fis.getFD());
                        mp.prepare();
                        mp.start();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.list_item_download_btn_delete);
            deleteBtn.setTag(position);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();
                    DownloadListItem item = getItem(pos);

                    String path = Bot4Shared.GeneratePath(item._music._title);

                    File file = new File(path);
                    if( file != null ) {
                        file.delete();
                        remove(item);
                        notifyDataSetChanged();
                    }
                }
            });
        }

        return view;
    }
}
