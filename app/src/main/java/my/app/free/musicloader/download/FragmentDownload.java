package my.app.free.musicloader.download;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.ModelMusic;
import my.app.free.musicloader.R;
import my.app.free.musicloader.download.musicplayer.MusicPlayer;
import my.app.free.musicloader.download.musicplayer.PlayOption;

/**
 * Created by loki on 2014. 5. 21..
 */
public class FragmentDownload extends Fragment implements AdapterView.OnItemClickListener, OnListItemProgressUpdate, View.OnClickListener {

    DownloadListAdapter _adapter;
    Bot4Shared _bot;
    private String TAG = "FragmentDownload";
    private ListView _downloadList;
    private MusicPlayer _musicPlayer;

    public FragmentDownload(Bot4Shared bot) {
        super();

        _bot = bot;
        _musicPlayer = new MusicPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        _adapter = new DownloadListAdapter(getActivity(),
                R.layout.list_item_download,
                new ArrayList<DownloadListItem>(),
                _musicPlayer);

        _downloadList = (ListView) view.findViewById(R.id.fragment_download_list);
        _downloadList.setOnItemClickListener(this);
        _downloadList.setAdapter(_adapter);

        File dir = new File(Environment.getExternalStorageDirectory() + Bot4Shared.PATH);
        if (dir != null) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String name = file.getName();

                ModelMusic music = new ModelMusic(name, "", "");
                DownloadListItem item = new DownloadListItem(music, _adapter.getCount());
                item._ratio = 1;
                _adapter.add(item);
            }
            _adapter.notifyDataSetChanged();
        }

        _musicPlayer.RefreshOrder();


        Button playBtn = (Button) view.findViewById(R.id.fragment_download_play);
        playBtn.setOnClickListener(this);

        Button pauseBtn = (Button) view.findViewById(R.id.fragment_download_pause);
        pauseBtn.setOnClickListener(this);

        Button prevBtn = (Button) view.findViewById(R.id.fragment_download_prev);
        prevBtn.setOnClickListener(this);

        Button nextBtn = (Button) view.findViewById(R.id.fragment_download_next);
        nextBtn.setOnClickListener(this);

        ToggleButton playOptionToggleBtn = (ToggleButton) view.findViewById(R.id.fragment_download_israndom);
        playOptionToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    _musicPlayer.SetPlayOption(PlayOption.Random);
                } else {
                    _musicPlayer.SetPlayOption(PlayOption.InOrder);
                }
                _musicPlayer.RefreshOrder();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DownloadListItem row = _adapter.getItem(i);
//        String path = Bot4Shared.GeneratePath(row._music._title);
    }

    public void ReceiveNewItem(ModelMusic music) {
        DownloadListItem row = new DownloadListItem(music, _adapter.getCount());

        String path = Bot4Shared.GeneratePath(row._music._title);
        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(_bot, music, path, row, this);
        downloadAsyncTask.execute();

        _adapter.add(row);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void OnProgressUpdate(int position, int progress) {
        View view = _downloadList.getChildAt(position);
        if (view != null) {
            ProgressBar bar = (ProgressBar) view.findViewById(R.id.list_item_download_progressBar);

            if (progress == 100) {
                bar.setVisibility(View.INVISIBLE);
            }

            bar.setProgress(progress);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_download_play:
                _musicPlayer.Play();
                break;
            case R.id.fragment_download_pause:
                _musicPlayer.Pause();
                break;
            case R.id.fragment_download_prev:
                _musicPlayer.Prev();
                break;
            case R.id.fragment_download_next:
                _musicPlayer.Next();
                break;
        }
    }
}
