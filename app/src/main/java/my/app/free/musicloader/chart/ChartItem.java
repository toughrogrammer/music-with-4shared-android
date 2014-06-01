package my.app.free.musicloader.chart;

import my.app.free.musicloader.ModelMusic;

/**
 * Created by loki on 2014. 5. 19..
 */
public class ChartItem {
    public ModelMusic _music;

    public ChartItem(String title, String hash, String link) {
        _music = new ModelMusic(title, hash, link);
    }

    public ChartItem(ModelMusic music) {
        _music = music;
    }
}
