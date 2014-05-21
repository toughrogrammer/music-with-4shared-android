package my.app.free.musicloader.download;

import android.os.AsyncTask;

import my.app.free.musicloader.Bot4Shared;

/**
 * Created by loki on 2014. 5. 19..
 */
public class DownloadAsyncTask extends AsyncTask<Void, Integer, Void> {

    private Bot4Shared _bot;
    private String _link;
    private String _path;
    private DownloadListItem _item;
    private OnListItemProgressUpdate _listener;
    private int _prevPercent;

    public DownloadAsyncTask(Bot4Shared bot, String link, String path, DownloadListItem item, OnListItemProgressUpdate listener) {
        _bot = bot;
        _link = link;
        _path = path;
        _item = item;
        _listener = listener;
    }

    @Override
    protected Void doInBackground(Void... args) {
        _bot.DownloadPreview(_link, _path, this);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        _listener.OnProgressUpdate(_item._index, _prevPercent);
    }

    public void update(float ratio) {
        int percent = (int) (ratio * 100);
        if (_prevPercent < percent) {
            _prevPercent = percent;
            publishProgress(percent);
        }
    }
}
