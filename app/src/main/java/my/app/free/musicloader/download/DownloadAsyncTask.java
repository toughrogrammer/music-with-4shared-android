package my.app.free.musicloader.download;

import android.os.AsyncTask;

import my.app.free.musicloader.Bot4Shared;

/**
 * Created by loki on 2014. 5. 19..
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, Void> {

    Bot4Shared _bot;

    public DownloadAsyncTask(Bot4Shared bot) {
        _bot = bot;
    }

    @Override
    protected Void doInBackground(String... args) {
        for (int i = 0; i < args.length; i += 2) {
            String link = args[i];
            String path = args[i + 1];

            _bot.DownloadPreview(link, path);
        }

        return null;
    }
}
