package my.app.free.musicloader.download;

import my.app.free.musicloader.ModelMusic;

/**
 * Created by loki on 2014. 5. 21..
 */
public class DownloadListItem {

    public ModelMusic _music;
    public DownloadState _state;
    public float _ratio;
    public DownloadListItem(ModelMusic music) {
        _music = music;
        _state = DownloadState.Finished;
    }

    public enum DownloadState {
        Downloading,
        Finished,
    }
}
