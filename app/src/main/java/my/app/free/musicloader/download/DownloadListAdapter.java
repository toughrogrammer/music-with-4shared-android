package my.app.free.musicloader.download;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.ModelMusic;
import my.app.free.musicloader.R;
import my.app.free.musicloader.download.musicplayer.MusicPlayer;

/**
 * Created by loki on 2014. 5. 21..
 */
public class DownloadListAdapter extends ArrayAdapter<DownloadListItem> {

    private final String TAG = "DownloadListAdapter";

    private Context _context;
    private ArrayList<DownloadListItem> _items;
    private MusicPlayer _musicPlayer;

    public DownloadListAdapter(Context context, int resource, ArrayList<DownloadListItem> objects, MusicPlayer player) {
        super(context, resource, objects);

        _context = context;
        _items = objects;
        _musicPlayer = player;
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
            if ((int) (item._ratio * 100) == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            ImageButton playBtn = (ImageButton) view.findViewById(R.id.list_item_download_btn_play);
            playBtn.setTag(item._music);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelMusic music = (ModelMusic) view.getTag();
                    if( _musicPlayer.GetCurrentPlaying()._title == music._title ) {
                        _musicPlayer.Pause();
                    } else {
                        _musicPlayer.Play(music);
                    }
                }
            });

            ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.list_item_download_btn_delete);
            deleteBtn.setTag(position);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setMessage("정말 지우시겠습니까?")
                            .setCancelable(true)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int pos = (Integer) view.getTag();
                                    DownloadListItem item = getItem(pos);

                                    String path = Bot4Shared.GeneratePath(item._music._title);
                                    File file = new File(path);
                                    if (file != null) {
                                        file.delete();
                                        remove(item);
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton("아니오", null);
                    builder.create().show();
                }
            });
        }

        return view;
    }

    @Override
    public void add(DownloadListItem object) {
        super.add(object);
        Log.d("DownloadListAdapter", "add music");
        _musicPlayer.AddMusic(object._music);
    }

    @Override
    public void remove(DownloadListItem object) {
        super.remove(object);
        _musicPlayer.RemoveMusic(object._music);
    }
}
