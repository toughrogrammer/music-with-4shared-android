package my.app.free.musicloader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by loki on 2014. 5. 18..
 */
public class Bot4Shared {

    private String _id;
    private String _password;
    private boolean _validAccount = false;
    private HashMap<String, String> _cookieMap;

    public Bot4Shared(String id, String password) {
        _id = id;
        _password = password;

        _cookieMap = new HashMap<String, String>();
    }

    public boolean SignIn() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://www.4shared.com/web/login");
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("login", _id));
        pairs.add(new BasicNameValuePair("password", _password));

        boolean success = false;
        do {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
                CloseableHttpResponse res = httpClient.execute(httpPost);

                StatusLine status = res.getStatusLine();
                if (status.getStatusCode() == 302) {
                    success = true;
                }

                Header[] headers = res.getHeaders("Set-Cookie");
                for (int i = 0; i < headers.length; i++) {
                    String key = headers[i].getName();
                    String value = headers[i].getValue();

                    _cookieMap.put(key, value);
                }

                res.close();
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

    public String getDirectLink(String downloadPage) {
        if( ! _validAccount )
            return "";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 먼저 다운로드 페이지에서 쿠키를 얻기 위해 한 번 HTTP Get을 한 번 날린다.
        HttpGet req = new HttpGet(downloadPage);
        try {
            CloseableHttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();
            if (status.getStatusCode() != 200) {
                return "";
            }

            Header[] headers = res.getHeaders("Set-Cookie");
            for (int i = 0; i < headers.length; i++) {
                String key = headers[i].getName();
                String value = headers[i].getValue();

                _cookieMap.put(key, value);
            }

            res.close();
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
            CloseableHttpResponse res = httpClient.execute(req);

            StatusLine status = res.getStatusLine();
            if (status.getStatusCode() != 200) {
                return "";
            }

            HttpEntity entity = res.getEntity();
            InputStream stream = entity.getContent();
            pageHtmlContent = Util.ReadAll(stream);

            entity.consumeContent();
            res.close();
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
        int index = directLink.lastIndexOf("&");
        directLink = directLink.substring(0, index);

        return directLink;
    }
}
