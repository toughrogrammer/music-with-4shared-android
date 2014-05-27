package my.app.free.musicloader.search;

import my.app.free.musicloader.ModelMusic;

/**
 * Created by loki on 2014. 5. 19..
 */
public class SearchResultItem {
    public ModelMusic _music;

    public SearchResultItem(String title, String hash, String link) {
        _music = new ModelMusic(title, hash, link);
    }

    public SearchResultItem(ModelMusic music) {
        _music = music;
    }
}
