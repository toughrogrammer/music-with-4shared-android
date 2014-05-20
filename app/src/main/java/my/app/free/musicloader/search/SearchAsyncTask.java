package my.app.free.musicloader.search;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.app.free.musicloader.Bot4Shared;

/**
 * Created by loki on 2014. 5. 19..
 */
public class SearchAsyncTask extends AsyncTask<String, Integer, ArrayList<SearchResultItem>> {

    Bot4Shared _bot;
    ResultAdapter _adapter;

    public SearchAsyncTask(Bot4Shared bot, ResultAdapter adapter) {
        _bot = bot;
        _adapter = adapter;
    }

    @Override
    protected ArrayList<SearchResultItem> doInBackground(String... args) {
        ArrayList<SearchResultItem> retList = new ArrayList<SearchResultItem>();

        int size = args.length;
        for (int i = 0; i < size; i++) {
            String query = args[i];
            JSONObject result = _bot.Search(query);

            try {
                JSONArray array = result.getJSONArray("files");
                for (int fileDataIndex = 0; fileDataIndex < array.length(); fileDataIndex++) {
                    JSONObject data = array.getJSONObject(fileDataIndex);

                    SearchResultItem item = new SearchResultItem(data.getString("name"), data.getString("downloadPage"));
                    retList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return retList;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<SearchResultItem> result) {
        super.onPostExecute(result);

        int size = result.size();
        for (int i = 0; i < size; i++) {
            _adapter.add(result.get(i));
        }
        _adapter.notifyDataSetChanged();
    }
}
