package my.app.free.musicloader;

import java.io.*;

/**
 * Created by loki on 2014. 5. 18..
 */
public class Util {
    public static String ReadAll(InputStream stream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }
}
