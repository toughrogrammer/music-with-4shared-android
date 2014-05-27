package my.app.free.musicloader.download;

import my.app.free.musicloader.ModelMusic;

/**
 * Created by loki on 2014. 5. 21..
 */
public class DownloadListItem {
    public ModelMusic _music;
    public int _index;
    public float _ratio;

    public DownloadListItem(ModelMusic music, int index) {
        _music = music;
        _index = index;
    }
}
