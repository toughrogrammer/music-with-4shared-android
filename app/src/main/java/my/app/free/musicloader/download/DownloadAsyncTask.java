package my.app.free.musicloader.download;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.search.ResultAdapter;
import my.app.free.musicloader.search.SearchResultItem;

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
        return null;
    }
}
