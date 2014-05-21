package my.app.free.musicloader;

import android.os.Environment;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by loki on 2014. 5. 18..
 */
public class Bot4Shared implements Serializable {

    public final static String PATH = "/MusicWith4Shared/";
    private String TAG = "Bot4Shared";

    private String _id;
    private String _password;
    private boolean _validAccount = false;
    private HashMap<String, String> _cookieMap;

    public Bot4Shared(String id, String password) {
        _id = id;
        _password = password;

        _cookieMap = new HashMap<String, String>();

        // 우리 앱을 위한 폴더 생성
        File dir = new File(Environment.getExternalStorageDirectory() + PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static String GeneratePath(String filename) {
        return Environment.getExternalStorageDirectory() + PATH + filename;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }


    public boolean SignIn() {
        boolean success = false;
        do {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();

                HttpPost httpPost = new HttpPost("http://www.4shared.com/web/login");

                ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("login", _id));
                pairs.add(new BasicNameValuePair("password", _password));
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));

                HttpResponse res = httpClient.execute(httpPost);

                StatusLine status = res.getStatusLine();
                Log.d(TAG, status.toString());
                if (status.getStatusCode() == 302) {
                    success = true;
                }

                this.SetCookie(res.getHeaders("Set-Cookie"));

                httpPost.abort();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (false);

        _validAccount = success;
        return success;
    }

    public String GetDirectLink(String downloadPage) {
        if (!_validAccount)
            return "";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 먼저 다운로드 페이지에서 쿠키를 얻기 위해 한 번 HTTP Get을 한 번 날린다.
        HttpGet req = new HttpGet(downloadPage);
        try {
            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();
            if (status.getStatusCode() != 200) {
                return "";
            }

            this.SetCookie(res.getHeaders("Set-Cookie"));

            req.abort();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 이제 진짜 다운로드 페이지를 열어서 바로 다운로드를 할 수 있는 링크를 찾는다.
        String pageHtmlContent = null;
        downloadPage = downloadPage.replaceFirst("mp3", "get");
        req = new HttpGet(downloadPage);
        try {
            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();
            if (status.getStatusCode() != 200) {
                return "";
            }

            HttpEntity entity = res.getEntity();
            InputStream stream = entity.getContent();
            pageHtmlContent = Util.ReadAll(stream);

            entity.consumeContent();
            req.abort();

            httpClient.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(pageHtmlContent);
        Element element = doc.getElementById("baseDownloadLink");
        String directLink = element.attr("value");

        return directLink;
    }

    public void DownloadOfficially(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 먼저 다운로드 페이지에서 쿠키를 얻기 위해 한 번 HTTP Get을 한 번 날린다.
        try {
            HttpGet req = new HttpGet(url);
            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();

            this.SetCookie(res.getHeaders("Set-Cookie"));

            req.abort();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 이제 진짜 다운로드 페이지를 열어서 바로 다운로드를 할 수 있는 링크를 찾는다.
        String pageHtmlContent = null;
        url = url.replaceFirst("mp3", "get");
        try {
            HttpGet req = new HttpGet(url);
            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();

            this.SetCookie(res.getHeaders("Set-Cookie"));

            HttpEntity entity = res.getEntity();
            InputStream stream = entity.getContent();
            pageHtmlContent = Util.ReadAll(stream);

            entity.consumeContent();
            req.abort();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(pageHtmlContent);
        Element element = doc.getElementById("baseDownloadLink");
        String directLink = element.attr("value");


        Log.e("bot", "start " + directLink);
        File file = new File("/mnt/sdcard/testfile.mp3");
        if (file.exists())
            file.delete();

        FileOutputStream fos;
        try {
            HttpGet req = new HttpGet(directLink);
            String cookie = this.GetCookie();
            req.setHeader("Cookie", cookie);
            Log.e("bot", cookie);

            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();
            Log.e(TAG, "File download : " + status.toString());

            int size = 0;
            InputStream input = res.getEntity().getContent();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) != -1) {
                size += len;
                fos.write(buffer, 0, len);
            }

            Log.e("bot", "file size : " + size);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("bot", "end");
    }

    public void DownloadPreview(String url, String path) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String previewUrl = "";
        try {
            HttpGet req = new HttpGet(url);
            req.addHeader(new BasicHeader("User-Agent",
                    "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53"));

            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();

            this.SetCookie(res.getHeaders("Set-Cookie"));

            HttpEntity entity = res.getEntity();
            InputStream stream = entity.getContent();
            String content = Util.ReadAll(stream);
            stream.close();
            req.abort();

            Document doc = Jsoup.parse(content);
            Element element = doc.getElementById("player1");
            Element source = element.child(0);
            previewUrl = source.attr("src");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HttpGet req = new HttpGet(previewUrl);

            HttpResponse res = httpClient.execute(req);

            long realSize = 0;
            Header[] headers = res.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header h = headers[i];
                if (h.getName().equals("")) {
                    realSize = Long.parseLong(h.getValue());
                }
            }

            if (realSize == 0) {
                // this preview file has some error
            }

            File file = new File(path);
            if (file.exists())
                file.delete();

            Log.e(TAG, "download start");

            int size = 0;
            InputStream input = res.getEntity().getContent();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) != -1) {
                size += len;
                fos.write(buffer, 0, len);
            }
            input.close();
            fos.close();

            Log.e(TAG, "download end : " + size);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close http client
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject Search(String query) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        JSONObject retJson = null;
        try {
            String url = "http://api.4shared.com/v0/files.json?";
            url += "oauth_consumer_key=c94c7b0a2ca7cfb7904a6721d0dfffff&";
            url += "category=1&";
            url += "query=" + URLEncoder.encode(query, "UTF-8");
//            url += "&offset=0&limit=3";

            HttpGet req = new HttpGet(url);

            HttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();
            if (status.getStatusCode() != 200) {
                // Error is occurred!!
            }

            this.SetCookie(res.getHeaders("Set-Cookie"));

            HttpEntity entity = res.getEntity();
            InputStream stream = entity.getContent();
            String result = Util.ReadAll(stream);
            retJson = new JSONObject(result);

            entity.consumeContent();

            req.abort();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retJson;
    }

    public void SetCookie(Header[] headers) {
        for (int i = 0; i < headers.length; i++) {
            String content = headers[i].getValue();
            Pattern pattern = Pattern.compile("(; )");
            for (String token : pattern.split(content)) {
                int divideIndex = token.indexOf('=');
                if (divideIndex == -1) {
                    // Such as HttpOnly, secure, ...
                    continue;
                }
                String key = token.substring(0, divideIndex);
                String value = "";
                if (divideIndex + 1 != token.length()) {
                    value = token.substring(divideIndex + 1, token.length());
                }

                _cookieMap.put(key, value);
            }
        }
    }

    public String GetCookie() {
        String cookie = "";
        Iterator<String> iterator = _cookieMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            cookie += key + "=" + _cookieMap.get(key) + "; ";
        }

        return cookie;
    }
}
