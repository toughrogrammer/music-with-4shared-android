package my.app.free.musicloader.chart;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.ModelMusic;
import my.app.free.musicloader.Util;

/**
 * Created by loki on 2014. 5. 27..
 */
public class ChartAsyncTask extends AsyncTask<Void, Void, Void> {

    Bot4Shared _bot;
    ChartListAdapter _adapter;

    public ChartAsyncTask(Bot4Shared bot, ChartListAdapter adapter) {
        _bot = bot;
        _adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String url = "http://localhost:3000/";

        try {
            HttpGet req = new HttpGet(url + "chart/list");

            HttpResponse res = httpClient.execute(req);
            HttpEntity entity = res.getEntity();
            InputStream stream = entity.getContent();
            JSONObject json = new JSONObject(Util.ReadAll(stream));

            JSONArray dataList = json.getJSONArray("data");
            for( int i = 0; i < dataList.length(); i ++ ) {
                JSONObject data = dataList.getJSONObject(i);
                String title = data.getString("name");
                String hash = data.getString("hash");
                String link = data.getString("link");

                Log.d("Chart", title + " " + hash);

                ModelMusic music = new ModelMusic(title, hash, link);
                _adapter.add( new ChartItem( music ) );
            }

            req.abort();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
