package my.app.free.musicloader.download;

import android.os.AsyncTask;

import my.app.free.musicloader.Bot4Shared;

/**
 * Created by loki on 2014. 5. 19..
 */
public class DownloadAsyncTask extends AsyncTask<Void, Integer, Void> {

    private Bot4Shared _bot;
    private String _savePath;
    private DownloadListItem _item;
    private OnListItemProgressUpdate _listener;
    private int _prevPercent;

    public DownloadAsyncTask(Bot4Shared bot, String savePath, DownloadListItem item, OnListItemProgressUpdate listener) {
        _bot = bot;
        _savePath = savePath;
        _item = item;
        _listener = listener;
    }

    @Override
    protected Void doInBackground(Void... args) {
        // 일단 다운로드 받고
        _bot.DownloadPreview(_item._music._link,
                _savePath,
                this);
        // 서버에게 다운 받은 음악을 알려준다
        _bot.Vote(_item._music);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        _listener.OnProgressUpdate(_item, _prevPercent);
    }

    public void update(float ratio) {
        int percent = (int) (ratio * 100);
        if (_prevPercent < percent) {
            _prevPercent = percent;
            publishProgress(percent);
        }
    }
}
